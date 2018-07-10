package com.example.mohamed.maghreb;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void click(View view){
        EditText editText = findViewById(R.id.editText);
        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+ editText.getText().toString() +"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        new MyasincTask().execute(url);
    }
    public class MyasincTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(1000);
                String dataJson = convert(urlConnection.getInputStream());
                publishProgress(dataJson);
            }catch(Exception e){
                e.printStackTrace();
            }
            return "";
        }
        public String convert(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String allString="";
            do{
                line = bufferedReader.readLine();
                if(line!=null){
                allString += line;
                }
            }while(line!=null);
            return allString;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            try {
                JSONObject json = new JSONObject(values[0]);
                JSONObject query = json.getJSONObject("query");
                JSONObject results = query.getJSONObject("results");
                JSONObject channel = results.getJSONObject("channel");
                JSONObject astronomy = channel.getJSONObject("astronomy");
                String sunrise = astronomy.getString("sunset");
                TextView textView = findViewById(R.id.textView);
                textView.setText(sunrise);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
