package it.bz.its.angelhack.threedudes.hikingbuddy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.adapters.TabsAdapter;
import it.bz.its.angelhack.threedudes.hikingbuddy.tabs.SlidingTabLayout;

public class MainActivity extends AppCompatActivity implements SlidingTabLayout.TabColorizer {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabsAdapter tabsAdapter = new TabsAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(tabsAdapter);

        SlidingTabLayout tabHost = (SlidingTabLayout) findViewById(R.id.tabHost);
        tabHost.setDistributeEvenly(true);
        tabHost.setCustomTabColorizer(this);
        tabHost.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
            SharedPreferences.Editor prefEdit = getSharedPreferences("infos", MODE_PRIVATE).edit();

            prefEdit.remove("token");
            prefEdit.commit();

            Intent loginIntent = new Intent(this, LoginActivity.class);

            startActivity(loginIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getIndicatorColor(int position) {
        return getResources().getColor(R.color.selector);
    }
}
