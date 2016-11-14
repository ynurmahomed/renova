package br.ufu.renova;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import br.ufu.renova.books.BooksFragment;
import br.ufu.renova.books.BooksPresenter;
import br.ufu.renova.preferences.AppPreferences;
import br.ufu.renova.preferences.PreferencesContract;
import br.ufu.renova.preferences.PreferencesFragment;
import br.ufu.renova.preferences.PreferencesPresenter;
import br.ufu.renova.scraper.UFUHttpClient;

import java.util.HashMap;
import java.util.Map;


public class AppActivity extends ActionBarActivity {

    private ViewPager viewPager;

    private AppPagerAdapter appPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);

        final ActionBar actionBar = getSupportActionBar();

        appPagerAdapter = new AppPagerAdapter(getSupportFragmentManager());

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
                        //.setText(BooksFragment.TITLE)
                        .setIcon(R.drawable.ic_action_view_as_list)
                        .setTabListener(tabListener)
        );

        actionBar.addTab(
                actionBar.newTab()
                        //.setText(PreferencesFragment.TITLE)
                        .setIcon(R.drawable.ic_action_settings)
                        .setTabListener(tabListener)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class AppPagerAdapter extends FragmentPagerAdapter {

        private Map<Integer, Fragment> mPageReferenceMap;

        public final static int BOOK_LIST_FRAGMENT = 0;
        public final static int SETTINGS_FRAGMENT = 1;

        public AppPagerAdapter(FragmentManager fm) {
            super(fm);
            mPageReferenceMap = new HashMap<>();
        }

        @Override
        public Fragment getItem(int index) {
            Fragment fragment = null;

            if (index == BOOK_LIST_FRAGMENT) {
                BooksFragment frag = BooksFragment.newInstance();
                UFUHttpClient httpClient = UFUHttpClient.getInstance();
                BooksPresenter presenter = new BooksPresenter(frag, httpClient);
                frag.setPresenter(presenter);
                fragment = frag;
            } else if (index == SETTINGS_FRAGMENT) {
                PreferencesFragment frag = PreferencesFragment.newInstance();
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                PreferencesContract.AppPreferences preferences = new AppPreferences(shared);
                PreferencesPresenter presenter = new PreferencesPresenter(frag, preferences);
                frag.setPresenter(presenter);
                fragment = frag;
            }

            mPageReferenceMap.put(index, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == BOOK_LIST_FRAGMENT) {
                return BooksFragment.TITLE;
            } else if (position == SETTINGS_FRAGMENT) {
                return PreferencesFragment.TITLE;
            }
            return "";
        }

        public Fragment getFragment(int index) {
            return mPageReferenceMap.get(index);
        }
    }
}
