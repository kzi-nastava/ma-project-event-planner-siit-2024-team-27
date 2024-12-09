package com.wde.eventplanner.activities;

import static com.wde.eventplanner.constants.CustomGraphicUtils.hideKeyboard;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.ActivityMainBinding;
import com.wde.eventplanner.viewmodels.EventsViewModel;
import com.wde.eventplanner.viewmodels.ListingsViewModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    // TOP LEVEL DESTINATIONS (ADD NEW PAGES HERE)
    private static final int[] TOP_LEVEL_DESTINATIONS = {
            R.id.nav_homepage,
            R.id.nav_events,
            R.id.nav_market,
            R.id.nav_my_listings,
            R.id.nav_my_events,
            R.id.nav_notifications,
            R.id.nav_listing_categories,
            R.id.nav_login
    };

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
            appBarConfiguration = new AppBarConfiguration.Builder(TOP_LEVEL_DESTINATIONS).setOpenableLayout(binding.drawerLayout).build();

            // Link toolbar and NavigationView with NavController
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navigationView, navController);
            binding.navigationView.getMenu().getItem(1).setChecked(true);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.nav_login)
                    binding.loginButton.setSelected(true);
                else if (destination.getId() != R.id.nav_close)
                    binding.loginButton.setSelected(false);
            });

            binding.navigationView.setNavigationItemSelectedListener(item -> {
                if (notCurrent(navController, item.getItemId()))
                    navController.navigate(item.getItemId());
                if (item.getItemId() != R.id.nav_close)
                    binding.loginButton.setSelected(false);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });

            binding.loginButton.setOnClickListener(view -> {
                if (notCurrent(navController, R.id.nav_login))
                    navController.navigate(R.id.nav_login);
                binding.loginButton.setSelected(true);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            });

            // wait for homepage data to fetch
            final View content = findViewById(android.R.id.content);
            Handler handler = new Handler(Looper.getMainLooper());
            ViewModelProvider viewModelProvider = new ViewModelProvider(this);
            EventsViewModel eventsViewModel = viewModelProvider.get(EventsViewModel.class);
            ListingsViewModel listingsViewModel = viewModelProvider.get(ListingsViewModel.class);
            AtomicBoolean alreadyShowed = new AtomicBoolean(false);
            AtomicBoolean timeout = new AtomicBoolean(false);
            handler.postDelayed(() -> timeout.set(true), 3000);
            content.getViewTreeObserver().addOnPreDrawListener(() -> {
                if (eventsViewModel.getTopEvents().isInitialized() && listingsViewModel.getTopListings().isInitialized())
                    alreadyShowed.set(true);
                if (timeout.get() && !alreadyShowed.get()) {
                    alreadyShowed.set(true);
                    Toast.makeText(this, "Error: data fetch timed out", Toast.LENGTH_SHORT).show();
                }
                return timeout.get() || eventsViewModel.getTopEvents().isInitialized() && listingsViewModel.getTopListings().isInitialized();
            });
        }
    }

    private boolean notCurrent(NavController navController, int id) {
        NavDestination currentDestination = navController.getCurrentDestination();
        return currentDestination == null || currentDestination.getId() != id;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    hideKeyboard(this, v);
                }
            }
        }
        return super.dispatchTouchEvent(event);
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
