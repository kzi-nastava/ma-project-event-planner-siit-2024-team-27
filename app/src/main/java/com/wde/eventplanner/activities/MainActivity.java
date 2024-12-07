package com.wde.eventplanner.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.core.splashscreen.SplashScreen;

import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.ActivityMainBinding;
import com.wde.eventplanner.viewmodels.EventsViewModel;
import com.wde.eventplanner.viewmodels.ListingsViewModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen.installSplashScreen(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.toolbar);
        setContentView(binding.getRoot());

        // Set up NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            appBarConfiguration = new AppBarConfiguration.Builder(R.id.HomepageFragment, R.id.LoginFragment,
                    R.id.SellerMyListingsFragment, R.id.AllEventsFragment, R.id.AllListingsFragment, R.id.NotificationsFragment,
                    R.id.AdminListingCategoriesFragment)
                    .setOpenableLayout(binding.drawerLayout)
                    .build();

            // Link toolbar and NavigationView with NavController
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navigationView, navController);
            binding.navigationView.getMenu().getItem(1).setChecked(true);

            binding.navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();

                if (id == R.id.homepageTab && notCurrent(navController, R.id.HomepageFragment)) {
                    navController.navigate(R.id.HomepageFragment);
                } else if (id == R.id.eventsTab && notCurrent(navController, R.id.AllEventsFragment)) {
                    navController.navigate(R.id.AllEventsFragment);
                } else if (id == R.id.marketTab && notCurrent(navController, R.id.AllListingsFragment)) {
                    navController.navigate(R.id.AllListingsFragment);
                } else if (id == R.id.myListingsTab && notCurrent(navController, R.id.SellerMyListingsFragment)) {
                    navController.navigate(R.id.SellerMyListingsFragment);
                } else if (id == R.id.notificationsTab && notCurrent(navController, R.id.NotificationsFragment)) {
                    navController.navigate(R.id.NotificationsFragment);
                } else if (id == R.id.adminListingCategoriesTab && notCurrent(navController, R.id.AdminListingCategoriesFragment)) {
                    navController.navigate(R.id.AdminListingCategoriesFragment);
                }

                if (!item.getTitle().toString().equals("Close")) {
                    binding.loginButton.setSelected(false);
                    item.setChecked(true);
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });

            binding.loginButton.setOnClickListener(view -> {
                if (notCurrent(navController, R.id.LoginFragment)) {
                    navController.navigate(R.id.LoginFragment);
                }
                binding.loginButton.setSelected(true);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            });

            // wait for homepage data to fetch
            final View content = findViewById(android.R.id.content);
            Handler handler = new Handler(Looper.getMainLooper());
            ViewModelProvider viewModelProvider = new ViewModelProvider(navController.getBackStackEntry(R.id.HomepageFragment));
            EventsViewModel eventsViewModel = viewModelProvider.get(EventsViewModel.class);
            ListingsViewModel listingsViewModel = viewModelProvider.get(ListingsViewModel.class);
            AtomicBoolean alreadyShowed = new AtomicBoolean(false);
            AtomicBoolean timeout = new AtomicBoolean(false);
            handler.postDelayed(() -> timeout.set(true), 3000);
            content.getViewTreeObserver().addOnPreDrawListener(() -> {
                if (eventsViewModel.isReady() && listingsViewModel.isReady())
                    alreadyShowed.set(true);
                if (timeout.get() && !alreadyShowed.get()) {
                    alreadyShowed.set(true);
                    Toast.makeText(this, "Error: data fetch timed out", Toast.LENGTH_SHORT).show();
                }
                return timeout.get() || eventsViewModel.isReady() && listingsViewModel.isReady();
            });
        }
    }

    private boolean notCurrent(NavController navController, int id) {
        NavDestination currentDestination = navController.getCurrentDestination();
        return currentDestination == null || currentDestination.getId() != id;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
