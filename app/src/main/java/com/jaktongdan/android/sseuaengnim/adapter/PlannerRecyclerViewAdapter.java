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
import com.jaktongdan.android.sseuaengnim.model.PlannerData;

import java.util.ArrayList;

public class PlannerRecyclerViewAdapter extends RecyclerView.Adapter<PlannerRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<PlannerData> items;

    public PlannerRecyclerViewAdapter(Context context, ArrayList<PlannerData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public PlannerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_planner, parent, false);
        return new PlannerRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlannerRecyclerViewAdapter.ViewHolder holder, int position) {
        PlannerData plannerData = items.get(position);

        try {
            holder.tvPlanDate.setText(plannerData.getPlanDate());
            holder.tvPlanTitle.setText(plannerData.getPlanTitle());
        } catch (NullPointerException e) {
            Log.e("error : ", "Test Timer");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

//    public void setOnItemClickListener(OnTestTimerItemClickListener listener) {
//        this.listener = listener;
//    }
//
//    public void onItemClick(ViewHolder viewHolder, View view, int position) {
//        if(listener != null) {
//            listener.onItemClick(viewHolder, view, position);
//        }
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlanDate;
        TextView tvPlanTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPlanDate = itemView.findViewById(R.id.tv_planDate);
            tvPlanTitle = itemView.findViewById(R.id.tv_planTitle);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    if(listener != null) {
//                        listener.onItemClick(ViewHolder.this, view, position);
//                    }
//                }
//            });
        }
    }

    public PlannerData getItem(int position) {
        return items.get(position);
    }
}
