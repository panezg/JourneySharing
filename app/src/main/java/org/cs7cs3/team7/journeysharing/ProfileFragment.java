package org.cs7cs3.team7.journeysharing;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    // Create an anonymous implementation of OnClickListener
    private Button.OnClickListener mSaveOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
        Log.d("myTag", "This is my message from button");

        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Button button = getView().findViewById(R.id.save_button);
        button.setOnClickListener(mSaveOnClickListener);
    }
}
