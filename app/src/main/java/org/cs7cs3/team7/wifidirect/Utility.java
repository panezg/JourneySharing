package org.cs7cs3.team7.wifidirect;

import android.content.Context;

import com.google.gson.Gson;

public class Utility {

    public static void toast(String str, Context context) {
        //TODO: Removing Toast functionality because it cannot be called from within a background thread directly, check if it is still necessary
        //Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static String extractIP(String subnetAddress) {
        String ip = subnetAddress.substring(subnetAddress.indexOf("/") + 1, subnetAddress.indexOf(":"));
        return ip;
    }

    //TODO: Check if the code below can be removed
    /*public static boolean copyFile(InputStream inputStream, OutputStream out) {
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        } catch (IOException e) {
            Log.d("DDDDX", e.toString());
            return false;
        }
        return true;
    }

    public static byte[] getInputStreamByteArray(InputStream input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;

        try {
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return (baos.toByteArray());
    }

    public static String getMyMacAddress() {

        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nintf : all) {
                if (!nintf.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nintf.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder sb1 = new StringBuilder();
                for (byte b : macBytes) {
                    String hex = Integer.toHexString(b & 0xFF);
                    if (hex.length() == 1) {
                        hex = "0" + hex;
                    }
                    sb1.append(hex + ":");
                }

                if (sb1.length() > 0) {
                    sb1.deleteCharAt(sb1.length() - 1);
                }
                return sb1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    public static String getMyIpAddress() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Utility", ex.toString());
        }
        return null;
    }

    public static String getWiFiIPAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = getDottedDecimalIP(wm.getConnectionInfo().getIpAddress());
        Log.d("JINCHI", "in getWiFiIPAddress(ip ==" +ip+")");
        return ip;
    }

    public static String getDottedDecimalIP(int ipAddr) {

        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddr = Integer.reverseBytes(ipAddr);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddr).toByteArray();

        //convert to dotted decimal notation:
        String ipAddrStr = getDottedDecimalIP(ipByteArray);
        return ipAddrStr;
    }

    public static String getDottedDecimalIP(byte[] ipAddr) {
        //convert to dotted decimal notation:
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        return ipAddrStr;
    }

    public static boolean isWifiConnected(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.isWifiEnabled()) { // WiFi adapter is ON
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            if (wifiInfo != null && wifiInfo.getNetworkId() == -1) {
                return false; // Not connected to an access-Point
            }
            return true;      // Connected to an Access Point
        } else {
            return false; // WiFi adapter is OFF
        }
    }

    public static boolean isWiFiEnabled(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiMgr.isWifiEnabled();
    }

    public static void requestPermission(String strPermission, int perCode, Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, strPermission)) {
            Toast.makeText(activity, "GPS permission allows us to access location data." +
                            " Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{strPermission}, perCode);
        }
    }

    public static boolean checkPermission(String strPermission, Context _c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ContextCompat.checkSelfPermission(_c, strPermission);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static void deletePersistentGroups(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel) {
        try {
            Method[] methods = WifiP2pManager.class.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals("deletePersistentGroup")) {
                    // Delete any persistent group
                    for (int netid = 0; netid < 32; netid++) {
                        methods[i].invoke(wifiP2pManager, channel, netid, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearKey(Context cxt, String key) {
        SharedPreferences.Editor prefsEditor = cxt.getSharedPreferences("kkd", Context.MODE_PRIVATE).edit();
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    public static void saveString(Context cxt, String key, String value) {
        SharedPreferences.Editor prefsEditor = cxt.getSharedPreferences("kkd", Context.MODE_PRIVATE).edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static String getString(Context cxt, String key) {
        SharedPreferences prefs = cxt.getSharedPreferences("kkd", Context.MODE_PRIVATE);
        String val = prefs.getString(key, null);
        return val;
    }

    public static void saveInt(Context cxt, String key, int value) {
        SharedPreferences.Editor prefsEditor = cxt.getSharedPreferences("kkd", Context.MODE_PRIVATE).edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public static int getInt(Context cxt, String key) {
        SharedPreferences prefs = cxt.getSharedPreferences("kkd", Context.MODE_PRIVATE);
        int val = prefs.getInt(key, -1);
        return val;
    }

    public static void saveBool(Context cxt, String key, boolean value) {
        SharedPreferences.Editor prefsEditor = cxt.getSharedPreferences("kkd", Context.MODE_PRIVATE).edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public static boolean getBool(Context cxt, String key) {
        SharedPreferences prefs = cxt.getSharedPreferences("kkd", Context.MODE_PRIVATE);
        boolean val = prefs.getBoolean(key, false);
        return val;
    }

    public static void clearPreferences(Context cxt) {
        SharedPreferences.Editor prefsEditor = cxt.getSharedPreferences("kkd", Context
                .MODE_PRIVATE).edit();
        prefsEditor.clear().commit();
    }*/

}
