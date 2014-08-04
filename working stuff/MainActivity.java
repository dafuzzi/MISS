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
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class MainActivity extends ActionBarActivity {

	private LinkedList<Client> searchList = new LinkedList<Client>();

	public String pathToAppData;
	String captureFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pathToAppData = this.getFilesDir().toString();
		String scriptNames[] = { "removeCaptureFiles.sh", "startCapture.sh", "stopCapture.sh" };
		captureFile = "capture-01.csv";

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

		final Button deleteButton = (Button) findViewById(R.id.button1);

		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				executer("su -c /data/data/com.example.testservice/files/removeCaptureFiles.sh");
			}
		});

		final Button addButton = (Button) findViewById(R.id.button2);

		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO read textview
			}
		});

		ToggleButton scan = (ToggleButton) findViewById(R.id.toggleButton1);

		scan.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					executer("su -c /data/data/com.example.testservice/files/startCapture.sh");
					executer("echo on");
				} else {
					executer("su -c /data/data/com.example.testservice/files/stopCapture.sh");
					check();
				}
			}
		});
		// Restarts service when app is rebuild. Line have to be removed in
		// final version.
		// startService(new Intent(context,MyService.class));
	}
	public void check(){		
		if ((new File(pathToAppData + "/" + captureFile).exists())) {
			File filePath = new File(this.getFilesDir(), "capture-01.csv");
			FileParser fp = new FileParser(filePath);
			LinkedList<Client> fc = fp.parseForClients();
			Log.d("Parser", "Number of found clients: " + fc.size());
			Toast.makeText(this, "Number of Client: " + fc.size(), Toast.LENGTH_LONG).show();

			LinkedList<Station> fs = fp.parseForStations();
			Log.d("Parser", "Number of found stations: " + fs.size());
			Toast.makeText(this, "Number of Stations: " + fs.size(), Toast.LENGTH_LONG).show();

			searchList.add(new Client("Seba's Phone","3C:C2:43:C9:6D:9C"));
			searchList.add(new Client("Fuzzi's Phone","90:27:E4:32:F2:03"));
			searchList.add(new Client("Random Stanger 1","00:19:07:07:C5:B0"));
			searchList.add(new Client("Random Stanger 2","00:1F:CA:CB:6B:E0"));

			for (Client client : searchList) {
				for (Client found : fc) {
					if (client.getMAC().equals(found.getMAC())) {
						Toast.makeText(this, "Found Client: " + client.getCustomName(), Toast.LENGTH_LONG).show();
						continue;
					}
				}
			}
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

	private void executer(String command) {
//		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
//			p.waitFor();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			String line = "";
//			while ((line = reader.readLine()) != null) {
//				output.append(line + "\n");
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		String response = output.toString();
//		Log.d("Shell", response);
	}

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

	private void makeScriptsExecutable(String path, String[] scriptNames) {
		String abs;
		for (String file : scriptNames) {
			abs = path + "/" + file;
			executer("chmod a+x " + abs);
		}

	}

}
