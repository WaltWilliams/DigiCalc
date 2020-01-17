package com.woodlandcoders.digicalc;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddOct extends Fragment implements AdapterView.OnItemSelectedListener{

    private Button octButton;
    private Spinner octSp;
    private String selection;


    public AddOct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_octal, container, false);
        octButton = view.findViewById(R.id.octButton);

        octSp = view.findViewById(R.id.octSp);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this.getActivity(), R.array.operators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        octSp.setAdapter(adapter);
        octSp.setOnItemSelectedListener(this);

        return view;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        selection = adapterView.getItemAtPosition(i).toString();
        octSp.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
