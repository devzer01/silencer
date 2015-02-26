package com.gems.silencer;

import java.util.ArrayList;
import java.util.Set;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MainActivity extends ActionBarActivity {

	private static final int REQUEST_ENABLE_BT = 1;
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        final String action = intent.getAction();

	        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
	            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
	                                                 BluetoothAdapter.ERROR);
	            switch (state) {
	            case BluetoothAdapter.STATE_OFF:
	                //setButtonText("Bluetooth off");
	                break;
	            case BluetoothAdapter.STATE_TURNING_OFF:
	                //setButtonText("Turning Bluetooth off...");
	                break;
	            case BluetoothAdapter.STATE_ON:
	                //setButtonText("Bluetooth on");
	                break;
	            case BluetoothAdapter.STATE_TURNING_ON:
	                //setButtonText("Turning Bluetooth on...");
	                break;
	            }
	        }
	    }
	};
	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mDeviceReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // Add the name and address to an array adapter to show in a ListView
	            //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	        }
	    }
};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
		}
		
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(mReceiver, filter);
		
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		// Register the BroadcastReceiver
		IntentFilter deviceFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mDeviceReceiver, deviceFilter); // Don't forget to unregister during onDestroy
		
		ArrayList<String> mArrayAdapter = new ArrayList<String>();
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a ListView
		        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		    }
		}
		
		/**
		 * Intent discoverableIntent = new
			Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
			
			AudioManager am;
			am= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
			
			//For Normal mode
			am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			
			//For Silent mode
			am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			
			//For Vibrate mode
			am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		 */
	}
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data)
	{
		if (resultCode == Activity.RESULT_OK) {
			//start the broadcaster
		} else if (resultCode == Activity.RESULT_CANCELED) {
			//show some Toast Message
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();

	    /* ... */

	    // Unregister broadcast listeners
	    unregisterReceiver(mReceiver);
	}
}
