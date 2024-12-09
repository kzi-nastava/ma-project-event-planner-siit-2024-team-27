package com.wde.eventplanner.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateEventViewModel extends ViewModel {
    private MutableLiveData<String> sharedData = new MutableLiveData<>();

    public void setData(String data) {
        sharedData.setValue(data);
    }

    public LiveData<String> getData() {
        return sharedData;
    }
}
