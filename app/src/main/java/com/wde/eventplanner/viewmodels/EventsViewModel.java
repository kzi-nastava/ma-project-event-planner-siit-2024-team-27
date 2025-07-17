package com.wde.eventplanner.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wde.eventplanner.clients.ClientUtils;
import com.wde.eventplanner.models.Page;
import com.wde.eventplanner.models.event.AgendaItem;
import com.wde.eventplanner.models.event.CreateEventDTO;
import com.wde.eventplanner.models.event.Event;
import com.wde.eventplanner.models.event.EventActivitiesDTO;
import com.wde.eventplanner.models.event.EventAdminDTO;
import com.wde.eventplanner.models.event.EventComplexView;
import com.wde.eventplanner.models.event.EventDetailedDTO;
import com.wde.eventplanner.utils.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public LiveData<EventDetailedDTO> getEvent(UUID id, boolean isGuest, UUID guestId) {
        MutableLiveData<EventDetailedDTO> eventLiveData = new MutableLiveData<>();

        ClientUtils.eventsService.getEvent(id, isGuest, guestId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<EventDetailedDTO> call, @NonNull Response<EventDetailedDTO> response) {
                if (response.isSuccessful()) {
                    eventLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch service. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<EventDetailedDTO> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });

        return eventLiveData;
    }

    public void deleteEvent(UUID id, UUID userId) {
        MutableLiveData<String> responseBody = new MutableLiveData<>();

        ClientUtils.eventsService.deleteEvent(id, userId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    responseBody.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to delete event. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
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
        fetchEvents(null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public void fetchEvents(String searchTerms, String city, String category, String dateRangeStart, String dateRangeEnd,
                            String minRating, String maxRating, String sortBy, String order, String page, String size, String organizerId) {
        ClientUtils.eventsService.getEvents(searchTerms, city, category, dateRangeStart,
                dateRangeEnd, minRating, maxRating, sortBy, order, page, size, organizerId).enqueue(new Callback<>() {
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

    public LiveData<EventComplexView> fetchEventFromOrganizer(String organizerId, String eventId) {
        MutableLiveData<EventComplexView> eventComplexViewData = new MutableLiveData<>();

        ClientUtils.eventsService.getEventFromOrganizer(UUID.fromString(organizerId), UUID.fromString(eventId)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<EventComplexView> call, @NonNull Response<EventComplexView> response) {
                if (response.isSuccessful()) {
                    eventComplexViewData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch event. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<EventComplexView> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });

        return eventComplexViewData;
    }

    public LiveData<ArrayList<AgendaItem>> fetchAgenda(String eventId) {
        MutableLiveData<ArrayList<AgendaItem>> agendaItems = new MutableLiveData<>();

        ClientUtils.eventsService.getAgenda(UUID.fromString(eventId)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<AgendaItem>> call, @NonNull Response<ArrayList<AgendaItem>> response) {
                if (response.isSuccessful()) {
                    agendaItems.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch agenda items. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<AgendaItem>> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });

        return agendaItems;
    }

    public LiveData<ArrayList<UUID>> createAgenda(ArrayList<AgendaItem> agendaItems) {
        MutableLiveData<ArrayList<UUID>> ids = new MutableLiveData<>();
        ClientUtils.eventsService.createAgenda(new EventActivitiesDTO(agendaItems, null)).enqueue(new Callback<>() {
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

    public LiveData<Void> updateAgenda(ArrayList<AgendaItem> agendaItems, UUID eventId) {
        MutableLiveData<Void> done = new MutableLiveData<>();
        ClientUtils.eventsService.updateAgenda(new EventActivitiesDTO(agendaItems, eventId)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful())
                    done.postValue(null);
                else
                    errorMessage.postValue("Failed to update agenda. Code: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return done;
    }

    public LiveData<Event> createEvent(CreateEventDTO createEventDTO) {
        MutableLiveData<Event> event = new MutableLiveData<>();
        ClientUtils.eventsService.createEvent(createEventDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Event> call, @NonNull Response<Event> response) {
                if (response.isSuccessful()) {
                    event.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to create event. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Event> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return event;
    }

    public LiveData<Void> updateEvent(CreateEventDTO createEventDTO) {
        MutableLiveData<Void> done = new MutableLiveData<>();
        ClientUtils.eventsService.updateEvent(createEventDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful())
                    done.postValue(null);
                 else
                    errorMessage.postValue("Failed to create event. Code: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                errorMessage.postValue("Error: " + t.getMessage());
            }
        });
        return done;
    }

    public LiveData<Response<Void>> putImages(List<File> imageFiles, UUID eventId) {
        MutableLiveData<Response<Void>> responseMessage = new MutableLiveData<>();

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("eventId", eventId.toString());

        for (File imageFile : imageFiles) {
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            requestBodyBuilder.addFormDataPart("images", imageFile.getName(), fileRequestBody);
        }

        ClientUtils.eventsService.putImages(requestBodyBuilder.build()).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                responseMessage.postValue(response);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Response<Void> errorResponse = Response.error(500, ResponseBody.create(MediaType.parse("text/plain"), "Failed to add the image"));
                responseMessage.postValue(errorResponse);
            }
        });

        return responseMessage;
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
