package com.example.vizora;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FomenuActivity extends AppCompatActivity {
    private static final String LOG_TAG = FomenuActivity.class.getName();
    private FirebaseUser user;
    // private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<NewsThing> mNewsList;
    private NewsThingAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mNews;

    private int gridNumber = 1;
    private int queryLimit = 10;

    private NotificationHandler mNotificationHandler;
    private AlarmManager mAlarmManager;
    private JobScheduler mJobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fomenu);
        // mAuth = FirebaseAuth.getInstance();
        // mAuth.signOut();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            Log.d(LOG_TAG, "Authenticated user!");
        }else{
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mNewsList = new ArrayList<>();
        mAdapter = new NewsThingAdapter(this, mNewsList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mNews = mFirestore.collection("News");

        queryData();
        //intializeData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReceiver, filter);

        mNotificationHandler = new NotificationHandler(this);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mJobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        //setAlarmManager();

        setJobScheduler();
    }

    BroadcastReceiver powerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action == null){
                return;
            }
            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    queryLimit = 10;
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    queryLimit = 5;
                    break;
            }

            queryData();
        }
    };

    private void queryData(){
        mNewsList.clear();

        mNews.orderBy("date").limit(5).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                NewsThing thing = document.toObject(NewsThing.class);
                mNewsList.add(thing);
            }
            if(mNewsList.size() == 0) {
                intializeData();
                queryData();
            }

            mAdapter.notifyDataSetChanged();
        });
    }

    private void intializeData() {
        String[] newsTitleList = getResources().getStringArray(R.array.news_thing_names);
        String[] newsDateList = getResources().getStringArray(R.array.news_thing_dates);
        String[] newsInfoList = getResources().getStringArray(R.array.news_thing_infos);

        //mNewsList.clear();

        for (int i= 0; i < newsTitleList.length; i++){
            mNews.add(new NewsThing(newsTitleList[i], newsDateList[i], newsInfoList[i]));
            //mNewsList.add(new NewsThing(newsTitleList[i], newsDateList[i], newsInfoList[i]));
        }

        //mAdapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.news_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {return false;}

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.newsPage:
                Log.d(LOG_TAG, "Clicked on NewsPage!");
                return true;
            case R.id.billsPage:
                Log.d(LOG_TAG, "Clicked on BillsPage!");
                return true;
            case R.id.DiktalasPage:
                Log.d(LOG_TAG, "Clicked on DiktalasPage!");
                return true;
            case R.id.settingsPage:
                Log.d(LOG_TAG, "Clicked on SettingsPage!");
                return true;
            case R.id.ContactPage:
                Log.d(LOG_TAG, "Clicked on ContactPage!");
                return true;
            case R.id.logoutButton:
                Log.d(LOG_TAG, "Clicked on LogoutButton!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /*private void changeSpanCount(MenuItem item, int drawableId, int spanCount){
        viewRow = !viewRow;
        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }*/


    public boolean onPrepareOptionsMenu(Menu menu) {
        /*
        final MenuItem alertMenuItem = menu.findItem(R.id.valami);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.valami2);
        countTextView = (TextView) rootView.findViewById(R.id.valami3);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });
        return super.onPrepareOptionsMenu(menu);
        */
        return false;
    }

    public void updateAlertIcon(NewsThing news){
        mNotificationHandler.send(news.getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReceiver);
    }

    private void setAlarmManager(){
        long repeatInterval = 60 * 1000;
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                repeatInterval,
                pendingIntent);

        // mAlarmManager.cancel(pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setJobScheduler() {
        int networkType = JobInfo.NETWORK_TYPE_UNMETERED;
        int hardDeadLine = 5000;

        ComponentName name = new ComponentName(getPackageName(), NotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(0, name)
                .setRequiredNetworkType(networkType)
                .setRequiresCharging(true)
                .setOverrideDeadline(hardDeadLine);

        mJobScheduler.schedule(builder.build());
        // mJobScheduler.cancel(0);
    }
}