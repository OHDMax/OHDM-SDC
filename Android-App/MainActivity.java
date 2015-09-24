package de.htw.berlin.mobanwtest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public class MainActivity extends Activity implements OnItemSelectedListener {

	public MyItemizedOverlay myItemizedOverlay = null;
	public MyLocationNewOverlay myLocationOverlay = null;
	public LocationManager locationManager;
	public MapController myMapController;
	public MapView mapView;
	public LocationListener myLocationListener;
	List<String> list = new ArrayList<String>();
	double[][] multi = new double[2][50];
	String[][] multiS = new String[4][50];

	private boolean fix = false;

	private void CenterLocatio(GeoPoint centerGeoPoint) {
		myMapController.animateTo(centerGeoPoint);
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		myMapController = (MapController) mapView.getController();
		GeoPoint geopoint = new GeoPoint((int) (52.77 * 1E6),
				(int) (13.22 * 1E6));
		myMapController.setZoom(12);
		myMapController.animateTo(geopoint);

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 50; j++) {
				multi[i][j] = 0.0;
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 50; j++) {
				multiS[i][j] = "";
			}
		}

		Drawable marker = getResources().getDrawable(
				android.R.drawable.star_big_on);
		int markerWidth = marker.getIntrinsicWidth();
		int markerHeight = marker.getIntrinsicHeight();
		marker.setBounds(0, markerHeight, markerWidth, 0);
		ResourceProxy resourceProxy = new DefaultResourceProxyImpl(
				getApplicationContext());
		myItemizedOverlay = new MyItemizedOverlay(marker, resourceProxy);
		mapView.getOverlays().add(myItemizedOverlay);

		GpsMyLocationProvider imlp = new GpsMyLocationProvider(
				this.getBaseContext());
		imlp.setLocationUpdateMinDistance(750);
		imlp.setLocationUpdateMinTime(30000);
		myLocationOverlay = new MyLocationNewOverlay(this.getBaseContext(),
				imlp, mapView);
		myLocationOverlay.setDrawAccuracyEnabled(true);
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay = new MyLocationNewOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		myLocationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, myLocationListener);

		Spinner dynamicSpinner = (Spinner) findViewById(R.id.spinner1);

		list.add("Marks");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dynamicSpinner.setAdapter(dataAdapter);
		dynamicSpinner.setOnItemSelectedListener(this);
	}// end onCreate

	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPut httpPut = new HttpPut(url);
				try {
					String json = "";
					// 3. build jsonObject
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("lon", myItemizedOverlay.getlon());
					jsonObject.put("lat", myItemizedOverlay.getlat());
					jsonObject.put("substance",
							myItemizedOverlay.getsubstance());
					jsonObject.put("value", myItemizedOverlay.getvalue());
					jsonObject.put("unit", myItemizedOverlay.getunit());
					long unixTime = System.currentTimeMillis() / 1000L;
					jsonObject.put("timestamp", unixTime);
					// 4. convert JSONObject to JSON to String
					json = jsonObject.toString();
					// 5. set json to StringEntity
					StringEntity se = new StringEntity(json);
					// 6. set httpPost Entity
					httpPut.setEntity(se);
					// 7. Set some headers to inform server about the type of
					// the content
					httpPut.setHeader("Accept", "application/json");
					httpPut.setHeader("Content-type",
							"application/json; charset=utf-8");
					// 8. Execute POST request to the given URL
					HttpResponse httpResponse = client.execute(httpPut);

					InputStream content = httpResponse.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(), "" + result,
					Toast.LENGTH_SHORT).show();
		}
	}

    
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

		for (int i = 1; i < list.size(); i++) {
			if (i == position) {
				Toast.makeText(getApplicationContext(), "Title: " +parent.getItemAtPosition(position).toString() +"\n"+
														"Substance: " +multiS[1][i] +"\n"+
														"Value: " +multiS[2][i] +"\n"+
														"Unit: " +multiS[3][i], Toast.LENGTH_SHORT).show();
				GeoPoint GeoMark = new GeoPoint(
						(int) (multi[1][i] * 1000000),
						(int) (multi[0][i] * 1000000));
				CenterLocatio(GeoMark);

				break;
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				String title = data.getStringExtra("title");
				String substance = data.getStringExtra("substance");
				String value = data.getStringExtra("value");
				String unit = data.getStringExtra("unit");

				myItemizedOverlay.settitle(title);
				myItemizedOverlay.setsubstance(substance);
				myItemizedOverlay.setvalue(value);
				myItemizedOverlay.setunit(unit);
				myItemizedOverlay.setlon(myItemizedOverlay.getTappedGeoPoint()
						.getLongitude());
				myItemizedOverlay.setlat(myItemizedOverlay.getTappedGeoPoint()
						.getLatitude());

				list.add(myItemizedOverlay.gettitle());

				Toast.makeText(getApplicationContext(), title + " added",
						Toast.LENGTH_SHORT).show();

				myItemizedOverlay.addItem(
						myItemizedOverlay.getTappedGeoPoint(), (String) title,
						(String) substance); // <--

				multi[0][myItemizedOverlay.size()] = myItemizedOverlay
						.getTappedGeoPoint().getLongitude();
				multi[1][myItemizedOverlay.size()] = myItemizedOverlay
						.getTappedGeoPoint().getLatitude();

				multiS[0][myItemizedOverlay.size()] = title;
				multiS[1][myItemizedOverlay.size()] = substance;
				multiS[2][myItemizedOverlay.size()] = value;
				multiS[3][myItemizedOverlay.size()] = unit;
				
				 DownloadWebPageTask task = new DownloadWebPageTask();
				 task.execute(new String[] {"https://studi.f4.htw-berlin.de/~s0544210/OHDM_PHP_API/SDC/create"});
			}
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Abbruch...",
						Toast.LENGTH_SHORT).show();
			}
		}
	}// onActivityResult

	public void btn_add(View view) {
		if (myItemizedOverlay.getTappedGeoPoint() != null) {
			Intent intent = new Intent(MainActivity.this, MyInputActivity.class);
			intent.putExtra("lon", ""
					+ myItemizedOverlay.getTappedGeoPoint().getLongitude());
			intent.putExtra("lat", ""
					+ myItemizedOverlay.getTappedGeoPoint().getLatitude());

			startActivityForResult(intent, 1);
		}

	}

	public void checkbox_clicked(View view) {
		fix = !fix;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_import:
			Toast.makeText(getApplicationContext(), "macht n fuffy",
					Toast.LENGTH_SHORT).show();
			return true;

		case R.id.menu_export:
			Toast.makeText(getApplicationContext(), "macht n fuffy",
					Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location argLocation) {
			// TODO Auto-generated method stub
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (argLocation.getLatitude() * 1000000),
					(int) (argLocation.getLongitude() * 1000000));
			if (fix) {
				CenterLocatio(myGeoPoint);
			}
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}// end MainActivity