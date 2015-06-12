package com.example.ittec.sunshine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {
    public final static String DETAIL_STR_NAME = "detailName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        //Intent intent = getIntent();
        //TextView tv = (TextView) findViewById(R.id.detail_string);
        //tv.setText(intent.getStringExtra(DETAIL_STR_NAME));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context ctx) {
        // return an Explizit Intent
        return new Intent(ctx, DetailActivity.class);
    }

    /**
     * A Placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {
        public PlaceholderFragment() {
        }
        // hit control o to see the overload methods in Android Studio
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment_detail,
                    container,
                    false);
            TextView forecast = (TextView) rootView
                    .findViewById(R.id.forecast_detail);
            // Activity Content is used to get the calling intent.
            forecast.setText(
                    getActivity().getIntent().getStringExtra(DETAIL_STR_NAME));
            return rootView;
        }
    }
}