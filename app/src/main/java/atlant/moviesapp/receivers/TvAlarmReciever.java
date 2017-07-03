package atlant.moviesapp.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.R;
import atlant.moviesapp.activity.MainActivity;
import atlant.moviesapp.activity.MovieDetailsActivity;
import atlant.moviesapp.activity.TvShowDetails;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.model.TvShowsResponse;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 05.06.2017..
 */

public class TvAlarmReciever extends BroadcastReceiver {
    Context context;
    Intent intent;
    private int REQUEST_CODE_TV = 10;
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
        if (isNetworkAvailable()) getSeries(1, id, session);
        else Toast.makeText(context, "no network", Toast.LENGTH_SHORT).show();
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

    private Call<TvShowsResponse> seriescall;
    private static final String API_KEY = BuildConfig.API_KEY;

    public void getSeries(int page, int id, String session) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        seriescall = apiservice.getUserFavoriteSeries(id, API_KEY, session, "created_at.desc", page);


        seriescall.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<TvShow> shows = response.body().getResults();
                    int c = 0;
                    for (TvShow s : shows) {
                        if (s.getReleaseDate().equals(today)) {
                            intent = new Intent(context, TvShowDetails.class);
                            intent.putExtra(context.getString(R.string.series), s.getId());
                            c++;
                            makeNotification(context, context.getString(R.string.appName),context.getString(R.string.norificationText, s.getName()), context.getString(R.string.appName), REQUEST_CODE_TV, intent);

                        }
                        if (c == 0)
                            makeNotification(context, context.getString(R.string.appName), context.getString(R.string.seriesNotFav), context.getString(R.string.appName), REQUEST_CODE_TV, new Intent(context, MainActivity.class));

                    }
                }
            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
            }
        });
        seriescall = apiservice.getUserSeriesWatchlist(id, API_KEY, session, "created_at.desc", page);


        seriescall.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<TvShow> shows = response.body().getResults();
                    int c = 0;
                    for (TvShow s : shows) {
                        if (s.getReleaseDate().equals(today)) {
                            intent = new Intent(context, TvShowDetails.class);
                            intent.putExtra(context.getString(R.string.series), s.getId());
                            makeNotification(context, context.getString(R.string.appName), context.getString(R.string.norificationText, s.getName()), context.getString(R.string.appName), REQUEST_CODE_TV, intent);
                            c++;
                        }
                        if (c == 0)
                            makeNotification(context, context.getString(R.string.appName), context.getString(R.string.seriesNotif), context.getString(R.string.appName), REQUEST_CODE_TV, new Intent(context, MainActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
            }
        });


    }
}