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
public class Convert extends Fragment implements AdapterView.OnItemSelectedListener {

    private Button convButton;
    private Spinner conSp;
    private String selection;


    public Convert() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_convert, container, false);
        convButton = view.findViewById(R.id.convertButton);

        conSp = view.findViewById(R.id.convertSp);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this.getActivity(), R.array.number_base, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conSp.setAdapter(adapter);
        conSp.setOnItemSelectedListener(this);

        convButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        selection = adapterView.getItemAtPosition(i).toString();
        conSp.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
