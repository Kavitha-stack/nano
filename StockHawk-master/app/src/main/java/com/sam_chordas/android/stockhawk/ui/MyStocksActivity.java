package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.QuoteCursorAdapter;
import com.sam_chordas.android.stockhawk.rest.RecyclerViewItemClickListener;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.service.StockIntentService;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.melnykov.fab.FloatingActionButton;
import com.sam_chordas.android.stockhawk.touch_helper.SimpleItemTouchHelperCallback;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MyStocksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Intent mServiceIntent;
    private ItemTouchHelper mItemTouchHelper;
    private static final int CURSOR_LOADER_ID = 0;
    private QuoteCursorAdapter mCursorAdapter;
    private Context mContext;
    private Cursor mCursor;
    boolean isConnected;

    private String className = MyStocksActivity.class.getSimpleName();

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;


        setContentView(R.layout.activity_my_stocks);
        // The intent service is for executing immediate pulls from the Yahoo API
        // GCMTaskService can only schedule tasks, they cannot execute immediately
        mServiceIntent = new Intent(this, StockIntentService.class);
        if (savedInstanceState == null) {
            // Run the initialize task service so that some stocks appear upon an empty database
            mServiceIntent.putExtra("tag", "init");
            if (isConnected()) {
                startService(mServiceIntent);
            } else {
                networkToast();
            }
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
/*
        TextView t = (TextView) findViewById(R.id.noData);
        Log.v(className, "before setting visibility : "+mCursorAdapter.getItemCount());
        if (mCursorAdapter.getItemCount() == 0) {
            t.setVisibility(View.VISIBLE);
        } else {
            t.setVisibility(View.GONE);
        }

*/
        mCursorAdapter = new QuoteCursorAdapter(this, null);
        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
                new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {


                        mCursor.moveToPosition(position);
                        String symbol = mCursor.getString(mCursor.getColumnIndex("symbol"));

                        Toast.makeText(getBaseContext(), symbol+" selected.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                        intent.putExtra(getString(R.string.symbol), symbol);
                        startActivity(intent);

                        /*
                        Intent detailIntent = new Intent(getBaseContext(), DetailActivity.class);
                        detailIntent.putExtra("symbol", "testing");
                        startActivity(detailIntent);
*/

                    }
                }));
        recyclerView.setAdapter(mCursorAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    MaterialDialog show = new MaterialDialog.Builder(mContext).title(R.string.symbol_search)
                            .content(R.string.content_test)
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {

                                    // On FAB click, receive user input. Make sure the stock doesn't already exist
                                    // in the DB and proceed accordingly
                                    final String sInput = input.toString().toUpperCase();

                                        Cursor c = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                                                new String[]{QuoteColumns.SYMBOL}, QuoteColumns.SYMBOL + "= ?",
                                                new String[]{sInput}, null);
                                        if (c.getCount() != 0) {
                                            Toast toast =
                                                    Toast.makeText(MyStocksActivity.this, getString(R.string.exist_stock),
                                                            Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                            toast.show();
                                            return;
                                        } else {
                                            // Add the stock to DB
                                            mServiceIntent.putExtra("tag", getString(R.string.add_tag));
                                            mServiceIntent.putExtra("symbol", sInput);

                                            startService(mServiceIntent);
                                        }




                                 //   final String sInput = input.toString().toUpperCase();
                                /*    AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
                                        @Override
                                        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

                                            Cursor c = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                                                    new String[]{QuoteColumns.SYMBOL}, QuoteColumns.SYMBOL + getString(R.string.ques),
                                                    new String[]{sInput}, null);

                                            if (c.getCount() != 0) {
                                                Toast toast =
                                                        Toast.makeText(MyStocksActivity.this, getString(R.string.exist_stock),
                                                                Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, Gravity.CENTER, 0);
                                                toast.show();
                                                return;
                                            } else {
                                                // Add the stock to DB
                                                mServiceIntent.putExtra("tag", getString(R.string.add_tag));
                                                mServiceIntent.putExtra("symbol", sInput.toLowerCase());

                                                startService(mServiceIntent);
                                            }

                                        }
                                    };
                                    queryHandler.startQuery(
                                            1, null,
                                            QuoteProvider.Quotes.CONTENT_URI,
                                            new String[]{QuoteColumns.SYMBOL},
                                            QuoteColumns.SYMBOL + getString(R.string.ques),
                                            new String[]{sInput}, null
                                    );*/

                                }
                            })
                            .show();
                } else {
                    networkToast();
                }



            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCursorAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        mTitle = getTitle();
        if (isConnected) {
            long period = 3600L;
            long flex = 10L;
            String periodicTag = "periodic";

            // create a periodic task to pull stocks once every hour after the app has been opened. This
            // is so Widget data stays up to date.
            PeriodicTask periodicTask = new PeriodicTask.Builder()
                    .setService(StockTaskService.class)
                    .setPeriod(period)
                    .setFlex(flex)
                    .setTag(periodicTag)
                    .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                    .setRequiresCharging(false)
                    .build();
            // Schedule task with tag "periodic." This ensure that only the stocks present in the DB
            // are updated.
            GcmNetworkManager.getInstance(this).schedule(periodicTask);
        }
    }


    @Override
    public void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    public void networkToast() {
        Toast.makeText(mContext, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_stocks, menu);
        restoreActionBar();
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

        if (id == R.id.action_change_units) {
            // this is for changing stock changes from percent value to dollar value
            Utils.showPercent = !Utils.showPercent;
            this.getContentResolver().notifyChange(QuoteProvider.Quotes.CONTENT_URI, null);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This narrows the return to only the stocks that are most current.
        return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
    }


    private void updateEmptyView() {
        Log.v(className, "inside update view");
        // update the text message based on the status of server.
        TextView tv = (TextView) findViewById(R.id.noData);
        if (mCursorAdapter.getItemCount() == 0) {

            tv.setVisibility(View.VISIBLE);
            if (null != tv) {
                Log.v(className, "inside tv not null");
                String message = getBaseContext().getString(R.string.no_data);
                if (!isConnected()) {
                    Log.v(className, "! is connected condtion update view");
                    message = getBaseContext().getString(R.string.no_data_suggestion);
                }
                Log.v(className, "message in  update view:" + message);
                tv.setText(message);
            }
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        mCursorAdapter.swapCursor(data);
        mCursor = data;

        updateEmptyView();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_server_status_key))) {
            updateEmptyView();
        }
    }

