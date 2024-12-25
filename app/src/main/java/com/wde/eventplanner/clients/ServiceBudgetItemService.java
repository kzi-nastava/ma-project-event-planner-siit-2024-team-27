package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.serviceBudgetItem.BookingSlots;
import com.wde.eventplanner.models.serviceBudgetItem.ReserveService;
import com.wde.eventplanner.models.serviceBudgetItem.ServiceBudgetItem;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceBudgetItemService {
    @POST("service-budget-items")
    Call<ServiceBudgetItem> createServiceBudgetItem(@Body ServiceBudgetItem serviceBudgetItem);

    @GET("service-budget-items/{serviceId}")
    Call<List<BookingSlots>> getSlotsForService(@Path("serviceId") UUID serviceId);

    @POST("service-budget-items/buy")
    Call<Void> reserveService(@Body ReserveService reserveService);
}
