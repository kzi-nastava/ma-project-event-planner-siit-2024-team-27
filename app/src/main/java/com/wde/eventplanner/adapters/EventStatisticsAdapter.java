package com.wde.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.wde.eventplanner.databinding.CardEventStatisticsBinding;
import com.wde.eventplanner.fragments.admin.event_statistics.EventStatisticsDialogFragment;
import com.wde.eventplanner.fragments.admin.event_statistics.EventStatisticsFragment;
import com.wde.eventplanner.models.event.EventAdminDTO;
import com.wde.eventplanner.utils.FileManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.viewmodels.EventReviewsViewModel;
import com.wde.eventplanner.viewmodels.EventsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventStatisticsAdapter extends RecyclerView.Adapter<EventStatisticsAdapter.EventStatisticsAdapterHolder> {
    private final EventReviewsViewModel eventReviewsViewModel;
    private final EventsViewModel eventsViewModel;
    private final EventStatisticsFragment parent;
    public final List<EventAdminDTO> events;

    public EventStatisticsAdapter(EventStatisticsFragment parent) {
        this(new ArrayList<>(), parent);
    }

    public EventStatisticsAdapter(List<EventAdminDTO> events, EventStatisticsFragment parent) {
        this.events = events;
        this.parent = parent;
        eventReviewsViewModel = new ViewModelProvider(this.parent.requireActivity()).get(EventReviewsViewModel.class);
        eventsViewModel = new ViewModelProvider(this.parent.requireActivity()).get(EventsViewModel.class);
        eventReviewsViewModel.getErrorMessage().observe(this.parent.getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(this.parent.requireContext(), error);
                eventReviewsViewModel.clearErrorMessage();
            }
        });
    }

    @NonNull
    @Override
    public EventStatisticsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardEventStatisticsBinding binding = CardEventStatisticsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventStatisticsAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventStatisticsAdapterHolder holder, int position) {
        EventAdminDTO event = events.get(position);
        holder.binding.nameTextView.setText(event.getName());
        holder.binding.ratingTextView.setText(String.format(Locale.US, "%.1f", event.getRating()));
        holder.binding.attendanceTextView.setText(String.format(Locale.US, "%d", event.getAttendance()));
        holder.binding.locationTextView.setText(event.getCity());

        holder.binding.graphButton.setOnClickListener(v -> eventReviewsViewModel.getReviewDistribution(event.getId()).observe(parent.getViewLifecycleOwner(), reviewDistribution ->
                new EventStatisticsDialogFragment(reviewDistribution).show(parent.getParentFragmentManager(), "graphDialog")
        ));

        holder.binding.downloadButton.setOnClickListener(v -> eventsViewModel.downloadReport(event.getId(), event.getName()).observe(parent.getViewLifecycleOwner(), file ->
                FileManager.openPdf(parent.requireContext(), file)
        ));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventStatisticsAdapterHolder extends RecyclerView.ViewHolder {
        CardEventStatisticsBinding binding;

        public EventStatisticsAdapterHolder(CardEventStatisticsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
