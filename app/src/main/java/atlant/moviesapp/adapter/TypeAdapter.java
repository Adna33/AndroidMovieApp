package atlant.moviesapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import atlant.moviesapp.R;
import atlant.moviesapp.fragments.HighestRatedMoviesFragment;
import atlant.moviesapp.fragments.LatestMoviesFragment;
import atlant.moviesapp.fragments.MostPopularMoviesFragment;
import atlant.moviesapp.fragments.MovieRatingsFragment;
import atlant.moviesapp.fragments.SeriesRatingsFragment;
import atlant.moviesapp.fragments.UserMoviesFragment;
import atlant.moviesapp.fragments.UserSeriesFragment;

/**
 * Created by Korisnik on 07.05.2017..
 */

public class TypeAdapter extends FragmentPagerAdapter {

    //tabs
    private static final int MOVIES =0;
    private static final int TVSHOWS =1;

    int tag;

    private Context context;

    public TypeAdapter(Context context, FragmentManager fragmentManager,int tag) {
        super(fragmentManager);
        this.context = context;
        this.tag=tag;
    }


    @Override
    public Fragment getItem(int position) {
        if(tag==0)
        switch (position) {
            case MOVIES:
                return new MovieRatingsFragment();
            case TVSHOWS:
                return new SeriesRatingsFragment();

            default:
                throw new UnsupportedOperationException("Undefined Fragment index: " + position);
        }
        else  if(tag==1)
            switch (position) {
                case MOVIES:
                    return new MovieRatingsFragment();
                case TVSHOWS:
                    return new UserSeriesFragment();

                default:
                    throw new UnsupportedOperationException("Undefined Fragment index: " + position);
            }
        else
            switch (position) {
                case MOVIES:
                    return new UserMoviesFragment();
                case TVSHOWS:
                    return new UserSeriesFragment();

                default:
                    throw new UnsupportedOperationException("Undefined Fragment index: " + position);
            }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.userTabs)[position];
    }





}
