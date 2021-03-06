package com.example.ittec.sunshine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {
    private final static String TAG = DetailActivity.class.getSimpleName();
    public final static String DETAIL_STR_NAME = "detailName";
    private static String shareStrSurfix;
    private ShareActionProvider mShareActionProvider;
    private String detailStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            // added the new fragment to the Framelayout with id container in
            // activity_detail
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        Intent intent = getIntent();
        if (intent != null) {
            detailStr = intent.getStringExtra(DETAIL_STR_NAME);
        }
        shareStrSurfix =" #" + getString(R.string.app_name);
        //TextView tv = (TextView) findViewById(R.id.detail_string);
        //tv.setText(intent.getStringExtra(DETAIL_STR_NAME));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.detail_menu_item_share);

        // Fetch and store ShareActionProvider
        // use MenuItemCompat.getActionProvoder instead of item.getActionProvider()
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND)
                // prevent the activity to be shared to from placed to activity stack.
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                .putExtra(Intent.EXTRA_TEXT, detailStr + shareStrSurfix)
                .setType("text/plain");
        mShareActionProvider.setShareIntent(shareIntent);
        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        } else {
            Log.d(TAG, "Share Action Provider is null?");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(SettingsActivity.makeIntent(this));
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
            // this containers no id.
            View rootView = inflater.inflate(
                    R.layout.fragment_detail,
                    container,
                    false);
            //The detail Activity called via intent.
            Intent intent = getActivity().getIntent();
            if(intent != null
                    && intent.getStringExtra(DETAIL_STR_NAME) != null) {
                TextView forecast = (TextView) rootView
                        .findViewById(R.id.forecast_detail);
                // Activity Content is used to get the calling intent.
                forecast.setText(
                        getActivity().getIntent().getStringExtra(DETAIL_STR_NAME));
            }
            return rootView;
        }
    }
}