package com.jaktongdan.android.sseuaengnim.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jaktongdan.android.sseuaengnim.Firestore;
import com.jaktongdan.android.sseuaengnim.R;
import com.jaktongdan.android.sseuaengnim.model.TestTimerData;

import java.util.ArrayList;

public class TestTimerRecyclerViewAdapter extends RecyclerView.Adapter<TestTimerRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<TestTimerData> items;
    OnTestTimerItemClickListener listener;

    public TestTimerRecyclerViewAdapter(Context context, ArrayList<TestTimerData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public TestTimerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timer_test, parent, false);
        return new TestTimerRecyclerViewAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TestTimerRecyclerViewAdapter.ViewHolder holder, int position) {
        TestTimerData testTimerData = items.get(position);

        try {
            holder.tvTimerTestName.setText(String.valueOf(testTimerData.getTestTimerName()));
            holder.tvTimerTestTime.setText(String.valueOf(testTimerData.getTestTimerTime()) + "분 시작");
        } catch (NullPointerException e) {
            Log.e("error : ", "Test Timer");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnTestTimerItemClickListener listener) {
        this.listener = listener;
    }

    public void onItemClick(ViewHolder viewHolder, View view, int position) {
        if(listener != null) {
            listener.onItemClick(viewHolder, view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimerTestName;
        TextView tvTimerTestTime;

        public ViewHolder(@NonNull View itemView, final OnTestTimerItemClickListener listener) {
            super(itemView);
            tvTimerTestName = itemView.findViewById(R.id.tv_timerTestName);
            tvTimerTestTime = itemView.findViewById(R.id.tv_timerTestTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }
    }

    public TestTimerData getItem(int position) {
        return items.get(position);
    }
}
