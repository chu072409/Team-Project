package com.example.weather.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.weather.R;

public class OutfitFragment extends Fragment {

    public OutfitFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outfit, container, false);

        CardView btnClosetCard = view.findViewById(R.id.btnClosetCard);
        btnClosetCard.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ClosetFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
