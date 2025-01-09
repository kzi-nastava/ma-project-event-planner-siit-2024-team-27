package com.wde.eventplanner.activities;

import static com.wde.eventplanner.utils.CustomGraphicUtils.hideKeyboard;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.Manifest;
import android.widget.EditText;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.wde.eventplanner.R;
import com.wde.eventplanner.services.NotificationService;
import com.wde.eventplanner.utils.MenuManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.databinding.ActivityMainBinding;
import com.wde.eventplanner.viewmodels.EventsViewModel;
import com.wde.eventplanner.viewmodels.ListingsViewModel;
import com.wde.eventplanner.viewmodels.NotificationsViewModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    public ActivityMainBinding binding;
    public NavController navController;

    // TOP LEVEL DESTINATIONS (ADD NEW PAGES HERE)
    private static final int[] TOP_LEVEL_DESTINATIONS = {
            R.id.nav_homepage,
            R.id.nav_events,
            R.id.nav_market,
            R.id.nav_my_listings,
            R.id.nav_my_events,
            R.id.nav_notifications,
            R.id.nav_event_types,
            R.id.nav_listing_categories,
            R.id.nav_reviews,
            R.id.nav_statistics,
            R.id.nav_login
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen.installSplashScreen(this);
        setupNotifications();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            }).launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.toolbar);
        setContentView(binding.getRoot());

        // Set up NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            appBarConfiguration = new AppBarConfiguration.Builder(TOP_LEVEL_DESTINATIONS).setOpenableLayout(binding.drawerLayout).build();

            // Link toolbar and NavigationView with NavController
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navigationView, navController);
            MenuManager.adjustMenu(this);

            binding.navigationView.getMenu().getItem(1).setChecked(true);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.nav_login)
                    binding.loginButton.setSelected(true);
                else if (destination.getId() != R.id.nav_close)
                    binding.loginButton.setSelected(false);
            });

            binding.navigationView.setNavigationItemSelectedListener(item -> {
                if (item.getItemId() != R.id.nav_close) {
                    if (notCurrent(navController, item.getItemId()))
                        navController.navigate(item.getItemId());
                    binding.loginButton.setSelected(false);
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });

            binding.loginButton.setOnClickListener(view -> {
                if (notCurrent(navController, R.id.nav_login))
                    navController.navigate(R.id.nav_login);
                binding.loginButton.setSelected(true);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            });

            binding.logoutButton.setOnClickListener(view -> {
                TokenManager.clearToken(this);
                MenuManager.adjustMenu(this);
                NotificationService.unsubscribe();
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            });

            // wait for homepage data to fetch
            View content = findViewById(android.R.id.content);
            Handler handler = new Handler(Looper.getMainLooper());
            ViewModelProvider viewModelProvider = new ViewModelProvider(this);
            EventsViewModel eventsViewModel = viewModelProvider.get(EventsViewModel.class);
            ListingsViewModel listingsViewModel = viewModelProvider.get(ListingsViewModel.class);
            NotificationsViewModel notificationsViewModel = viewModelProvider.get(NotificationsViewModel.class);
            AtomicBoolean alreadyShowed = new AtomicBoolean(false);
            AtomicBoolean timeout = new AtomicBoolean(false);
            handler.postDelayed(() -> timeout.set(true), 3000);
            content.getViewTreeObserver().addOnPreDrawListener(() -> {
                if (eventsViewModel.getTopEvents().isInitialized() && listingsViewModel.getTopListings().isInitialized())
                    alreadyShowed.set(true);
                if (timeout.get() && !alreadyShowed.get()) {
                    alreadyShowed.set(true);
                    SingleToast.show(this, "Error: data fetch timed out");
                }
                return timeout.get() || eventsViewModel.getTopEvents().isInitialized() && listingsViewModel.getTopListings().isInitialized();
            });

            notificationsViewModel.getNotifications().observe(this, notifications -> {
                if (notifications.stream().anyMatch(notification -> !notification.isSeen()))
                    binding.navigationView.getMenu().findItem(R.id.nav_notifications).setIcon(R.drawable.ic_notifications_on);
                else
                    binding.navigationView.getMenu().findItem(R.id.nav_notifications).setIcon(R.drawable.ic_notifications);
            });
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("type")) {
            String type = intent.getStringExtra("type");
            String entityId = intent.getStringExtra("entityId");
            MenuManager.navigateToFragment(type, entityId, this, navController);
        }
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("type")) {
            String type = intent.getStringExtra("type");
            String entityId = intent.getStringExtra("entityId");
            MenuManager.navigateToFragment(type, entityId, this, navController);
        }
    }

    private boolean notCurrent(NavController navController, int id) {
        NavDestination currentDestination = navController.getCurrentDestination();
        return currentDestination == null || currentDestination.getId() != id;
    }

    private void setupNotifications() {
        NotificationChannel channel = new NotificationChannel("user_notifications", "User notifications", NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            }).launch(Manifest.permission.POST_NOTIFICATIONS);

        NotificationService.onMessageReceived = () -> MenuManager.refreshNotificationIcon(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()) && v.getId() != R.id.guestListEmailInput) {
                    v.clearFocus();
                    hideKeyboard(this, v);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
