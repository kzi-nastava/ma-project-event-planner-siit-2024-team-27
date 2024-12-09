package com.wde.eventplanner.fragments.anonymous;

import static com.wde.eventplanner.constants.RegexConstants.EMAIL_REGEX;
import static com.wde.eventplanner.constants.RegexConstants.isStrongPassword;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.wde.eventplanner.R;
import com.wde.eventplanner.databinding.FragmentLoginBinding;
import com.wde.eventplanner.models.DemoUser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private List<DemoUser> userList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        userList = new ArrayList<>();
        userList.add(new DemoUser("email@email.com", "Faks1312!"));
        userList.add(new DemoUser("ftn@ftn.com", "Faks1312!"));
        userList.add(new DemoUser("a@a.com", "Faks1312!"));

        binding.loginButton.setOnClickListener(this::onLoginClicked);
        binding.passwordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
                onLoginClicked(v);
                return true;
            }
            return false;
        });

        binding.registerOrganizerButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_login_to_organizer_registration));
        binding.registerSellerButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_login_to_seller_registration));

        return binding.getRoot();
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidUser(String email, String password) {
        for (DemoUser user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void onLoginClicked(View v) {
        // check if user exists
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please fill in the fields");
        } else if (!isValidEmail(email)) {
            showToast("Invalid email format");
        } else if (!isStrongPassword(password)) {
            showToast("Password is too weak. Use 8+ chars, upper & lowercase, number and special char.");
        } else if (!isValidUser(email, password)) {
            showToast("Wrong email or password");
        } else {
            Toast.makeText(getContext(), "Successful login", Toast.LENGTH_SHORT).show();

            // login User
            NavController navController = Navigation.findNavController(v);
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(navController.getGraph().getStartDestinationId(), true)
                    .build();
            navController.navigate(R.id.action_login_to_homepage, null, navOptions); //pops full backstack
        }
    }
}
