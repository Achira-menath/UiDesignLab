package com.example.uidesignlab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class DetailFragment extends Fragment {

    private TextView txtReceivedMessage;

    public DetailFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_detail,
                container,
                false
        );

        txtReceivedMessage = view.findViewById(
                R.id.txtReceivedMessage
        );

        Button btnShowInfo = view.findViewById(
                R.id.btnShowInfo
        );

        btnShowInfo.setOnClickListener(v -> {

            FragmentTransaction transaction =
                    getParentFragmentManager()
                            .beginTransaction();

            transaction.replace(
                    R.id.dynamic_fragment_container,
                    new InfoFragment()
            );

            transaction.addToBackStack(null);

            transaction.commit();

        });

        return view;
    }

    public void updateMessage(String message) {

        if (txtReceivedMessage != null) {
            txtReceivedMessage.setText(message);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        txtReceivedMessage = null;
    }
}
