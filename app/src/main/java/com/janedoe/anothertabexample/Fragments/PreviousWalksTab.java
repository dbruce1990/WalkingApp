package com.janedoe.anothertabexample.Fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.janedoe.anothertabexample.R;
import com.janedoe.anothertabexample.Models.WalkModel;

import java.util.ArrayList;
import com.janedoe.anothertabexample.Adapters.*;

public class PreviousWalksTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_previous_walks, container, false);
        ArrayList<WalkModel> walks = new ArrayList<>();
        WalkModel walk1 = new WalkModel();
        walk1.setDescription("Walked around the block.");
        walk1.setElapsed_time(SystemClock.uptimeMillis() - 99999999);
        walk1.setElapsed_time(100000000);

        WalkModel walk2 = new WalkModel();
        walk2.setDescription("Walked to store.");
        walk2.setElapsed_time(SystemClock.uptimeMillis() - 98999000);
        walk2.setElapsed_time(100000000);

        WalkModel walk3 = new WalkModel();
        walk3.setDescription("Ran a million miles.");
        walk3.setElapsed_time(SystemClock.uptimeMillis() - 97999999);
        walk3.setElapsed_time(18222214);

        WalkModel walk4 = new WalkModel();
        walk4.setDescription("Biked");
        walk4.setElapsed_time(SystemClock.uptimeMillis() - 96999999);
        walk4.setElapsed_time(4322342);

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
