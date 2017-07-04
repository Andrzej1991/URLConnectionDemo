package com.company.andrzej.rolki.urlconnectiondemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tvData;

    //1 https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt
    //2 https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoList.txt
    //3 https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnHit = (Button) findViewById(R.id.btnHint);
        tvData = (TextView) findViewById(R.id.tvJsonItem);

        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new JSONTASK().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt");

                new JSONTASK().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoList.txt");

            }

        });

    }

    private class JSONTASK extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("movies");


                StringBuffer finalBufferedData = new StringBuffer();
                for(int i = 0; i < parentArray.length(); i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    String moviewName = finalObject.getString("movie");
                    int year = finalObject.getInt("year");
                    finalBufferedData.append(moviewName + " - " + year + "\n");
                }

                return finalBufferedData.toString();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);
        }
    }
}
