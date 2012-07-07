package net.araim.tictactoe.bluetooth;

import java.io.IOException;
import java.util.UUID;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class BluetoothListener extends Thread {
	private final BluetoothServerSocket mmServerSocket;
	private BluetoothAdapter mBluetoothAdapter;
	private IBluetoothSocketManager bluetoothSocketManager;
	

	public BluetoothListener(BluetoothAdapter bluetoothAdapter) {
		mBluetoothAdapter = bluetoothAdapter;
		// Use a temporary object that is later assigned to mmServerSocket,
		// because mmServerSocket is final
		BluetoothServerSocket tmp = null;
		try {
			// MY_UUID is the app's UUID string, also used by the client code
			tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("ttt",
					new UUID(8832742342l, 23942787634764l));
		} catch (IOException e) {
		}
		mmServerSocket = tmp;
	}

	public void run() {
		BluetoothSocket socket = null;
		// Keep listening until exception occurs or a socket is returned
		while (true) {
			try {
				socket = mmServerSocket.accept();
			} catch (IOException e) {
				break;
			}
			// If a connection was accepted
			if (socket != null) {
				// Do work to manage the connection (in a separate thread)
				bluetoothSocketManager.processSocket(socket);
				break;
			}
		}
	}

	/** Will cancel the listening socket, and cause the thread to finish */
	public void cancel() {
		try {
			mmServerSocket.close();
		} catch (IOException e) {
		}
	}

	public void setBluetoothSocketManager(IBluetoothSocketManager bluetoothSocketManager) {
		this.bluetoothSocketManager = bluetoothSocketManager;
	}

	public IBluetoothSocketManager getBluetoothSocketManager() {
		return bluetoothSocketManager;
	}
}