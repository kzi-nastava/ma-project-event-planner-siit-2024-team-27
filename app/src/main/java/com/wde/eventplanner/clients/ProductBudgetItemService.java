package com.wde.eventplanner.clients;

import com.wde.eventplanner.models.productBudgetItem.BuyProductDTO;
import com.wde.eventplanner.models.productBudgetItem.ProductBudgetItemDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProductBudgetItemService {
    @POST("product-budget-items")
    Call<ProductBudgetItemDTO> createProductBudgetItem(@Body ProductBudgetItemDTO productBudgetItemDTO);

    @POST("product-budget-items/buy")
    Call<ProductBudgetItemDTO> buyProduct(@Body BuyProductDTO buyProductDTO);
}
