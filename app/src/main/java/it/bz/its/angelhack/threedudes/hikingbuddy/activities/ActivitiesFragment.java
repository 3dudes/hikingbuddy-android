package it.bz.its.angelhack.threedudes.hikingbuddy.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.Utils;
import it.bz.its.angelhack.threedudes.hikingbuddy.adapters.ActivitiesAdapter;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.ActivitiesResponse;
import it.bz.its.angelhack.threedudes.hikingbuddy.services.ActivityService;
import it.bz.its.angelhack.threedudes.hikingbuddy.services.UserService;
import it.bz.its.angelhack.threedudes.hikingbuddy.widget.DividerDecoration;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by philipgiuliani on 17.05.15.
 */
public class ActivitiesFragment extends Fragment implements Callback<ActivitiesResponse> {
    private ViewSwitcher viewSwitcher;
    private RecyclerView recyclerView;
    private ActivitiesAdapter adapter;

    public ActivitiesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activities, container, false);

        viewSwitcher = (ViewSwitcher) v.findViewById(R.id.viewSwitcher);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        adapter = new ActivitiesAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerDecoration(getActivity()).setPaddingLeft(getResources().getDimensionPixelSize(R.dimen.list_image_padding)));

        RestAdapter restAdapter = Utils.getRestAdapter(getActivity());
        ActivityService api = restAdapter.create(ActivityService.class);

        api.getActivities(this);

        return v;
    }

    @Override
    public void success(ActivitiesResponse activitiesResponse, Response response) {
        adapter.setActivities(activitiesResponse.getActivities());
        viewSwitcher.showNext();
    }

    @Override
    public void failure(RetrofitError error) {
        Log.d("ERROR", error.getMessage());
    }
}