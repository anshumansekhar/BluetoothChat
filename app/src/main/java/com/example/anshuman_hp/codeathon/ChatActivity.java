package com.example.anshuman_hp.codeathon;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatActivity extends AppCompatActivity {
    public static final String TAG="ChatActivity";
    RecyclerView chatRecycler;
    FloatingActionButton sendButton;
    EditText text;
    recyclerAdapter adapter=new recyclerAdapter();
    Intent j;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        j=getIntent();
        boolean from=j.getBooleanExtra("fromAvailable",true);
        boolean FromService=j.getBooleanExtra("From_Service",false);
        Log.e(TAG,j+"  "+from);
        text=(EditText)findViewById(R.id.sendedit);
        chatRecycler=(RecyclerView)findViewById(R.id.ChatrecyclerView);
        sendButton=(FloatingActionButton)findViewById(R.id.sendButton);
        if(FromService) {
            Intent x=new Intent(ChatActivity.this,ChatService.class);
            x.setData(Uri.parse("Start_ReadWriteThread"));
            x.putExtra("FromService",true);
            startService(x);
        }
        else {
            Intent x=new Intent(ChatActivity.this,ChatService.class);
            x.setData(Uri.parse("Start_ReadWriteThread"));
            x.putExtra("FromService",false);
            startService(x);

        }
        chatRecycler.setAdapter(adapter);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"Sending Measssage ");
                String message=text.getText().toString();
                Log.e(TAG,message);
                ChatService.readWriteThread.write(message);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
