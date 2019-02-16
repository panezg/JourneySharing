package com.example.myapplication1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServerAsyncTask extends AsyncTask {
    private MainActivity activity;
    //private TextView statusText;

    /*public FileServerAsyncTask(Context context) {
        this.context = context;
    }*/

    public FileServerAsyncTask(MainActivity activity){
        this.activity = activity;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
            try {
                ServerSocket serverSocket = new ServerSocket(Constants.PORT);
                Log.d("JINCHI","Waiting");
                Socket client = serverSocket.accept();
                Log.d("JINCHI","Coonected to client");
                InputStream inputstream = client.getInputStream();
                Log.d("JINCHI","1");
                DataInputStream dis=new DataInputStream(inputstream);
                Log.d("JINCHI","2");

                InputStreamReader osw =new InputStreamReader(client.getInputStream());
                BufferedReader reader = new BufferedReader(osw);
                String readed = "";
                while (true){
                    String line = reader.readLine();
                    if(line == null)
                        break;
                    readed += line;
                }

//                Log.d("JINCHI","3");
//                char inputString[]=new char[50];
//                osw.read(inputString,0,10);
                //Log.d("paras","4");
                if(dis==null)
                    Log.d("JINCHI","true");
                Log.d("JINCHI","5");
                Log.d("JINCHI","received"+ readed);
                //copyFile(inputstream, new FileOutputStream(f));
                serverSocket.close();

              //  Toast.makeText((Context)objects[0],readed,Toast.LENGTH_SHORT).show();
                return readed;
            } catch (Exception e) {
                Log.e("JINCHI", e.getMessage());
                return null;
            }

    }

    @Override
    protected void onPostExecute(Object o) {
        String str = (String)o;
        activity.toast(str);
    }
}
