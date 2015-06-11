package com.ericsson.socketclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

/*
 * Author : Rabin Banerjee
 * GPL
 */
public class CommunicationHandler extends HandlerThread {

	private Handler commhandler;
	
	
	Socket echoSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    
    String serverHostname = "127.0.0.1";
	int port = 80;
	
	public CommunicationHandler(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public CommunicationHandler(String name,String serverHostname, int port) {
		super(name);
		this.serverHostname = serverHostname;
		this.port = port;
		// TODO Auto-generated constructor stub
	}

	
	
	
	

	@Override
	protected void onLooperPrepared() {
		// TODO Auto-generated method stub
		super.onLooperPrepared();
		
		
		initSocket();
		prepareHandler();
		
		
	}
	private void prepareHandler()
	{
		commhandler = new Handler(getLooper())
		{
			public void handleMessage(Message msg) {
				if(getEchoSocket() != null){
					if(!getEchoSocket().isConnected()){
						initSocket();
					}
					if(out != null && in!= null){
					out.println(msg.getData().getString("msg"));
					try {
						Toast.makeText(MainActivity.appcontext, "Response from " + serverHostname +" is " +in.readLine(),3000).show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(MainActivity.appcontext, "No response from " + serverHostname,3000).show();
						e.printStackTrace();
					}
				}
				}
				else{
					Toast.makeText(MainActivity.appcontext, "Not Connected to any Server",3000).show();
				}
		    }
		};
	}
	private void initSocket()
	{
		try {
            echoSocket = new Socket();
            echoSocket.connect(new InetSocketAddress(serverHostname, port), 5000);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        echoSocket.getInputStream()));
          Toast.makeText(MainActivity.appcontext, "Connected to host: " + serverHostname, 3000).show();
        } catch(SocketTimeoutException e){
        	e.printStackTrace();
            Toast.makeText(MainActivity.appcontext, "Socket timeout to host: " + serverHostname +" on port "+port, 3000).show();
        
        }catch (UnknownHostException e) {
        	e.printStackTrace();
            Toast.makeText(MainActivity.appcontext, "Don't know about host: " + serverHostname, 3000).show();
        	
            
        	// System.exit(1);
        } catch (IOException e) {
        	e.printStackTrace();
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + serverHostname);
            
            Toast.makeText(MainActivity.appcontext, "Couldn't get I/O for "
                    + "the connection to: " + serverHostname,3000).show();
            //System.exit(1);
        }
	}
	
	
	public Handler getCommhandler() {
		return commhandler;
	}



	public Socket getEchoSocket() {
		return echoSocket;
	}

	
	

}
