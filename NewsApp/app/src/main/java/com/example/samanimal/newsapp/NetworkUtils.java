package com.example.samanimal.newsapp;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Samanimal on 6/20/17.
 */

public class NetworkUtils {

    public static final String base_url=
            "https://newsapi.org/v1/articles";

    public static final String source_parameter = "source";
    public static final String sortBy_parameter = "sortBy";
    public static final String apiKey_parameter = "apiKey";



    public static URL buildUrl(String locationQuery) {

        URL url = null;
        try {
            Uri uri = Uri.parse(base_url).buildUpon()
                    .appendQueryParameter(source_parameter, "the-next-web")
                    .appendQueryParameter(sortBy_parameter, "latest")
                    .appendQueryParameter(apiKey_parameter, locationQuery).build();
            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("//A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
