package com.example.glenn.seng_300_project;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskItemsAdapter extends BaseAdapter {

    private Context mContext;
    private List<TaskInterval> mTaskItems;

    public TaskItemsAdapter(Context mContext, List<TaskInterval> mTaskItems) {
        this.mContext = mContext;
        this.mTaskItems = mTaskItems;
    }

    // constructor

    public List<TaskInterval> getData()
    {
        return mTaskItems;
    }

    /**
     * To get the size of the array in number of elements
     * @return
     */
    @Override
    public int getCount() {
        return mTaskItems.size();
    }

    /**
     * To get the item at the index, position and return the element
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return mTaskItems.get(position);
    }

    /**
     * To get the item index and return it
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.list_view_custom_layout, null);
        TextView taskTime = (TextView) v.findViewById(R.id.list_time);
        TextView taskItem = (TextView) v.findViewById(R.id.list_item);
        // Set test for TextView
        taskItem.setText(mTaskItems.get(position).taskName);
        taskTime.setText(mTaskItems.get(position).startTime);
        // Save product id to tag
        v.setTag(mTaskItems.get(position).startTime);

        return v;
    }

}