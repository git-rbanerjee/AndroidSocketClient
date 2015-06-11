package com.ericsson.socketclient;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/*
 * Author : Rabin Banerjee
 * GPL
 */
public class MainActivity extends Activity {
	Button sendbutton,createSocket;
	EditText text,hostIP,portnum;
	TextView response;
	static Context appcontext;
	
	CommunicationHandler commThread;
	
	Handler uiHandler;
	
	String serverHostname = "";
	int port = 10000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendbutton = (Button) findViewById(R.id.button1);
        createSocket = (Button) findViewById(R.id.button2);
        text = (EditText) findViewById( R.id.editText1);
        hostIP = (EditText) findViewById( R.id.editText2);
        portnum = (EditText) findViewById( R.id.editText3);
        
        
        hostIP.setHint("SocketServer IP");
        portnum.setHint("SocketServer Port");
        text.setHint("String to Send");
        
        //hostIP.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        portnum.setInputType(InputType.TYPE_CLASS_NUMBER);
        
        appcontext=getApplicationContext();
        
        
        createSocket.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
				if(commThread.getEchoSocket() !=null){
					commThread.getEchoSocket().close();
					commThread.getLooper().quit();
					if(!serverHostname.equals(""))
						Toast.makeText(getApplicationContext(), "Disconnected from " + serverHostname, 500).show();
				}
				}catch(Exception e)
				{
					
				}
				serverHostname = hostIP.getText().toString();
				try{
				port = Integer.parseInt(portnum.getText().toString());
				}catch(Exception e){}
				
				commThread =  new CommunicationHandler("COMM_Thread",serverHostname, port);
		        commThread.start();
			}
		});
        sendbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(commThread !=null && commThread.getCommhandler() !=null){
				Message messg=commThread.getCommhandler().obtainMessage();
				Bundle bundledata = new Bundle();
				bundledata.putString("msg", text.getText().toString());
				messg.setData(bundledata);
				commThread.getCommhandler().sendMessage(messg);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "No socket created", 500).show();
				}
				// TODO Auto-generated method stub
				
			}
		});
        
        
        
    }


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(commThread !=null && commThread.getEchoSocket() != null)
		{
			try {
				commThread.getEchoSocket().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		
	}
	
	
	
	
}
