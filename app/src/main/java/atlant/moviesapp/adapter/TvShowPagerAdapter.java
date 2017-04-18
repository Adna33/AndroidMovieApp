package atlant.moviesapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import atlant.moviesapp.R;
import atlant.moviesapp.fragments.AiringTvFragment;
import atlant.moviesapp.fragments.HighestRatedMoviesFragment;
import atlant.moviesapp.fragments.HighestRatedTvFragment;
import atlant.moviesapp.fragments.LatestMoviesFragment;
import atlant.moviesapp.fragments.LatestTvFragment;
import atlant.moviesapp.fragments.MostPopularMoviesFragment;
import atlant.moviesapp.fragments.MostPopularTvFragment;

/**
 * Created by Korisnik on 13.04.2017..
 */

public class TvShowPagerAdapter extends FragmentPagerAdapter {

    //tabs
    private static final int MOST_POPULAR =0;
    private static final int LATEST =1;
    private static final int HIGHEST_RATED =2;
    private static final int AIRING =3;

    private Context context;

    public TvShowPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MOST_POPULAR:
                return new MostPopularTvFragment();
            case LATEST:
                return new LatestTvFragment();
            case HIGHEST_RATED:
                return new HighestRatedTvFragment();
            case AIRING:
                return new AiringTvFragment();

            default:
                throw new UnsupportedOperationException("Undefined Fragment index: " + position);
        }
    }

    @Override
    public int getCount() {
        return 4;
    } //Category_count

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.TvTabs)[position];
    }





}
