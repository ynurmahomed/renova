package com.renovapp.app;

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.renovapp.app.scraper.HttpClient;


public class AppActivity extends ActionBarActivity implements SettingsFragment.OnFragmentInteractionListener,
                                                             BookListFragment.OnFragmentInteractionListener {

    public static final String EXTRA_LIBRARY_CLIENT = "EXTRA_LIBRARY_CLIENT";

    private ViewPager viewPager;
    private HttpClient library;
    private AppPagerAdapter appPagerAdapter;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);

        final ActionBar actionBar = getSupportActionBar();

        library = (HttpClient) getIntent().getSerializableExtra(EXTRA_LIBRARY_CLIENT);

        appPagerAdapter = new AppPagerAdapter(getSupportFragmentManager());

        prefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        viewPager = (ViewPager) findViewById(R.id.app_view_pager);
        viewPager.setAdapter(appPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                // When swiping between pages, select the
                // corresponding tab.
                getSupportActionBar().setSelectedNavigationItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // When the tab is selected, switch to the
                // corresponding page in the ViewPager.
                viewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        actionBar.addTab(
                actionBar.newTab()
                    //.setText(BookListFragment.TITLE)
                    .setIcon(R.drawable.ic_action_view_as_list)
                    .setTabListener(tabListener)
        );

        actionBar.addTab(
                actionBar.newTab()
                    //.setText(SettingsFragment.TITLE)
                    .setIcon(R.drawable.ic_action_settings)
                    .setTabListener(tabListener)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNotificationDateSelect(int numDays) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getString(R.string.preference_notifications), numDays);
        editor.commit();
    }

    @Override
    public void onLogout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.preference_login), "");
        editor.putString(getString(R.string.preference_password), "");
        editor.commit();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class AppPagerAdapter extends FragmentPagerAdapter {

        public AppPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            Context appContext = AppActivity.this;

            if (i == 0) {
                fragment = BookListFragment.newInstance(library);
            } else if (i == 1) {
                int defaultValue = appContext.getResources().getInteger(R.integer.preference_notifications_default);
                String resource = appContext.getString(R.string.preference_notifications);
                fragment = SettingsFragment.newInstance(prefs.getInt(resource, defaultValue));
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return BookListFragment.TITLE;
            } else if (position == 1) {
                return SettingsFragment.TITLE;
            }
            return "";
        }
    }

}
