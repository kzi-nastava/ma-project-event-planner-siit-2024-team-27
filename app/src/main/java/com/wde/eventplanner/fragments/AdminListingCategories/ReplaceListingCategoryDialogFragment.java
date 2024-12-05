package com.wde.eventplanner.fragments.AdminListingCategories;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.wde.eventplanner.R;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;
import com.wde.eventplanner.models.listingCategory.ListingType;
import com.wde.eventplanner.models.listingCategory.ReplacingListingCategoryDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReplaceListingCategoryDialogFragment extends DialogFragment {
    private ArrayList<ListingCategoryDTO> availableCategories;
    private AdminListingCategoriesFragment parentFragment;
    private ListingCategoryDTO toBeReplacedCategory;
    private ListingCategoryDTO replacingCategory;

    public ReplaceListingCategoryDialogFragment(List<ListingCategoryDTO> allCategories,
                                                ListingCategoryDTO toBeReplacedCategory,
                                                AdminListingCategoriesFragment parentFragment) {
        this.availableCategories = allCategories
                .stream()
                .filter(c -> c.getListingType() == toBeReplacedCategory.getListingType())
                .collect(Collectors.toCollection(ArrayList::new));

        this.toBeReplacedCategory = toBeReplacedCategory;
        this.parentFragment = parentFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_replace_listing_category_admin_dialog, null);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = android.view.Gravity.CENTER;
        dialog.getWindow().setAttributes(params);

        Button closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dismiss());

        MaterialAutoCompleteTextView listingCategories = view.findViewById(R.id.typeDropdown);
        ArrayAdapter<ListingCategoryDTO> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, availableCategories);

        listingCategories.setAdapter(adapter);
        listingCategories.setOnFocusChangeListener((_un1, _un2) -> listingCategories.showDropDown());

        listingCategories.setOnItemClickListener((parent, _un1, position, _un2) ->
                replacingCategory = (ListingCategoryDTO) parent.getItemAtPosition(position));

        MaterialButton replaceButton = view.findViewById(R.id.replaceButton);
        replaceButton.setOnClickListener(v -> {
            replaceListing();
            dismiss();
        });

        return dialog;
    }

    private void replaceListing() {
        parentFragment.replacePendingListing(new ReplacingListingCategoryDTO(
                toBeReplacedCategory.getId(),
                replacingCategory.getId(),
                toBeReplacedCategory.getListingType()));
    }
}
