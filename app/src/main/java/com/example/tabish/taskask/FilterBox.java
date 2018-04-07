package com.example.tabish.taskask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

public class FilterBox extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_box);
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                overridePendingTransition( R.anim.slide_out_down, R.anim.slide_in_down );
                return true;

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.slide_out_down, R.anim.slide_in_down );
    }*/
}
