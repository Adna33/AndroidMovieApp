package atlant.moviesapp.views;


import android.support.v4.app.Fragment;
import android.view.MenuItem;

/**
 * Created by Korisnik on 18.04.2017..
 */

public interface MainActivityView {
     void showFragment(Fragment f, MenuItem item);
     void setupToolbar(String s);
     void checkLogin();
}
