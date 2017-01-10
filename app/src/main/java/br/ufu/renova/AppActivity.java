package br.ufu.renova;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import br.ufu.renova.books.BooksFragment;
import br.ufu.renova.books.BooksPresenter;
import br.ufu.renova.preferences.PreferencesContract;
import br.ufu.renova.preferences.PreferencesPresenter;
import br.ufu.renova.preferences.SettingsFragment;
import br.ufu.renova.datasource.ILibraryDataSource;

import java.util.HashMap;
import java.util.Map;


public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_library_books_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_settings_white_24dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.app_view_pager);
        AppPagerAdapter appPagerAdapter = new AppPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(appPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
                ILibraryDataSource dataSource = Injection.provideDataSource();
                BooksPresenter presenter = new BooksPresenter(frag, dataSource);
                frag.setPresenter(presenter);
                fragment = frag;
            } else if (index == SETTINGS_FRAGMENT) {
                SettingsFragment frag = new SettingsFragment();
                PreferencesContract.AppPreferences prefs = Injection.provideAppPreferences(getApplicationContext());
                PreferencesPresenter presenter = new PreferencesPresenter(frag, prefs);
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

        public Fragment getFragment(int index) {
            return mPageReferenceMap.get(index);
        }
    }
}
