package com.woodlandcoders.digicalc;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class BinKeyPad extends Fragment implements View.OnClickListener {

    private Button oneButton;
    private Button zeroButton;
    private Button minusButton;
    private Button deleteButton;
    private Button backButton;
    private Button forwardButton;

    private BinViewModel binViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // "container" is the parent view.
        // "AttachToRoot" is if we are attaching this layout to the parent view,
        //  which in this case we are not.
        View view = inflater.inflate(R.layout.bin_key_pad, container, false);

        // The ViewModel Instantiation
        binViewModel = new ViewModelProvider(getActivity()).get(BinViewModel.class);


        oneButton = view.findViewById(R.id.buttonOneB);
        zeroButton = view.findViewById(R.id.buttonZeroB);
        deleteButton = view.findViewById(R.id.buttonDeleteB);
        minusButton = view.findViewById(R.id.buttonMinusB);
        backButton = view.findViewById(R.id.buttonBackB);
        forwardButton = view.findViewById(R.id.buttonForwardB);

        oneButton.setOnClickListener(this);
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
            case R.id.buttonOneB:
                binViewModel.insertBinDigit("1");
                break;
            case R.id.buttonZeroB:
                binViewModel.insertBinDigit("0");
                break;
            case R.id.buttonMinusB:
                binViewModel.insertBinDigit("-");
                break;
            case R.id.buttonDeleteB:
                binViewModel.insertBinDigit("Z");
                break;
            case R.id.buttonBackB:
                binViewModel.insertBinDigit("<");
                break;
            case R.id.buttonForwardB:
                binViewModel.insertBinDigit(">");
                break;
        }
    }
}
