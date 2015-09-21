package com.example.musicsync;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSocketHandler extends Thread {

	private static final String TAG = "ClientSocketHandler";
	private Handler handler;
	private MusicSyncManager musicSync;
	private InetAddress mAddress;
	private MusicInfoManager miM;

	public ClientSocketHandler(Handler handler, InetAddress groupOwnerAddress,
			MusicInfoManager m) {
		this.handler = handler;
		this.mAddress = groupOwnerAddress;
		this.miM = m;
	}

	@Override
	public void run() {
		Socket socket = new Socket();
		try {
			socket.bind(null);
			socket.connect(new InetSocketAddress(mAddress.getHostAddress(),
					GroupListActivity.SERVER_PORT), 5000);
			Log.d(TAG, "Launching the I/O handler");
			musicSync = new MusicSyncManager(socket, handler, miM);
			new Thread(musicSync).start();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}

	public MusicSyncManager getmusicSync() {
		return musicSync;
	}

}
