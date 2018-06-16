package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends NavigationBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);

        // get permission upon the first opening of the program to use storage
        // if permission is not granted we should probably close the app
        LogTasks.verifyStoragePermissions(MainActivity.this);
/*
        if(alreadyRegistered()){
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            finish();
        }
*/

        final EditText inputFirstName = (EditText) findViewById(R.id.input_first_name);
        final EditText inputLastName = (EditText) findViewById(R.id.input_last_name);
        final EditText inputEmail = (EditText) findViewById(R.id.input_email);
        final TextView textView4 = (TextView) findViewById(R.id.textView4);

        //Listen for taps outside a view
        View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(view);
                }
            }
        };

        //Hide soft keyboard when user taps outside the edit text
        inputFirstName.setOnFocusChangeListener(focusListener);
        inputLastName.setOnFocusChangeListener(focusListener);
        inputEmail.setOnFocusChangeListener(focusListener);

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Hide the keyboard
                hideKeyboard(v);

                String firstName = inputFirstName.getText().toString();
                String lastName = inputLastName.getText().toString();
                String email = inputEmail.getText().toString();

                //First name, last name, email are not empty and email is valid.
                if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()){
                    textView4.setText("Please fill in all required fields.");
                }
                //Email address is not a valid format
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    textView4.setText("Email address is invalid.");
                }
                else
                {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("FIRST_KEY", firstName).apply();
                    editor.putString("LAST_KEY", lastName).apply();
                    editor.putString("EMAIL_KEY", email).apply();
                    startActivity(new Intent(MainActivity.this, StartActivity.class));

                    finish();
                }

            }
        });
    }

    /**
     * Check if the user has already registered
     * @return true if the user is already registered, false other
     */
    public boolean alreadyRegistered(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        return pref.contains("FIRST_KEY") && pref.contains("LAST_KEY") && pref.contains("EMAIL_KEY");
    }

    public void hideKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }
}

