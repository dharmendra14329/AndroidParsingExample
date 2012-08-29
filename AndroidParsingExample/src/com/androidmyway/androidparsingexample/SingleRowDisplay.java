package com.androidmyway.androidparsingexample;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidmyway.androidparsingexample.database.PlaceDataSQL;
import com.androidmyway.androidparsingexample.utils.Utils;

public class SingleRowDisplay extends ActionBarActivity {
	private EditText editName,editDesc,editCost;
	private static PlaceDataSQL placeData;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);
        onInitialize();
        getdata();
        setValues();
        
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.rowmenu, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	Intent i = new Intent(SingleRowDisplay.this,MainActivity.class);
    	        startActivity(i);
    	        SingleRowDisplay.this.finish();
                break;

            case R.id.menu_prev:
            	onPrevClick();
                break;
                
            case R.id.menu_next:
            	onNextClick();
                break;
                
            case R.id.menu_new:
            	onNewClick();
                break;
                
            case R.id.menu_delete:
            	onDeleteCLick();
                break;
            
        }
        return super.onOptionsItemSelected(item);
    }
	
	void onInitialize(){
		placeData = new PlaceDataSQL(SingleRowDisplay.this);
		editName = (EditText) findViewById(R.id.edit_name);
		editDesc = (EditText) findViewById(R.id.edit_desc);
		editCost = (EditText) findViewById(R.id.edit_cost);
	}
	
	void setValues(){
		if(Utils.selIndex == -1)
			return;
		ArrayList<String> data = Utils.xmlData.get(Utils.selIndex);
		editName.setText(data.get(1));
		editDesc.setText(data.get(3));
		editCost.setText(data.get(2));
	}
	
	public void onNextClick(){
		Utils.selIndex  = Utils.selIndex + 1;
		if(Utils.selIndex >= Utils.xmlData.size()){
			Utils.selIndex-- ;
			Toast.makeText(getApplicationContext(), "End of Records", Toast.LENGTH_SHORT).show();
			return;
		}
		setValues();
	}
	
	public void onPrevClick(){
		Utils.selIndex  = Utils.selIndex - 1;
		if(Utils.selIndex <= -1){
			Utils.selIndex++ ;
			Toast.makeText(getApplicationContext(), "End of Records", Toast.LENGTH_SHORT).show();
			return;
		}
		setValues();
	}

	public void onNewClick(){
		editName.setText("");
		editDesc.setText("");
		editCost.setText("");
	}
	
	public void onUpdateClick(View v){
		SQLiteDatabase db = placeData.getWritableDatabase();
		ContentValues values;
		values = new ContentValues();
		//values.put("id", id);
		values.put("name", editName.getText().toString());
		values.put("description", editDesc.getText().toString());
		values.put("cost", editCost.getText().toString());
		
		db.update("xmlTable", values, "id="+ Utils.xmlData.get(Utils.selIndex).get(0),null);
		db.close();
		
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(Utils.xmlData.get(Utils.selIndex).get(0));
		temp.add(editName.getText().toString());
		temp.add(editCost.getText().toString());
		temp.add(editDesc.getText().toString());
		
		// TODO Logic for updating data in arraylist 
		Utils.xmlData.add(Utils.selIndex,temp);
		Toast.makeText(getApplicationContext(), "Record Updated.", Toast.LENGTH_SHORT).show();
	}
	
	public void onAddClick(View v){
		SQLiteDatabase db = placeData.getWritableDatabase();
		ContentValues values;
		values = new ContentValues();
		//values.put("id", id);
		values.put("name", editName.getText().toString());
		values.put("description", editDesc.getText().toString());
		values.put("cost", editCost.getText().toString());
		
		db.insert("xmlTable", null, values);
		int index = 0;
		String query = "SELECT id from xmlTable order by id DESC limit 1";
		Cursor c = db.rawQuery(query,null);
		if (c != null && c.moveToFirst()) {
			index = c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
		}
		c.close();
		db.close();
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(String.valueOf(index));
		temp.add(editName.getText().toString());
		temp.add(editCost.getText().toString());
		temp.add(editDesc.getText().toString());
		
		Utils.xmlData.add(temp);
		
		Utils.selIndex = Utils.xmlData.size()-1;
		
		setValues();
		
		Toast.makeText(getApplicationContext(), "Record Added.", Toast.LENGTH_SHORT).show();
	}
	
	public void onDeleteCLick(){
		SQLiteDatabase db = placeData.getWritableDatabase();
    	db.delete("xmlTable", "id=" + Utils.xmlData.get(Utils.selIndex).get(0), null);
    	db.close();
    	Utils.xmlData.remove(Utils.selIndex);
    	Utils.selIndex = Utils.selIndex - 1;
    	if(Utils.selIndex == -1)
    		Utils.selIndex = Utils.selIndex + 2;
    	if(Utils.xmlData.size() == 0){
    		onNewClick();
    		Toast.makeText(getApplicationContext(), "No record to display.", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	setValues();
    	Toast.makeText(getApplicationContext(), "Record Deleted.", Toast.LENGTH_SHORT).show();
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
        
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	Intent i = new Intent(SingleRowDisplay.this,MainActivity.class);
	        startActivity(i);
	        SingleRowDisplay.this.finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}
