package com.example.osw.androidlabs;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;


public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    Button loginButton;
    SharedPreferences sharedPref;
    TextView email;
    String prefFile = "prefFile";
    String defaultEmail = "defaultEmail";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        //SharedPreferences sharedPref = getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        final TextView email = (TextView) findViewById(R.id.loginEmail);

        email.setText(sharedPref.getString(defaultEmail, "email@domain.com"));

        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                String s = email.getText().toString();

                SharedPreferences.Editor edit =sharedPref.edit();
                edit.putString(defaultEmail, s);
                edit.commit();

                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);

            }

        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }


    private void savePref(View view){
       /* String s = email.getText().toString();

        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString(defaultEmail, s);
        edit.commit();*/
    }
}
