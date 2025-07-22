package com.wde.eventplanner.components;

import static com.wde.eventplanner.utils.CustomGraphicUtils.dp2px;
import static com.wde.eventplanner.utils.CustomGraphicUtils.hideKeyboard;

import android.content.Context;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.wde.eventplanner.adapters.MultiDropdownAdapter;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

public class MultiDropDown<T> extends MaterialAutoCompleteTextView {
    @AllArgsConstructor
    public static class MultiDropDownItem<T> {
        public String name;
        public T value;
        public boolean checked;

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }

    private final ArrayList<MultiDropDownItem<T>> selectedItems = new ArrayList<>();
    private Function<T, String> getName;

    public ArrayList<T> getSelected() {
        return selectedItems == null ? null : selectedItems.stream().map(item -> item.value).collect(Collectors.toCollection(ArrayList::new));
    }

    @SuppressWarnings("unchecked")
    public void setSelected(ArrayList<T> selectedValues) {
        selectedItems.clear();
        MultiDropdownAdapter<?> adapter = ((MultiDropdownAdapter<?>) getAdapter());
        adapter.items.forEach(item -> {
            if (selectedValues.stream().anyMatch(selected -> getName.apply(selected).equals(item.name)))
                selectedItems.add((MultiDropDownItem<T>) item);
        });
        updateSelection();
    }

    @SuppressWarnings("unchecked")
    public void setSelected(Function<T, Boolean> isSelected) {
        selectedItems.clear();
        MultiDropdownAdapter<?> adapter = ((MultiDropdownAdapter<?>) getAdapter());
        adapter.items.forEach(item -> {
            if (isSelected.apply((T) item.value))
                selectedItems.add((MultiDropDownItem<T>) item);
        });
        updateSelection();
    }

    private void setUp() {
        setThreshold(0);
        setSaveEnabled(false);
        setCursorVisible(false);
        setInputType(InputType.TYPE_NULL);
        setOnClickListener(v -> showDropDown());
        setOnItemClickListener(this::onDropdownItemClicked);
        setDropDownVerticalOffset(dp2px(getResources(), 1));
        setOnFocusChangeListener((v, hasFocus) -> {
            hideKeyboard(getContext(), this);
            if (hasFocus) showDropDown();
        });
    }

    public MultiDropDown(@NonNull Context context) {
        super(context);
        setUp();
    }

    public MultiDropDown(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    public MultiDropDown(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    @SuppressWarnings("unchecked")
    public void onDropdownItemClicked(AdapterView<?> parent, View v, int position, long id) {
        MultiDropDownItem<T> selected = (MultiDropDownItem<T>) parent.getItemAtPosition(position);
        selected.checked = !selected.checked;

        if (selected.checked && selectedItems.stream().noneMatch(item -> item.name.equals(selected.name)))
            selectedItems.add(selected);
        else if (!selected.checked)
            selectedItems.removeIf(item -> item.name.equals(selected.name));

        updateSelection();
    }

    private void updateSelection() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < selectedItems.size(); i++) {
            builder.append(selectedItems.get(i).name);
            if (i != selectedItems.size() - 1)
                builder.append(", ");
        }

        setText(builder.toString());

        if (getAdapter() != null) {
            MultiDropdownAdapter<?> adapter = ((MultiDropdownAdapter<?>) getAdapter());
            adapter.items.forEach(item -> item.checked = selectedItems.stream().anyMatch(selectedItem -> selectedItem.name.equals(item.name)));
            adapter.notifyDataSetChanged();
        }
    }

    public boolean onTouchOutsideDropDown(View v, MotionEvent event) {
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
            clearFocus();
            hideKeyboard(getContext(), this);
        }
        return false;
    }

    public void setItems(ArrayList<MultiDropDownItem<T>> items) {
        setDropDownHeight(Math.min(items.size() * dp2px(getResources(), 44), dp2px(getResources(), 200)));
        setAdapter(new MultiDropdownAdapter<>(getContext(), items));
    }

    public void changeValues(ArrayList<T> values, Function<T, String> getName) {
        if (values != null)
            setItems(values.stream().map(value -> new MultiDropDownItem<>(getName.apply(value), value, false)).collect(Collectors.toCollection(ArrayList::new)));
        this.getName = getName;
    }

    @Override
    public void dismissDropDown() {
    }
}
