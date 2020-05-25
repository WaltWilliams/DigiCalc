package com.woodlandcoders.digicalc;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OctViewModel extends ViewModel {
    private MutableLiveData<CharSequence> octDigit = new MutableLiveData<>();

    public void insertOctDigit(CharSequence inDigit){
        octDigit.setValue(inDigit);
    }

    public LiveData<CharSequence> getOctDigit(){
        return octDigit;
    }

}