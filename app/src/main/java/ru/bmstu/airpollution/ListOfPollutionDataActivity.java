package ru.bmstu.airpollution;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import ru.bmstu.airpollution.model.Pollution;
import ru.bmstu.airpollution.model.PollutionData;
import ru.bmstu.airpollution.model.Toxin;

import static android.content.ContentValues.TAG;

public class ListOfPollutionDataActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TEXT_VIEW_LOADING = "Search in progress...";
    private static final String NOTHING_FOUND = "Nothing found :(";

    private TextView progressTextView;
    private RecyclerView.Adapter itemListAdapter;

    private String toxinType;

    private List<PollutionData> pollutionDataToShow = new ArrayList<>();
    private List<PollutionData> pollutionData = new ArrayList<>();

    public static final String EXTRA_TOXIN_TYPE_VALUE = "toxinTypeValue";
    public static final String EXTRA_SEARCH_X_VALUE = "searchXValue";
    public static final String EXTRA_SEARCH_Y_VALUE = "searchYValue";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_pollution_data);

        // get data from previous activity
        Intent intent = getIntent();
        toxinType = intent.getStringExtra(EXTRA_TOXIN_TYPE_VALUE);
        double x = Double.parseDouble(intent.getStringExtra(EXTRA_SEARCH_X_VALUE));
        double y = Double.parseDouble(intent.getStringExtra(EXTRA_SEARCH_Y_VALUE));
        Log.i(TAG, "onCreate: received: " + toxinType + "; " + x + "; " + y);

        // meanwhile load places
        new RequestPollutionAsyncTask().requestInternet(toxinType, x, y);

        // place keyword to header
        progressTextView = findViewById(R.id.progress_text);

        // configure list
        RecyclerView recyclerView = findViewById(R.id.rv_items);

        RecyclerView.LayoutManager itemListLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(itemListLayoutManager);

        itemListAdapter = new ListOfPollutionDataAdapter(pollutionDataToShow);
        recyclerView.setAdapter(itemListAdapter);

        // mark view as "Loading ..."
        setLoadingTextView();
    }


    private void setLoadingTextView() {
        progressTextView.setText(TEXT_VIEW_LOADING);
    }

    private void setNothingFoundTextView() {
        progressTextView.setText(NOTHING_FOUND);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        updateListSize();
    }

    public void updateListSize() {
        pollutionDataToShow.clear();
        pollutionDataToShow.addAll(pollutionData);

        itemListAdapter.notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    class RequestPollutionAsyncTask extends AsyncTask<URL, Void, String> {
        private static final String openWeatherApiKey = "eac8579e03701ee067c1929181dcbde0";
        private final String openWeatherPollutionSource = "http://api.openweathermap.org/pollution/";
        private String openWeatherApiVersion = "v1";
        private String pollutionType = "co";
        private String location = "0.0,10.0";
        private String datetime = "2019Z";

        private final HashMap<String, String> openWeatherSourceQueryParams = new HashMap<String, String>() {{
            put("appid", openWeatherApiKey);
        }};

        private Uri getUri() {
            List<String> openWeatherSourceAdditionalPathElems = new ArrayList<String>() {{
                add(openWeatherApiVersion);
                add(pollutionType);
                add(location);
                add(datetime + ".json");
            }};
            Uri.Builder builder = Uri.parse(openWeatherPollutionSource).buildUpon();
            openWeatherSourceAdditionalPathElems.forEach(builder::appendPath);
            openWeatherSourceQueryParams.forEach(builder::appendQueryParameter);
            return builder.build();
        }

        private URL getUrl() {
            Uri uri = getUri();
            URL url = null;
            try {
                url = new URL(uri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Log.i("url", url.toString());
            return url;
        }

        @SuppressLint("DefaultLocale")
        void requestInternet(String toxin, double x, double y) {
            pollutionType = toxin.toLowerCase();
            location = x + "," + y;
            Log.i("request", "params: " + pollutionType + "; " + location);
            execute(getUrl());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            StringBuilder result = new StringBuilder();
            for (URL url : urls) {
                try {
                    result.append(getResponseFromHttpUrl(url)).append('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return result.toString();
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            progressTextView.setVisibility(View.GONE);

            Pollution pollution = Pollution.parseFromJsonString(jsonString);
            List<PollutionData> feed = pollution.getData();

            if (null != pollution.getErrorSubscript() || !feed.isEmpty()) {
                pollution.setToxin(new Toxin(toxinType));
//                progressTextView.setText(.getLabel()+" in ("+x+"; "+y+")");
                pollutionData.addAll(feed);
                updateListSize();

                Log.i("request", "data " + pollution.getData().size());
            } else {
                setNothingFoundTextView();

                Log.e("request", "error " + pollution.getErrorSubscript());
            }
        }

        private String getResponseFromHttpUrl(URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();
                Scanner sc = new Scanner(in);
                sc.useDelimiter("\\A");
                boolean hasInput = sc.hasNext();
                if (hasInput) {
                    return sc.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }
    }
}
