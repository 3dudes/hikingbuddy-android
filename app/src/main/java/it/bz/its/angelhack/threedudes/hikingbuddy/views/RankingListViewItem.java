package it.bz.its.angelhack.threedudes.hikingbuddy.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.bz.its.angelhack.threedudes.hikingbuddy.R;
import it.bz.its.angelhack.threedudes.hikingbuddy.models.Ranking;

public class RankingListViewItem {
    private Ranking rank;
    private ViewHolder viewHolder;

    public RankingListViewItem(Ranking r) {
        this.rank = r;
    }

    public View getView(Context context, View oldView) {
        View itemView = oldView;
        boolean reqViewCreation = false;

        if (itemView == null) {
            reqViewCreation = true;
        } else {
            reqViewCreation = (itemView.getTag() instanceof ViewHolder) == false;
        }

        if (reqViewCreation) {
            itemView = LayoutInflater.from(context).inflate(R.layout.ranking_list_view_element, null);

            viewHolder = new ViewHolder();
            /* viewHolder.layout = (RelativeLayout) itemView.findViewById(R.id.shop_item_layout);
            viewHolder.tvId = (TextView) itemView.findViewById(R.id.item_prod_id);
            viewHolder.tvName = (TextView) itemView.findViewById(R.id.item_prod_name);
            viewHolder.tvExtraInfo = (TextView) itemView.findViewById(R.id.item_prod_extrainfo);*/
            viewHolder.imgAvatar = (ImageView) itemView.findViewById(R.id.img_ranking_img_profile);

            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }

        // Load initial view data
        Picasso.with(context).load(rank.getUser().getPicture().getNormalImageUri()).into(viewHolder.imgAvatar);

        return itemView;
    }

    protected class ViewHolder {
        RelativeLayout layout;
        ImageView imgAvatar;
        TextView tvId;
        TextView tvName;
        TextView tvExtraInfo;
    }
}
