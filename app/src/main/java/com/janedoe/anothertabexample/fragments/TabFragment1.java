package com.janedoe.anothertabexample.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.janedoe.anothertabexample.R;
import com.janedoe.anothertabexample.models.WalkModel;

import java.util.ArrayList;
import com.janedoe.anothertabexample.Adapters.*;

public class TabFragment1 extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_fragment_1, container, false);
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

        ListItemAdapter listAdapter = new ListItemAdapter(getContext(), R.layout.list_item_fragment, walks);
        if(listAdapter == null)
            Log.d("ListAdapter: ", "Null!!!");
        else
            Log.d("ListAdapter: ", "Not Null!!!");
        ListView listView = (ListView) root.findViewById(R.id.list_view);
        if(listView == null)
            Log.d("ListView: ", "Null!!!");
        else
            Log.d("ListView: ", "Not Null!!!");
        listView.setAdapter(listAdapter);

        return root;
    }
}
