package com.example.anshuman_hp.codeathon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Anshuman-HP on 30-07-2017.
 */

public class ServerThread extends Thread {
    public static final String TAG="ServerThread";
    BluetoothAdapter bluetoothAdapter;
    BluetoothServerSocket serverSocket;
    Context ctx;
    public BluetoothSocket socket;
    public ServerThread(Context context,BluetoothAdapter adapter){
        bluetoothAdapter=adapter;
        ctx=context;
        try {
            Log.e(TAG,"Starting Server");
           serverSocket= bluetoothAdapter.listenUsingRfcommWithServiceRecord(adapter.getName(),
                    UUID.randomUUID());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void run() {
        super.run();
        try {
            Log.e(TAG,"Listening For Request");
            socket=serverSocket.accept();
            Log.e(TAG,"Connected to "+socket.getRemoteDevice().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
