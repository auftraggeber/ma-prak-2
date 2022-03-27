package de.fh_zwickau.propertyapp;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/******************************************************************************
 * Mobile Anwendungen, Westsächsische Hochschule Zwickau.
 *
 * FILENAME:   	PropertyDetails.java
 * AUTHORS:     Rainer Wasinger
 *
 * NOTE:    	***THIS CLASS NEEDS MODIFIYING BY THE STUDENTS.***
 * 				In particular, see the methods: . 
 *
 * CLASS DESCRIPTION:
 * PropertyDetails. This Activity class is used to display information on the
 * selected property.
 *
 *****************************************************************************/
public class PropertyDetails extends Activity {
	private static final String TAG = "PropertyDetails";
	ListView mListView;
	PropertyDB mDb;
	Cursor cursor;

	/**
	 * onCreate. Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		String propertyID = null;
		String toRentStr = null;
		boolean toRent = false; //Contains whether the user is looking to 'buy'
		//or 'rent' a property. This information is obtained from the Extras bundle.

		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		setContentView(R.layout.propertydetails);

		/* Holen Sie sich die Property-ID, die wir sehen sollen, um zu prüfen,
		 * ob der Benutzer "rent" oder "buy" ausgewählt hat. */
		//*************************************************************
		//* DONE [08]: Fügen Sie Java-Code ein, der benötigt wird, um die
		//* Extras des Intents (in diesem Fall 'EXTRA_PROPERTY_ID' und
		//* 'EXTRA_TO_RENT') abzurufen, und speichern Sie diese in der
		//* lokalen String-Variablen propertyID und der lokalen booleschen
		//* Variablen 'toRent'.
		//*
		//*************************************************************
		// INSERT CODE HERE.
		//*************************************************************
		toRent = getIntent().getExtras().getBoolean(PropertyApp.EXTRA_TO_RENT);
		propertyID = getIntent().getExtras().getString(PropertyApp.EXTRA_PROPERTY_ID);

		//Open the database.
		mDb = new PropertyDB(this);
		mDb.open(toRent, false);
		cursor = mDb.getCursorToThePropertyList();
		startManagingCursor(cursor);

		Cursor c = mDb.getProperty(propertyID);
		if (c != null) {
			if (c.moveToNext()) {
				Property p = createPropertyFromCursor(c);
				fillInPropertyDetails(p);
			}
			c.close(); //Cursor management.
			c = null;
		}
	} // onCreate

	/**
	 * fillInPropertyDetails.
	 */
	public void fillInPropertyDetails(Property p) {
		TableLayout featureTable = null;
		TableRow tr = null;
		TextView label = null, value = null;

		TextView title = (TextView)findViewById(R.id.propertyTitle);
		title.setText(p.getAddress());

		ImageView photo = (ImageView)findViewById(R.id.propertyImage);
		photo.setImageResource(R.drawable.house);

		featureTable = (TableLayout)findViewById(R.id.propertyFeatureTable);

		if (!p.getPrice().equals("")) {
			tr = new TableRow(this);
			label = new TextView(this);
			value = new TextView(this);
			label.setText("Price:");
			value.setText(p.getPrice());
			tr.addView(label);
			tr.addView(value);
			featureTable.addView(tr);
		}

		if (!p.getSuburb().equals("")) {
			tr = new TableRow(this);
			label = new TextView(this);
			value = new TextView(this);
			label.setText("Suburb:");
			value.setText(p.getSuburb());
			tr.addView(label);
			tr.addView(value);
			featureTable.addView(tr);
		}

		if (!p.getBedrooms().equals("")) {
			tr = new TableRow(this);
			label = new TextView(this);
			value = new TextView(this);
			label.setText("Bedrooms:");
			value.setText(p.getBedrooms());
			tr.addView(label);
			tr.addView(value);
			featureTable.addView(tr);
		}

		if (!p.getBathrooms().equals("")) {
			tr = new TableRow(this);
			label = new TextView(this);
			value = new TextView(this);
			label.setText("Bathrooms:");
			value.setText(p.getBathrooms());
			tr.addView(label);
			tr.addView(value);
			featureTable.addView(tr);
		}

		if (!p.getCarSpaces().equals("")) {
			tr = new TableRow(this);
			label = new TextView(this);
			value = new TextView(this);
			label.setText("CarSpaces:");
			value.setText(p.getCarSpaces());
			tr.addView(label);
			tr.addView(value);
			featureTable.addView(tr);
		}

		TextView description = (TextView)findViewById(R.id.propertyDescription);
		description.setText(Html.fromHtml(p.getDescription()));
	} //fillInPropertyDetails

	/**
	 * onDestroy. Called when the activity is destroyed.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		mDb.close(); // Close the database to avoid memory leaks.
	} // onDestroy

	/**
	 * createPropertyFromCursor. Creates a property object from the given cursor.
	 */
	public Property createPropertyFromCursor(Cursor c) {
		if (c == null || c.isAfterLast() || c.isBeforeFirst()) {
			return null;
		}
		Property p = new Property();
		p.setAddress(c.getString(c.getColumnIndexOrThrow(PropertyDB.KEY_ADDRESS)));
		p.setDescription(c.getString(c.getColumnIndexOrThrow(PropertyDB.KEY_DESCRIPTION)));
		p.setPropertyID(c.getString(c.getColumnIndexOrThrow(PropertyDB.KEY_PROPERTY_ID)));
		p.setPrice(c.getString(c.getColumnIndexOrThrow(PropertyDB.KEY_PRICE)));
		p.setType(c.getString(c.getColumnIndexOrThrow(PropertyDB.KEY_TYPE)));
		p.setSuburb(c.getString(c.getColumnIndexOrThrow(PropertyDB.KEY_SUBURB)));
		p.setRegion(c.getString(c.getColumnIndexOrThrow(PropertyDB.KEY_REGION)));
		p.setBedrooms(c.getString(c.getColumnIndexOrThrow(PropertyDB.KEY_BEDROOMS)));
		p.setBathrooms(c.getString(c.getColumnIndexOrThrow(PropertyDB.KEY_BATHROOMS)));
		p.setCarSpaces(c.getString(c.getColumnIndexOrThrow(PropertyDB.KEY_CARSPACES)));

		return p;
	} // createPropertyFromCursor

} //PropertyDetails class