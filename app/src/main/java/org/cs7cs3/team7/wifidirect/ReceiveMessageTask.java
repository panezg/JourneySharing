package org.cs7cs3.team7.wifidirect;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//import android.support.v4.content.LocalBroadcastManager;

public class ReceiveMessageTask implements Runnable {
    private ServerSocket serverSocket;
    private Socket client;
    private Context context;

    public ReceiveMessageTask(Context context){
        this.context=context;
    }

    @Override
    public void run() {
        Log.d("DATA","Receiving Message peers starts");
        try {
            serverSocket = new ServerSocket(Constants.PORT);
            while(true) {
                client = serverSocket.accept();
                Log.d("DATA", "Connected to peer socket");
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
                String fromIP= Utility.extractIP(client.getRemoteSocketAddress().toString());
                Message message= Utility.fromJson(messageJson);
                message.setFromIP(fromIP);
                messageJson= Utility.toJson(message);

                Log.d("DATA", "Received following message : " + messageJson);

                //broadcasting method
                notifyReceiver(messageJson);
            }
        } catch (Exception e) {
            Log.e("DATA","Exception occured in ReceiveMessageTask" );
            e.printStackTrace();
        }

        Log.d("DATA","Receiving Message peers starts");
    }
    private void notifyReceiver(String messageJson){
        //locally broadcast the received message to local listeners
        Log.d("DATA", "broadcast the message");

        //this local broadcast is for normal peers to receive message
        Intent intent=new Intent("MESSAGE_RECEIVED");
        intent.putExtra("message",messageJson);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        //send local broadcast is for the group owner to broadcast it to all the other peers
        Intent intentOwner=new Intent("MESSAGE_RECEIVED_FOR_GROUP_OWNER");
        intentOwner.putExtra("message",messageJson);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentOwner);

    }

}
