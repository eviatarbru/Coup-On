package com.coupOn.platform.coupOn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shashank.platform.coup_on.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {

    // recycler view
    RecyclerView notificationRv;


    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // init recyclerview
        notificationRv = view.findViewById(R.id.notificationRv);
        return view;
    }
}