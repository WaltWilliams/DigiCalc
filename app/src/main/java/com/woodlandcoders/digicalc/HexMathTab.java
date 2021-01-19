package com.woodlandcoders.digicalc;

import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private boolean isCheckBoxChecked;
    private Boolean wChoice = null;


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
        final HexKeyPad wHexKeyPad = new HexKeyPad();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.hexKeyFrame, wHexKeyPad).commit();

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


        // This section of code from this line to the next line was added to Jan-21.
        // This adds the Caps-lock feature on the Hexadecimal tab.
        // ======================================================================================
        // Reading the stored upper or lower case wChoice.
        // Stored in a json file in this app's internal memory.
        final Context wContext = getContext();

        InputStreamReader wISR = null;
        try {
            InputStream wIS = wContext.openFileInput(getResources().getString(R.string.jsonFilename));
            wISR = new InputStreamReader(wIS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        try {
            BufferedReader bf =new BufferedReader(wISR);
            JSONObject wJO = new JSONObject(bf.readLine());
            wChoice = wJO.getBoolean("caps");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


        // Check Box event handler.
        CheckBox wCB = view.findViewById(R.id.capsCheckBox);
        wCB.setChecked(wChoice);
        wHexKeyPad.setButtonCaseUpper(wChoice);
        wCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    editText1.setText(upperCaseToLower(editText1.getText().toString()));
                    editText2.setText(upperCaseToLower(editText2.getText().toString()));
                    if(resultTextView.length() > 0){
                        resultTextView.setText(upperCaseToLower(resultTextView.getText().toString()));
                    }
                    isCheckBoxChecked = false;
                    wHexKeyPad.setButtonCaseUpper(false);
                    wHexKeyPad.changeButtonTextCase();
                    wChoice = false;
                }
                if(isChecked){
                    editText1.setText(lowerCaseToUpper(editText1.getText().toString()));
                    editText2.setText(lowerCaseToUpper(editText2.getText().toString()));
                    if(resultTextView.length() > 0){
                        resultTextView.setText(lowerCaseToUpper(resultTextView.getText().toString()));
                    }
                    isCheckBoxChecked = true;
                    wHexKeyPad.setButtonCaseUpper(true);
                    wHexKeyPad.changeButtonTextCase();
                    wChoice = true;
                }

                // When the CheckBox is clicked and its state changes, the new state
                // needs to be stored so that when the apps
                JSONObject jo = new JSONObject();
                File choiceStorageFile = getContext().getFileStreamPath(getResources().getString(R.string.jsonFilename));
                if(choiceStorageFile.exists()){
                    OutputStreamWriter osw = null;
                    try {
                        osw = new OutputStreamWriter(wContext.openFileOutput(getResources().getString(R.string.jsonFilename), Context.MODE_PRIVATE));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        jo.put("caps", wChoice);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        osw.write(jo.toString());
                        osw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // ======================================================================================


        // This is where the math is performed.
        // ===============================================================================
        // Spinner functionality stuff.
        hexSp = view.findViewById(R.id.hexSp);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource(Objects.requireNonNull(this.getActivity()), R.array.operators, R.layout.custom_spinner_layout);
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
                // If the checkbox is not checked convert from lower case to upper case and go.
                if(!isCheckBoxChecked){
                    s1 = lowerCaseToUpper(s1);
                    s2 = lowerCaseToUpper(s2);
                    resultTextView.setText(upperCaseToLower(maths.hexMath(s1, s2, selection)));
                }
                if(isCheckBoxChecked){
                    resultTextView.setText(maths.hexMath(s1, s2, selection));
                }
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
                        charSequence == "F" ||
                        charSequence == "a" ||  // Added Jan2021
                        charSequence == "b" ||
                        charSequence == "c" ||
                        charSequence == "d" ||
                        charSequence == "e" ||
                        charSequence == "f"){
                    isValue = true;
                }

                // the "DataContainer" is a home spun class. It is a data structure. Look to the left.
                // -------------------------------------------------------------------------------
                // This section of code provides adds the functionality to the EditText for the
                // arrow, backspace keys and places the minus sign to the far left no matter
                // where the cursor location when the minus key is tapped. It also prevents
                // digits from being placed to the left of the minus sign.

                // Theses if statement insures that if you start with a minus sign you can still
                // enter a full 16 digits
                int wNumberOfDigitsFor64 = 16;
                if(editText1.length() > 0) {
                    if(editText1.getText().charAt(0) == '-'){
                        wNumberOfDigitsFor64 = 17;
                    }
                }
                if(editText1.length() < wNumberOfDigitsFor64 | isMinus | isBackSpace | isArrow) {
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
                    if(editText2.length() > 0) {
                        if(editText2.getText().charAt(0) == '-'){
                            wNumberOfDigitsFor64 = 17;
                        }
                    }
                }
                if(editText2.length() < wNumberOfDigitsFor64 | isMinus | isBackSpace | isArrow){
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




    private String upperCaseToLower(String str){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == 'A'){
                sb.append('a');
            }else if(str.charAt(i) == 'B'){
                sb.append('b');
            }else if(str.charAt(i) == 'C'){
                sb.append('c');
            }else if(str.charAt(i) == 'D'){
                sb.append('d');
            }else if(str.charAt(i) == 'E'){
                sb.append('e');
            }else if(str.charAt(i) == 'F'){
                sb.append('f');
            }
            else{
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }


    private String lowerCaseToUpper(String str){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == 'a'){
                sb.append('A');
            }else if(str.charAt(i) == 'b'){
                sb.append('B');
            }else if(str.charAt(i) == 'c'){
                sb.append('C');
            }else if(str.charAt(i) == 'd'){
                sb.append('D');
            }else if(str.charAt(i) == 'e'){
                sb.append('E');
            }else if(str.charAt(i) == 'f'){
                sb.append('F');
            }
            else{
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
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
