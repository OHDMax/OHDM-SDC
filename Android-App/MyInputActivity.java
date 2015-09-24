package de.htw.berlin.mobanwtest;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MyInputActivity extends ActionBarActivity {

	private EditText tvtitle;
	private EditText tvsubstance;
	private EditText tvvalue;
	private EditText tvunit;

	private EditText tvlon;
	private EditText tvlat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_input);

		tvtitle = (EditText) findViewById(R.id.editText1);
		tvsubstance = (EditText) findViewById(R.id.editText2);
		tvvalue = (EditText) findViewById(R.id.editText3);
		tvunit = (EditText) findViewById(R.id.editText4);

		tvlon = (EditText) findViewById(R.id.editText5);
		tvlat = (EditText) findViewById(R.id.editText6);

		tvlon.setEnabled(false);
		tvlat.setEnabled(false);

		Bundle subActivityExtras = getIntent().getExtras();

		if (subActivityExtras.containsKey("lon")) {
			String lon = subActivityExtras.getString("lon");
			tvlon.setText(lon);
		}
		if (subActivityExtras.containsKey("lat")) {
			String lat = subActivityExtras.getString("lat");
			tvlat.setText(lat);
		}
	}

	public void btn_abbrechen(View view) {
		tvtitle.setText("");
		tvsubstance.setText("");
		tvvalue.setText("");
		tvunit.setText("");
		tvlon.setText("");
		tvlat.setText("");

		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		finish();
	}

	public void btn_ok(View view) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("title", tvtitle.getText().toString());
		returnIntent.putExtra("substance", tvsubstance.getText().toString());
		returnIntent.putExtra("value", tvvalue.getText().toString());
		returnIntent.putExtra("unit", tvunit.getText().toString());

		setResult(RESULT_OK, returnIntent);
		finish();

	}

}