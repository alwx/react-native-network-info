package com.pusherman.networkinfo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.net.Inet4Address;
import java.net.InetAddress;

import java.net.NetworkInterface;
import java.util.Collections;

public class RNNetworkInfo extends ReactContextBaseJavaModule {
  WifiManager wifi;

  public static final String TAG = "RNNetworkInfo";

  public RNNetworkInfo(ReactApplicationContext reactContext) {
    super(reactContext);

    wifi = (WifiManager)reactContext.getApplicationContext()
            .getSystemService(Context.WIFI_SERVICE);
  }

  @Override
  public String getName() {
    return TAG;
  }

  @ReactMethod
  public void getSSID(final Callback callback) {
    WifiInfo info = wifi.getConnectionInfo();

    // This value should be wrapped in double quotes, so we need to unwrap it.
    String ssid = info.getSSID();
    if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
      ssid = ssid.substring(1, ssid.length() - 1);
    }

    callback.invoke(ssid);
  }

  @ReactMethod
  public void getIPAddress(final Callback callback) {
    try {
      for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
        if (ni.isUp() && !ni.isLoopback() && !ni.isVirtual()) {
          for (InetAddress address : Collections.list(ni.getInetAddresses())) {
            if (address instanceof Inet4Address) {
              callback.invoke(address.getHostAddress());
              return;
            }
          }
        }
      }
    } catch (Exception ex) {
      Log.e(TAG, ex.toString());
    }
  }
}
