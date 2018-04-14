package com.example.anshuman_hp.codeathon;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by Anshuman-HP on 30-07-2017.
 */

public class ClientThread extends Thread {
    public static final String TAG="ClientThread";
    BluetoothDevice device;
    public BluetoothSocket socket;
    public ClientThread(BluetoothDevice bluetoothDevice)
    {
        device=bluetoothDevice;
    }
    @Override
    public void run() {
        super.run();
        try {
            Log.e(TAG,"Awaiting Connection From Server");
            socket=device.createRfcommSocketToServiceRecord(UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66"));
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
