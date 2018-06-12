package com.example.glenn.seng_300_project;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskItemsAdapter extends BaseAdapter {

    private Context mContext;
    private List<TaskList> mTaskItems;

    public TaskItemsAdapter(Context mContext, List<TaskList> mTaskItems) {
        this.mContext = mContext;
        this.mTaskItems = mTaskItems;
    }

    // constructor
    @Override
    public int getCount() {
        return mTaskItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.list_view_custom_layout, null);
        TextView taskTime = (TextView) v.findViewById(R.id.list_time);
        TextView taskItem = (TextView) v.findViewById(R.id.list_item);
        // Set test for TextView
        taskItem.setText(mTaskItems.get(position).getTask());
        taskTime.setText(mTaskItems.get(position).getTime());
        // Save product id to tag
        v.setTag(mTaskItems.get(position).getTime());

        return v;
    }

}