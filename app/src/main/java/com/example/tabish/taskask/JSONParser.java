
package com.example.tabish.taskask;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import org.json.JSONException;
import java.net.URLEncoder;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    String serverIp = "http://10.20.3.205/taskask/";
    //String serverIp = "http://192.168.0.103/taskask/";

    // constructor
    public JSONParser() {

    }

    /*Function makes http request to the url provided, and returns the json response from server*/
    public JSONObject makeHttpRequest(String urlx, String method,
                                      List<myDict> params) {

        urlx = serverIp + urlx;

        URL url = null;
        HttpURLConnection urlConnection = null;
        String jsonResponse = "";
        InputStream inputStream = null;
        String paramString = "";

        //If method is GET, parameters are ammended in the url
        if(method=="GET")
        {

            try
            {
                //Build a string that would be in URL (because of GET)
                paramString = getPostDataString(params);
            }
            catch (UnsupportedEncodingException e){
                return null;
            }

            urlx += "?" + paramString;
        }




        try {
            //Create new url based on input
           url = new URL(urlx);
        }
        catch (MalformedURLException e)
        {
            Log.e("JSONParser ","Error In Url: " + urlx);
        }

        try {

            // check for request method
            if(method == "POST"){


                //Self explanatiroy code here
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                Log.e("Url conn",urlConnection.getResponseMessage());
                //In post method, parameters doesnt go in url, but written in output stream rather.
                OutputStream os = urlConnection.getOutputStream();


                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(params));
                writer.flush();
                writer.close();
                os.close();

                //Check report of connection from server.
                int responseCode=urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    jsonResponse = readFromStream(urlConnection.getInputStream());
                }
                else {
                    jsonResponse="";
                    Log.e("JSONParser ","POST method! HTTP_OK failed!" + Integer.toString(responseCode));

                }


            }
            //If method is get, then connect accordingly
            else if(method == "GET"){
                // request method is GET
                Log.d("JSON Parser class", url.toString());

                try
                {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(100000);
                    urlConnection.setConnectTimeout(15000);
                    urlConnection.connect();
                }
                catch (IOException e)
                {
                    Log.e("Url connection",e.toString());



                }
                inputStream = urlConnection.getInputStream();

                //format and convert output from server to json formatted string
                try {
                    jsonResponse = readFromStream(inputStream);


                }catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }



            }


        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {


            e.printStackTrace();
        }

        /*Convert JSON string to JSON object*/
        try {
            Log.d("JSON Parser", "JSON Responded: " + jsonResponse);
            jObj = new JSONObject(jsonResponse);
        } catch (JSONException e) {

            Log.e("JSON Parser", "Error parsing data " + e.toString());
            try{
                jObj = new JSONObject();
                jObj.put("success",0);
            }
            catch (JSONException x)
            {
                Log.e("Json exception ","Error creating json object. Fatal Error");
            }
        }

        // return JSON object

        return jObj;

    }

    /*Method that builts the string from input stream - it reads the input stream in UTF 8 and build the string*/
    private  String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line !=null){
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();
    }

    /*Method that ammends the parameters in format that PHP can accept*/
    private String getPostDataString(List<myDict> params) throws UnsupportedEncodingException{
        Log.e("JSON HERE"," REAACHER");
        StringBuilder result = new StringBuilder();
        int i = 0;
        for(myDict element : params){
            if (i!=0)
            {
                result.append("&");

            }
            else
            {
                i=1;
            }
            result.append(URLEncoder.encode(element.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(element.getValue(), "UTF-8"));


        }

        return result.toString();
    }
}