package com.example.tabish.taskask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class TaskComplete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_complete);

        String otherUser = getIntent().getStringExtra("DisplayName");
        TextView displayName = findViewById(R.id.displayName);
        displayName.setText(otherUser);

        RatingBar rateUser = (RatingBar) findViewById(R.id.rateUser);
        rateUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(),Float.toString(rating),Toast.LENGTH_SHORT).show();
                Intent homeScreen = new Intent(getApplicationContext(),AllTasksActivity.class);
                startActivity(homeScreen);
                finish();
            }
        });
    }
}
