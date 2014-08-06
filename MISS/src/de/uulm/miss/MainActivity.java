package de.uulm.miss;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
public class MainActivity extends ActionBarActivity {

	private String pathToAppData;
	private String captureFile;
	private String scriptNames[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pathToAppData = this.getFilesDir().toString();
		captureFile = "capture-01.csv";
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
		
		startService(new Intent(this, MISService.class));
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
			executer("chmod a+x " + abs);
		}

	}

	/**
	 * @param command
	 */
	private void executer(String command) {
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
