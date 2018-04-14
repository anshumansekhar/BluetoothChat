package com.example.anshuman_hp.codeathon;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import static com.example.anshuman_hp.codeathon.ChatService.ServerThread.TAG;

/**
 * Created by Anshuman-HP on 31-07-2017.
 */

public class ChatService extends IntentService {
    public static  BluetoothSocket  socket;
    public  static ArrayList<String> message=new ArrayList<>();
    public ChatService()
    {
        super("ChatService");
    }
    public static ReadWriteThread readWriteThread;
    public ChatService(String name) {
        super(name);
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("Chat_Service","Service Started");
        ServerThread serverThread=new ServerThread();
        Uri data=intent.getData();
        boolean fromService=intent.getBooleanExtra("FromService",false);
        if(data.toString().equals("Start Server")){
            serverThread.run();
        }
        else if(data.toString().equals("Start_ReadWriteThread")) {
            if (fromService) {
                readWriteThread = new ReadWriteThread(ChatService.socket);
            }
            else{
                readWriteThread=new ReadWriteThread(Available_Devices.socket);
            }
            readWriteThread.run();
        }
    }
    public class ServerThread extends Thread {
        public static final String TAG="ServerThread";
        BluetoothServerSocket serverSocket;
        public ServerThread(){
        }
        @Override
        public void run() {
            super.run();
            try {
                BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
                Log.e(TAG,"Starting Server");
                serverSocket= bluetoothAdapter.listenUsingRfcommWithServiceRecord(getString(R.string.app_name),
                        UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66"));
                Log.e(TAG,"Listening For Request");
                socket=serverSocket.accept();
                Log.e(TAG,"Connected to "+socket.getRemoteDevice().getName());
                if(socket.isConnected()){
                    Log.e(TAG,"going to chat activity");
                    Intent i=new Intent(getBaseContext(),ChatActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("From_Service",true);
                    startActivity(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public static class ReadWriteThread extends Thread {
        BluetoothSocket socket;
        InputStream inputStream;
        OutputStream outputStream;
        String text;
        public ReadWriteThread(BluetoothSocket bluetoothSocket)
        {
            socket=bluetoothSocket;
            Log.e(TAG,socket.getRemoteDevice().toString());
            try {
                inputStream=socket.getInputStream();
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        @Override
        public void run() {
            try {
                Log.e(TAG,"Read write thread running");
                Log.e(TAG,String.valueOf(inputStream.read()));
                Scanner s = new Scanner(inputStream).useDelimiter("\\A");
                text =( s.hasNext() ? s.next() : "");
                Log.e(TAG,text);
                message.add(text);
                readWriteThread.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void write(String Text)
        {
            Log.e(TAG+" writing" ,Text);
            try {
                message.add(Text);
                Log.e("Writing",Text);
                outputStream.write(Text.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
