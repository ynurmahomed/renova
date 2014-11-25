package com.renovapp.app;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.renovapp.app.scraper.Book;
import com.renovapp.app.scraper.HttpClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AppActivity extends ActionBarActivity implements SettingsFragment.OnFragmentInteractionListener,
                                                             BookListFragment.OnFragmentInteractionListener {

    ViewPager viewPager;
    HttpClient library;
    AppPagerAdapter appPagerAdapter;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app);

        final ActionBar actionBar = getSupportActionBar();

        library = (HttpClient) getIntent().getSerializableExtra(LoginActivity.EXTRA_LIBRARY_CLIENT);

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

        createBooksHash();

        //createNotifications();
        //setNotifications();
        testee();

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

    private void createBooksHash(){
        for (Book b: library.getBooks()) {
            BooksListGlobal.getInstance().setBook(b);
        }
    }

    /*
    private void createNotifications() {
        DateFormat dateFormat = new SimpleDateFormat("dd 'de' MMMM", new Locale("pt", "BR"));

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher);

        Intent resultIntent = new Intent(this, LoginActivity.class);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        for (Book b: library.getBooks()) {
            mBuilder.setContentTitle(b.getTitle());
            mBuilder.setContentText("Livro com vencimento em " + dateFormat.format(b.getExpiration()));
            mBuilder.setContentIntent(stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));

            mNotificationManager.notify(0, mBuilder.build());
        }

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
    */


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

    ///*
    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        //Notification.Builder builder = new Notification.Builder(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder.build();
    }

    public void setNotifications(/*int days, Book book*/){
        scheduleNotification(getNotification("5 second delay"), 10000);
    }
    //*/



    public void testee(){
        // get a Calendar object with current time
        for(Book b : BooksListGlobal.getInstance().getAllBooks().values()){
            /*
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, 10);
            Intent intent = new Intent(this, NotificationPublisher.class);
            //Intent intent = new Intent(this, LoginActivity.class);
            //intent.putExtra("alarm_message", "O'Doyle Rules!");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher);
            builder.setContentTitle("Renove seu(s) livro(s)");
            builder.setContentText(b.getTitle());
            //builder.setSmallIcon(R.drawable.ic_launcher);
            intent.putExtra(b.getBarcode(), 1);
            intent.putExtra(b.getTitle(), builder.build());

            // In reality, you would want to have a static variable for the request code instead of 192837
            PendingIntent sender = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Get the AlarmManager service
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
            //*/
        }
    }

}
