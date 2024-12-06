package com.wde.eventplanner.fragments.AdminListingCategories;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.fragment.app.DialogFragment;

import com.wde.eventplanner.databinding.DialogReplaceListingCategoryBinding;
import com.wde.eventplanner.models.listingCategory.ListingCategoryDTO;
import com.wde.eventplanner.models.listingCategory.ReplacingListingCategoryDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReplaceListingCategoryDialogFragment extends DialogFragment {
    private ArrayList<ListingCategoryDTO> availableCategories;
    private AdminListingCategoriesFragment parentFragment;
    private DialogReplaceListingCategoryBinding binding;
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
        binding = DialogReplaceListingCategoryBinding.inflate(getLayoutInflater());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = android.view.Gravity.CENTER;
        dialog.getWindow().setAttributes(params);

        ArrayAdapter<ListingCategoryDTO> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, availableCategories);

        binding.typeDropdown.setAdapter(adapter);
        binding.typeDropdown.setOnFocusChangeListener((_un1, _un2) -> binding.typeDropdown.showDropDown());
        binding.typeDropdown.setOnItemClickListener((parent, _un1, position, _un2) ->
                replacingCategory = (ListingCategoryDTO) parent.getItemAtPosition(position));
        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.replaceButton.setOnClickListener(v -> {
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
