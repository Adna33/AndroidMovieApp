package atlant.moviesapp.model;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Korisnik on 04.05.2017..
 */

public class ApplicationState extends Application {

    private static Account user = null;

    public static Account getUser() {
        return user;
    }

    public static void setUser(Account user) {
        ApplicationState.user = user;
    }

    public static boolean isLoggedIn() {
        return user != null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());

    }

}
