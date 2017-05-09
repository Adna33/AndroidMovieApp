package atlant.moviesapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import atlant.moviesapp.R;
import atlant.moviesapp.fragments.YouTubeFragment;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.presenters.LoginPresenter;
import atlant.moviesapp.views.LoginView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView{

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
    SharedPreferences sp;

    @OnClick(R.id.signup)
    public void onClick() {
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra("WebSite","https://www.themoviedb.org/account/signup");
        startActivity(intent);

    }

    @OnClick(R.id.login_forgot)
    public void onForgot() {
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra("WebSite","https://www.themoviedb.org/account/reset-password");
        startActivity(intent);

    }

    @OnClick(R.id.login_button)
    public void onClickButton()
    {
        Log.d("ACCOUNT","usao");
        username=loginName.getText().toString();
        password=loginPassword.getText().toString();
        presenter.requestToken(username,password);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter=new LoginPresenter(this);

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

        Gson gson = new Gson();
        SharedPreferences userDetails = this.getSharedPreferences("userdetails", MODE_PRIVATE);
        SharedPreferences.Editor edit = userDetails.edit();
        edit.clear();
        String user = gson.toJson(ApplicationState.getUser());
        edit.putString("user", user);
        edit.putString("password", pass);
        edit.apply();
        Toast.makeText(this, "Welcome "+ApplicationState.getUser().getUsername(),Toast.LENGTH_SHORT).show();
        Intent i= new Intent(this,MainActivity.class);
        startActivity(i);

    }

    @Override
    public void showError() {
        loginName.setText("");
        loginPassword.setText("");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Wrong username or password, please try again")
                .setTitle("Error");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
