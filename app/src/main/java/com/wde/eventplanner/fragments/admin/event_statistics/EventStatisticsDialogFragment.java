package com.wde.eventplanner.fragments.admin.event_statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.DialogEventStatisticsBinding;
import com.wde.eventplanner.models.reviews.ReviewDistribution;

import java.util.ArrayList;
import java.util.Locale;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EventStatisticsDialogFragment extends DialogFragment {
    private ReviewDistribution reviewDistribution;

    public EventStatisticsDialogFragment(ReviewDistribution reviewDistribution) {
        this.reviewDistribution = reviewDistribution;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogEventStatisticsBinding binding = DialogEventStatisticsBinding.inflate(inflater, container, false);

        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (reviewDistribution != null) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0, reviewDistribution.getOne()));
            entries.add(new BarEntry(1, reviewDistribution.getTwo()));
            entries.add(new BarEntry(2, reviewDistribution.getThree()));
            entries.add(new BarEntry(3, reviewDistribution.getFour()));
            entries.add(new BarEntry(4, reviewDistribution.getFive()));

            BarDataSet barDataSet = new BarDataSet(entries, "");
            barDataSet.setValueTextSize(14f);
            barDataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.pale_text));
            barDataSet.setColor(ContextCompat.getColor(requireContext(), R.color.secondary));
            barDataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.format(Locale.US, "%d", (int) value);
                }
            });

            BarData barData = new BarData(barDataSet);
            barData.setBarWidth(0.8f);
            binding.barChart.setData(barData);

            binding.barChart.animateY(500);

            binding.barChart.getDescription().setEnabled(false);
            binding.barChart.getAxisRight().setEnabled(false);
            binding.barChart.getLegend().setEnabled(false);

            binding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{"1 Star", "2 Stars", "3 Stars", "4 Stars", "5 Stars"}));
            binding.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            binding.barChart.getXAxis().setGranularity(1f);
            binding.barChart.getXAxis().setGranularityEnabled(true);
            binding.barChart.getXAxis().setDrawGridLines(true);
            binding.barChart.getXAxis().setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_text));
            binding.barChart.getXAxis().setAxisMinimum(-0.5f);
            binding.barChart.getXAxis().setAxisMaximum(4.5f);

            YAxis yAxisLeft = binding.barChart.getAxisLeft();
            yAxisLeft.setGranularity(1f);
            yAxisLeft.setGranularityEnabled(true);
            yAxisLeft.setAxisMinimum(0);
            yAxisLeft.setDrawLabels(true);
            yAxisLeft.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_text));
            yAxisLeft.setDrawGridLines(true);

            binding.barChart.invalidate();
        }

        binding.closeButton.setOnClickListener(v -> dismiss());
        return binding.getRoot();
    }
}
