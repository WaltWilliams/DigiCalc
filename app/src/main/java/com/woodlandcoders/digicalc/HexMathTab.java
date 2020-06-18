package com.woodlandcoders.digicalc;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

import java.util.Objects;


public class HexMathTab extends Fragment implements AdapterView.OnItemSelectedListener {
// The OnItemSelectedListener is for the spinner

    private CommonUtils commonUtils;

    private Spinner hexSp;
    private EditText editText1;
    private EditText editText2;
    private String selection;
    private TextView resultTextView;
    private TextView hexCtField1;
    private TextView hexCtField2;


    public HexMathTab() {
        // Empty constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hex_math_tab, container, false);

        final Base16Maths maths = Base16Maths.getInstance();

        commonUtils = CommonUtils.getInstance();

        // Inserting keyboard fragment.
        // The Child Fragment.
        HexKeyPad hexKeyPad = new HexKeyPad();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.hexKeyFrame, hexKeyPad).commit();

        Button execButton = view.findViewById(R.id.hexButton);
        Button clr = view.findViewById(R.id.hexClearFieldButton);
        resultTextView = view.findViewById(R.id.hexAnswer);

        editText1 = view.findViewById(R.id.hexEditText1);
        editText1.setCursorVisible(true);
        editText1.requestFocus(); // Places the cursor in the field on start up.
        editText1.setShowSoftInputOnFocus(false);// Disables soft keyboard without disabling the cursor.

        editText2 = view.findViewById(R.id.hexEditText2);
        editText2.setCursorVisible(true);
        editText2.setShowSoftInputOnFocus(false);// Disables soft keyboard without disabling the cursor.
        // Counter fields.
        hexCtField1 = view.findViewById(R.id.hexCounterField1);
        hexCtField2 = view.findViewById(R.id.hexCounterField2);
        hexCtField1.setText(R.string.zero16);
        hexCtField2.setText(R.string.zero16);


        // Spinner functionality stuff.
        hexSp = view.findViewById(R.id.hexSp);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(Objects.requireNonNull(this.getActivity()), R.array.operators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hexSp.setAdapter(adapter);
        hexSp.setOnItemSelectedListener(this);

        // The execute button.
        execButton.setOnClickListener(new View.OnClickListener() {
            // --Commented out by Inspection (6/11/20 5:50 PM):String s;

            @Override
            public void onClick(View v) {

                String s1 = String.valueOf(editText1.getText());
                String s2 = String.valueOf(editText2.getText());
                resultTextView.setText(maths.hexMath(s1, s2, selection));
            }
        });

        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText1.getText().clear();
                editText2.getText().clear();
                resultTextView.setText("");
                hexCtField1.setText(R.string.zero16);
                hexCtField2.setText(R.string.zero16);
            }
        });

        return view;

    } // End of onCreateView.


    private int cp = 0; // Related to the code just below. "cursor position"

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HexViewModel hexViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(HexViewModel.class);
        hexViewModel.getHexDigit().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                boolean isValue = false;
                boolean isMinus = false;
                boolean isBackSpace = false;
                boolean isArrow = false;

                if(charSequence == "-"){
                    isMinus = true;
                }
                if(charSequence == "Z"){
                    isBackSpace = true;
                }
                if(charSequence == "<" | charSequence == ">"){
                    isArrow = true;
                }
                if(charSequence == "1" ||
                        charSequence == "2" ||
                        charSequence == "3" ||
                        charSequence == "4" ||
                        charSequence == "5" ||
                        charSequence == "6" ||
                        charSequence == "7" ||
                        charSequence == "8" ||
                        charSequence == "9" ||
                        charSequence == "0" ||
                        charSequence == "A" ||
                        charSequence == "B" ||
                        charSequence == "C" ||
                        charSequence == "D" ||
                        charSequence == "E" ||
                        charSequence == "F"){
                    isValue = true;
                }

                if(editText1.length() < 16 | isMinus | isBackSpace | isArrow) {
                    if (editText1.isFocused()) {
                        cp = editText1.getSelectionStart();
                        CharSequence et1 = editText1.getText();

                        DataContainer container = commonUtils.etBehavior(charSequence, et1, cp, isMinus, isValue, isBackSpace);
                        editText1.setText(container.cs);
                        editText1.setSelection(container.pos);
                        // Android Studio doesn't like concatenation inside .setText methods
                        String countVal1 = editText1.getSelectionStart() + getString(R.string.slash16);
                        hexCtField1.setText(countVal1);
                    }
                }
                if(editText2.length() < 16 | isMinus | isBackSpace | isArrow){
                    if (editText2.isFocused()) {
                        cp = editText2.getSelectionStart();
                        String et2 = editText2.getText().toString();

                        DataContainer container = commonUtils.etBehavior(charSequence, et2, cp, isMinus, isValue, isBackSpace);
                        editText2.setText(container.cs);
                        editText2.setSelection(container.pos);
                        // Android Studio doesn't like concatenation inside .setText methods
                        String countVal2 = editText2.getSelectionStart() + getString(R.string.slash16);
                        hexCtField2.setText(countVal2);
                    }
                }
            }
        });
        hexViewModel.insertHexDigit("");
    }



    // This is for the spinner so one can determine the selected math mode.
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selection = adapterView.getItemAtPosition(i).toString();
        hexSp.setSelection(i);

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
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

}
