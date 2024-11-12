package com.wde.eventplanner.fragments;

import static com.wde.eventplanner.constants.RegexConstants.EMAIL_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.isStrongPassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.wde.eventplanner.DemoUser;
import com.wde.eventplanner.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

    private List<DemoUser> userList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_screen, container, false);

        userList = new ArrayList<>();
        userList.add(new DemoUser("email@email.com", "Faks1312!"));
        userList.add(new DemoUser("ftn@ftn.com", "Faks1312!"));
        userList.add(new DemoUser("a@a.com", "Faks1312!"));

        TextInputEditText emailInput = view.findViewById(R.id.emailInput);
        TextInputEditText passwordInput = view.findViewById(R.id.passwordInput);
        MaterialButton loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                showSnackbar(view, "Please fill in the fields");
            } else if (!isValidEmail(email)) {
                showSnackbar(view, "Invalid email format");
            } else if (!isStrongPassword(password)) {
                showSnackbar(view, "Password is too weak. Use 8+ chars, upper & lowercase, number and special char.");
            } else if (!isValidUser(email, password)) {
                showSnackbar(view, "Wrong email or password");
            } else {
                Toast.makeText(getContext(), "Successful login", Toast.LENGTH_SHORT).show();
                // Navigate to HomeScreen or trigger other actions
            }
        });
        MaterialButton registerOrganizerButton = view.findViewById(R.id.registerOrganizerButton);

        registerOrganizerButton.setOnClickListener(v -> {
            // Get the NavController
            NavController navController = Navigation.findNavController(v);

            // Navigate to RegistrationFragment
            navController.navigate(R.id.action_login_to_organizer_registration);

            // Update the toolbar title
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar); // Adjust toolbar ID
            toolbar.setTitle("Registration"); // Set the title for the registration screen
        });

        MaterialButton registerSellerButton = view.findViewById(R.id.registerSellerButton);

        registerSellerButton.setOnClickListener(v -> {
            // Get the NavController
            NavController navController = Navigation.findNavController(v);

            // Navigate to RegistrationFragment
            navController.navigate(R.id.action_login_to_seller_registration);

            // Update the toolbar title
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar); // Adjust toolbar ID
            toolbar.setTitle("Registration"); // Set the title for the registration screen
        });


        return view;
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view.findViewById(R.id.main_layout), message, Snackbar.LENGTH_LONG).show();
    }

    private boolean isValidUser(String email, String password) {
        for (DemoUser user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
