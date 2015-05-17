package it.bz.its.angelhack.threedudes.hikingbuddy.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;
import java.util.List;

import it.bz.its.angelhack.threedudes.hikingbuddy.Constants;
import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.Utils;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Location;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Mission;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.MissionSessionResponse;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Route;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.RoutePoint;
import it.bz.its.angelhack.threedudes.hikingbuddy.services.MissionService;
import it.bz.its.angelhack.threedudes.hikingbuddy.services.MissionSessionService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RouteMapActivity extends FragmentActivity {
    private static final String TAG = "RouteMapActivity";
    public static final String MISSION_HOLDER_ID = "RouteMapActivity.Mission";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Mission currentMission;
    private Route currentRoute;
    private Date missionStartTime;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        // Prepare the NFC listener
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Process the starting intent
        Intent starterIntent = getIntent();
        if (starterIntent != null && starterIntent.hasExtra(MISSION_HOLDER_ID)) {
            currentMission = starterIntent.getParcelableExtra(MISSION_HOLDER_ID);

            starterIntent.removeExtra(MISSION_HOLDER_ID);
        }

        // Create the timer update logic
        final TextView tvTimer = (TextView) this.findViewById(R.id.tv_mission_counter);
        missionStartTime = new Date();
        Handler counterHandler = new Handler();
        counterHandler.post(new Runnable() {
            @Override
            public void run() {
                Activity ctx = RouteMapActivity.this;

                ctx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Date now = new Date();
                        long dateDiff = now.getTime() - missionStartTime.getTime();
                        long minutesElapsed = dateDiff / (60 * 1000);
                        long secondsElapsed = (dateDiff - minutesElapsed * 60 * 1000) / 1000;
                        long millisecondsElapsed = (dateDiff % 1000) / 10;

                        tvTimer.setText(String.format("%02d:%02d:%02d", minutesElapsed, secondsElapsed, millisecondsElapsed));
                    }
                });

                Handler mainHandler = new Handler(ctx.getMainLooper());
                mainHandler.postDelayed(this, Constants.TIMER_INCREMENT_VAL_MS);
            }
        });

        // Handle the "Cancel Mission" logic
        Button btCancelMission = (Button) this.findViewById(R.id.bt_cancel_mission);
        btCancelMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showYesNoAlertDialog(RouteMapActivity.this,
                        "Are you sure?",
                        "Do you really want to cancel this current mission?",
                        new Runnable() {
                            @Override
                            public void run() {
                                RestAdapter restAdapter = Utils.getRestAdapter(RouteMapActivity.this);
                                MissionSessionService msr = restAdapter.create(MissionSessionService.class);

                                final ProgressDialog progDialog = Utils.newLoadingDialog(RouteMapActivity.this, "Cancelling mission ...");
                                progDialog.show();
                                msr.abortMission(new Callback<Response>() {
                                    @Override
                                    public void success(Response response, Response response2) {
                                        Intent endMissionIntentActivity = new Intent(RouteMapActivity.this, EndMissionActivity.class);

                                        startActivity(endMissionIntentActivity);
                                        progDialog.dismiss();
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        Utils.showOkAlertDialog(RouteMapActivity.this, "Problem", error.getMessage(), null);
                                        progDialog.dismiss();
                                    }
                                });
                            }
                        },
                        null // No clicked
                );
            }
        });

        if (currentMission != null) {
            // Load the route
            RestAdapter restAdapter = Utils.getRestAdapter(RouteMapActivity.this);
            MissionService ms = restAdapter.create(MissionService.class);

            final ProgressDialog progDialog = Utils.newLoadingDialog(RouteMapActivity.this, "Downloading route ...");
            progDialog.show();
            ms.getRoute(currentMission.getId(), new Callback<Route>() {
                @Override
                public void success(Route route, Response response2) {
                    Log.d(TAG, "Received a route with " + route.getRoute().size() + " geo points.");

                    currentRoute = route;
                    setUpMapIfNeeded();

                    progDialog.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.showOkAlertDialog(RouteMapActivity.this, "Problem", error.getMessage(), null);
                    progDialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        super.onPause();

        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d(TAG, "New intent received: " + intent.getAction());
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        RestAdapter restAdapter = Utils.getRestAdapter(RouteMapActivity.this);
        MissionSessionService msr = restAdapter.create(MissionSessionService.class);

        final ProgressDialog progDialog = Utils.newLoadingDialog(RouteMapActivity.this, "Ending mission ...");
        progDialog.show();
        msr.checkTag(Utils.bytesToHex(tag.getId()), new Callback<MissionSessionResponse>() {
            @Override
            public void success(MissionSessionResponse response, Response response2) {
                Intent endMissionIntentActivity = new Intent(RouteMapActivity.this, EndMissionActivity.class);

                startActivity(endMissionIntentActivity);
                progDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.showOkAlertDialog(RouteMapActivity.this, "Problem", error.getMessage(), null);
                progDialog.dismiss();
            }
        });
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        if (mMap != null) {
            // Add the important markers
            if (currentMission != null) {
                for (Location missLoc: currentMission.getImportantPoints()) {
                    mMap.addMarker(new MarkerOptions().position(missLoc.getGoogleMapCoord()));
                }

                // Reposition the camera to see the whole rute
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        // Draw the route
                        new AsyncRouteRenderer().execute(currentRoute);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(currentMission.getLatLngBounds(), 40));
                    }
                });
            }
        }
    }

    private class AsyncRouteRenderer extends AsyncTask<Route, PolylineOptions, Long> {
        ProgressDialog pgd;

        protected void onPreExecute() {
            pgd = Utils.newLoadingDialog(RouteMapActivity.this, "Loading route ...");
            pgd.show();
        }

        protected Long doInBackground(Route... rs) {
            if (rs != null) {
                Route route = rs[0];
                List<RoutePoint> routePoints = route.getRoute();

                if (routePoints.size() > 1) {
                    for (int i = 0; i < routePoints.size() - 1; i++) {
                        PolylineOptions segmOptions = new PolylineOptions()
                                .add(routePoints.get(i).getGoogleMapCoord(), routePoints.get(i + 1).getGoogleMapCoord())
                                .width(4)
                                .color(Color.RED);

                        publishProgress(segmOptions);
                    }
                }
            }

            return 0L;
        }

        protected void onProgressUpdate(PolylineOptions... lineOpt) {
            if (lineOpt != null) {
                mMap.addPolyline(lineOpt[0]);
            }
        }

        protected void onPostExecute(Long result) {
            pgd.dismiss();
        }
    }
}
