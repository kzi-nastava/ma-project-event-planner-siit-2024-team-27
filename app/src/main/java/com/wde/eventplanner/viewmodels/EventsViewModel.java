package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.Page;
import com.wde.eventplanner.models.event.AgendaItem;
import com.wde.eventplanner.models.event.Event;
import com.wde.eventplanner.models.event.EventActivitiesDTO;
import com.wde.eventplanner.models.event.EventAdminDTO;
import com.wde.eventplanner.models.event.EventComplexView;
import com.wde.eventplanner.utils.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<EventComplexView>> eventsComplexViewData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<EventAdminDTO>> publicEventsLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Event>> topEventsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Page<Event>> eventsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<Event>> getTopEvents() {
        return topEventsLiveData;
    }

    public LiveData<ArrayList<EventComplexView>> getEventsComplexView() {
        return this.eventsComplexViewData;
    }

    public LiveData<ArrayList<EventAdminDTO>> getPublicEvents() {
        return this.publicEventsLiveData;
    }

    public LiveData<Page<Event>> getEvents() {
        return eventsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }

    public void fetchTopEvents() {
        ClientUtils.eventsService.getTopEvents().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Event>> call, @NonNull Response<ArrayList<Event>> response) {
                if (response.isSuccessful()) {
                    topEventsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch events. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Event>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public void fetchPublicEvents() {
        ClientUtils.eventsService.getPublicEvents().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<EventAdminDTO>> call, @NonNull Response<ArrayList<EventAdminDTO>> response) {
                if (response.isSuccessful()) {
                    publicEventsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch events. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<EventAdminDTO>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public void fetchEvents() {
        fetchEvents(null, null, null, null, null, null, null, null, null, null, null);
    }

    public void fetchEvents(String searchTerms, String city, String category, String dateRangeStart, String dateRangeEnd,
                            String minRating, String maxRating, String sortBy, String order, String page, String size) {
        ClientUtils.eventsService.getEvents(searchTerms, city, category, dateRangeStart,
                dateRangeEnd, minRating, maxRating, sortBy, order, page, size).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Page<Event>> call, @NonNull Response<Page<Event>> response) {
                if (response.isSuccessful()) {
                    eventsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch events. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Page<Event>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public void fetchEventsFromOrganizer(String id) {
        ClientUtils.eventsService.getEventsFromOrganizer(UUID.fromString(id)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<EventComplexView>> call, @NonNull Response<ArrayList<EventComplexView>> response) {
                if (response.isSuccessful()) {
                    eventsComplexViewData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch events. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<EventComplexView>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
    }

    public LiveData<ArrayList<UUID>> createAgenda(ArrayList<AgendaItem> agendaItems) {
        MutableLiveData<ArrayList<UUID>> ids = new MutableLiveData<>();
        ClientUtils.eventsService.createAgenda(new EventActivitiesDTO(agendaItems)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<UUID>> call, @NonNull Response<ArrayList<UUID>> response) {
                if (response.isSuccessful()) {
                    ids.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to create agenda. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<UUID>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return ids;
    }

    public LiveData<File> downloadReport(UUID eventId, String eventName) {
        MutableLiveData<File> file = new MutableLiveData<>();
        ClientUtils.eventsService.getPdfReport(eventId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody != null) {
                            String fileName = "event_" + eventName.strip().replaceAll("\\s+", "_").toLowerCase() + "_report.pdf";
                            file.postValue(FileManager.saveFileToDownloads(responseBody.byteStream(), fileName));
                        } else
                            errorMessage.postValue("Failed to retrieve PDF. Response body is null.");
                    } catch (IOException e) {
                        errorMessage.postValue("Failed to save pdf report. Error: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                errorMessage.postValue("Failed to download pdf report. Code: " + t.getMessage());
            }
        });
        return file;
    }
}
