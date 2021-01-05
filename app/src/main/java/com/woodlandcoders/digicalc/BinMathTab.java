package com.woodlandcoders.digicalc;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class BinMathTab extends Fragment implements AdapterView.OnItemSelectedListener {

    private CommonUtils commonUtils;

    private Spinner binSp;
    private EditText editText1;
    private EditText editText2;
    private TextView resultTextView;
    private TextView binCtField1;
    private TextView binCtField2;
    private String selection;


    public BinMathTab() {
        // Empty constructor.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bin_math_tab, container, false);

        final Base2Maths maths = Base2Maths.getInstance();
        commonUtils = CommonUtils.getInstance();

        Button execButton = view.findViewById(R.id.binButton);
        Button clr = view.findViewById(R.id.binClearFieldButton);
        resultTextView = view.findViewById(R.id.binAnswer);

        // Inserting keyboard fragment.
        // The Child Fragment.
        BinKeyPad binKeyPad = new BinKeyPad();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.binKeyFrame, binKeyPad).commit();

        editText1 = view.findViewById(R.id.binEditText1);
        editText1.setCursorVisible(true);
        editText1.requestFocus(); // Places the cursor in the field on start up.
        editText1.setShowSoftInputOnFocus(false);// Disables soft keyboard without disabling the cursor.

        editText2 = view.findViewById(R.id.binEditText2);
        editText2.setCursorVisible(true);
        editText2.setShowSoftInputOnFocus(false);// Disables soft keyboard without disabling the cursor.

        // Counter fields.
        binCtField1 = view.findViewById(R.id.binCounterField1);
        binCtField2 = view.findViewById(R.id.binCounterField2);
        binCtField1.setText(R.string.zero64);
        binCtField2.setText(R.string.zero64);

        // Spinner functionality stuff.
        binSp = view.findViewById(R.id.binSp);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(Objects.requireNonNull(this.getActivity()), R.array.operators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binSp.setAdapter(adapter);
        binSp.setOnItemSelectedListener(this);

        // The execute button.
        execButton.setOnClickListener(new View.OnClickListener() {
            // --Commented out by Inspection (6/11/20 5:50 PM):String s;
            @Override
            public void onClick(View v) {
                String s1 = String.valueOf(editText1.getText());
                String s2 = String.valueOf(editText2.getText());
                resultTextView.setText(maths.binMath(s1, s2, selection));
            }
        });

        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.getText().clear();
                editText2.getText().clear();
                resultTextView.setText("");
                binCtField1.setText(R.string.zero64);
                binCtField2.setText(R.string.zero64);
            }
        });
        return view;

    } // End of onCreateView.


    private int cursorPosition = 0; // Related to the code just below. "cursor position"

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BinViewModel binViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(BinViewModel.class);
        binViewModel.getBinDigit().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                boolean isValue = false;
                boolean isMinus = false;
                boolean isBackSpace = false;
                boolean isArrow = false;

                if(charSequence == "-") {
                    isMinus = true;
                }
                if(charSequence == "Z") {
                    isBackSpace = true;
                }
                if(charSequence == "1" || charSequence == "0") {
                    isValue = true;
                }
                if(charSequence == "<" | charSequence == ">"){
                    isArrow = true;
                }

                if(editText1.length() < 64 | isMinus | isBackSpace | isArrow) {
                    if (editText1.isFocused()) {
                        cursorPosition = editText1.getSelectionStart();
                        CharSequence et1 = editText1.getText();

                        DataContainer container = commonUtils.etBehavior(charSequence, et1, cursorPosition, isMinus, isValue, isBackSpace);
                        editText1.setText(container.cs);
                        editText1.setSelection(container.pos);
                        // Android Studio doesn't like concatenation inside .setText methods
                        String countVal1 = editText1.getSelectionStart() + getString(R.string.slash64);
                        binCtField1.setText(countVal1);
                    }
                }
                if(editText2.length() < 64| isMinus | isBackSpace | isArrow){
                    if (editText2.isFocused()) {
                        cursorPosition = editText2.getSelectionStart();
                        String et2 = editText2.getText().toString();

                        DataContainer container = commonUtils.etBehavior(charSequence, et2, cursorPosition, isMinus, isValue, isBackSpace);
                        editText2.setText(container.cs);
                        editText2.setSelection(container.pos);
                        // Android Studio doesn't like concatenation inside .setText methods
                        String countVal2 = editText2.getSelectionStart() + getString(R.string.slash64);
                        binCtField2.setText(countVal2);
                    }
                }
            }
        });
        binViewModel.insertBinDigit("");
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

    // Turns off the soft keyboard on start up or when the activity comes into view
    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


}
