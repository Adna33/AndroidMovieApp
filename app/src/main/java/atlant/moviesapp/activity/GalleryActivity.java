package atlant.moviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.GalleryAdapter;
import atlant.moviesapp.model.Backdrop;
import atlant.moviesapp.presenters.GalleryPresenter;
import atlant.moviesapp.views.GalleryView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends AppCompatActivity implements GalleryView {

    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_name)
    TextView nameField;

    @BindView(R.id.tv_image_num)
    TextView imageNumField;

    GalleryPresenter presenter;
    GalleryAdapter mAdapter;
    List<Backdrop> images;
    int id,TAG;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.movies_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        presenter= new GalleryPresenter(this);
        images= new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);
        Intent intent = getIntent();
        id = intent.getIntExtra(getString(R.string.id), 0);
        TAG = intent.getIntExtra(getString(R.string.tag), 0);
        name=intent.getStringExtra(getString(R.string.name));
        nameField.setText(name);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        presenter.getImages(TAG,id);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void showImages(List<Backdrop> backdrops) {
        images.clear();
        imageNumField.setText(getString(R.string.imagesTitle, String.format("%d",backdrops.size())));
        images.addAll(backdrops);
        mAdapter.notifyDataSetChanged();
    }
}
