package it.bz.its.angelhack.threedudes.hikingbuddy.activities;

import android.content.Intent;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.Utils;


public class StartMissionActivity extends ActionBarActivity {
    private static final String TAG = "StartMissionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_mission);

        Intent starterIntent = getIntent();
        if (starterIntent != null && starterIntent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Tag nfcTagInfo = starterIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String tagId = Utils.bytesToHex(nfcTagInfo.getId());

            Log.d(TAG, "Application started by NFC tag with id : " + tagId);
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
