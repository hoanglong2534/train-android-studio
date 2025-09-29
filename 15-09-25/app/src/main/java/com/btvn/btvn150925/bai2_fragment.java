package com.btvn.btvn150925;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class bai2_fragment extends Fragment {

    private static final String ARG_USER_NAME = "user_name";

    private String userName;

    public bai2_fragment() {
        // Required empty public constructor
    }

    // Factory method
    public static bai2_fragment newInstance(String userName, String unused) {
        bai2_fragment fragment = new bai2_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, userName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_USER_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bai2_fragment, container, false);

        TextView tv = view.findViewById(R.id.textViewFragment);
        if (userName != null && !userName.isEmpty()) {
            tv.setText("Xin chào, " + userName + "!");
        } else {
            tv.setText("Xin chào!");
        }

        return view;
    }
}
