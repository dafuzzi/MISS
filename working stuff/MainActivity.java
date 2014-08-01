package com.example.testservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Global shit i have to use more than once
		String pathToAppData = this.getFilesDir().toString();
		String scriptNames[] = { "removeCaptureFiles.sh", "startCapture.sh", "stopCapture.sh" };
		String captureFile = "capture-01.csv";
		
		Boolean init = false;
		for (String file : scriptNames) {
			if(!(new File(pathToAppData+"/"+file).exists())){
				init = true;
			}
		}
		if(init){
			if(generateScriptsFromAssets(scriptNames)){
				makeScriptsExecutable(scriptNames);
			}
		}
		
		// Restarts service when app is rebuild. Line have to be removed in
		// final version.
		// startService(new Intent(context,MyService.class));

		// File filePath = new File(this.getFilesDir(), "capture-01.csv");
		// File path = new File(this.getFilesDir().toString());
		// FileParser fp = new FileParser(filePath);

		// LinkedList<Station> fs = fp.parseForStations();
		// LinkedList<Client> fc = fp.parseForClients();
		// Log.d("Parser", "Number of found stations: " + fs.size());
		// Log.d("Parser", "Number of found clients: " + fc.size());

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

	private void executer(String command) {
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
		Log.d("Shell", response);
	}
	
	private boolean generateScriptsFromAssets(String files[]){
		try {
			for (String file : files) {
				String data = "";
				InputStream inputStream = this.getAssets().open(file);
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString+"\n");
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

	private void makeScriptsExecutable(String[] scriptNames) {
		
		
	}

}
