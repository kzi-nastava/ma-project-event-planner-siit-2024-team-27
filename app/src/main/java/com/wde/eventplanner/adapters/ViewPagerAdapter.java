package com.wde.eventplanner.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter<T extends Fragment & ViewPagerAdapter.HasTitle> extends FragmentStatePagerAdapter {
    public interface HasTitle {
        String getTitle();
    }

    private final T[] fragments;

    @SafeVarargs
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull T... fragments) {
        super(fragmentManager);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position].getTitle();
    }
}
