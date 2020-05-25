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

    private Button oneButton;
    private Button twoButton;
    private Button threeButton;
    private Button fourButton;
    private Button fiveButton;
    private Button sixButton;
    private Button sevenButton;
    private Button eightButton;
    private Button nineButton;
    private Button aButton;
    private Button bButton;
    private Button cButton;
    private Button dButton;
    private Button eButton;
    private Button fButton;
    private Button zeroButton;
    private Button minusButton;
    private Button deleteButton;
    private Button backButton;
    private Button forwardButton;


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


        oneButton = view.findViewById(R.id.buttonOneH);
        twoButton = view.findViewById(R.id.buttonTwoH);
        threeButton = view.findViewById(R.id.buttonThreeH);
        fourButton = view.findViewById(R.id.buttonFourH);
        fiveButton = view.findViewById(R.id.buttonFiveH);
        sixButton = view.findViewById(R.id.buttonSixH);
        sevenButton = view.findViewById(R.id.buttonSevenH);
        eightButton = view.findViewById(R.id.buttonEightH);
        nineButton = view.findViewById(R.id.buttonNineH);
        zeroButton = view.findViewById(R.id.buttonZeroH);
        aButton = view.findViewById(R.id.buttonA);
        bButton = view.findViewById(R.id.buttonB);
        cButton = view.findViewById(R.id.buttonC);
        dButton = view.findViewById(R.id.buttonD);
        eButton = view.findViewById(R.id.buttonE);
        fButton = view.findViewById(R.id.buttonF);
        deleteButton = view.findViewById(R.id.buttonDeleteH);
        minusButton = view.findViewById(R.id.buttonMinusH);
        backButton = view.findViewById(R.id.buttonBackH);
        forwardButton = view.findViewById(R.id.buttonForwardH);


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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonOneH:
                hexViewModel.insertHexDigit("1");
                break;
            case R.id.buttonTwoH:
                hexViewModel.insertHexDigit("2");
                break;
            case R.id.buttonThreeH:
                hexViewModel.insertHexDigit("3");
                break;
            case R.id.buttonFourH:
                hexViewModel.insertHexDigit("4");
                break;
            case R.id.buttonFiveH:
                hexViewModel.insertHexDigit("5");
                break;
            case R.id.buttonSixH:
                hexViewModel.insertHexDigit("6");
                break;
            case R.id.buttonSevenH:
                hexViewModel.insertHexDigit("7");
                break;
            case R.id.buttonEightH:
                hexViewModel.insertHexDigit("8");
                break;
            case R.id.buttonNineH:
                hexViewModel.insertHexDigit("9");
                break;
            case R.id.buttonZeroH:
                hexViewModel.insertHexDigit("0");
                break;
            case R.id.buttonA:
                hexViewModel.insertHexDigit("A");
                break;
            case R.id.buttonB:
                hexViewModel.insertHexDigit("B");
                break;
            case R.id.buttonC:
                hexViewModel.insertHexDigit("C");
                break;
            case R.id.buttonD:
                hexViewModel.insertHexDigit("D");
                break;
            case R.id.buttonE:
                hexViewModel.insertHexDigit("E");
                break;
            case R.id.buttonF:
                hexViewModel.insertHexDigit("F");
                break;
            case R.id.buttonMinusH:
                hexViewModel.insertHexDigit("-");
                break;
            case R.id.buttonDeleteH:
                hexViewModel.insertHexDigit("Z");
                break;
            case R.id.buttonBackH:
                hexViewModel.insertHexDigit("<");
                break;
            case R.id.buttonForwardH:
                hexViewModel.insertHexDigit(">");
                break;

        }
    }
}
