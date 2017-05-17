package com.sorashiro.metroplanning.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorashiro.metroplanning.R;
import com.sorashiro.metroplanning.jni.CoreData;
import com.sorashiro.metroplanning.util.LogAndToastUtil;

import java.util.List;

public class LevelListAdapter extends RecyclerView.Adapter<LevelViewHolder> {

    private Context             context;
    private List<String>        list;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public LevelListAdapter(Context context, List<String> lists) {
        this.context = context;
        this.list = lists;
    }

    @Override
    public LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_level, parent, false);
        LevelViewHolder viewHolder = new LevelViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LevelViewHolder holder, int position) {
        int level = position + 1;
        String levelStr = "LEVEL " + level;
        holder.textLevel.setText(levelStr);

        String gameData = CoreData.getLevelData(level);
        String[] gameDataSegments = gameData.split(",");
        String[] mapData = gameDataSegments[0].split(" ");
        String[] metroData = gameDataSegments[1].split(" ");
        String[] stationData = gameDataSegments[2].split(" ");
        String[] turnoutData = gameDataSegments[3].split(" ");
        holder.textMetro.setText(metroData[0]);
        holder.textStation.setText(stationData[0]);
        holder.textTurnout.setText(turnoutData[0]);
        holder.textTime.setText(mapData[2]);
        holder.textPassenger.setText(mapData[3]);

        if (mOnItemClickListener != null) {
            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

class LevelViewHolder extends RecyclerView.ViewHolder {

    CardView mCardView;
    TextView textLevel;
    TextView textMetro;
    TextView textStation;
    TextView textTurnout;
    TextView textTime;
    TextView textPassenger;

    public LevelViewHolder(View itemView) {
        super(itemView);
        mCardView = (CardView) itemView.findViewById(R.id.card_level_data);
        textLevel = (TextView) itemView.findViewById(R.id.text_level);
        textMetro = (TextView) itemView.findViewById(R.id.text_metro);
        textStation = (TextView) itemView.findViewById(R.id.text_station);
        textTurnout = (TextView) itemView.findViewById(R.id.text_turnout);
        textTime = (TextView) itemView.findViewById(R.id.text_time);
        textPassenger = (TextView) itemView.findViewById(R.id.text_passenger);
    }
}
