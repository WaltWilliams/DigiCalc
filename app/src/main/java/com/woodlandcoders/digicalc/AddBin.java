package com.woodlandcoders.digicalc;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBin extends Fragment implements AdapterView.OnItemSelectedListener {

    private Button binButton;
    private Spinner binSp;
    private String selection;
    private EditText editText1;
    private EditText editText2;
    private TextView resultTextView;


    public AddBin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_bin, container, false);

        editText1 = view.findViewById(R.id.binEditText1);
        editText2 = view.findViewById(R.id.binEditText2);
        resultTextView = view.findViewById(R.id.binAnswer);
        binButton = view.findViewById(R.id.binButton);
        binSp = view.findViewById(R.id.binSp);

        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this.getActivity(), R.array.operators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binSp.setAdapter(adapter);
        binSp.setOnItemSelectedListener(this);

        // Instantiate Singleton Class aseMaths so it can be used everywhere.
        final BaseMaths maths = BaseMaths.getInstance();

        binSp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String v1 = editText1.getText().toString();
                String v2 = editText2.getText().toString();

                String result = maths.performMath("binary", v1, v2, selection);
                resultTextView.setText(result);
            }
        });

        return view;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        selection = adapterView.getItemAtPosition(i).toString();
        binSp.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
