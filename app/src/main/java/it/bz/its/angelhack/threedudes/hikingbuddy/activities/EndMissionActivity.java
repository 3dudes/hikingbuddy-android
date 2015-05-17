package it.bz.its.angelhack.threedudes.hikingbuddy.activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.List;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.Utils;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Ranking;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.RankingResponse;
import it.bz.its.angelhack.threedudes.hikingbuddy.services.MissionService;
import it.bz.its.angelhack.threedudes.hikingbuddy.views.RankingListAdapter;
import it.bz.its.angelhack.threedudes.hikingbuddy.views.RankingListViewItem;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class EndMissionActivity extends AppCompatActivity {
    private static final String TAG = "EndMissionActivitiy";
    public static final String MISSION_ID = "EndMissionActivity.MissionID";
    public static final String MISSION_TIME_ELAPSED = "EndMissionActivity.MissionTimeElapsed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_mission);

        final ViewSwitcher vsEndSwitcher = (ViewSwitcher) this.findViewById(R.id.vs_end_ranking);
        final ListView rankingListView = (ListView) this.findViewById(R.id.lv_end_ranking);

        Intent starterIntent = getIntent();
        int missionId = 0;
        String timeElapsed = "00:00:00";
        if (starterIntent != null) {
            missionId = starterIntent.getIntExtra(MISSION_ID, 0);
            timeElapsed = starterIntent.getStringExtra(MISSION_TIME_ELAPSED);
        }

        TextView tvFinalCounterValue = (TextView) this.findViewById(R.id.tv_end_final_counter_val);
        tvFinalCounterValue.setText(timeElapsed);

        Button btEndMission = (Button) this.findViewById(R.id.bt_ok_end_mission);
        btEndMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileStarter = new Intent(EndMissionActivity.this, MainActivity.class);

                startActivity(profileStarter);
            }
        });

        RestAdapter restAdapter = Utils.getRestAdapter(this);
        MissionService ms = restAdapter.create(MissionService.class);
        ms.getRanking(missionId, new Callback<RankingResponse>() {
            @Override
            public void success(RankingResponse rankingResponse, Response response) {
                List<Ranking> rankingList = rankingResponse.getRankingList();
                RankingListAdapter rkla = new RankingListAdapter(getApplication(), rankingList);

                Log.d(TAG, "Acquired " + rankingList.size() + " ranking entries.");
                rankingListView.setAdapter(rkla);
                rankingListView.invalidate();
                vsEndSwitcher.showNext();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_end_mission, menu);
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
