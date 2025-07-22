package com.wde.eventplanner.fragments.seller.catalogue;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.CatalogueProductAdapter;
import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentCatalogueItemsBinding;
import com.wde.eventplanner.models.products.CatalogueProduct;
import com.wde.eventplanner.utils.FileManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.ProductsViewModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProductCatalogueFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private final ArrayList<CatalogueProduct> originalCatalogue = new ArrayList<>();
    private FragmentCatalogueItemsBinding binding;
    private ProductsViewModel productsViewModel;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        productsViewModel = new ViewModelProvider(requireActivity()).get(ProductsViewModel.class);
        binding = FragmentCatalogueItemsBinding.inflate(inflater, container, false);
        String sellerId = TokenManager.getUserId(binding.getRoot().getContext()).toString();

        binding.catalogueItems.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.catalogueItems.setNestedScrollingEnabled(false);

        productsViewModel.getCatalogue(sellerId).observe(getViewLifecycleOwner(), catalogue -> {
            binding.catalogueItems.setAdapter(new CatalogueProductAdapter(catalogue));
            for (CatalogueProduct catalogueProduct : catalogue)
                originalCatalogue.add(new CatalogueProduct(catalogueProduct));
        });

        binding.pdfButton.setOnClickListener(v -> productsViewModel.downloadCatalogue(sellerId)
                .observe(getViewLifecycleOwner(), file -> FileManager.openPdf(requireContext(), file)));

        binding.updateButton.setOnClickListener(v -> {
            CatalogueProductAdapter adapter = (CatalogueProductAdapter) binding.catalogueItems.getAdapter();
            if (adapter != null) {
                ArrayList<CatalogueProduct> changed = adapter.items.stream().filter(item ->
                        originalCatalogue.stream().noneMatch(original -> original.equals(item))).collect(Collectors.toCollection(ArrayList::new));

                productsViewModel.updateCatalogue(sellerId, changed).observe(getViewLifecycleOwner(), done ->
                        SingleToast.show(requireContext(), "Catalogue updated"));
            }
        });

        return binding.getRoot();
    }

    @Override
    public String getTitle() {
        return "Product";
    }
}
