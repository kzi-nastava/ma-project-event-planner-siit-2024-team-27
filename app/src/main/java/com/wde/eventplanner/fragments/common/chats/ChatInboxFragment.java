package com.wde.eventplanner.fragments.common.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.ChatInboxAdapter;
import com.wde.eventplanner.databinding.FragmentChatInboxBinding;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.ChatsViewModel;

public class ChatInboxFragment extends Fragment {
    private FragmentChatInboxBinding binding;
    private ChatsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatInboxBinding.inflate(inflater, container, false);
        binding.chats.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.chats.setNestedScrollingEnabled(false);

        viewModel = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                viewModel.clearErrorMessage();
            }
        });

        viewModel.getChats(TokenManager.getProfileId(binding.getRoot().getContext())).observe(getViewLifecycleOwner(), chats ->
                binding.chats.setAdapter(new ChatInboxAdapter(chats))
        );

        return binding.getRoot();
    }
}
