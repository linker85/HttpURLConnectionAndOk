package com.example.com.jsonlibexamples;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.com.jsonlibexamples.models.Example;
import com.example.com.jsonlibexamples.models.Result;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTAG_";
    //private String json = "{\"results\":[{\"gender\":\"male\",\"name\":{\"title\":\"mr\",\"first\":\"حامد\",\"last\":\"احمدی\"},\"location\":{\"street\":\"5091 17 شهریور\",\"city\":\"بجنورد\",\"state\":\"قزوین\",\"postcode\":77808},\"email\":\"حامد.احمدی@example.com\",\"login\":{\"username\":\"organicfrog860\",\"password\":\"orchard\",\"salt\":\"92DtIuTP\",\"md5\":\"57f6d4407484d2c679ef38bf7d8084ef\",\"sha1\":\"cec25e60fff205e22a22a1174f0c6ecbf060493b\",\"sha256\":\"c629405075f75e4eab44ed177e1cbea6726cdbb5b4de5c7aacee397d3e9b358d\"},\"dob\":\"1972-06-28 14:34:58\",\"registered\":\"2003-09-04 13:28:18\",\"phone\":\"045-96816790\",\"cell\":\"0978-285-0124\",\"id\":{\"name\":\"\",\"value\":null},\"picture\":{\"large\":\"https://randomuser.me/api/portraits/men/74.jpg\",\"medium\":\"https://randomuser.me/api/portraits/med/men/74.jpg\",\"thumbnail\":\"https://randomuser.me/api/portraits/thumb/men/74.jpg\"},\"nat\":\"IR\"}],\"info\":{\"seed\":\"4ca25626a3830bec\",\"results\":1,\"page\":1,\"version\":\"1.1\"}}";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getJSON();

        // should be a singleton
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://randomuser.me/api")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding

                // Read data on the worker thread
                final String responseData = response.body().string();

                // Run view-related code back on the main thread
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(TAG, "run: " + responseData);
                            Gson gson = new Gson();
                            Example obj = gson.fromJson(responseData, Example.class);
                            List<Result> lista = obj.getResults();
                            for (Result e : lista) {
                                Log.d(TAG, "onCreate: " + e.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    private void getJSON() {
        // because its running on the main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("https://randomuser.me/api");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch( Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        Log.d(TAG, "getJSON: " + result.toString());
        try {
            Gson gson = new Gson();
            Example obj = gson.fromJson(result.toString(), Example.class);
            List<Result> lista = obj.getResults();
            for (Result e : lista) {
                Log.d(TAG, "getJSON->: " + e.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
