package com.woodlandcoders.digicalc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class HexKeyPad extends Fragment implements View.OnClickListener {

    private Button aButton;
    private Button bButton;
    private Button cButton;
    private Button dButton;
    private Button eButton;
    private Button fButton;
    private boolean isUpperCase;

    private HexViewModel hexViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // "container" is the parent view.
        // "AttachToRoot" is if we are attaching this layout to the parent view,
        //  which in this case we are not.
        View view = inflater.inflate(R.layout.hex_key_pad, container, false);

        // The ViewModel Instantiation
        hexViewModel = new ViewModelProvider(getActivity()).get(HexViewModel.class);


        Button oneButton = view.findViewById(R.id.button1H);
        Button twoButton = view.findViewById(R.id.button2H);
        Button threeButton = view.findViewById(R.id.button3H);
        Button fourButton = view.findViewById(R.id.button4H);
        Button fiveButton = view.findViewById(R.id.button5H);
        Button sixButton = view.findViewById(R.id.button6H);
        Button sevenButton = view.findViewById(R.id.button7H);
        Button eightButton = view.findViewById(R.id.button8H);
        Button nineButton = view.findViewById(R.id.button9H);
        Button zeroButton = view.findViewById(R.id.buttonZeroH);
        aButton = view.findViewById(R.id.buttonA);
        bButton = view.findViewById(R.id.buttonB);
        cButton = view.findViewById(R.id.buttonC);
        dButton = view.findViewById(R.id.buttonD);
        eButton = view.findViewById(R.id.buttonE);
        fButton = view.findViewById(R.id.buttonF);
        Button deleteButton = view.findViewById(R.id.button_DeleteH);
        Button minusButton = view.findViewById(R.id.buttonMinusH);
        Button backButton = view.findViewById(R.id.buttonLeftH);
        Button forwardButton = view.findViewById(R.id.buttonRightH);

        // Initial setting of button text.
        if(isUpperCase){
            aButton.setText("A");
            bButton.setText("B");
            cButton.setText("C");
            dButton.setText("D");
            eButton.setText("E");
            fButton.setText("F");
        }

        if(!isUpperCase){
            aButton.setText("a");
            bButton.setText("b");
            cButton.setText("c");
            dButton.setText("d");
            eButton.setText("e");
            fButton.setText("f");
        }

        oneButton.setOnClickListener(this);
        twoButton.setOnClickListener(this);
        threeButton.setOnClickListener(this);
        fourButton.setOnClickListener(this);
        fiveButton.setOnClickListener(this);
        sixButton.setOnClickListener(this);
        sevenButton.setOnClickListener(this);
        eightButton.setOnClickListener(this);
        nineButton.setOnClickListener(this);
        aButton.setOnClickListener(this);
        bButton.setOnClickListener(this);
        cButton.setOnClickListener(this);
        dButton.setOnClickListener(this);
        eButton.setOnClickListener(this);
        fButton.setOnClickListener(this);
        zeroButton.setOnClickListener(this);
        minusButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        forwardButton.setOnClickListener(this);

        return view;
    }


    public void setButtonCaseUpper(boolean boolValue) {
        isUpperCase = boolValue;
    }

    public void changeButtonTextCase(){
        if(isUpperCase){
            aButton.setText("A");
            bButton.setText("B");
            cButton.setText("C");
            dButton.setText("D");
            eButton.setText("E");
            fButton.setText("F");
        }

        if(!isUpperCase){
            aButton.setText("a");
            bButton.setText("b");
            cButton.setText("c");
            dButton.setText("d");
            eButton.setText("e");
            fButton.setText("f");
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1H:
                hexViewModel.insertHexDigit("1");
                break;
            case R.id.button2H:
                hexViewModel.insertHexDigit("2");
                break;
            case R.id.button3H:
                hexViewModel.insertHexDigit("3");
                break;
            case R.id.button4H:
                hexViewModel.insertHexDigit("4");
                break;
            case R.id.button5H:
                hexViewModel.insertHexDigit("5");
                break;
            case R.id.button6H:
                hexViewModel.insertHexDigit("6");
                break;
            case R.id.button7H:
                hexViewModel.insertHexDigit("7");
                break;
            case R.id.button8H:
                hexViewModel.insertHexDigit("8");
                break;
            case R.id.button9H:
                hexViewModel.insertHexDigit("9");
                break;
            case R.id.buttonZeroH:
                hexViewModel.insertHexDigit("0");
                break;
            case R.id.buttonA:
                if(isUpperCase){
                    hexViewModel.insertHexDigit("A");
                }
                else {
                    hexViewModel.insertHexDigit("a");
                }
                break;
            case R.id.buttonB:
                if(isUpperCase){
                    hexViewModel.insertHexDigit("B");
                }
                else {
                    hexViewModel.insertHexDigit("b");
                }
                break;
            case R.id.buttonC:
                if(isUpperCase){
                    hexViewModel.insertHexDigit("C");
                }
                else {
                    hexViewModel.insertHexDigit("c");
                }
                break;
            case R.id.buttonD:
                if(isUpperCase){
                    hexViewModel.insertHexDigit("D");
                }
                else {
                    hexViewModel.insertHexDigit("d");
                }
                break;
            case R.id.buttonE:
                if(isUpperCase){
                    hexViewModel.insertHexDigit("E");
                }
                else {
                    hexViewModel.insertHexDigit("e");
                }
                break;
            case R.id.buttonF:
                if(isUpperCase){
                    hexViewModel.insertHexDigit("F");
                }
                else {
                    hexViewModel.insertHexDigit("f");
                }
                break;
            case R.id.buttonMinusH:
                hexViewModel.insertHexDigit("-");
                break;
            case R.id.button_DeleteH:
                hexViewModel.insertHexDigit("Z");
                break;
            case R.id.buttonLeftH:
                hexViewModel.insertHexDigit("<");
                break;
            case R.id.buttonRightH:
                hexViewModel.insertHexDigit(">");
                break;

        }
    }
}
