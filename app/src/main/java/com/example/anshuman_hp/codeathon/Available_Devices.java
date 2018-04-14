package com.example.anshuman_hp.codeathon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_CONNECTING;
import static com.example.anshuman_hp.codeathon.MainActivity.i;


public class Available_Devices extends AppCompatActivity {
    public static  final String TAG="AvailableDevice";

    ListView pairedDevices, availableDevices;
    BroadcastReceiver discoveryFinishReceiver;
    ArrayAdapter<String> discoveredDevicesAdapter;
    ArrayAdapter<String> pairedDevicesAdapter;
    public static ArrayList<BluetoothDevice> discovereddevices;
    public static ArrayList<BluetoothDevice> paireddevices;
    public BluetoothAdapter bluetoothAdapter;
    public static BluetoothSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available__devices);
        pairedDevices = (ListView) findViewById(R.id.PairedDevices);
        availableDevices = (ListView) findViewById(R.id.AvailableDevices);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        discoveredDevicesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.device_list);
        pairedDevicesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.device_list);
        discovereddevices=new ArrayList<>();
        paireddevices=new ArrayList<>();
        discoveryFinishReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        discoveredDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                        discovereddevices.add(device);
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    if (discoveredDevicesAdapter.getCount() == 0) {
                        discoveredDevicesAdapter.add("None");
                    }
                }
            }
        };

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryFinishReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryFinishReceiver, filter);

        final Set<BluetoothDevice> pairedDevicesSet = BluetoothAdapter.getDefaultAdapter().getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevicesSet.size() > 0) {
            for (BluetoothDevice device : pairedDevicesSet) {
                pairedDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                paireddevices.add(device);
            }
        } else {
            pairedDevicesAdapter.add("No Paired Devices");
        }
        pairedDevices.setAdapter(pairedDevicesAdapter);
        availableDevices.setAdapter(discoveredDevicesAdapter);
        availableDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stopService(i);
                bluetoothAdapter.cancelDiscovery();
                try {
                    socket=discovereddevices.get(position).createRfcommSocketToServiceRecord(UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66"));
                    socket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(socket.isConnected()) {
                    Intent j = new Intent(Available_Devices.this, ChatActivity.class);
                    j.putExtra("fromAvailable", true);
                    j.putExtra("From_Service",false);
                    discoveryFinishReceiver.clearAbortBroadcast();
                    startActivity(j);
                }

            }
        });
        pairedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stopService(i);
                bluetoothAdapter.cancelDiscovery();
                try {
                    socket=paireddevices.get(position).createRfcommSocketToServiceRecord(UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66"));
                    socket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(socket.isConnected()) {
                    Intent g = new Intent(Available_Devices.this, ChatActivity.class);
                    g.putExtra("fromAvailable", true);
                    g.putExtra("From_Service",false);
                    discoveryFinishReceiver.clearAbortBroadcast();
                    startActivity(g);
                }

            }
        });
    }
}
