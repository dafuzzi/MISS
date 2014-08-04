package de.uulm.miss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootStarter extends BroadcastReceiver {

	  @Override
	  public void onReceive(Context context, Intent intent) {
	    Intent i = new Intent(context, MISService.class);
	    context.startService(i);
	  }

}
