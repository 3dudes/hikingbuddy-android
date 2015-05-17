package it.bz.its.angelhack.threedudes.hikingbuddy.models;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class HeightGraph {
    private static final String TAG = "HeightGraph";

    double minVal = 0.0;
    double maxVal = 0.0;
    List<String> heightProfile;

    public void selfAnalyze() {
        for (String prof : heightProfile) {
            double val = Double.parseDouble(prof);

            if (val < minVal) {
                minVal = val;
            } else {
                maxVal = val;
            }
        }
    }

    public List<Entry> getGraphEntries() {
        List<Entry> toRet = new ArrayList<>();
        int xId = 0;

        for (String prof : heightProfile) {
            float val = Float.parseFloat(prof);
            Log.d(TAG, String.format("%f", val));
            toRet.add(new Entry(val, xId));

            xId++;
        }

        return toRet;
    }
}
