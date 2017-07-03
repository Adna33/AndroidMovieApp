package atlant.moviesapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korisnik on 22.05.2017..
 */

public class ConnectivityStateReceiver extends BroadcastReceiver {

    protected List<ConnectivityStateReceiverListener> listeners;
    protected Boolean connected;

    public ConnectivityStateReceiver() {
        listeners = new ArrayList<>();
        connected = null;
    }

    public void onReceive(Context context, Intent intent) {
        if(intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();

        if(ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
            connected = false;
        }

        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for(ConnectivityStateReceiverListener listener : listeners)
            notifyState(listener);
    }

    private void notifyState(ConnectivityStateReceiverListener listener) {
        if(connected == null || listener == null)
            return;

        if(connected == true)
            listener.networkAvailable();
        else
            listener.networkUnavailable();
    }

    public void addListener(ConnectivityStateReceiverListener l) {
        listeners.add(l);
        notifyState(l);
    }

    public void removeListener(ConnectivityStateReceiverListener l) {
        listeners.remove(l);
    }

    public interface ConnectivityStateReceiverListener {
        public void networkAvailable();
        public void networkUnavailable();
    }
}