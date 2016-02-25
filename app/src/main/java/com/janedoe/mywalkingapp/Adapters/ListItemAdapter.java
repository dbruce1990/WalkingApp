package com.janedoe.mywalkingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.janedoe.mywalkingapp.Models.WalkModel;
import com.janedoe.mywalkingapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by janedoe on 12/10/2015.
 */
public class ListItemAdapter extends ArrayAdapter<WalkModel> {

    public ListItemAdapter(Context context, int resource, ArrayList<WalkModel> walks) {
        super(context, resource, walks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View listItem = inflater.inflate(R.layout.fragment_previous_walk_item, parent, false);

        WalkModel walk = getItem(position);

        TextView date = (TextView) listItem.findViewById(R.id.date);
        TextView elapsedTime = (TextView) listItem.findViewById(R.id.elapsed_time);
        TextView description = (TextView) listItem.findViewById(R.id.description);

        date.setText(new SimpleDateFormat("MM/dd/yyyy").format(walk.getCreatedAt()));

        String formattedTime = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(walk.getElapsedTime()),
                TimeUnit.MILLISECONDS.toMinutes(walk.getElapsedTime()) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(walk.getElapsedTime())),
                TimeUnit.MILLISECONDS.toSeconds(walk.getElapsedTime()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(walk.getElapsedTime())));
        elapsedTime.setText(formattedTime);
        description.setText(walk.getDescription());

        return listItem;

    }
}
