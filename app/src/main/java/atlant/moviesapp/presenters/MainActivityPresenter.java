package atlant.moviesapp.presenters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import atlant.moviesapp.R;
import atlant.moviesapp.fragments.MovieFragment;
import atlant.moviesapp.fragments.NewsFeedFragment;
import atlant.moviesapp.fragments.TvShowFragment;
import atlant.moviesapp.views.MainActivityView;
import butterknife.ButterKnife;

/**
 * Created by Korisnik on 18.04.2017..
 */

public class MainActivityPresenter {
    private MainActivityView view;

    public MainActivityPresenter(MainActivityView view) {
        this.view = view;
    }
    public int selectFragment(MenuItem item) {
        String s="";
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.menu_feed:
                fragment = new NewsFeedFragment();
                s="News Feed";
                break;
            case R.id.menu_movies:
                fragment = new MovieFragment();
                s="Movies";
                break;
            case R.id.menu_tvshows:
                fragment = new TvShowFragment();
                s="Tv Shows";
                break;
        }
        view.showFragment(fragment,item);
        view.setupToolbar(s);


        return item.getItemId();


    }


}
