package atlant.moviesapp.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.TypeAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserWatchlistActivity extends AppCompatActivity {

    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.movie_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.movie_view_pager)
    ViewPager viewPager;

    @BindView(R.id.main_tab_layout)
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_watchlist);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.watchlist_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewPager.setAdapter(new TypeAdapter(this, getSupportFragmentManager(),2));
        tabLayout.setupWithViewPager(viewPager);
    }
}
