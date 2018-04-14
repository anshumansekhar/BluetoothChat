package com.example.anshuman_hp.codeathon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    BluetoothAdapter bluetoothAdapter;
    static  final int REQUEST_ENABLE_BLUETOOTH=1;
    Button startDiscovery;
    public static Intent i;

    @Override
    protected void onStart() {
        super.onStart();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }
        if (bluetoothAdapter != null) {
            if(!bluetoothAdapter.isDiscovering()) {
                Intent discoverableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(
                        BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
                startActivity(discoverableIntent);
                Log.e(TAG,"BlueTooth Discoverable");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i=new Intent(MainActivity.this,ChatService.class);
        i.setData(Uri.parse("Start Server"));
        startService(i);
        startDiscovery=(Button)findViewById(R.id.Start_Discovery);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null)
        {
            Toast.makeText(this, "Bluetooth is not Supported on The Device!\n" +
                    "The Application is now Exiting", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"Bluetooth not supported");
            finish();
        }
        startDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();
                startActivity(new Intent(MainActivity.this,Available_Devices.class));
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      switch (requestCode)
      {
          case REQUEST_ENABLE_BLUETOOTH:
              if(resultCode==RESULT_OK){

              }
              else {
                  // User did not enable Bluetooth or an error occurred
                  Log.d(TAG, "BT not enabled");
                  Toast.makeText(getApplicationContext(),"Bluetooth not Switched On\n" +
                                  "The Application is now Exiting",
                          Toast.LENGTH_SHORT).show();
                  finish();
              }

      }
    }
}
