package com.wde.eventplanner.fragments.common;

import static com.wde.eventplanner.constants.GraphicUtils.dp2px;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.wde.eventplanner.adapters.DropdownArrayAdapter;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

public class CustomDropDown<T> extends MaterialAutoCompleteTextView {
    @AllArgsConstructor
    public static class CustomDropDownItem<T> {
        public String name;
        public T value;

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }

    private ArrayList<CustomDropDownItem<T>> items;
    private boolean isAutoCompleteDisabled = false;
    private CustomDropDownItem<T> selected;
    private Function<T, String> getName;
    private ArrayList<T> originalValues;
    private Runnable callback;

    public T getSelected() {
        return selected == null ? null : selected.value;
    }

    private void setUp() {
        setThreshold(0);
        setSaveEnabled(false);
        setDropDownVerticalOffset(dp2px(getResources(), 1));
        setOnClickListener(v -> showDropDown());
        setOnItemClickListener(this::onDropdownItemClicked);
        setOnFocusChangeListener(this::onDropdownFocusChanged);
        setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE | InputType.TYPE_CLASS_TEXT);
    }

    public CustomDropDown(@NonNull Context context) {
        super(context);
        setUp();
    }

    public CustomDropDown(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    public CustomDropDown(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    public void disableAutoComplete() {
        if (getAdapter() != null) ((DropdownArrayAdapter<?>) getAdapter()).ignoreFiltering = true;
        setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        setInputType(InputType.TYPE_NULL);
        isAutoCompleteDisabled = true;
        setCursorVisible(false);
    }

    public void setOnDropdownItemClickListener(Runnable callback) {
        this.callback = callback;
    }

    @SuppressWarnings("unchecked")
    public void onDropdownItemClicked(AdapterView<?> parent, View v, int position, long id) {
        selected = (CustomDropDownItem<T>) parent.getItemAtPosition(position);
        if (callback != null) callback.run();
    }

    private void onDropdownFocusChanged(View v, boolean hasFocus) {
        if (hasFocus) {
            if (!isAutoCompleteDisabled) setText("");
            postDelayed(this::showDropDown, 50);
        } else {
            String input = getText().toString().trim();
            Optional<CustomDropDownItem<T>> found = items.stream().filter(item -> item.name.equalsIgnoreCase(input)).findFirst();
            if (found.isPresent()) {
                selected = found.get();
                setText(found.get().name);
            } else {
                selected = null;
                setText("");
            }
        }
        if (isAutoCompleteDisabled) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    public boolean onTouchOutsideDropDown(View v, MotionEvent event) {
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        if (!rect.contains((int) event.getRawX(), (int) event.getRawY())) {
            clearFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        return false;
    }

    @Override
    public void onFilterComplete(int count) {
        setDropDownHeight(Math.min(count * dp2px(getResources(), 44), dp2px(getResources(), 200)));
        super.onFilterComplete(count);
    }

    public void setItems(ArrayList<CustomDropDownItem<T>> items) {
        this.items = items;
        setDropDownHeight(Math.min(items.size() * dp2px(getResources(), 44), dp2px(getResources(), 200)));
        setAdapter(new DropdownArrayAdapter<>(getContext(), items, isAutoCompleteDisabled));
    }

    public void onValuesChanged(ArrayList<T> values, Function<T, String> getName) {
        onValuesChanged(values, getName, null);
    }

    public void onValuesChanged(ArrayList<T> values, Function<T, String> getName, Predicate<T> filter) {
        this.originalValues = new ArrayList<>(values);
        this.getName = getName;
        if (filter != null)
            values = values.stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
        setItems(values.stream().map(value -> new CustomDropDownItem<>(getName.apply(value), value)).collect(Collectors.toCollection(ArrayList::new)));
    }

    public void changeFilter(Predicate<T> filter) {
        if (filter != null && getName != null) {
            ArrayList<T> values = originalValues.stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
            setItems(values.stream().map(value -> new CustomDropDownItem<>(getName.apply(value), value)).collect(Collectors.toCollection(ArrayList::new)));
        }
    }

    @Override
    public void onSelectionChanged(int start, int end) {    // Disables text selection
        CharSequence text = getText();
        if (text != null && (start != text.length() || end != text.length())) {
            setSelection(text.length(), text.length());
            return;
        }
        super.onSelectionChanged(start, end);
    }
}
