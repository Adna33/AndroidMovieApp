package atlant.moviesapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import atlant.moviesapp.R;
import atlant.moviesapp.model.ApplicationState;
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

   /* static Boolean isTouched = false;

    @OnTouch(R.id.moviesSwitch)
    public boolean changeMode() {
        isTouched = true;
        return false;
    }

    @OnCheckedChanged(R.id.moviesSwitch)
    public void onCheckedChanged() {
        if (isTouched) {
            isTouched = false;
            if (movieSwitch.isEnabled()) {
                movieSwitch.setEnabled(false);
            }
            else {
                movieSwitch.setEnabled(true);
            }
        }
    }*/


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
        }
    }
}
