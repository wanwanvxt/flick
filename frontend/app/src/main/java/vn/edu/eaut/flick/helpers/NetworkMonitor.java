package vn.edu.eaut.flick.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;
import vn.edu.eaut.flick.R;

public class NetworkMonitor {
  private final ConnectivityManager connectivityManager;
  private final ConnectivityManager.NetworkCallback networkCallback;
  private boolean isConnected = false;

  public NetworkMonitor(Context context) {
    connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    networkCallback = new ConnectivityManager.NetworkCallback() {
      @Override
      public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        if (!isConnected) {
          isConnected = true;
          showSnackBar(context, R.string.internet_available);
        }
      }

      @Override
      public void onLost(@NonNull Network network) {
        super.onLost(network);
        if (!isConnected) {
          isConnected = false;
          showSnackBar(context, R.string.internet_lost);
        }
      }

      @Override
      public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        boolean hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        if (hasInternet && !isConnected) {
          isConnected = true;
          showSnackBar(context, R.string.internet_available);
        } else if (!hasInternet && isConnected) {
          isConnected = false;
          showSnackBar(context, R.string.internet_lost_capability);
        }
      }
    };
  }

  public void registerNetworkCallback() {
    NetworkRequest networkRequest = new NetworkRequest.Builder()
      .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
      .build();
    connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
  }

  public void unregisterNetworkCallback() {
    connectivityManager.unregisterNetworkCallback(networkCallback);
  }

  private void showSnackBar(Context context, @StringRes int resId) {
    Snackbar.make(((Activity) context).findViewById(android.R.id.content), resId, Snackbar.LENGTH_LONG).show();
  }
}
