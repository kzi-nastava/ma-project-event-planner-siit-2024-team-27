package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.productBudgetItem.BuyProductDTO;
import com.wde.eventplanner.models.productBudgetItem.ProductBudgetItemDTO;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductBudgetItemService {
    @POST("product-budget-items")
    Call<ProductBudgetItemDTO> createProductBudgetItem(@Body ProductBudgetItemDTO productBudgetItemDTO);

    @PUT("product-budget-items/{itemId}")
    Call<Void> updateProductBudgetItem(@Path("itemId") UUID itemId, @Body Double newPrice);

    @DELETE("product-budget-items/{eventId}/{categoryId}")
    Call<Void> deleteProductBudgetItem(@Path("eventId") UUID eventId, @Path("categoryId") UUID categoryId);

    @POST("product-budget-items/buy")
    Call<ProductBudgetItemDTO> buyProduct(@Body BuyProductDTO buyProductDTO);
}
