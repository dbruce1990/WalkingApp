package com.janedoe.anothertabexample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TabFragment1 extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        String[] foods = {"Bacon", "Eggs", "Tuna", "Candy", "Meatball", "Potato"};
        ListAdapter listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, foods);
        ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
        listView.setAdapter(listAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_1, container, false);
    }
}
