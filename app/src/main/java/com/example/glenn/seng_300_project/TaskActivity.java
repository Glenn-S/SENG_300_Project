package com.example.glenn.seng_300_project;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends NavigationBaseActivity {

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private WrappedLinearLayoutManager mLayoutManager;

    private TaskManager mTaskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_task);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        //Divides items in list
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), LinearLayout.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        //Do not change list size based on content
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new WrappedLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mTaskManager = new TaskManager(this);
        mTaskManager.openDB();

        mAdapter = new TaskAdapter(this, mTaskManager);
        mRecyclerView.setAdapter(mAdapter);

        //Button to add new task
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Alert Dialog for entering a new task
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TaskActivity.this);
                dialogBuilder.setTitle("Enter a short task name:");

                //Edit text accept the name of the new task
                final EditText input = new EditText(TaskActivity.this);
                input.setHint("New Task");

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialogBuilder.setView(input);

                //User Clicks "OK", add new task to list
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAdapter.add(input.getText().toString());
                        mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                });

                //User clicks "Cancel", change nothing
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                //Make keyboard appear automatically
                AlertDialog addTaskDialog = dialogBuilder.create();
                addTaskDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                addTaskDialog.show();
            }
        });

    }

    /**
     * This class provides a workaround for a threading error in RecycleView.
     * https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in
     */
    public class WrappedLinearLayoutManager extends LinearLayoutManager{

        public WrappedLinearLayoutManager(Context context){
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state){
            try{
                super.onLayoutChildren(recycler,state);
            }
            catch(IndexOutOfBoundsException e){
                Log.e("LinearLayoutManager: ", "Catching threading error in RecycleView");
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mTaskManager.closeDB();
    }
}
