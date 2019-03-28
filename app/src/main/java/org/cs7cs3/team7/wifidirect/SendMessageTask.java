package org.cs7cs3.team7.wifidirect;

import android.util.Base64;
import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SendMessageTask implements Runnable {
    private static String WIFI_P2P_DEBUG_LABEL = "JINCHI_DATA";

    private Message message;
    private Socket socket;

    public SendMessageTask(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN SendMessageTask.run()");
            Log.d(WIFI_P2P_DEBUG_LABEL, "Sending message starts");

            //Creates a new socket from which the client will attempt to establish a TCP connection
            //to the server
            socket = new Socket();
            //Request the OS to choose a ephemeral local port for the client to use
            socket.bind(null);
            //Attempts to connect to the device acting as group owner or server, at the predefined listening port
            socket.connect((new InetSocketAddress(message.getDestinationIP(), Constants.LISTENING_PORT)), Constants.REQUEST_TIMEOUT);
            Log.d(WIFI_P2P_DEBUG_LABEL, "Connected to recipient device");
            Log.d(WIFI_P2P_DEBUG_LABEL, "message: " + message.toJSON());

            //Encrypt the message
            String cipherTextString = Base64.encodeToString(Crypto.encryptMsg(message.toJSON() + "\n"), Base64.DEFAULT);

            //Send the message
            Log.d(WIFI_P2P_DEBUG_LABEL, "cipherTextString: " + cipherTextString);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            writer.write(cipherTextString);
            writer.flush();
            writer.close();
            socket.close();
            Log.d(WIFI_P2P_DEBUG_LABEL, "Sending message completed");
            Log.d(WIFI_P2P_DEBUG_LABEL, "END SendMessageTask.run()");
        } catch (Exception e) {
            Log.e(WIFI_P2P_DEBUG_LABEL, "Exception occurred in SendMessageTask");
            e.printStackTrace();
            Log.d(WIFI_P2P_DEBUG_LABEL, "END SendMessageTask.run()");
        }
    }
}
