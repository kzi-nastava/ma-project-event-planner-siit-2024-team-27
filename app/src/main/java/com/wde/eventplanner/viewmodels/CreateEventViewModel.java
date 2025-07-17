package com.wde.eventplanner.viewmodels;

import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.models.event.AgendaItem;
import com.wde.eventplanner.models.event.EventComplexView;
import com.wde.eventplanner.models.event.GuestInfo;
import com.wde.eventplanner.models.event.ListingBudgetItemDTO;

import java.util.ArrayList;

public class CreateEventViewModel extends ViewModel {
    public ArrayList<ListingBudgetItemDTO> originalBudgetItems = new ArrayList<>();
    public ArrayList<ListingBudgetItemDTO> budgetItems = new ArrayList<>();
    public final ArrayList<GuestInfo> guestList = new ArrayList<>();
    public ArrayList<AgendaItem> agendaItems = new ArrayList<>();
    public String eventTypeId = "";
    public EventComplexView event;

    public void clearAllData() {
        originalBudgetItems.clear();
        budgetItems.clear();
        agendaItems.clear();
        guestList.clear();
        eventTypeId = "";
        event = null;
    }
}
