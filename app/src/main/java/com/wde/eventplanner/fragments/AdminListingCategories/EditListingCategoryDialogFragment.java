package com.wde.eventplanner.fragments.AdminListingCategories;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.wde.eventplanner.databinding.DialogEditListingCategoryBinding;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;

public class EditListingCategoryDialogFragment extends DialogFragment {
    private AdminListingCategoriesFragment parentFragment;
    private DialogEditListingCategoryBinding binding;
    private ListingCategoryDTO listingCategoryDTO;
    private boolean isEditingActive;

    public EditListingCategoryDialogFragment(ListingCategoryDTO listingCategoryDTO,
                                             AdminListingCategoriesFragment parentFragment,
                                             boolean isEditingActive) {
        this.listingCategoryDTO = listingCategoryDTO;
        this.parentFragment = parentFragment;
        this.isEditingActive = isEditingActive;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        binding = DialogEditListingCategoryBinding.inflate(getLayoutInflater());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = android.view.Gravity.CENTER;
        dialog.getWindow().setAttributes(params);

        binding.name.setText(listingCategoryDTO.getName());
        binding.description.setText(listingCategoryDTO.getDescription());
        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.editListingCategoryButton.setOnClickListener(v -> {
            editListing();
            dismiss();
        });

        return dialog;
    }

    private void editListing() {
        String name = binding.name.getText().toString();
        String description = binding.description.getText().toString();
        ListingCategoryDTO dto = new ListingCategoryDTO(listingCategoryDTO.getId(), name, listingCategoryDTO.getIsPending(),
                description, listingCategoryDTO.getIsDeleted(), listingCategoryDTO.getListingType());

        if (isEditingActive)
            this.parentFragment.editActiveListing(dto);
        else
            this.parentFragment.editPendingListing(dto);
    }
}
