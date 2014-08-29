package de.uulm.miss;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.support.v7.app.ActionBarActivity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.*;

public class MainActivity extends ActionBarActivity {

	private String pathToAppData;
	private String scriptNames[];

	ToggleButton scan;
	Button add;
	TextView text;
	EditText edit1;
	EditText edit2;

	Notification resultReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pathToAppData = this.getFilesDir().toString();
		scriptNames = new String[] { "removeCaptureFiles.sh", "startCapture.sh", "stopCapture.sh" };

		resultReceiver = new Notification(null);

		Boolean init = false;
		for (String file : scriptNames) {
			if (!(new File(pathToAppData + "/" + file).exists())) {
				init = true;
			}
		}
		if (init) {
			generateScriptsFromAssets(scriptNames);
			makeScriptsExecutable(pathToAppData, scriptNames);
		}

		scan = (ToggleButton) findViewById(R.id.toggleButton1);
		add = (Button) findViewById(R.id.button1);
		text = (TextView) findViewById(R.id.textView1);
		edit1 = (EditText) findViewById(R.id.editText1);
		edit2 = (EditText) findViewById(R.id.editText2);

		if (isMyServiceRunning(MISService.class)) {
			scan.setChecked(true);
		} else {
			scan.setChecked(false);
		}

		scan.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					if (!isMyServiceRunning(MISService.class)) {
						startService(new Intent(getApplicationContext(), MISService.class));
					}
					text.setText("service is running");
				} else {
					if (isMyServiceRunning(MISService.class)) {
						stopService(new Intent(getApplicationContext(), MISService.class));
					}
					text.setText("service stopped");
				}
			}
		});

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = edit1.getText().toString();
				String mac = edit2.getText().toString();
				if (name != "" && mac.length() == 17) {
					Intent intent = new Intent(getApplicationContext(), MISService.class);

					intent.putExtra("receiver", resultReceiver);
					intent.putExtra("operation", "add");
					intent.putExtra("client", new Client(name, mac));
					edit1.setText("");
					edit2.setText("");
					startService(intent);
					scan.setChecked(true);
				}
			}
		});

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

	/**
	 * @param files
	 * @return
	 */
	private boolean generateScriptsFromAssets(String files[]) {
		try {
			for (String file : files) {
				String data = "";
				InputStream inputStream = this.getAssets().open(file);
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString + "\n");
				}

				inputStream.close();
				data = stringBuilder.toString();

				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(file, MODE_PRIVATE));
				outputStreamWriter.write(data);
				outputStreamWriter.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param path
	 * @param scriptNames
	 */
	private void makeScriptsExecutable(String path, String[] scriptNames) {
		String abs;
		for (String file : scriptNames) {
			abs = path + "/" + file;
			executerWithResponse("chmod a+x " + abs);
		}

	}

	/**
	 * @param command
	 */
	private void executerWithResponse(String command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response = output.toString();
		Log.d("MISService Activity", response);
	}

	/**
	 * @param MISService
	 * @return
	 */
	private boolean isMyServiceRunning(Class<?> MISService) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (MISService.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
