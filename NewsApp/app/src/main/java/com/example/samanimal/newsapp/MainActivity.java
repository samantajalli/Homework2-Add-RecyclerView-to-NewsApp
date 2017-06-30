package com.example.samanimal.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivity";
    private EditText search;
    private ProgressBar progress;
    private TextView textView;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (EditText) findViewById(R.id.searchQuery);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.displayJSON);
        rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
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
            case R.id.refresh:
                Log.i("Menu", "Refresh clicked");
                net = new NetworkTask(""); // no query string, will refresh page
                net.execute();
                return true;
            case R.id.search:
                Log.i("Menu", "Search clicked");
                net = new NetworkTask("54a9ccbaf0874645903d2c513cfd9be6"); // news api key as query string
                net.execute();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    class NetworkTask extends AsyncTask<URL, Void, ArrayList<NewsItem> >{

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
        protected ArrayList<NewsItem> doInBackground(URL... params) {
            ArrayList<NewsItem> result = null;
            URL url = NetworkUtils.buildUrl(query);
            Log.d(TAG, "url: " + url.toString());
            try {
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                result = NetworkUtils.parseJSON(json);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute( final ArrayList<NewsItem> data) {
            super.onPostExecute(data);
            progress.setVisibility(View.GONE);
            if (data != null) {
                GithubAdapter adapter = new GithubAdapter(data, new GithubAdapter.ItemClickListener(){

                    @Override
                    public void onItemClick(int clickedItemIndex){ // add code here to finish

                            String url = data.get(clickedItemIndex).getUrl();
                            Log.d(TAG, String.format("Url %s", url));
                        }
                });
                rv.setAdapter(adapter);
            }
        }
    }
}
