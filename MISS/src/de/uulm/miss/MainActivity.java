package de.uulm.miss;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.support.v7.app.ActionBarActivity;
//import android.app.ActivityManager;
//import android.app.ActivityManager.RunningServiceInfo;
//import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;

public class MainActivity extends ActionBarActivity {

	private String pathToAppData;
	private String scriptNames[];

	ToggleButton scan;
	Button add;
	TextView text;
	EditText edit1;
	EditText edit2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pathToAppData = this.getFilesDir().toString();
		scriptNames = new String[] { "removeCaptureFiles.sh", "startCapture.sh", "stopCapture.sh" };

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
//	private boolean isMyServiceRunning(Class<?> MISService) {
//		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//			if (MISService.getName().equals(service.service.getClassName())) {
//				return true;
//			}
//		}
//		return false;
//	}
}
