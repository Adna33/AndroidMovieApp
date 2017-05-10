package atlant.moviesapp.model;

import android.app.Application;

/**
 * Created by Korisnik on 04.05.2017..
 */

public class ApplicationState extends Application{

    private static Account user=null;

    public static Account getUser() {
        return user;
    }

    public static void setUser(Account user) {
        ApplicationState.user = user;
    }

    public static boolean isLoggedIn()
    { return user!=null;}
}
