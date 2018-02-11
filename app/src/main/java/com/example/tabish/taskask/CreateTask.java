package com.example.tabish.taskask;

import java.util.ArrayList;
import java.util.List;


import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.app.NavUtils;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CreateTask extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;


    JSONParser jsonParser = new JSONParser();
    EditText lanlonStr = null;



    // url to create new task
    //Todo: Shift url to json parser class
    private static String url_create_task = "create_task.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    //Todo: Commenting left
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        addSpinnerItems();




        /*This onclicklistner ensures new checklist box is creater whenever "Add another" is tapped" */
        TextView addChecklistElement = (TextView) findViewById(R.id.addChecklistElement);
        addChecklistElement.setOnClickListener(new View.OnClickListener(){
            int counter = 1;
            @Override
            public void onClick(View view)
            {
                LinearLayout checkList = (LinearLayout) findViewById(R.id.createChecklist);
                final LinearLayout debugLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.checklist_item, checkList, false);
                final AutoCompleteTextView temp = (AutoCompleteTextView) debugLayout.findViewById(R.id.checkNo);
                temp.setOnTouchListener(new View.OnTouchListener(){
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        temp.showDropDown();
                        return false;
                    }
                                        });

                temp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                        //TODO: set focus on next view

                        EditText acTo = (EditText) debugLayout.findViewById(R.id.descOfElem);
                        acTo.setFocusableInTouchMode(true);
                        acTo.requestFocus();
                        Log.e("Create TASK",temp.getText().toString());
                        if(temp.getText().toString().equals("Go "))
                        {
                            Log.e("Create TASK","here");
                            Intent i = new Intent(CreateTask.this, GotoMap.class);
                            startActivityForResult(i, 1);
                            lanlonStr = acTo;
                            acTo.setEnabled(false);

                            Log.e("Editable",lanlonStr.getText().toString());



                        }
                    }
                });
                TextView numb = (TextView) debugLayout.findViewById(R.id.numb);
                numb.setText(Integer.toString(counter) + ".");
                counter ++;
                autoComp(temp);

                checkList.addView(debugLayout);

            }
        });
        //-------------------------------------------------------------------//


        /*Real time updation of amount - Amount would be updated as the user types on fee*/
        final EditText fees = (EditText) findViewById(R.id.fee);
        fees.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = fees.getText().toString();
                TextView total = (TextView) findViewById(R.id.total);

                if(value.length()!=0)
                {

                    total.setText(Integer.toString(Integer.parseInt(value)*3));
                }
                else
                {

                    total.setText("Total Amount");

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //--------------------------------------------------------------------//

        /*Event on Post task button*/
        Button postTask = (Button) findViewById(R.id.post);
        postTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Get urgency level from spinner and convert its type to string
                Spinner urgencyLevel = (Spinner)findViewById(R.id.urgencySpinner);
                String _ulevel =  urgencyLevel.getSelectedItem().toString();

                EditText description = (EditText) findViewById(R.id.desc);
                String _description = description.getText().toString();


                String _fees = fees.getText().toString();
                int _fee;
                if(_fees.equals(""))
                {
                    _fee = 1;
                }
                else
                {
                    _fee = Integer.parseInt(_fees);
                }

                String _tag = _description;
                if(_description.length()>=8)
                {
                    _tag = _description.substring(0, 8);
                }

                Spinner criticalLevel = (Spinner)findViewById(R.id.criticalSpinner);
                String _clevel = criticalLevel.getSelectedItem().toString();

                String _pby = "Tabish";
                String _tlimit = "10";

                //TODO: Get rate from database
                int rate = 3;
                String _rate = "3";
                int amount = _fee * rate;
                String _amount = Integer.toString(amount);

                //TODO: Clean CreateTask

                new postTask().execute(_ulevel,_description,_fees,_tag,_clevel,_pby,_tlimit,_rate,_amount);
            }
        });
    }

    //Todo: Completed and Commented
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Double lat = data.getDoubleExtra("Latitude",0);
                Double lon = data.getDoubleExtra("Longitude",0);

                Toast.makeText(getApplicationContext(), lat.toString()+','+lon.toString(),Toast.LENGTH_SHORT).show();
                lanlonStr.setText(lat.toString()+','+lon.toString());

            }
        }
    }

    private void autoComp(AutoCompleteTextView textView)
    {
        // Get a reference to the AutoCompleteTextView in the layout in arugment

        // Get the string array
                String[] countries = {"Call ","Wait ","Pick ","Drop ","Meet ","Buy ","Sell ","Pay ","Find ","Go ","Collect "};
        // Create the adapter and set it to the AutoCompleteTextView
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);


                textView.setAdapter(adapter);
    }

    //Todo: Completed and Commented
    private  void addSpinnerItems()
    {
    /* Function to populate critical level and Urgency level data
    in spinners. The data is in not  fetched from database to
    save connection time on creating task*/

        /*Adding itmes to Urgency Level Spinner*/
        Spinner urgencyLevel = (Spinner)findViewById(R.id.urgencySpinner);
        List<String> list1 = new ArrayList<String>();
        list1.add("Most");
        list1.add("Medium");
        list1.add("Least");
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list1);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter1.notifyDataSetChanged();
        urgencyLevel.setAdapter(dataAdapter1);

        /*Adding itmes to Critical Level Spinner*/
        Spinner criticalLevel = (Spinner)findViewById(R.id.criticalSpinner);
        List<String> list2 = new ArrayList<String>();
        list2.add("Maximum");
        list2.add("Moderate");
        list2.add("Average");
        list2.add("Light");
        list2.add("Minimum");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.notifyDataSetChanged();
        criticalLevel.setAdapter(dataAdapter2);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Background Async Task to Create new product
     * */
    class postTask extends AsyncTask<String, Void, Integer> {


        /*Initialize JSONarrays for use within async class*/
        JSONArray checklist = new JSONArray();
        JSONArray nature = new JSONArray();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateTask.this);
            pDialog.setMessage("Posting Task..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();



            /*Getting fields of all dynamically added checklists*/
            LinearLayout layout = (LinearLayout) findViewById(R.id.createChecklist);
            int count = layout.getChildCount();
            View v = null;
            for(int i=0; i<count; i++) {
                v = layout.getChildAt(i);
                TextView temp = (TextView) v.findViewById(R.id.descOfElem);
                AutoCompleteTextView temp2 = (AutoCompleteTextView) v.findViewById(R.id.checkNo);

                checklist.put(temp.getText().toString());
                nature.put(temp2.getText().toString());

            }
            //--------------------------------------------//
        }

        /**
         * Creating task
         * */
        protected Integer doInBackground(String... args) {
            Integer successful = 2;

            // Building Parameters
            List<myDict> params = new ArrayList<myDict>();
            params.add(new myDict("urgencylevel", args[0]));
            params.add(new myDict("description", args[1]));
            params.add(new myDict("fees", args[2]));
            params.add(new myDict("tag", args[3]));
            params.add(new myDict("clevel", args[4]));
            params.add(new myDict("postedby", args[5]));
            params.add(new myDict("timelimit", args[6]));
            params.add(new myDict("rate", args[7]));
            params.add(new myDict("amount", args[8]));


            //------------------------------------------//


            //--Adding checklist items as JSON ARRays---P.S The ordering here matters--//
            String checklistStringed = checklist.toString();
            params.add(new myDict("checklistItems", checklistStringed));


            String natureString = nature.toString();
            params.add(new myDict("natureOfElement", natureString));


            //Passing built arguments to JSON parser class//
            JSONObject json = jsonParser.makeHttpRequest(url_create_task,"POST", params);

            // check log cat fro response
            Log.d("Create Response ", json.toString());

            // check for success tag - show success message if task created, otherwise show error
            try {

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // closing this screen
                    successful = 1;
                    finish();
                } else {
                    successful = 2;
                    Log.e("Create TASK","Nothing Created!");

                }
            } catch (JSONException e) {
               successful = 3;
                e.printStackTrace();
            }

            return successful;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Integer success) {
            // dismiss the dialog once done
            pDialog.dismiss();

            if(success==1)
            {
                Toast.makeText(getApplicationContext(), "Task created successfully.",Toast.LENGTH_SHORT).show();
            }
            else if(success==2)
            {
                Toast.makeText(getApplicationContext(), "Nothing Created! Try again.",Toast.LENGTH_SHORT).show();
            }
            else if(success == 3)
            {
                Toast.makeText(getApplicationContext(), "Server Error.",Toast.LENGTH_SHORT).show();
            }
        }

    }
}