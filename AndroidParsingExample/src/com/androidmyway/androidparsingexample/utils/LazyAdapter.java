package com.androidmyway.androidparsingexample.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidmyway.androidparsingexample.R;

public class LazyAdapter extends BaseAdapter {
    
    private ArrayList<ArrayList<String>> data;
    private LayoutInflater inflater=null;
    
    public LazyAdapter(Activity a, ArrayList<ArrayList<String>> info) {
	        data=info;
	        inflater = (LayoutInflater)a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        	return data.size();
    }

    public Object getItem(int position) {
        	return position;
    }

    public long getItemId(int position) {
    		return position;
    }
    
	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_item, null);
        ArrayList<String> temp = data.get(position);
        TextView usertext=(TextView)vi.findViewById(R.id.name);
        TextView disttext =(TextView)vi.findViewById(R.id.desciption);
        TextView taxinotext=(TextView)vi.findViewById(R.id.cost);
        
        
        usertext.setText(temp.get(1).toString());
        disttext.setText(temp.get(3).toString());
        taxinotext.setText(temp.get(2).toString());
         
		return vi;
    }
    
}