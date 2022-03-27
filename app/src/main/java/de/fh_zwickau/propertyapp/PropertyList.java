package de.fh_zwickau.propertyapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;

/******************************************************************************
 * Mobile Anwendungen, Westsächsische Hochschule Zwickau.
 *
 * FILENAME:   	PropertyList.java
 * AUTHORS:     Rainer Wasinger
 *
 * NOTE:    	***THIS CLASS NEEDS MODIFIYING BY THE STUDENTS.***
 * 				In particular, see the methods: .
 *
 * CLASS DESCRIPTION:
 * PropertyList. This activity class is responsible for presenting the user
 * with a list of property results (either to buy or to rent).
 * The associated GUI for PropertyList is contained inside propertylisting.xml
 * (as well as propertylistrow.xml).
 *
 * The activity retrieves boolean information as to whether it will be
 * displaying the buy or rent list from the Extras passed to it through an
 * Intent call.
 *
 * PropertyListAdapter.	 This is a custom Adapter class that bridges our
 * ListView and the data in the database.
 *
 *****************************************************************************/
public class PropertyList extends Activity {
	private static final String TAG = "PropertyList";
	List<Property> mPropertyList = new ArrayList<Property>();
	PropertyListAdapter mPropertyListAdapter = null;
	ListView mListView;
	PropertyDB mDb;
	Cursor cursor;

	/**
	 * onCreate. Called when the activity is started. Also responsible for
	 * setting the content view, and for loading up everything required to show
	 * a PropertyList.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final String toRentStr;
		boolean toRent = false; //Contains whether the user is looking to 'buy'
		//or 'rent' a property. This information is obtained from the Extras bundle.

		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		//*********************************************************************
		//* TODO [05]: Legen Sie den Activity-Inhalt aus einer Layout-Ressource
		//* fest, d.h. verknüpfen Sie die Activity mit der XML-GUI. Ein Beispiel
		//* dafür, wie das gemacht wird, finden Sie in der onCreate-Methode in
		//* PropertyDetails.java.
		//*
		//*********************************************************************
		// INSERT CODE HERE.
		//*********************************************************************
		setContentView(R.layout.propertylisting);

		/* Extras aus dem Intent holen */
		//*********************************************************************
		//* TODO [06]: Fügen Sie Java-Code ein, der benötigt wird, um die Extras
		//* des Intents (in diesem Fall 'EXTRA_TO_RENT') abzurufen und speichern
		//* Sie diese in der lokalen booleschen Variablen 'toRent'.
		//*
		//*********************************************************************
		// INSERT CODE HERE.
		//*********************************************************************

		mDb = new PropertyDB(this);
		mDb.open(toRent, false);
		// We let the system manage this cursor, so that the life
		// cycle of the cursor matches the life cycle of the activity that is
		// displaying the cursor's data.
		cursor = mDb.getCursorToThePropertyList();
		startManagingCursor(cursor);

		mListView = (ListView) findViewById(R.id.propertyListings);

		// Create the custom PropertyListAdapter to bridge our ListView and
		// the data in the database.
		mPropertyListAdapter = new PropertyListAdapter();
		retrievePropertiesFromDB();
		mListView.setAdapter(mPropertyListAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Log.d(TAG, "PropertyList.onItemClick()");
				//*************************************************************
				//* TODO [07]: Einfügen von Java-Code, der zum Auslösen des
				//* PropertyDetails-Intent erforderlich ist, und Verwenden der
				//* putExtra-Methode zum Hinzufügen zusätzlicher Informationen
				//* zum Intent, nämlich der ID der Immobilie, über die der
				// Benutzer weitere Details sucht (d. h. EXTRA_PROPERTY_ID),
				// sowie den entsprechenden buy/rent-Datensatz (d. h. EXTRA_TO_RENT).
				//*
				//*************************************************************
				// INSERT CODE HERE.
				//*************************************************************
			}
		});
	} // onCreate

	/**
	 * retrievePropertiesFromDB. Performs all the actions required to start
	 * showing a particular list.
	 */
	public void retrievePropertiesFromDB() {
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Property p = createPropertyFromCursor(cursor);
				mPropertyListAdapter.add(p); //NB: When we add a Property, we
				//need to add it to the ArrayAdapter via add() ñ the adapter
				//will, in turn, put it in the ArrayList. Otherwise, if we add
				//it straight to the ArrayList, the adapter will not know about
				//the added Property and therefore will not display it
				cursor.moveToNext();
			}
		}
	} // retrievePropertiesFromDB

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
	 * PropertyListAdapter class.
	 *
	 * This is a custom Adapter that bridges our ListView and the data in the
	 * database.
	 *
	 * @author Rainer Wasinger.
	 */
	class PropertyListAdapter extends ArrayAdapter<Property> {
		PropertyListAdapter() {
			super(PropertyList.this, android.R.layout.simple_list_item_1,
					mPropertyList);
		}

		/**
		 * getView. Gets a view that represents a summary of this property.
		 * Inflated from the propertylistrow.xml file into a real view.
		 */
		public View getView(int position, View row, ViewGroup parent) {
			TableLayout featureTable = null;
			TableRow tr = null;
			TextView label = null, value = null;

			LayoutInflater inflater=getLayoutInflater();
			row = inflater.inflate(R.layout.propertylistrow, null);
			Property p = mPropertyList.get(position);

			ImageView photo = (ImageView) row.findViewById(R.id.propertyImage);
			photo.setImageResource(R.drawable.house);

			featureTable = (TableLayout) row.findViewById(R.id.summaryFeatureTable);

			if (!p.getPrice().equals("")) {
				tr = new TableRow(PropertyList.this);
				label = new TextView(PropertyList.this);
				value = new TextView(PropertyList.this);
				label.setText("Price:");
				value.setText(p.getPrice());
				tr.addView(label);
				tr.addView(value);
				featureTable.addView(tr);
			}

			if (!p.getSuburb().equals("")) {
				tr = new TableRow(PropertyList.this);
				label = new TextView(PropertyList.this);
				value = new TextView(PropertyList.this);
				label.setText("Suburb:");
				value.setText(p.getSuburb());
				tr.addView(label);
				tr.addView(value);
				featureTable.addView(tr);
			}

			if (!p.getBedrooms().equals("")) {
				tr = new TableRow(PropertyList.this);
				label = new TextView(PropertyList.this);
				value = new TextView(PropertyList.this);
				label.setText("Bedrooms:");
				value.setText(p.getBedrooms());
				tr.addView(label);
				tr.addView(value);
				featureTable.addView(tr);
			}

			if (!p.getBathrooms().equals("")) {
				tr = new TableRow(PropertyList.this);
				label = new TextView(PropertyList.this);
				value = new TextView(PropertyList.this);
				label.setText("Bathrooms:");
				value.setText(p.getBathrooms());
				tr.addView(label);
				tr.addView(value);
				featureTable.addView(tr);
			}

			if (!p.getCarSpaces().equals("")) {
				tr = new TableRow(PropertyList.this);
				label = new TextView(PropertyList.this);
				value = new TextView(PropertyList.this);
				label.setText("CarSpaces:");
				value.setText(p.getCarSpaces());
				tr.addView(label);
				tr.addView(value);
				featureTable.addView(tr);
			}

			TextView title = (TextView) row.findViewById(R.id.summaryTitle);
			title.setText(p.getAddress());

			return(row);
		} //getView

	} //PropertyListAdapter class

} // PropertyList class
