package com.example.glenn.seng_300_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends NavigationBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText1 = (EditText) findViewById(R.id.editText1);
                EditText editText2 = (EditText) findViewById(R.id.editText2);
                EditText editText3 = (EditText) findViewById(R.id.editText3);
                TextView textView4 = (TextView) findViewById(R.id.textView4);

                String firstName = editText1.getText().toString();
                String lastName = editText2.getText().toString();
                String email = editText3.getText().toString();

                if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty())
                    textView4.setText("Please fill in all required fields.");
                else
                {
                    editor.putString("FIRST_KEY", firstName);
                    editor.putString("LAST_KEY", lastName);
                    editor.putString("EMAIL_KEY", email);
                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                }

            }
        });
    }
}

