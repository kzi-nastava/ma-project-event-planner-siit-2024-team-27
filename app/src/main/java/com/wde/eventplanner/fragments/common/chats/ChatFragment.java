package com.wde.eventplanner.fragments.common.chats;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wde.eventplanner.adapters.ChatMessageAdapter;
import com.wde.eventplanner.clients.WebSocketClient;
import com.wde.eventplanner.databinding.FragmentChatBinding;
import com.wde.eventplanner.models.chat.ChatMessage;
import com.wde.eventplanner.utils.MenuManager;
import com.wde.eventplanner.utils.SingleToast;
import com.wde.eventplanner.utils.TokenManager;
import com.wde.eventplanner.viewmodels.ChatsViewModel;

import java.util.UUID;

public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
    private ChatsViewModel viewModel;
    private UUID toProfileId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        binding.messages.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.messages.setNestedScrollingEnabled(false);

        String chatId = requireArguments().getString("chatId");

        viewModel = new ViewModelProvider(requireActivity()).get(ChatsViewModel.class);
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                SingleToast.show(requireContext(), error);
                viewModel.clearErrorMessage();
            }
        });

        viewModel.getMessages(UUID.fromString(chatId)).observe(getViewLifecycleOwner(), messages -> {
            binding.messages.setAdapter(new ChatMessageAdapter(messages));
            binding.messages.scrollToPosition(messages.size() - 1);
        });

        viewModel.getChat(TokenManager.getProfileId(binding.getRoot().getContext()), UUID.fromString(chatId)).observe(getViewLifecycleOwner(), chat -> {
            binding.user.setText(chat.getChatPartnerNameAndSurname());
            binding.title.setText(chat.getListingName());
            toProfileId = chat.getChatPartnerId();
            // todo report button

            binding.title.setOnClickListener(v -> {
                String type = chat.getListingType() == null ? "EVENT" : chat.getListingType().toString().toUpperCase();
                MenuManager.navigateToFragment(type, chat.getListingId().toString(), binding.getRoot().getContext(), NavHostFragment.findNavController(this));
            });
        });

        final boolean[] isKeyboardVisible = {false};
        View rootView = requireActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;
            boolean isNowVisible = keypadHeight > screenHeight * 0.15;
            if (isNowVisible != isKeyboardVisible[0]) {
                isKeyboardVisible[0] = isNowVisible;
                if (binding.messages.getAdapter() != null)
                    binding.messages.scrollToPosition(((ChatMessageAdapter) binding.messages.getAdapter()).messages.size() - 1);
            }
        });

        binding.sendButton.setOnClickListener(v -> sendMessage(chatId));
        binding.messageInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage(chatId);
                return true;
            }
            return false;
        });

        WebSocketClient.setListener(binding.getRoot().getContext(), chatId, message ->
                requireActivity().runOnUiThread(() -> {
                    ChatMessageAdapter adapter = (ChatMessageAdapter) binding.messages.getAdapter();
                    if (adapter != null) {
                        adapter.messages.add(message);
                        adapter.notifyItemInserted(adapter.messages.size() - 1);
                        binding.messages.scrollToPosition(adapter.messages.size() - 1);
                    }
                }));

        return binding.getRoot();
    }

    private void sendMessage(String chatId) {
        if (binding.messageInput.getText() != null && !binding.messageInput.getText().toString().isBlank()) {
            ChatMessage message = new ChatMessage(UUID.fromString(chatId), binding.messageInput.getText().toString(), toProfileId);
            WebSocketClient.sendMessage(binding.getRoot().getContext(), message);
            binding.messageInput.setText("");
        }
    }
}
