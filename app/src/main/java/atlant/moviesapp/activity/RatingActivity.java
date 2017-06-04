package atlant.moviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;

import atlant.moviesapp.R;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.BodyRating;
import atlant.moviesapp.presenters.RatingPresenter;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.views.RatingView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Body;

public class RatingActivity extends AppCompatActivity implements RatingView {

    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;


    private RatingPresenter presenter;
    Integer id;
    String title;
    Integer TAG;
    String type;
    private BodyRating bodyRating;
    float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ButterKnife.bind(this);
        presenter = new RatingPresenter(this);
        Intent intent = getIntent();
        title = intent.getStringExtra(getString(R.string.title));
        id = intent.getIntExtra(getString(R.string.id), 0);
        TAG = intent.getIntExtra(getString(R.string.tag), 0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rating, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (ratingBar != null)
                    rating = ratingBar.getRating();
                presenter.addRatingToList(TAG, id);
                if (isNetworkAvailable()) {
                    double dr = (double) ratingBar.getRating();
                    presenter.postRating(id, ApplicationState.getUser().getSessionId(), dr, TAG);
                } else {
                    presenter.postRatingRealm(TAG, id, rating);

                }
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
