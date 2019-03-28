package org.cs7cs3.team7.wifidirect;

import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveMessageTask implements Runnable {
    private static final String WIFI_P2P_DEBUG_LABEL = "JINCHI_DATA";

    private ServerSocket serverSocket;
    private Socket client;
    private P2PCommsManager commsManager;

    public ReceiveMessageTask(P2PCommsManager commsManager) {
        this.commsManager = commsManager;
    }

    @Override
    public void run() {
        Log.d(WIFI_P2P_DEBUG_LABEL, "BEGIN ReceiveMessageTask.run()");
        try {

            serverSocket = new ServerSocket(Constants.LISTENING_PORT);
            while (true) {
                client = serverSocket.accept();
                Log.d(WIFI_P2P_DEBUG_LABEL, "Connected to peer socket");
                InputStream inputstream = client.getInputStream();
                DataInputStream dis = new DataInputStream(inputstream);
                InputStreamReader osw = new InputStreamReader(client.getInputStream());
                BufferedReader reader = new BufferedReader(osw);
                String messageJson = "";
                while (true) {
                    String line = reader.readLine();
                    if (line == null)
                        break;
                    messageJson += line;
                }
                //IP of other peers in the group is not readily available to group owner. Hence, group owner needs to extract the IP of connection
                //from socket when client peer sends the message to group owner at first time.
                String originIP = Utility.extractIP(client.getRemoteSocketAddress().toString());
                Log.d(WIFI_P2P_DEBUG_LABEL, "Received following message from this IP [" + originIP + "]: " + messageJson);
                Message message = Message.fromJSON(messageJson);
                message.setOriginIP(originIP);
                commsManager.notifyMessageReceived(message);
            }
        } catch (Exception e) {
            Log.e(WIFI_P2P_DEBUG_LABEL, "Exception occurred in ReceiveMessageTask.run()");
            e.printStackTrace();
        }
        Log.d(WIFI_P2P_DEBUG_LABEL, "END ReceiveMessageTask.run()");
    }
}
