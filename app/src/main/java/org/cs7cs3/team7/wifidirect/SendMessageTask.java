package org.cs7cs3.team7.wifidirect;

import android.util.Log;

import org.cs7cs3.team7.journeysharing.Constants;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SendMessageTask implements Runnable {

    int port;
    int len;
    Socket socket;
    byte buf[]  = new byte[1024];
    String sendToIP;
    Message message;
    public SendMessageTask(String sendToIP, Message message){
        this.sendToIP=sendToIP;
        this.message=message;
    }
    /*@Override
    protected Object doInBackground(Object[] objects) {

    }*/

    @Override
    public void run() {
        try {
            Log.d("DATA","Sending Message starts");

            socket = new Socket();
            socket.bind(null);

            socket.connect((new InetSocketAddress(sendToIP, Constants.PORT)), Constants.REQUEST_TIMEOUT);
            Log.d("DATA","Connected to socket");
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            //set your your MAC ID
            message.setFromMAC(Constants.myMACAddress);

            writer.write(Utility.toJson(message)+"\n");
            writer.flush();
            writer.close();
            socket.close();

            Log.d("DATA","Sending Message completed");
        } catch (Exception e) {
            Log.e("DATA","Exception occured in SendMessageTask" );
            e.printStackTrace();
        }
        //Thread.currentThread().stop();
    }
}
