package com.example.myapplication1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class FileClientAsyncTask extends AsyncTask {

    int port;
    int len;
    Socket socket;
    byte buf[]  = new byte[1024];


    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            socket = new Socket();
            Log.d("JINCHI","Trying to connect to server");
            socket.bind(null);
            String host=(String) objects[0];
            String outgoingString=(String) objects[1];
            socket.connect((new InetSocketAddress(host, Constants.PORT)), Constants.REQUEST_TIMEOUT);
            Log.d("JINCHI","Connected to Server");
            /*OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dis=new DataOutputStream(outputStream);
            dis.writeBytes(outgoingString;*/

            //socket = new Socket("ip address", 4014);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.write(outgoingString+"\n");
            writer.flush();
            writer.close();

          //  OutputStreamWriter osw =new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
          //  osw.write(outgoingString, 0, outgoingString.length());

            Log.d("JINCHI","write complete");
            socket.close();
            return null;
        } catch (Exception e) {
            Log.e("JINCHI", e.getMessage());
            return null;
        }

    }

}
