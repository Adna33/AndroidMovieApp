package atlant.moviesapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import atlant.moviesapp.R;
import atlant.moviesapp.model.ApplicationState;

/**
 * Created by Korisnik on 09.06.2017..
 */

public class SharedPrefsUtils {
    private SharedPrefsUtils() {
    }

    public static Boolean isMovieNotifOn(Context context, String key) {
        Boolean result = false;
        SharedPreferences userDetails = context.getSharedPreferences(key, 0);
        if (userDetails.contains(context.getString(R.string.movieNotification))) {
            result = context.getSharedPreferences(key, 0).getBoolean(context.getString(R.string.movieNotification), false);
        }
        return result;
    }

    public static void setNotif(Context context, String key, String type) {
        context.getSharedPreferences(key, 0).edit().putBoolean(type, true).apply();
    }

    public static void removeNotif(Context context, String key, String type) {
        context.getSharedPreferences(key, Context.MODE_PRIVATE).edit().remove(type).apply();
    }

    public static Boolean isTvNotifOn(Context context, String key) {
        Boolean result = false;
        SharedPreferences userDetails = context.getSharedPreferences(key, 0);
        if (userDetails.contains(context.getString(R.string.tvNotification))) {
            result = context.getSharedPreferences(key, 0).getBoolean(context.getString(R.string.tvNotification), false);
        }
        return result;
    }

    public static void loginPref(Context context, String key, String pass) {
        Gson gson = new Gson();
        SharedPreferences userDetails = context.getSharedPreferences(key, 0);
        SharedPreferences.Editor edit = userDetails.edit();
        edit.clear();
        String user = gson.toJson(ApplicationState.getUser());
        edit.putString(context.getString(R.string.user), user);
        edit.putString(context.getString(R.string.password), pass);
        edit.apply();
    }

    public static void removePref(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(key, 0);
        sp.edit().remove(context.getString(R.string.user)).apply();
        sp.edit().remove(context.getString(R.string.password)).apply();
        if(sp.contains("movieNotification"))
            sp.edit().remove("movieNotification").apply();

        if(sp.contains("tvNotification"))
            sp.edit().remove("tvNotification").apply();

    }


}
