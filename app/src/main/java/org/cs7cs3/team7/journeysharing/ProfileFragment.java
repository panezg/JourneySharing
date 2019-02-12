package org.cs7cs3.team7.journeysharing;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    public static final String SHARE_PREFS = "sharedPrefs";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    private String name;
    private String phone;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    // Create an anonymous implementation of OnClickListener
    private Button.OnClickListener mSaveOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            saveData();
        }
    };

    private void saveData() {
        Log.d("myTag", "This is my message from button");
        View userInfoLayout = getView().findViewById(R.id.user_info_include);
        EditText namesEditText = userInfoLayout.findViewById(R.id.names_text);
        EditText phoneEditText = userInfoLayout.findViewById(R.id.phone_number_text);
        Log.d("myTag", "Names: " + namesEditText.getText().toString());
        Log.d("myTag", "Phone: " + phoneEditText.getText().toString());
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, namesEditText.getText().toString());
        editor.putString(PHONE, phoneEditText.getText().toString());
        editor.apply();

        Toast.makeText(this.getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        name = sharedPreferences.getString(NAME, "");
        phone = sharedPreferences.getString(PHONE, "");
    }

    private void updateView() {
        View userInfoLayout = getView().findViewById(R.id.user_info_include);
        EditText namesEditText = userInfoLayout.findViewById(R.id.names_text);
        namesEditText.setText(name);
        EditText phoneEditText = userInfoLayout.findViewById(R.id.phone_number_text);
        phoneEditText.setText(phone);
    }

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
        loadData();
        updateView();
    }
}
