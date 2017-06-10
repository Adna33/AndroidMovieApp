package atlant.moviesapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import atlant.moviesapp.R;
import atlant.moviesapp.fragments.MovieFragment;
import atlant.moviesapp.fragments.NewsFeedFragment;
import atlant.moviesapp.fragments.SearchFragment;
import atlant.moviesapp.fragments.TvShowFragment;
import atlant.moviesapp.helper.SharedPrefsUtils;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyRating;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.presenters.MainActivityPresenter;
import atlant.moviesapp.realm.models.RealmPostMovie;
import atlant.moviesapp.realm.models.RealmPostSeries;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.receivers.ConnectivityStateReceiver;
import atlant.moviesapp.views.MainActivityView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements MainActivityView, NavigationView.OnNavigationItemSelectedListener, ConnectivityStateReceiver.ConnectivityStateReceiverListener {

    private static final String SELECTED_ITEM = "arg_selected_item";

    @BindView(R.id.activity_main)
    LinearLayout linearLayout;

    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigation;

    @BindView(R.id.container)
    FrameLayout container;

    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.main_drawer)
    NavigationView drawerList;

    private int mSelectedItem;
    private MainActivityPresenter presenter;
    private ConnectivityStateReceiver receiver;
    private boolean registeredReceiver = false;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    EditText edtSeach;
    private int mSelectedId;
    InputMethodManager imm;
    private static final int MOVIE = 0;
    private static final int TVSHOW = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        receiver = new ConnectivityStateReceiver();
        receiver.addListener(this);
        this.registerReceiver(receiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        registeredReceiver = true;


        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        presenter = new MainActivityPresenter(this);
        // isSearchOpened = false;
        //bottom navigation

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mSelectedItem = presenter.selectFragment(item);
                return true;
            }
        });


        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = bottomNavigation.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = bottomNavigation.getMenu().getItem(0);


        }
        mSelectedItem = presenter.selectFragment(selectedItem);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        checkLogin();


    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();
        if (ApplicationState.isLoggedIn()) {
            selectItem(mSelectedId);
        } else
            selectItemGuest(mSelectedId);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //save selected item so it will remains same even after orientation change
        outState.putInt(getString(R.string.selectedId), mSelectedId);
    }

    private void selectItem(int mSelectedId) {

        switch (mSelectedId) {
            case R.id.nav_favourites:
                Intent fav = new Intent(this, UserFavoritesActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(fav);
                break;

            case R.id.nav_watchlist:
                Intent watch = new Intent(this, UserWatchlistActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(watch);
                break;

            case R.id.nav_ratings:
                Intent rate = new Intent(this, UserRatingsActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(rate);
                break;

            case R.id.nav_logout:
                ApplicationState.setUser(null);
                RealmUtil.getInstance().deleteRealmAccount();
                SharedPrefsUtils.removePref(this,getString(R.string.userDetails));
                drawerLayout.closeDrawer(GravityCompat.START);
                recreate();
                break;
            case R.id.nav_settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(settings);
                break;

        }

    }

    private void selectItemGuest(int mSelectedId) {
        final int REQUEST = 1;

        switch (mSelectedId) {

            case R.id.nav_login:
                Intent intent = new Intent(this, LoginActivity.class);
                drawerLayout.closeDrawer(drawerList);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (isSearchOpened) {
            if (getCurrentFocus().getWindowToken() != null)
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            setupDrawerToggle();
            handleMenuSearch();


            return;
        }
        MenuItem homeItem = bottomNavigation.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            mSelectedItem = presenter.selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void showFragment(Fragment fragment, MenuItem item) {
        for (int i = 0; i < bottomNavigation.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigation.getMenu().getItem(i);
            if (menuItem.getItemId() == item.getItemId()) {
                menuItem.setChecked(true);
            } else {
                menuItem.setChecked(false);
            }
        }


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment, fragment.getTag());
            ft.commit();
        }

        if (item.getItemId() == bottomNavigation.getMenu().getItem(0).getItemId()) {
            bottomNavigation.getMenu().getItem(0).setChecked(true);
        }

    }

    @Override
    public void setupToolbar(String s) {
        //  setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(s);
    }


    @Override
    public void checkLogin() {

        //navigation Drawer
        mTitle = mDrawerTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerList.setNavigationItemSelectedListener(this);
        setupDrawerToggle();

        if (ApplicationState.isLoggedIn()) {
            drawerList.getMenu().clear();
            drawerList.inflateMenu(R.menu.menu_drawer_login);

            View header = drawerList.getHeaderView(0);
            TextView name = (TextView) header.findViewById(R.id.nav_header_username);
            name.setText(ApplicationState.getUser().getUsername());


        } else {
            drawerList.getMenu().clear();
            drawerList.inflateMenu(R.menu.menu_drawer_logout);
            View header = drawerList.getHeaderView(0);
            TextView name = (TextView) header.findViewById(R.id.nav_header_username);
            name.setText("");

        }

        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupDrawerToggle() {
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:

                if (isNetworkAvailable()) {
                    handleMenuSearch();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getString(R.string.errorMessage))
                            .setTitle(getString(R.string.errorTitle));
                    builder.setPositiveButton(getString(R.string.OkButton), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch() {

        ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open
            bottomNavigation.setVisibility(View.VISIBLE);


            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_search));

            action.setDisplayHomeAsUpEnabled(false);
            action.setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setupDrawerToggle();
            bottomNavigation.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;

            Fragment fragment = null;
            switch (mSelectedItem) {
                case R.id.menu_feed:
                    fragment = new NewsFeedFragment();
                    break;
                case R.id.menu_movies:
                    fragment = new MovieFragment();
                    break;
                case R.id.menu_tvshows:
                    fragment = new TvShowFragment();
                    break;
            }
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment, fragment.getTag());
                ft.commit();
            }

            isSearchOpened = false;
        } else {

            action.setDisplayHomeAsUpEnabled(true);
            action.setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            bottomNavigation.setVisibility(View.INVISIBLE);
            bottomNavigation.getLayoutParams().height = 0;

            action.setDisplayShowCustomEnabled(true);
            action.setCustomView(R.layout.search_bar);
            action.setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            edtSeach = (EditText) action.getCustomView().findViewById(R.id.editSearch); //the text editor
            edtSeach.getBackground().clearColorFilter();


            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        if (edtSeach.getText() != null)
                            doSearch(edtSeach.getText());
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        return true;
                    }
                    return false;
                }
            });

            edtSeach.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                private Timer timer = new Timer();
                private final long DELAY = 500;

                @Override
                public void afterTextChanged(final Editable s) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    // TODO: do what you need here (refresh list)
                                    if (edtSeach.getText() != null)
                                        doSearch(s);
                                }
                            },
                            DELAY
                    );

                }
            });

            edtSeach.requestFocus();


            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            mSearchAction.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));

            isSearchOpened = true;
        }
    }

    private void doSearch(CharSequence s) {

        String str = s.toString();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.edttext), str);

        SearchFragment fragobj = new SearchFragment();
        fragobj.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragobj, getString(R.string.search));

        ft.commit();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStart() {
        if (!isSearchOpened) {
            setupDrawerToggle();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onStart();
    }

    @Override
    protected void onPostResume() {
        if (!isSearchOpened) {
            setupDrawerToggle();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkLogin();
        if (!registeredReceiver) {
            receiver = new ConnectivityStateReceiver();
            receiver.addListener(this);
            this.registerReceiver(receiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
            registeredReceiver = true;
        }

        super.onPostResume();
    }


    @Override
    public void networkAvailable() {

        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        if (ApplicationState.isLoggedIn()) {
            List<RealmPostMovie> postMovies = RealmUtil.getInstance().getAllPostMovies();
            List<RealmPostSeries> postSeries = RealmUtil.getInstance().getAllPostSeries();
            if (postMovies != null)
                for (RealmPostMovie movie : postMovies) {
                    BodyFavourite bodyFavourite = new BodyFavourite(getString(R.string.movie), movie.getId(), movie.isFavorite());
                    presenter.postFavorite(movie.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
                    BodyWatchlist bodyWatchlist = new BodyWatchlist(getString(R.string.movie), movie.getId(), movie.isInWatchlist());
                    presenter.postWatchlist(movie.getId(), ApplicationState.getUser().getSessionId(), bodyWatchlist);
                    if (movie.getRating() != null) {
                        BodyRating bodyRating = new BodyRating(Double.parseDouble(movie.getRating()));
                        presenter.postRating(movie.getId(), ApplicationState.getUser().getSessionId(), bodyRating, MOVIE);
                    }

                }
            if (postSeries != null)
                for (RealmPostSeries series : postSeries) {
                    BodyFavourite bodyFavourite = new BodyFavourite(getString(R.string.tv), series.getId(), series.isFavorite());
                    presenter.postFavorite(series.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
                    BodyWatchlist bodyWatchlist = new BodyWatchlist(getString(R.string.tv), series.getId(), series.isInWatchlist());
                    presenter.postWatchlist(series.getId(), ApplicationState.getUser().getSessionId(), bodyWatchlist);
                    if (series.getRating() != null) {
                        BodyRating bodyRating = new BodyRating(Double.parseDouble(series.getRating()));
                        presenter.postRating(series.getId(), ApplicationState.getUser().getSessionId(), bodyRating, TVSHOW);
                    }
                }
            RealmUtil.getInstance().deleteAllPostMovies();
            RealmUtil.getInstance().deleteAllPostSeries();


        }

    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onStop();
        if (registeredReceiver) {
            unregisterReceiver(receiver);
            registeredReceiver = false;
        }
    }
}
