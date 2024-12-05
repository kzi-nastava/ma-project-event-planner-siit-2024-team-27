package com.wde.eventplanner.fragments.AdminListingCategories;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.wde.eventplanner.R;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;

public class EditListingCategoryDialogFragment extends DialogFragment  {
    private ListingCategoryDTO listingCategoryDTO;
    private AdminListingCategoriesFragment parentFragment;
    private TextInputEditText name;
    private TextInputEditText description;
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

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_listing_category_admin_dialog, null);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = android.view.Gravity.CENTER;
        dialog.getWindow().setAttributes(params);

        Button closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dismiss());

        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);

        name.setText(listingCategoryDTO.getName());
        description.setText(listingCategoryDTO.getDescription());

        MaterialButton createButton = view.findViewById(R.id.editListingCategoryButton);
        createButton.setOnClickListener(v -> {
            editListing();
            dismiss();
        });

        return dialog;
    }

    private void editListing() {
        String name = this.name.getText().toString();
        String description = this.description.getText().toString();

        if (isEditingActive) {
            this.parentFragment.editActiveListing(new ListingCategoryDTO(
                    listingCategoryDTO.getId(),
                    name,
                    listingCategoryDTO.getIsPending(),
                    description,
                    listingCategoryDTO.getIsDeleted(),
                    listingCategoryDTO.getListingType()));
        } else {
            this.parentFragment.editPendingListing(new ListingCategoryDTO(
                    listingCategoryDTO.getId(),
                    name,
                    listingCategoryDTO.getIsPending(),
                    description,
                    listingCategoryDTO.getIsDeleted(),
                    listingCategoryDTO.getListingType()));
        }
    }
}
