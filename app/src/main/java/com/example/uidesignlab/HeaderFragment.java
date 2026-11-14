package com.example.uidesignlab;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HeaderFragment extends Fragment {

    private static final String TAG = "LifecycleTracker";

    private OnMessageSendListener listener;

    public HeaderFragment() {
        // Required empty constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnMessageSendListener) {
            listener = (OnMessageSendListener) context;
        } else {
            throw new RuntimeException(
                    context.toString() +
                            " must implement OnMessageSendListener"
            );
        }

        Log.d(TAG, "Header onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_header,
                container,
                false
        );

        Button btnSend = view.findViewById(R.id.btnSendToDetail);

        btnSend.setOnClickListener(v -> {

            if (listener != null) {
                listener.onMessageSend(
                        "Hello from HeaderFragment!"
                );
            }

        });


        Button btnClear = view.findViewById(R.id.btnClearMessage);

        btnClear.setOnClickListener(v -> {

            if (listener != null) {
                listener.onClearMessage();
            }

        });


        Log.d(TAG, "Header onCreateView");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d(TAG, "Header onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }
}
