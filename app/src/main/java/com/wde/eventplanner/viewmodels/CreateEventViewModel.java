package com.wde.eventplanner.viewmodels;

import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.models.event.GuestInfo;

import java.util.ArrayList;

public class CreateEventViewModel extends ViewModel {
    public final ArrayList<GuestInfo> guestList = new ArrayList<>();

    public void clearAllData() {
        guestList.clear();
    }

//    EXAMPLE
//    private MutableLiveData<String> sharedData = new MutableLiveData<>();
//
//    public void setData(String data) {
//        sharedData.setValue(data);
//    }
//
//    public LiveData<String> getData() {
//        return sharedData;
//    }
}
