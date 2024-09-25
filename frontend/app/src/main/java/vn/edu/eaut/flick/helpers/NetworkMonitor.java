package vn.edu.eaut.flick.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;

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
          showToast(context, "Internet available");
        }
      }

      @Override
      public void onLost(@NonNull Network network) {
        super.onLost(network);
        if (!isConnected) {
          isConnected = false;
          showToast(context, "Lost internet connection");
        }
      }

      @Override
      public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        boolean hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        if (hasInternet && !isConnected) {
          isConnected = true;
          showToast(context, "Internet available");
        } else if (!hasInternet && isConnected) {
          isConnected = false;
          showToast(context, "Lost internet capability");
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

  private void showToast(Context context, String message) {
    Toast.makeText(context, message, Toast.LENGTH_LONG)
      .show();
  }
}
