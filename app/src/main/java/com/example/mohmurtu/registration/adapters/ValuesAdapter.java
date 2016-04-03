package com.example.mohmurtu.registration.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.mohmurtu.registration.R;
import com.example.mohmurtu.registration.model.Value;

import java.util.List;

/**
 * Created by Apresh on 6/7/2015.
 */
public class ValuesAdapter extends ArrayAdapter{
    Context context;
    List<Value> valueList;
    String propertyName;
    String propertyId;

    public ValuesAdapter(Context context, List<Value> valueList, String propertyName,
                         String propertyId) {
        super(context,0, valueList);
        this.context = context;
        this.valueList = valueList;
        this.propertyId = propertyId;
        this.propertyName = propertyName;
    }
    @Override
    public int getCount() {
        return (this.valueList != null ? this.valueList.size():0);
    }

    @Override
    public Object getItem(int position) {
        return this.valueList.get(position);
    }


    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_value_item, null);
        }
        TextView valueName = (TextView) convertView.findViewById(R.id.valueName);
        Value value = (Value) getItem(position);
        value.setPropertyName(propertyName);
        value.setPropertyId(propertyId);
        valueName.setText(value.getValueName());
        convertView.setTag(value);
        return convertView;
    }



}


