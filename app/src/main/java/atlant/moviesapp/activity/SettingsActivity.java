package atlant.moviesapp.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import atlant.moviesapp.R;
import atlant.moviesapp.helper.SharedPrefsUtils;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.receivers.MovieAlarmReceiver;
import atlant.moviesapp.receivers.TvAlarmReciever;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnTouch;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.account_name)
    TextView name;

    @BindView(R.id.account_username)
    TextView username;

    @BindView(R.id.moviesSwitch)
    SwitchCompat movieSwitch;

    @BindView(R.id.seriesSwitch)
    SwitchCompat seriesSwitch;

    private Boolean isMovieNotifOn = false;
    private Boolean isTvNotifOn = false;
    private int REQUEST_CODE_MOVIE = 0;
    private int REQUEST_CODE_TV = 10;
    private Integer accountId;
    private String session_id;

    @OnCheckedChanged(R.id.moviesSwitch)
    public void onCheckedChangedMovie(boolean isChecked) {
        if (isMovieNotifOn) {
            isMovieNotifOn = false;
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


            Calendar c = Calendar.getInstance();
            Intent i = new Intent(this, MovieAlarmReceiver.class);
            i.putExtra(getString(R.string.idExtra), accountId);
            i.putExtra(getString(R.string.sessionExtra), session_id);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE_MOVIE, i, PendingIntent.FLAG_UPDATE_CURRENT);
            if (isChecked) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                SharedPrefsUtils.setNotif(this, getString(R.string.userDetails), getString(R.string.movieNotification));
            } else {
                SharedPrefsUtils.removeNotif(this, getString(R.string.userDetails), getString(R.string.movieNotification));
                alarmManager.cancel(pendingIntent);
            }
        }

    }

    @OnCheckedChanged(R.id.seriesSwitch)
    public void onCheckedChangedSeries(CompoundButton buttonView, boolean isChecked) {
        if (isTvNotifOn) {
            isTvNotifOn = false;

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);


            Intent i = new Intent(this, TvAlarmReciever.class);
            i.putExtra(getString(R.string.idExtra), accountId);
            i.putExtra(getString(R.string.sessionExtra), session_id);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE_TV, i, PendingIntent.FLAG_UPDATE_CURRENT);
            if (isChecked) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                SharedPrefsUtils.setNotif(this, getString(R.string.userDetails), getString(R.string.tvNotification));

            } else {
                SharedPrefsUtils.removeNotif(this, getString(R.string.userDetails), getString(R.string.tvNotification));
                alarmManager.cancel(pendingIntent);
            }

        }
    }

    @OnTouch(R.id.moviesSwitch)
    public boolean changeModeMovie() {
        isMovieNotifOn = true;
        return false;
    }

    @OnTouch(R.id.seriesSwitch)
    public boolean changeModeSeries() {
        isTvNotifOn = true;
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.settings_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (ApplicationState.isLoggedIn()) {
            if (ApplicationState.getUser().getName() != null)
                name.setText(ApplicationState.getUser().getName());

            if (ApplicationState.getUser().getUsername() != null)
                username.setText(ApplicationState.getUser().getUsername());
            session_id = ApplicationState.getUser().getSessionId();
            accountId = ApplicationState.getUser().getId();
        }

        isMovieNotifOn = SharedPrefsUtils.isMovieNotifOn(this, getString(R.string.userDetails));
        isTvNotifOn = SharedPrefsUtils.isTvNotifOn(this, getString(R.string.userDetails));
        if (movieSwitch.isChecked() != isMovieNotifOn)
            movieSwitch.setChecked(isMovieNotifOn);
        if (movieSwitch.isChecked() != isTvNotifOn)
            seriesSwitch.setChecked(isTvNotifOn);


    }
}
