package com.wde.eventplanner.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.google.android.material.navigation.NavigationView;
import com.wde.eventplanner.R;
import com.wde.eventplanner.activities.MainActivity;
import com.wde.eventplanner.databinding.ActivityMainBinding;
import com.wde.eventplanner.models.user.UserRole;
import com.wde.eventplanner.viewmodels.NotificationsViewModel;

public class MenuManager {
    public static void adjustMenu(FragmentActivity activity) {
        NavController navController = ((MainActivity) activity).navController;
        ActivityMainBinding binding = ((MainActivity) activity).binding;
        NavigationView navigationView = binding.navigationView;
        UserRole role = TokenManager.getRole(activity);
        navigationView.getMenu().clear();

        if (role == UserRole.ADMIN) navigationView.inflateMenu(R.menu.menu_admin);
        else if (role == UserRole.ORGANIZER) navigationView.inflateMenu(R.menu.menu_organizer);
        else if (role == UserRole.SELLER) navigationView.inflateMenu(R.menu.menu_seller);
        else if (role == UserRole.GUEST) navigationView.inflateMenu(R.menu.menu_guest);
        else if (role == UserRole.ANONYMOUS) navigationView.inflateMenu(R.menu.menu_anonymous);

        if (role == UserRole.ANONYMOUS) {
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.logoutButton.setVisibility(View.INVISIBLE);
            binding.profileButton.setVisibility(View.INVISIBLE);
        } else {
            binding.loginButton.setVisibility(View.INVISIBLE);
            binding.logoutButton.setVisibility(View.VISIBLE);
            binding.profileButton.setVisibility(View.VISIBLE);
        }

        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(navController.getGraph().getStartDestinationId(), true).build();
        navController.navigate(R.id.nav_homepage, null, navOptions);
        MenuManager.refreshNotificationIcon(activity);
    }

    public static void refreshNotificationIcon(FragmentActivity activity) {
        new ViewModelProvider(activity).get(NotificationsViewModel.class).fetchNotifications();
    }

    public static void navigateToFragment(String type, String entityId, Context context, NavController navController) {
        UserRole role = TokenManager.getRole(context);
        Bundle bundle = new Bundle();
        type = type.toUpperCase();
        int fragmentId = -1;

        if (role == UserRole.ORGANIZER) {
            if (type.equals("SERVICE"))
                fragmentId = R.id.nav_service_detail_organizer;
            else if (type.equals("PRODUCT"))
                fragmentId = R.id.nav_product_detail_organizer;
        } else if (role == UserRole.SELLER) {
            if (type.equals("SERVICE"))
                fragmentId = R.id.nav_service_detail_seller;
            else if (type.equals("PRODUCT"))
                fragmentId = R.id.nav_product_detail_seller;
        } else {
            if (type.equals("SERVICE"))
                fragmentId = R.id.nav_service_detail_user;
            else if (type.equals("PRODUCT"))
                fragmentId = R.id.nav_product_detail_user;
        }

        if (type.equals("SERVICE") || type.equals("PRODUCT"))
            bundle.putString("staticId", entityId);

        if (fragmentId != -1)
            navController.navigate(fragmentId, bundle);
    }
}
