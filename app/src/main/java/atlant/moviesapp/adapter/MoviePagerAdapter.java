package atlant.moviesapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import atlant.moviesapp.R;
import atlant.moviesapp.fragments.HighestRatedMoviesFragment;
import atlant.moviesapp.fragments.LatestMoviesFragment;
import atlant.moviesapp.fragments.MostPopularMoviesFragment;

/**
 * Created by Korisnik on 12.04.2017..
 */

public class MoviePagerAdapter extends FragmentPagerAdapter {

    //tabs
    private static final int MOST_POPULAR =0;
    private static final int LATEST =1;
    private static final int HIGHEST_RATED =2;

    private Context context;

    public MoviePagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MOST_POPULAR:
                return new MostPopularMoviesFragment();
            case LATEST:
                return new LatestMoviesFragment();
            case HIGHEST_RATED:
                return new HighestRatedMoviesFragment();

            default:
                throw new UnsupportedOperationException("Undefined Fragment index: " + position);
        }
    }

    @Override
    public int getCount() {
        return 3;
    } //Category_count

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.tabs)[position];
    }





}
