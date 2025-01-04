package com.wde.eventplanner.fragments.anonymous;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.wde.eventplanner.R;
import com.wde.eventplanner.services.NotificationService;
import com.wde.eventplanner.utils.MenuManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.databinding.FragmentLoginBinding;
import com.wde.eventplanner.models.user.User;
import com.wde.eventplanner.viewmodels.UsersViewModel;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private UsersViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);

        binding.loginButton.setOnClickListener(this::onLoginClicked);
        binding.passwordInput.setOnEditorActionListener((v, actionId, event) -> {
            boolean isDone = actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER);
            if (isDone) onLoginClicked(v);
            return isDone;
        });

        binding.registerOrganizerButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_login_to_organizer_registration));
        binding.registerSellerButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_login_to_seller_registration));

        return binding.getRoot();
    }

    private void onLoginClicked(View v) {
        if (binding.emailInput.getText() == null || binding.passwordInput.getText() == null) return;

        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            SingleToast.show(requireContext(), "Please fill in the fields");
            return;
        }

        viewModel.login(new User(email, password)).observe(getViewLifecycleOwner(), token -> {
            if (token != null && token.getToken() != null && !token.getToken().isBlank()) {
                TokenManager.saveToken(token.getToken(), requireContext());
                MenuManager.adjustMenu(requireActivity());
                NotificationService.subscribe(TokenManager.getProfileId(requireContext()));
            } else
                SingleToast.show(requireContext(), "Wrong email or password");
        });
    }
}
