package it.bz.its.angelhack.threedudes.hikingbuddy.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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

public class StartMissionActivity extends Activity {
    private static final String TAG = "StartMissionActivity";

    private Mission m;
    private String tagId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_mission);

        // Load views
        final ViewSwitcher vSwitcher = (ViewSwitcher) this.findViewById(R.id.vs_start_mission);
        final TextView tvInvitation = (TextView) this.findViewById(R.id.tv_invitation_message);
        final TextView tvMissionStartName = (TextView) this.findViewById(R.id.tv_mission_start_name);
        final TextView tvMissionEndName = (TextView) this.findViewById(R.id.tv_mission_end_name);
        final Button btStartMission = (Button) this.findViewById(R.id.bt_start_mission);

        btStartMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestAdapter msAdapter = Utils.getRestAdapter();
                MissionSessionService missionServiceImpl = msAdapter.create(MissionSessionService.class);

                //TODO
                final ProgressDialog pgLoadingDialog = Utils.newLoadingDialog(StartMissionActivity.this, "Starting mission ...");
                pgLoadingDialog.show();
                missionServiceImpl.checkTag("1", tagId,
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
            RestAdapter restAdapter = Utils.getRestAdapter();
            MissionService missionServiceInfoImpl = restAdapter.create(MissionService.class);

            // Make the actual request
            // TODO
            missionServiceInfoImpl.getMission("1", tagId, new Callback<MissionResponse>() {
                @Override
                public void success(final MissionResponse mr, Response response) {
                    StartMissionActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Mission info acquired.");

                            // Prepare everything before displaying the mission
                            // TODO
                            m = mr.getMission();
                            Location startLocation = m.getStartLocation();
                            Location endLocation = m.getEndLocation();

                            tvInvitation.setText(String.format("Hi %s to the %s mission", "XXX", m.getName()));
                            tvMissionStartName.setText(startLocation.getName());
                            tvMissionEndName.setText(endLocation.getName());
                            btStartMission.setEnabled(true);

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
