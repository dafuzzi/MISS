package de.uulm.miss;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MISService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO create ServiceLogic thread and start
		Log.d("SERVICE", "Service is running");
		Toast.makeText(this,  "Started MISS", Toast.LENGTH_LONG).show();
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean stopService(Intent name) {
		// TODO Auto-generated method stub
		return super.stopService(name);
	}

}
