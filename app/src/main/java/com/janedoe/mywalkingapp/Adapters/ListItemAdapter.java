package com.janedoe.mywalkingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.janedoe.mywalkingapp.Models.Route;
import com.janedoe.mywalkingapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by janedoe on 12/10/2015.
 */
public class ListItemAdapter extends ArrayAdapter<Route> {

    public ListItemAdapter(Context context, int resource, ArrayList<Route> walks) {
        super(context,resource, walks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View listItem = inflater.inflate(R.layout.fragment_previous_walk_item, parent, false);

        Route model = getItem(position);

        TextView date = (TextView) listItem.findViewById(R.id.date);
        TextView elapsedTime = (TextView) listItem.findViewById(R.id.elapsed_time);
        TextView description = (TextView) listItem.findViewById(R.id.description);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = sdf.format(new Date(model.getCreatedAt()));

        date.setText(formattedDate);
        elapsedTime.setText(String.valueOf(model.getElapsedTime()));
        description.setText(model.getDescription());

        return listItem;

    }
}
