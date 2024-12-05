package com.wde.eventplanner.fragments.AdminListingCategories;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.wde.eventplanner.R;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;
import com.wde.eventplanner.models.listingCategory.ListingType;

public class CreateListingCategoryDialogFragment extends DialogFragment  {
    private AdminListingCategoriesFragment parentFragment;
    private MaterialAutoCompleteTextView listingCategories;

    private ListingType selectedListingType;
    private TextInputEditText name;
    private TextInputEditText description;

    public CreateListingCategoryDialogFragment(AdminListingCategoriesFragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_create_listing_category_admin_dialog, null);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = android.view.Gravity.CENTER;
        dialog.getWindow().setAttributes(params);

        Button closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dismiss());

        listingCategories = view.findViewById(R.id.typeDropdown);
        ArrayAdapter<ListingType> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new ListingType[] {ListingType.SERVICE, ListingType.PRODUCT});

        listingCategories.setAdapter(adapter);
        listingCategories.setOnFocusChangeListener((_un1, _un2) -> listingCategories.showDropDown());

        listingCategories.setOnItemClickListener((parent, _un1, position, _un2) ->
                selectedListingType = (ListingType) parent.getItemAtPosition(position));

        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);

        MaterialButton createButton = view.findViewById(R.id.createListingCategoryButton);
        createButton.setOnClickListener(v -> {
            createListing();
            dismiss();
        });

        return dialog;
    }

    private void createListing() {
        String name = this.name.getText().toString();
        String description = this.description.getText().toString();

        parentFragment.createActiveListing(new ListingCategoryDTO(name, description, selectedListingType));
    }
}
