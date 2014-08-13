package de.uulm.miss;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class Notification extends ResultReceiver {
	public Notification(Handler handler) {
		super(handler);
	}

	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		Log.d("NOTIFY","received");
	}

}
