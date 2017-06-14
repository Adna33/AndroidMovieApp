package atlant.moviesapp.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.R;
import atlant.moviesapp.activity.MainActivity;
import atlant.moviesapp.activity.MovieDetailsActivity;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.presenters.MovieListPresenter;
import atlant.moviesapp.presenters.UserFavoritesPresenter;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 04.06.2017..
 */

public class MovieAlarmReceiver extends BroadcastReceiver {

    private int REQUEST_CODE_MOVIE = 0;
    Context context;
    Intent intent;

    String today;
    Integer id;
    String session;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        id = intent.getIntExtra(context.getString(R.string.idExtra), 0);
        session = intent.getStringExtra(context.getString(R.string.sessionExtra));
        today = df.format(c.getTime());
        if (isNetworkAvailable()) getMovies(1, id, session);
        else Toast.makeText(context, "no network", Toast.LENGTH_SHORT).show();

       // makeNotification(context, context.getString(R.string.appName), "Notification try", context.getString(R.string.appName), REQUEST_CODE_MOVIE, new Intent(context, MainActivity.class));


    }

    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void makeNotification(Context context, String title, String body, String ticker, Integer notificationId, Intent i) {


        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.applogo)
                .setContentTitle(title)
                .setTicker(ticker)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE).setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, builder.build());

    }

    private Call<MoviesResponse> call;
    private static final String API_KEY = BuildConfig.API_KEY;

    public void getMovies(int page, int id, String session) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        call = apiservice.getUserMovieWatchlist(id, API_KEY, session, "created_at.desc", page);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Movie> movies = response.body().getResults();
                    int c = 0;
                    for (Movie m : movies) {
                        if (m.getReleaseDate().equals(today)) {

                            intent = new Intent(context, MovieDetailsActivity.class);
                            intent.putExtra(context.getString(R.string.movie), m);
                            makeNotification(context, context.getString(R.string.appName), context.getString(R.string.norificationText,m.getTitle()) , context.getString(R.string.appName), REQUEST_CODE_MOVIE, intent);
                            c++;
                        }
                      if (c == 0)
                           makeNotification(context, context.getString(R.string.appName), "There is no upcoming watchlist releasing today", context.getString(R.string.appName), REQUEST_CODE_MOVIE, new Intent(context, MainActivity.class));

                    }
                }

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("TAG", t.toString());

            }
        });

        call = apiservice.getUserFavoriteMovies(id, API_KEY, session, "created_at.desc", page);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Movie> movies = response.body().getResults();
                    int c = 0;
                    for (Movie m : movies) {
                        if (m.getReleaseDate().equals(today)) {
                            intent = new Intent(context, MovieDetailsActivity.class);
                            intent.putExtra(context.getString(R.string.movie), m);
                            makeNotification(context, context.getString(R.string.appName), context.getString(R.string.norificationText,m.getTitle()), context.getString(R.string.appName), REQUEST_CODE_MOVIE, intent);
                            c++;
                        }
                        if (c == 0)
                            makeNotification(context, context.getString(R.string.appName), context.getString(R.string.movieNotifaction), context.getString(R.string.appName), REQUEST_CODE_MOVIE, new Intent(context, MainActivity.class));


                    }

                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("TAG", t.toString());
            }
        });


    }
}