/*
    @Retention(RetentionPolicy.CLASS.SOURCE)
    @IntDef({SERVER_STATUS_OK, SERVER_STATUS_DOWN, SERVER_STATUS_INVALID, SERVER_UNKNOWN})
    public @interface ServerMode {
    }

    public static final int SERVER_STATUS_OK = 0;
    public static final int SERVER_STATUS_DOWN = 1;
    public static final int SERVER_STATUS_INVALID = 2;
    public static final int SERVER_UNKNOWN = 3;



    @Retention(RetentionPolicy.CLASS.SOURCE)
    @IntDef({SERVER_STOCK_OK,  STOCK_INVALID, STOCK_UNKNOWN})
    public @interface StockMode {
    }

    public static final int SERVER_STOCK_OK = 0;
    public static final int STOCK_INVALID = 1;
    public static final int STOCK_UNKNOWN = 2;
*/
   /*public abstract void setServerMode(@ServerMode int mode);
    @ServerMode
    public abstract int getServerMode();*/


    /**
     * @param c Context used to get the SharedPreferences
     * @return the location status integer type
     */
   /* @SuppressWarnings("ResourceType")
    static public
    @MyStocksActivity.ServerMode
    int getServerStatus(Context c) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getInt(c.getString(R.string.pref_server_status_key), MyStocksActivity.SERVER_UNKNOWN);
    }*/


}
