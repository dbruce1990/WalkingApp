package com.janedoe.anothertabexample.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.janedoe.anothertabexample.Models.Route;
import com.janedoe.anothertabexample.R;

import java.util.ArrayList;
import com.janedoe.anothertabexample.Adapters.*;

public class PreviousWalksTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_previous_walks, container, false);
        ArrayList<Route> walks = new ArrayList<>();
        Route walk1 = new Route();
        walk1.setDescription("Walked around the block.");
        walk1.setElapsedTime(SystemClock.uptimeMillis() - 99999999);
        walk1.setElapsedTime(100000000);

        Route walk2 = new Route();
        walk2.setDescription("Walked to store.");
        walk2.setElapsedTime(SystemClock.uptimeMillis() - 98999000);
        walk2.setElapsedTime(100000000);

        Route walk3 = new Route();
        walk3.setDescription("Ran a million miles.");
        walk3.setElapsedTime(SystemClock.uptimeMillis() - 97999999);
        walk3.setElapsedTime(18222214);

        Route walk4 = new Route();
        walk4.setDescription("Biked");
        walk4.setElapsedTime(SystemClock.uptimeMillis() - 96999999);
        walk4.setElapsedTime(4322342);

        walks.add(walk1);
        walks.add(walk2);
        walks.add(walk3);
        walks.add(walk4);

        ListItemAdapter listAdapter = new ListItemAdapter(getContext(), R.layout.fragment_previous_walk_item, walks);
        ListView listView = (ListView) root.findViewById(R.id.list_view);
        listView.setAdapter(listAdapter);

        return root;
    }
}
