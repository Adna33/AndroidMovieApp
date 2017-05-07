package atlant.moviesapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import atlant.moviesapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.activity_signup)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        Intent myIntent = getIntent();
        String url = myIntent.getStringExtra("WebSite");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }
}
