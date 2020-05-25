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

public class OctKeyPad extends Fragment implements View.OnClickListener {

    private Button oneButton;
    private Button twoButton;
    private Button threeButton;
    private Button fourButton;
    private Button fiveButton;
    private Button sixButton;
    private Button sevenButton;
    private Button zeroButton;
    private Button minusButton;
    private Button deleteButton;
    private Button backButton;
    private Button forwardButton;

    private OctViewModel octViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // "container" is the parent view.
        // "AttachToRoot" is if we are attaching this layout to the parent view,
        //  which in this case we are not.
        View view = inflater.inflate(R.layout.oct_key_pad, container, false);

        // The ViewModel Instantiation
        octViewModel = new ViewModelProvider(getActivity()).get(OctViewModel.class);


        oneButton = view.findViewById(R.id.buttonOneO);
        twoButton = view.findViewById(R.id.buttonTwoO);
        threeButton = view.findViewById(R.id.buttonThreeO);
        fourButton = view.findViewById(R.id.buttonFourO);
        fiveButton = view.findViewById(R.id.buttonFiveO);
        sixButton = view.findViewById(R.id.buttonSixO);
        sevenButton = view.findViewById(R.id.buttonSevenO);
        zeroButton = view.findViewById(R.id.buttonZeroO);
        deleteButton = view.findViewById(R.id.buttonDeleteO);
        minusButton = view.findViewById(R.id.buttonMinusO);
        backButton = view.findViewById(R.id.buttonBackO);
        forwardButton = view.findViewById(R.id.buttonForwardO);

        oneButton.setOnClickListener(this);
        twoButton.setOnClickListener(this);
        threeButton.setOnClickListener(this);
        fourButton.setOnClickListener(this);
        fiveButton.setOnClickListener(this);
        sixButton.setOnClickListener(this);
        sevenButton.setOnClickListener(this);
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
            case R.id.buttonOneO:
                octViewModel.insertOctDigit("1");
                break;
            case R.id.buttonTwoO:
                octViewModel.insertOctDigit("2");
                break;
            case R.id.buttonThreeO:
                octViewModel.insertOctDigit("3");
                break;
            case R.id.buttonFourO:
                octViewModel.insertOctDigit("4");
                break;
            case R.id.buttonFiveO:
                octViewModel.insertOctDigit("5");
                break;
            case R.id.buttonSixO:
                octViewModel.insertOctDigit("6");
                break;
            case R.id.buttonSevenO:
                octViewModel.insertOctDigit("7");
                break;
            case R.id.buttonZeroO:
                octViewModel.insertOctDigit("0");
                break;
            case R.id.buttonMinusO:
                octViewModel.insertOctDigit("-");
                break;
            case R.id.buttonDeleteO:
                octViewModel.insertOctDigit("Z");
                break;
            case R.id.buttonBackO:
                octViewModel.insertOctDigit("<");
                break;
            case R.id.buttonForwardO:
                octViewModel.insertOctDigit(">");
                break;

        }
    }
}
