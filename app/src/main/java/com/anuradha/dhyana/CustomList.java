package com.anuradha.dhyana;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This method creates the custom listView.
 */
public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final Integer[] imageId;

    public CustomList(Activity context,
                      String[] benefitStr, Integer[] imageId) {
        super(context, R.layout.custom_list, benefitStr);
        this.context = context;
        this.imageId = imageId;
    }

    /**
     * This method is called to create each listView by loading the appropraiate views from the custom_list.xml
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View rowView = view;
        LayoutInflater inflater = context.getLayoutInflater();
        if (view == null)
            rowView = inflater.inflate(R.layout.custom_list, parent, false);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.list_txt1);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_img);
        Resources res = context.getResources();
        String[] benefits_arr = res.getStringArray(R.array.benefits_array);
        txtTitle.setText(benefits_arr[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}