package it.bz.its.angelhack.threedudes.hikingbuddy.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Ranking;

public class RankingListAdapter extends ArrayAdapter<RankingListViewItem> {
    private final Context context;
    private final List<RankingListViewItem> values;

    public RankingListAdapter(Context context, List<Ranking> values) {
        super(context, 0);
        this.context = context;
        this.values = new ArrayList<>();
        for(Ranking ranking : values) {
            this.values.add(new RankingListViewItem(ranking));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = this.values.get(position).getView(context, convertView);

        return itemView;
    }

    @Override
    public int getCount() {
        return values.size();
    }
}
