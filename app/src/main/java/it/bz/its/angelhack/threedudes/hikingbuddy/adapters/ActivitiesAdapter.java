package it.bz.its.angelhack.threedudes.hikingbuddy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.Utils;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Mission;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.MissionSession;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.User;

/**
 * Created by philipgiuliani on 17.05.15.
 */
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder> {
    private List<MissionSession> activities = new ArrayList<>();
    private final Context context;
    private final LayoutInflater layoutInflater;

    public void setActivities(List<MissionSession> activities) {
        this.activities = activities;
        notifyDataSetChanged();
    }

    public ActivitiesAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ActivitiesAdapter.ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ActivitiesAdapter.ActivityViewHolder viewHolder, int i) {
        MissionSession activity = activities.get(i);

        // user data
        User user = activity.getUser();
        viewHolder.textViewFullName.setText(user.getFullName());
        Picasso.with(context).load(user.getPicture().getThumbImageUri()).into(viewHolder.imageView);

        // created at
        Date currDate = new Date();
        String timeLeft = DateUtils.getRelativeTimeSpanString(activity.getCompletedAt().getTime(), currDate.getTime(), DateUtils.SECOND_IN_MILLIS).toString();
        // DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        viewHolder.textViewCreatedAt.setText(timeLeft);//(dateFormat.format(activity.getCompletedAt()));

        // text
        Mission mission = activity.getMission();
        String usedTime = Utils.pretifyAverageTime(activity.getScore());
        viewHolder.textViewDescription.setText("Completed " + mission.getName() + " in " + usedTime);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textViewFullName;
        private TextView textViewCreatedAt;
        private TextView textViewDescription;

        public ActivityViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewFullName = (TextView) itemView.findViewById(R.id.textViewFullname);
            textViewCreatedAt = (TextView) itemView.findViewById(R.id.textViewCreatedAt);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
        }
    }
}
