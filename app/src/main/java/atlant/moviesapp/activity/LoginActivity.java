package atlant.moviesapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import atlant.moviesapp.R;
import atlant.moviesapp.helper.SharedPrefsUtils;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.presenters.LoginPresenter;
import atlant.moviesapp.views.LoginView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.login_button)
    Button loginBtn;

    @BindView(R.id.login_email)
    EditText loginName;

    @BindView(R.id.login_password)
    EditText loginPassword;

    @BindView(R.id.login_forgot)
    TextView forgotAccount;

    @BindView(R.id.signup)
    TextView signup;

    private LoginPresenter presenter;
    private String username;
    private String password;

    @OnClick(R.id.signup)
    public void onClick() {
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra(getString(R.string.website), getString(R.string.signupLink));
        startActivity(intent);

    }

    @OnClick(R.id.login_forgot)
    public void onForgot() {
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra(getString(R.string.website), getString(R.string.resetLink));
        startActivity(intent);

    }

    @OnClick(R.id.login_button)
    public void onClickButton() {
        if (isNetworkAvailable()) {
            username = loginName.getText().toString();
            password = loginPassword.getText().toString();
            presenter.requestToken(username, password);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.errorMessage))
                    .setTitle(getString(R.string.errorTitle));

            builder.setPositiveButton(getString(R.string.OkButton), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginPresenter(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.login_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void loggedUser(String pass) {

        SharedPrefsUtils.loginPref(this, getString(R.string.userDetails), pass);
        Toast.makeText(this, getString(R.string.welcomeMessage, ApplicationState.getUser().getUsername()), Toast.LENGTH_SHORT).show();
        presenter.getMovieFavorites(1);
        presenter.getSeriesFavorites(1);
        presenter.getMovieRatings(1);
        presenter.getSeriesRatings(1);
        presenter.getMovieWatchlist(1);
        presenter.getSeriesWatchlist(1);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void showError() {
        loginName.setText("");
        loginPassword.setText("");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.LoginError)
                .setTitle(R.string.ErrorTitle);

        builder.setPositiveButton(R.string.OkButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
