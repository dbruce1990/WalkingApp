package com.janedoe.anothertabexample.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.janedoe.anothertabexample.R;
import com.janedoe.anothertabexample.fragments.TabFragment1;
import com.janedoe.anothertabexample.models.WalkModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by janedoe on 12/10/2015.
 */
public class ListItemAdapter extends ArrayAdapter<WalkModel> {

    public ListItemAdapter(Context context, int resource, ArrayList<WalkModel> walks) {
        super(context,resource, walks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View listItem = inflater.inflate(R.layout.list_item_fragment, parent, false);

        WalkModel model = getItem(position);

        TextView date = (TextView) listItem.findViewById(R.id.date);
        TextView elapsedTime = (TextView) listItem.findViewById(R.id.elapsed_time);
        TextView description = (TextView) listItem.findViewById(R.id.description);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = sdf.format(new Date(model.getDate()));

        date.setText(formattedDate);
        elapsedTime.setText(String.valueOf(model.getElapsedTime()));
        description.setText(model.getDescription());

        return listItem;

    }
}
