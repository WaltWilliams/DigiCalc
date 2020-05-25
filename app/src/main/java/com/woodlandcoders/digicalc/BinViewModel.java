package com.woodlandcoders.digicalc;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BinViewModel extends ViewModel {
    private MutableLiveData<CharSequence> binDigit = new MutableLiveData<>();

    public void insertBinDigit(CharSequence inDigit){
        binDigit.setValue(inDigit);
    }

    public LiveData<CharSequence> getBinDigit(){
        return binDigit;
    }

}