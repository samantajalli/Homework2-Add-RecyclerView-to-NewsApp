package com.example.samanimal.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText search;
    private ProgressBar progress;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (EditText) findViewById(R.id.searchQuery);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.displayJSON);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        NetworkTask net = null;

        switch (item.getItemId()) {
            case R.id.Refresh:
                Log.i("Menu", "Refresh clicked");
                net = new NetworkTask(""); // no query string, will refresh page
                return true;
            case R.id.Search:
                Log.i("Menu", "Search clicked");
                net = new NetworkTask("54a9ccbaf0874645903d2c513cfd9be6"); // news api key as query string
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class NetworkTask extends AsyncTask<URL, Void, String> {

        String query;

        NetworkTask(String s) {
            query = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);

        }


        @Override
        protected String doInBackground(URL... params) {
            String result = null;
            String TAG = "MainActivity";
            URL url = NetworkUtils.buildUrl(query);
            Log.d(TAG, "url: " + url.toString());
            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);
            if (s == null) {
                textView.setText("Sorry, no text was received");
            } else {
                textView.setText(s);
            }
        }
    }
}
