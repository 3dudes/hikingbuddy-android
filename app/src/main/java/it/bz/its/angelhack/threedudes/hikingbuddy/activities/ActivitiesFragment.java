package it.bz.its.angelhack.threedudes.hikingbuddy.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;

/**
 * Created by philipgiuliani on 17.05.15.
 */
public class ActivitiesFragment extends Fragment {
    public ActivitiesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activities, container, false);

        return v;
    }
}