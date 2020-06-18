package com.woodlandcoders.digicalc;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HexViewModel extends ViewModel {
    private final MutableLiveData<CharSequence> hexDigit0 = new MutableLiveData<>();

    public void insertHexDigit(CharSequence inDigit){
        hexDigit0.setValue(inDigit);
    }

    public LiveData<CharSequence> getHexDigit(){
        return hexDigit0;
    }

}