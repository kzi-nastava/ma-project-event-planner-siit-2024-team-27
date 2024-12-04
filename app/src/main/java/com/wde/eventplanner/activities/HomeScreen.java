package com.wde.eventplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.wde.eventplanner.R;

public class HomeScreen extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = new Intent(HomeScreen.this, SplashScreen.class);
        startActivity(intent);

        setContentView(R.layout.activity_home_screen);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Button loginButton = findViewById(R.id.login_button);

        // Set up NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.HomepageFragment, R.id.LoginFragment, R.id.SellerMyListingsFragment, R.id.AllEventsFragment, R.id.AllListingsFragment, R.id.NotificationsFragment)
                .setOpenableLayout(drawerLayout)
                .build();

        // Link toolbar and NavigationView with NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
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
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        loginButton.setOnClickListener(view -> {
            // Check if the current destination is not LoginFragment
            if (notCurrent(navController, R.id.LoginFragment)) {
                navController.navigate(R.id.LoginFragment);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        });
    }

    private boolean notCurrent(NavController navController, int id) {
        NavDestination currentDestination = navController.getCurrentDestination();
        return currentDestination == null || currentDestination.getId() != id;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
