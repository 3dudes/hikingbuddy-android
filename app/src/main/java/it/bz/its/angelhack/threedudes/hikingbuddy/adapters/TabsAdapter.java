package it.bz.its.angelhack.threedudes.hikingbuddy.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.activities.ActivitiesFragment;
import it.bz.its.angelhack.threedudes.hikingbuddy.activities.MissionsFragment;

/**
 * Created by philipgiuliani on 17.05.15.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {
    private static final int FRAGMENT_TIMELINE = 0;
    private static final int FRAGMENT_MISSIONS = 1;
    private Context context;

    public TabsAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == FRAGMENT_TIMELINE) {
            return new ActivitiesFragment();
        }
        else if(position == FRAGMENT_MISSIONS) {
            return new MissionsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] names = context.getResources().getStringArray(R.array.activity_main_tabs);
        return names[position];
    }
}

