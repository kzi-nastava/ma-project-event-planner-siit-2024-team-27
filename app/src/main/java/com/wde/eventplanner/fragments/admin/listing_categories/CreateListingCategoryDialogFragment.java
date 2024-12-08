package com.wde.eventplanner.fragments.admin.listing_categories;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.fragment.app.DialogFragment;

import com.wde.eventplanner.databinding.DialogCreateListingCategoryBinding;
import com.wde.eventplanner.models.listing.ListingType;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;

public class CreateListingCategoryDialogFragment extends DialogFragment {
    private ListingCategoriesFragment parentFragment;
    private DialogCreateListingCategoryBinding binding;
    private ListingType selectedListingType;

    public CreateListingCategoryDialogFragment(ListingCategoriesFragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        binding = DialogCreateListingCategoryBinding.inflate(getLayoutInflater());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = android.view.Gravity.CENTER;
        dialog.getWindow().setAttributes(params);

        binding.closeButton.setOnClickListener(v -> dismiss());

        ArrayAdapter<ListingType> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new ListingType[]{ListingType.SERVICE, ListingType.PRODUCT});

        binding.typeDropdown.setAdapter(adapter);
        binding.typeDropdown.setOnFocusChangeListener((_un1, _un2) -> binding.typeDropdown.showDropDown());

        binding.typeDropdown.setOnItemClickListener((parent, _un1, position, _un2) ->
                selectedListingType = (ListingType) parent.getItemAtPosition(position));

        binding.createButton.setOnClickListener(v -> {
            createListing();
            dismiss();
        });

        return dialog;
    }

    private void createListing() {
        String name = binding.name.getText().toString();
        String description = binding.description.getText().toString();

        parentFragment.createActiveListing(new ListingCategoryDTO(name, description, selectedListingType));
    }
}
