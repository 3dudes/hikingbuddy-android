package it.bz.its.angelhack.threedudes.hikingbuddy.activities;

import android.app.ProgressDialog;
import android.content.Intent;

import it.bz.its.angelhack.threedudes.hikingbuddy.models.HeightGraph;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Location;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.Utils;
import it.bz.its.angelhack.threedudes.hikingbuddy.enums.HttpCodes;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Mission;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.MissionResponse;
import it.bz.its.angelhack.threedudes.hikingbuddy.services.MissionService;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.MissionSessionResponse;
import it.bz.its.angelhack.threedudes.hikingbuddy.services.MissionSessionService;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.RestErrorResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StartMissionActivity extends AppCompatActivity {
    private static final String TAG = "StartMissionActivity";

    private Mission m;
    private String tagId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_mission);

        // Load views
        ImageView imgAvatar = (ImageView) this.findViewById(R.id.img_profile);
        final ViewSwitcher vSwitcher = (ViewSwitcher) this.findViewById(R.id.vs_start_mission);
        final ViewSwitcher vSwitcherGraph = (ViewSwitcher) this.findViewById(R.id.vs_load_height_graph);
        final TextView tvInvitation = (TextView) this.findViewById(R.id.tv_invitation_message);
        final TextView tvMissionStartName = (TextView) this.findViewById(R.id.tv_mission_start_name);
        final TextView tvMissionEndName = (TextView) this.findViewById(R.id.tv_mission_end_name);
        final TextView tvRouteDistance = (TextView) this.findViewById(R.id.tv_route_distance);
        final TextView tvAverageTime = (TextView) this.findViewById(R.id.tv_route_average_length);
        final Button btStartMission = (Button) this.findViewById(R.id.bt_start_mission);

        // Restore user_name and avatar
        SharedPreferences prefs = getSharedPreferences("infos", MODE_PRIVATE);
        Picasso.with(this).load(prefs.getString("avatar", "@drawable/default_profile_image")).into(imgAvatar);

        btStartMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestAdapter msAdapter = Utils.getRestAdapter(StartMissionActivity.this);
                MissionSessionService missionServiceImpl = msAdapter.create(MissionSessionService.class);

                final ProgressDialog pgLoadingDialog = Utils.newLoadingDialog(StartMissionActivity.this, "Starting mission ...");
                pgLoadingDialog.show();
                missionServiceImpl.checkTag(tagId,
                        new Callback<MissionSessionResponse>() {
                            @Override
                            public void success(MissionSessionResponse missionSessionResponse, Response response) {
                                // Stop the loading dialog
                                pgLoadingDialog.dismiss();

                                // Prepare and start the map activity
                                Intent mapIntent = new Intent(StartMissionActivity.this, RouteMapActivity.class);

                                mapIntent.putExtra(RouteMapActivity.MISSION_HOLDER_ID, m);
                                startActivity(mapIntent);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                // Stop the loading dialog
                                pgLoadingDialog.dismiss();

                                HttpCodes httpCode = HttpCodes.getFromNumericValue(error.getResponse().getStatus());
                                RestErrorResponse restErrorResp = (RestErrorResponse) error.getBodyAs(RestErrorResponse.class);

                                switch (httpCode) {
                                    case UNPROCESSABLE:
                                        Utils.showOkAlertDialog(StartMissionActivity.this,
                                                "Mission not started",
                                                restErrorResp.getJoinedErrors(),
                                                null);
                                        break;
                                    default:
                                        Utils.showOkAlertDialog(StartMissionActivity.this,
                                                "Problem",
                                                error.getMessage(),
                                                null);
                                        break;
                                }
                            }
                        });
            }
        });

        Intent starterIntent = getIntent();
        if (starterIntent != null && starterIntent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Tag nfcTagInfo = starterIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            tagId = Utils.bytesToHex(nfcTagInfo.getId());

            Log.d(TAG, "Application started by NFC tag with id: " + tagId);

            // Create the rest manager and hook it up to the models
            RestAdapter restAdapter = Utils.getRestAdapter(StartMissionActivity.this);
            final MissionService missionServiceInfoImpl = restAdapter.create(MissionService.class);

            // Make the actual request
            missionServiceInfoImpl.getMission(tagId, new Callback<MissionResponse>() {
                @Override
                public void success(final MissionResponse mr, Response response) {
                    StartMissionActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Mission info acquired.");

                            // Prepare everything before displaying the mission
                            m = mr.getMission();
                            Location startLocation = m.getStartLocation();
                            Location endLocation = m.getEndLocation();

                            tvInvitation.setText(String.format("%s", m.getName()));
                            tvAverageTime.setText(Utils.pretifyAverageTime(m.getAverageTime()));
                            tvRouteDistance.setText(Utils.pretifyDistance(m.getDistance()));
                            tvMissionStartName.setText(startLocation.getName());
                            tvMissionEndName.setText(endLocation.getName());
                            btStartMission.setEnabled(true);

                            // Process the graph
                            final LineChart chart = (LineChart) findViewById(R.id.chart);
                            chart.setDescription("Terrain Height");
                            chart.getAxisLeft().setDrawLabels(false);
                            // chart.getAxisRight().setDrawLabels(false);
                            chart.getXAxis().setDrawLabels(false);
                            chart.getLegend().setEnabled(false);
                            missionServiceInfoImpl.getHeightInfo(m.getId(), new Callback<HeightGraph>() {
                                @Override
                                public void success(HeightGraph heightGraph, Response response) {
                                    List<Entry> dataSet = heightGraph.getGraphEntries();
                                    ArrayList<String> xVals = new ArrayList<String>();
                                    LineDataSet lds;

                                    for (int i = 0; i < dataSet.size(); i++) {
                                        xVals.add((i) + "");
                                    }
                                    lds = new LineDataSet(heightGraph.getGraphEntries(), "3");
                                    lds.setColor(Color.BLACK);
                                    lds.setLineWidth(0.5f);
                                    lds.setDrawValues(false);
                                    lds.setDrawCircles(false);
                                    lds.setDrawCubic(false);
                                    lds.setDrawFilled(true);

                                    chart.setData(new LineData(xVals, lds));
                                    chart.invalidate();
                                    vSwitcherGraph.showNext();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Utils.showOkAlertDialog(StartMissionActivity.this, "Problem", error.getMessage(), null);
                                }
                            });

                            vSwitcher.showNext();
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.w(TAG, "There was an error while acquiring the mission: " + error.getMessage());

                    HttpCodes httpCode = HttpCodes.getFromNumericValue(error.getResponse().getStatus());
                    Runnable activityKiller = new Runnable() {
                        @Override
                        public void run() {
                            // End everything since the current activity only makes sense
                            // with a valid, registered ID Tag
                            StartMissionActivity.this.finish();
                        }
                    };

                    switch (httpCode) {
                        case NOTFOUND:
                            Utils.showOkAlertDialog(StartMissionActivity.this,
                                    "Not Found",
                                    "The requested NFC tag is not registered inside our system!",
                                    activityKiller);
                            break;
                        case UNAUTHORIZED:
                            // Not logged in?
                            Intent loginIntent = new Intent(StartMissionActivity.this, LoginActivity.class);

                            startActivity(loginIntent);
                            break;
                        default:
                            Utils.showOkAlertDialog(StartMissionActivity.this, "Problem", error.getMessage(), activityKiller);
                            break;
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_mission, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
