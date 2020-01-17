package com.woodlandcoders.digicalc;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddHex extends Fragment implements AdapterView.OnItemSelectedListener {

    private Button hexButton;
    private Spinner hexSp;
    private String selection;


    public AddHex() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_hex, container, false);
        hexButton = view.findViewById(R.id.hexButton);


        hexSp = view.findViewById(R.id.hexSp);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this.getActivity(), R.array.operators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hexSp.setAdapter(adapter);
        hexSp.setOnItemSelectedListener(this);

        return view;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selection = adapterView.getItemAtPosition(i).toString();
        hexSp.setSelection(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
