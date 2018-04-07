package com.example.tabish.taskask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeUser extends Activity {
    Button accept;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);
        session = new SessionManager(getApplicationContext());
        accept = findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(),MainScreenActivity.class);
                session.setNewUser();
                startActivity(login);
                finish();
            }
        });
    }
}
