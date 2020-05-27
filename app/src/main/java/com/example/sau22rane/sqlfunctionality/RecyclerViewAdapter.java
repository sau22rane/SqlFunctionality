package com.example.sau22rane.sqlfunctionality;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.EventViewHolder>{


    Context context;
    ArrayList<String> mList;
    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }
    public RecyclerViewAdapter(Context context, ArrayList<String> eventList){
        this.context = context;
        this.mList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        EventViewHolder vh = new EventViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, final int position) {
        holder.mTextView.setText(mList.get(position));
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });

        Log.d("TAG","asdf"+mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.UID);

        }
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}
