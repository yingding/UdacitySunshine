package com.example.ittec.sunshine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    private final String TAG= ForecastFragment.class.getSimpleName();
    private ArrayList<String> weekForecast;
    private ArrayAdapter<String> forecastAdapter;
    private Context appCtx; // applicationContext lives longer as Activity Context
    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        // Set ApplicationContext to member variable
        appCtx = getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);




        // Once the root view for the Fragment has been created,
        // the ListView with some dummy data.

        // Create some dummy data for the ListView. Here's a sample weekly data
        // represented as "day, whether, high/low"
        /*
        String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Froggy - 70/40",
                "Weds - Cloudy - 72/63",
                "Thurs - Asteroids - 75/65",
                "Fri - Heavy Rain - 65/56",
                "Sat - HELP TRAPPED IN WEATHERSTATION - 60/51",
                "Sun - Sunny - 80/68"
        };
        weekForecast = new ArrayList<>(
                Arrays.asList(forecastArray));
        */
        /*
         * used activity context, which contains the globle information of
         * the app environment.
         */
        forecastAdapter = new ArrayAdapter<>(
                // The curent context (the fragment's parent activity)
                getActivity(),
                // Item view reasoure used to created view
                R.layout.list_item_forecast,
                // ID of textView within the list_item_forecast
                R.id.list_item_forecast_textview,
                // weekForecast
                new ArrayList<String>() // empty ArrayList without fake data.
        );
        ListView listView = (ListView) rootView.findViewById(R.id.listView_forecast);
        listView.setAdapter(forecastAdapter);

        // extend listView with onItemClickListener() Callback
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // direct access to the forecastAdapter instead of parent(Uncasted AdapterView)
                // String itemStr = (String) parent.getItemAtPosition(position);
                String itemStr = forecastAdapter.getItem(position);

                /*
                // if the fragment is detached, the getActivity() will give back a null
                // it is better to call getActivity().getApplicationContext() and
                // put it in an instance member variable, while the fragment is created.
                // Toast toast = Toast.makeText(getActivity(), itemString, Toast.LENGTH_LONG);
                Toast toast = Toast.makeText(appCtx, itemStr, Toast.LENGTH_LONG);
                // center toast in horizontal and vertical center of the screen
                // no x,y margin
                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                toast.show();*/
                //Intent intent = DetailActivity.makeIntent(appCtx);
                Intent intent = DetailActivity.makeIntent(getActivity());
                intent.putExtra(DetailActivity.DETAIL_STR_NAME, itemStr);
                /* If calling makeIntent() with application context
                 * or calling startActivity() from outside of an Activity
                 * the following flag for this intent should be set.
                 * This flag will not allow to start multi activity instance,
                 * it will only resume the same existing Activity, which is
                 * in this case acceptable
                 */
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appCtx.startActivity(intent);
            }
        });

        return rootView;
    }

    /**
     * this is called after onViewCreate()
     */
    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    private void updateWeather() {
        FetchWeatherTask weatherTask = new FetchWeatherTask();
        // use activity context which is in the same package
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());//or with appCtx
        String locationStr = prefs.getString(
                // get the key from string resource
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default)
        );
        weatherTask.execute(locationStr);
    }

    private String formatHighLows(double high, double low) {
        // Data is fetched in Celsius by default.
        // If user prefers to see in Fahrenheit, convert the values here.
        // We do this rather than fetching in Fahrenheit so that the user can
        // change this option without us having to re-fetch the data once
        // we start storing the values in a database
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(appCtx);
        String unitType = sharedPrefs.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_metric));
        if (unitType.equals(getString(R.string.))
        )
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchWeatherTask
            extends AsyncTask<String, Void, String[]> {

        private final String TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up, Verify size of params.
            if (params.length == 0) {
                return null;
            }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String[] forecastDataArray = null;

            // Query param values
            String postCodeStr = params[0];
            String format = "json";
            String units = "metric";
            int numDays = 7;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
                final String FORECAST_BASE_URL =
                        "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                Uri.Builder builder = Uri.parse(FORECAST_BASE_URL).buildUpon();
                Uri builtUri = builder.appendQueryParameter(QUERY_PARAM, postCodeStr)
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .build();
                URL url = new URL(builtUri.toString());
                // Log.v(TAG, "Built Url :" + builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    //forecastJsonStr = null;
                    return null; // finally block will be called before this.
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    // forecastJsonStr = null;
                    return null;
                }
                forecastJsonStr = buffer.toString();
                //Log.v(TAG,"WetherData: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            /*
             * return the results
             */
            if (forecastJsonStr != null) {
                WeatherDataParser dataParser = new WeatherDataParser();
                try {
                    forecastDataArray =
                            dataParser.getWeatherDataFromJson(forecastJsonStr, numDays);
                } catch (JSONException e) {
                    Log.e(TAG, "Error ", e);
                }
            }
            //return forecastJsonStr;
            return forecastDataArray;
        }
        // hit control o to see the override methods

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                forecastAdapter.setNotifyOnChange(false);
                forecastAdapter.clear();
                for (String dayForecastStr : result) {
                    forecastAdapter.add(dayForecastStr);
                }
                forecastAdapter.notifyDataSetChanged();
                forecastAdapter.setNotifyOnChange(true);
            }
        }
    }
}
