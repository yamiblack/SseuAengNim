package com.jaktongdan.android.sseuaengnim.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jaktongdan.android.sseuaengnim.R;
import com.jaktongdan.android.sseuaengnim.model.StudyTimerData;
import com.jaktongdan.android.sseuaengnim.model.TestTimerData;

import java.util.ArrayList;

public class StudyTimerRecyclerViewAdapter extends RecyclerView.Adapter<StudyTimerRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<StudyTimerData> items;
    OnStudyTimerItemClickListener listener;

    public StudyTimerRecyclerViewAdapter(Context context, ArrayList<StudyTimerData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public StudyTimerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timer_study, parent, false);
        return new StudyTimerRecyclerViewAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudyTimerRecyclerViewAdapter.ViewHolder holder, int position) {
        StudyTimerData studyTimerData = items.get(position);

        try {
            holder.tvTimerHistoryDate.setText(studyTimerData.getTodayDate() + "에는");
            holder.tvTimerHistoryTime.setText(studyTimerData.getStudyTime() + " 만큼 공부했습니다.");
        } catch (NullPointerException e) {
            Log.e("error : ", "Study Timer");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnStudyTimerItemClickListener listener) {
        this.listener = listener;
    }

    public void onItemClick(ViewHolder viewHolder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(viewHolder, view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimerHistoryDate;
        TextView tvTimerHistoryTime;

        public ViewHolder(@NonNull View itemView, final OnStudyTimerItemClickListener listener) {
            super(itemView);
            tvTimerHistoryDate = itemView.findViewById(R.id.tv_timerHistoryDate);
            tvTimerHistoryTime = itemView.findViewById(R.id.tv_timerHistoryTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }
    }

    public StudyTimerData getItem(int position) {
        return items.get(position);
    }
}
