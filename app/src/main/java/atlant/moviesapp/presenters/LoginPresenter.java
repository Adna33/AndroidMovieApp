package atlant.moviesapp.presenters;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;

import atlant.moviesapp.model.Account;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.Session;
import atlant.moviesapp.model.Token;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.LoginView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class LoginPresenter {

    LoginView view;

    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<Token> tokenCall;
    private Call<Token> validateTokenCall;
    private Call<Session> createSessionCall;
    private Call<Account> getAccountCall;

    public void requestToken(final String username, final String password) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        tokenCall = apiservice.requestToken(API_KEY);
        tokenCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Token token = response.body();
                    String tokenString = token.getRequestToken();

                    validateToken(username, password, tokenString);


                } else {
                    view.showError();
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });

    }

    public void validateToken(String username, final String password, final String token) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        validateTokenCall = apiservice.validateToken(API_KEY, username, password, token);
        validateTokenCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    String tokenString=response.body().getRequestToken();

                    createSession(tokenString,password);


                } else {
                    view.showError();
                    Log.e("TAG", "Error");
                }

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });

    }


    public void createSession(String token, final String password) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        createSessionCall = apiservice.startSession(API_KEY, token);
        createSessionCall.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Session session=response.body();
                    String sessionId=session.getSessionId();
                    Log.d("ACCOUNT",sessionId);
                    getAccount(sessionId,password);


                } else {
                    view.showError();
                    Log.e("TAG", "Error");
                }

            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });

    }

    public void getAccount(final String sessionId, final String password) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        getAccountCall = apiservice.getAccount(API_KEY, sessionId);
        getAccountCall.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    ApplicationState.setUser(response.body());
                    ApplicationState.getUser().setSessionId(sessionId);
                    view.loggedUser(password);

                } else {
                    view.showError();
                    Log.e("TAG", "Error");
                }

            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });

    }
}
