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

import com.wde.eventplanner.adapters.CatalogueServiceAdapter;
import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.databinding.FragmentCatalogueItemsBinding;
import com.wde.eventplanner.models.services.CatalogueService;
import com.wde.eventplanner.utils.FileManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.ServicesViewModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ServiceCatalogueFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private final ArrayList<CatalogueService> originalCatalogue = new ArrayList<>();
    private FragmentCatalogueItemsBinding binding;
    private ServicesViewModel servicesViewModel;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        servicesViewModel = new ViewModelProvider(requireActivity()).get(ServicesViewModel.class);
        binding = FragmentCatalogueItemsBinding.inflate(inflater, container, false);
        String sellerId = TokenManager.getUserId(binding.getRoot().getContext()).toString();

        binding.catalogueItems.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.catalogueItems.setNestedScrollingEnabled(false);

        servicesViewModel.getCatalogue(sellerId).observe(getViewLifecycleOwner(), catalogue -> {
            binding.catalogueItems.setAdapter(new CatalogueServiceAdapter(catalogue));
            for (CatalogueService catalogueService : catalogue)
                originalCatalogue.add(new CatalogueService(catalogueService));
        });

        binding.pdfButton.setOnClickListener(v -> servicesViewModel.downloadCatalogue(sellerId)
                .observe(getViewLifecycleOwner(), file -> FileManager.openPdf(requireContext(), file)));

        binding.updateButton.setOnClickListener(v -> {
            CatalogueServiceAdapter adapter = (CatalogueServiceAdapter) binding.catalogueItems.getAdapter();
            if (adapter != null) {
                ArrayList<CatalogueService> changed = adapter.items.stream().filter(item ->
                        originalCatalogue.stream().noneMatch(original -> original.equals(item))).collect(Collectors.toCollection(ArrayList::new));

                servicesViewModel.updateCatalogue(sellerId, changed).observe(getViewLifecycleOwner(), done ->
                        SingleToast.show(requireContext(), "Catalogue updated"));
            }
        });

        return binding.getRoot();
    }

    @Override
    public String getTitle() {
        return "Service";
    }
}
