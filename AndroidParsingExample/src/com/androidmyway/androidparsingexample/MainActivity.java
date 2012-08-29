package com.androidmyway.androidparsingexample;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.androidmyway.androidparsingexample.database.PlaceDataSQL;
import com.androidmyway.androidparsingexample.parser.XmlElement;
import com.androidmyway.androidparsingexample.parser.XmlParser;
import com.androidmyway.androidparsingexample.utils.LazyAdapter;
import com.androidmyway.androidparsingexample.utils.Utils;

public class MainActivity extends ActionBarActivity {
	
    private static PlaceDataSQL placeData;
	private ListView listView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        onInitialize();
        getdata();
        if(Utils.xmlData.size()>0)
        	getdata();
        else
        	new getadsResult().execute();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case android.R.id.home:
	        	Utils.selIndex = 1;
	        	Intent i = new Intent(MainActivity.this,SingleRowDisplay.class);
		        startActivity(i);
		        MainActivity.this.finish();
	            break;

            case R.id.menu_refresh:
                //Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
                getActionBarHelper().setRefreshActionItemState(true);
                getWindow().getDecorView().postDelayed(
                        new Runnable() {
                            public void run() {
                                getActionBarHelper().setRefreshActionItemState(false);
                                getdata();
                            }
                        }, 1000);
                break;
            
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	// TODO Auto-generated method stub
    	switch (item.getItemId()) {
	        case android.R.id.home:
	        	Utils.selIndex = 1;
	        	Intent i = new Intent(MainActivity.this,SingleRowDisplay.class);
		        startActivity(i);
		        MainActivity.this.finish();
	            break;
    	}
    	return super.onMenuItemSelected(featureId, item);
    	
    }
    
    void onInitialize(){
    	
    	placeData = new PlaceDataSQL(MainActivity.this);
        
        listView = (ListView) findViewById(R.id.xmllistview);
        listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Utils.selIndex = arg2;
				Intent i = new Intent(MainActivity.this,SingleRowDisplay.class);
				startActivity(i);
				MainActivity.this.finish();
			}
		});
        
    }
    
    private void insertData(ArrayList<String> data) {
    	SQLiteDatabase db = placeData.getWritableDatabase();
    	ContentValues values;
		values = new ContentValues();
		//values.put("id", id);
		values.put("name", data.get(1));
		values.put("description", data.get(3));
		values.put("cost", data.get(2));
	
		db.insert("xmlTable", null, values);
		
		db.close();

		//Toast.makeText(getApplicationContext(), "Values Inserted Successfully.", Toast.LENGTH_SHORT).show();
		
    }

    public void getdata(){
    	Utils.xmlData.clear();
    	String selectQuery = "SELECT * FROM xmlTable;";
    	SQLiteDatabase db = placeData.getWritableDatabase();
        
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        Utils.xmlData.clear();
        if (cursor.moveToFirst()) {
            do {
            	ArrayList<String> temp = new ArrayList<String>();
        		temp.add(cursor.getString(0));
        		temp.add(cursor.getString(1));
        		temp.add(cursor.getString(3));
        		temp.add(cursor.getString(2));
        		
        		Utils.xmlData.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        LazyAdapter adapter = new LazyAdapter(MainActivity.this, Utils.xmlData);
		listView.setAdapter(adapter);
    }
    
    class getadsResult extends AsyncTask<Object, Object, Object> {

		private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

		protected void onPreExecute() {
			Dialog.setMessage("Loading please wait........");
			Dialog.show();
			Utils.xmlData.clear();
		}

		protected Object doInBackground(Object... params) {
			try {
				//String Url = "http://www.spymek.com/php/display.xml";
				AssetManager assetManager = getApplicationContext().getAssets();
			    InputStream is = assetManager.open("display.xml");
			    XmlElement ele = null;
				try {
					//ele = XmlParser.parse(Url.toString().trim().replaceAll(" ", "%20"));
					ele = XmlParser.parse(is);
					for (int i = 0; i < ele.getElements().size(); i++) {
						ArrayList<String> temp = new ArrayList<String>();
						for (XmlElement e : ele.getElements().get(i).getElements()) {
							temp.add(e.getValue().toString());
						}
						insertData(temp);
						Utils.xmlData.add(temp);
					}
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		protected void onPostExecute(Object result) {
			LazyAdapter adapter = new LazyAdapter(MainActivity.this, Utils.xmlData);
			listView.setAdapter(adapter);
			if (Dialog.isShowing()) {
				Dialog.dismiss();
			}
		}

	}

}
