package atlant.moviesapp.activity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.ActorAdapter;
import atlant.moviesapp.adapter.FilmographyAdapter;
import atlant.moviesapp.model.Actor;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.presenters.ActorDetailsPresenter;
import atlant.moviesapp.views.ActorView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActorActivity extends AppCompatActivity implements ActorView {

    @BindView(R.id.actor_name)
    TextView name;

    @BindView(R.id.actor_image)
    ImageView image;

    @BindView(R.id.actor_birth_date)
    TextView birthDate;

    @BindView(R.id.actor_web)
    TextView website;

    @BindView(R.id.actor_biography)
    TextView biography;

    @BindView(R.id.actor_full_bio)
    TextView fullBiography;

    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.film_recycler_view)
    RecyclerView recyclerView;


    private boolean isClicked = true;

    private ActorDetailsPresenter presenter;

    @OnClick(R.id.actor_full_bio)
    public void submit() {
        if (!isClicked) {

            biography.setMaxLines(4);
            isClicked = true;
            fullBiography.setText("See Full Bio");
        } else {

            biography.setMaxLines(Integer.MAX_VALUE);
            isClicked = false;
            fullBiography.setText("Hide");
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.actor_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        Integer actorId = intent.getIntExtra("actorId", 0);

        presenter = new ActorDetailsPresenter(this);
        presenter.getActor(actorId);
        presenter.getHighestRatedMovies(actorId);


    }

    @Override
    public void showActor(Actor actor) {
        name.setText(actor.getName());
        birthDate.setText(actor.getBirthday() + ", " + actor.getPlaceOfBirth());
        website.setText(actor.getHomepage());
        biography.setText(actor.getBiography());
        Glide.with(this).load(actor.getImagePath())
                .crossFade().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(image);


    }

    @Override
    public void showMovies(final List<Movie> data) {

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new FilmographyAdapter(data, R.layout.filmography_item, this));
        recyclerView.addOnItemTouchListener(new FilmographyAdapter.RecyclerTouchListener(this, recyclerView, new FilmographyAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(ActorActivity.this, MovieDetailsActivity.class);
                intent.putExtra("movie", data.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null)
            presenter.onStop();
    }

    @Override
    public void onDestroy() {
        if (presenter != null)
            presenter.onDestroy();
        super.onDestroy();
    }
}
