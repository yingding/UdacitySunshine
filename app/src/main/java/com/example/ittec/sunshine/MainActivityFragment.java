package com.example.ittec.sunshine;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // Once the root view for the Fragment has been created,
        // the ListView with some dummy data.

        // Create some dummy data for the ListView. Here's a sample weekly data
        // represented as "day, whether, high/low"
        String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Froggy - 70/40",
                "Weds - Cloudy - 72/63",
                "Thurs - Asteroids - 75/65",
                "Fri - Heavy Rain - 65/56",
                "Sat - HELP TRAPPED IN WEATHERSTATION - 60/51",
                "Sun - Sunny - 80/68"
        };
        ArrayList<String> weekForecast= new ArrayList<>(
                Arrays.asList(forecastArray));
        /*
         * used activity context, which contains the globle information of
         * the app environment.
         */
        ArrayAdapter<String> forecastAdapter = new ArrayAdapter<>(
                // The curent context (the fragment's parent activity)
                getActivity(),
                // Item view reasoure used to created view
                R.layout.list_item_forecast,
                // ID of textView within the list_item_forecast
                R.id.list_item_forecast_textview,
                weekForecast
        );
        ListView listView = (ListView) rootView.findViewById(R.id.listView_forecast);
        listView.setAdapter(forecastAdapter);
        return rootView;
    }
}
