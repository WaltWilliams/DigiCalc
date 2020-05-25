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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class BinMathTab extends Fragment implements AdapterView.OnItemSelectedListener {

    private BinViewModel binViewModel;
    // The Child Fragment.
    private BinKeyPad binKeyPad;

    private Button execButton;
    private Button clr;
    private Spinner binSp;
    private EditText editText1;
    private EditText editText2;
    private TextView resultTextView;
    private String selection;


    public BinMathTab() {
        // Empty constructor.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bin_math_tab, container, false);


        final Base2Maths maths = Base2Maths.getInstance();

        execButton = view.findViewById(R.id.binButton);
        clr = view.findViewById(R.id.binClearFieldButton);
        resultTextView = view.findViewById(R.id.binAnswer);

        // Inserting keyboard fragment.
        binKeyPad = new BinKeyPad();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.binKeyFrame, binKeyPad).commit();


        editText1 = view.findViewById(R.id.binEditText1);
        editText1.setCursorVisible(true);
        editText1.requestFocus(); // Places the cursor in the field on start up.
        editText1.setShowSoftInputOnFocus(false);// Disables soft keyboard without disabling the cursor.

        editText2 = view.findViewById(R.id.binEditText2);
        editText2.setCursorVisible(true);
        editText2.setShowSoftInputOnFocus(false);// Disables soft keyboard without disabling the cursor.

        // Spinner functionality stuff.
        binSp = view.findViewById(R.id.binSp);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(this.getActivity(), R.array.operators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binSp.setAdapter(adapter);
        binSp.setOnItemSelectedListener(this);

        // The execute button.
        execButton.setOnClickListener(new View.OnClickListener() {
            String s;
            @Override
            public void onClick(View v) {
                if((editText1.length() != 0) && (editText2.length() != 0)){
                    String s1 = String.valueOf(editText1.getText());
                    String s2 = String.valueOf(editText2.getText());
                    s = maths.binMath(s1, s2, selection);
                }
                else{
                    s = "Missing value";
                }
                resultTextView.setText(s);
            }
        });

        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.getText().clear();
                editText2.getText().clear();
                resultTextView.setText("");
            }
        });
        return view;

    } // End of onCreateView.


    private int cp = 0; // Related to the code just below. "cursor position"

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binViewModel = new ViewModelProvider(getActivity()).get(BinViewModel.class);
        binViewModel.getBinDigit().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                boolean isValue = false;
                boolean isMinus = false;
                boolean isBackSpace = false;

                if (charSequence == "-") {
                    isMinus = true;
                }
                if (charSequence == "Z") {
                    isBackSpace = true;
                }
                if (charSequence == "1" || charSequence == "0") {
                    isValue = true;
                }

                if (editText1.isFocused()) {
                    cp = editText1.getSelectionStart();
                    CharSequence et1 = editText1.getText();

                    DataContainer container = CommonUtils.etBehavior(charSequence, et1, cp, isMinus, isValue, isBackSpace);
                    editText1.setText(container.cs);
                    editText1.setSelection(container.pos);
                }
                if (editText2.isFocused()) {
                    cp = editText2.getSelectionStart();
                    String et2 = editText2.getText().toString();

                    DataContainer container = CommonUtils.etBehavior(charSequence, et2, cp, isMinus, isValue, isBackSpace);
                    editText2.setText(container.cs);
                    editText2.setSelection(container.pos);
                }
            }
        });
    }



    // This is for the spinner so one can determine the selected math mode.
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selection = adapterView.getItemAtPosition(i).toString();
        binSp.setSelection(i);

        if(selection.compareTo("Add") == 0){
            editText1.setHint("Addend");
            editText2.setHint("Addend");
        }
        else if(selection.compareTo("Mult") == 0){
            editText1.setHint("Multiplicand");
            editText2.setHint("Multiplier");
        }
        else if(selection.compareTo("Div") == 0){
            editText1.setHint("Dividend");
            editText2.setHint("Divisor");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
