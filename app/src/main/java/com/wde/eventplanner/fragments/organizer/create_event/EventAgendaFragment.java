package com.wde.eventplanner.fragments.organizer.create_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.adapters.AgendaItemAdapter;
import com.wde.eventplanner.adapters.ViewPagerAdapter;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.databinding.FragmentEventAgendaBinding;
import com.wde.eventplanner.models.event.AgendaItem;
import com.wde.eventplanner.viewmodels.CreateEventViewModel;

public class EventAgendaFragment extends Fragment implements ViewPagerAdapter.HasTitle {
    private CreateEventViewModel createEventViewModel;
    private FragmentEventAgendaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = FragmentEventAgendaBinding.inflate(inflater, container, false);
            createEventViewModel = new ViewModelProvider(requireActivity()).get(CreateEventViewModel.class);

            binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
            binding.recyclerView.setNestedScrollingEnabled(false);

            binding.addButton.setOnClickListener(v -> {
                if (createEventViewModel.agendaItems.stream().allMatch(AgendaItem::isFilled)) {
                    createEventViewModel.agendaItems.add(new AgendaItem());
                    RecyclerView.Adapter<?> adapter = binding.recyclerView.getAdapter();
                    if (adapter != null)
                        adapter.notifyItemChanged(createEventViewModel.agendaItems.size() - 1);
                } else
                    SingleToast.show(requireContext(), "Fill out all previous items!");
            });

            binding.recyclerView.setAdapter(new AgendaItemAdapter(createEventViewModel.agendaItems, getParentFragmentManager()));
        }

        return binding.getRoot();
    }

    @Override
    public String getTitle() {
        return "Agenda";
    }
}