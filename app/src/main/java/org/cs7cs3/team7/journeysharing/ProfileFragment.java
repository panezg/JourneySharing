package org.cs7cs3.team7.journeysharing;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.cs7cs3.team7.journeysharing.database.entity.User;
import org.cs7cs3.team7.journeysharing.view_models.ProfileViewModel;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.AndroidSupportInjection;


public class ProfileFragment extends Fragment {
    public static final String UID_KEY = "uid";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ProfileViewModel viewModel;

    private EditText namesEditText;
    private EditText phoneEditText;
    private Spinner genderSpinner;
    private Button saveButton;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();

        View userInfoLayout = getView().findViewById(R.id.user_info_include);
        namesEditText = userInfoLayout.findViewById(R.id.names_text);
        phoneEditText = userInfoLayout.findViewById(R.id.phone_number_text);
        genderSpinner = userInfoLayout.findViewById(R.id.gender_spinner);
        saveButton = getView().findViewById(R.id.save_button);

        this.configureViewModel();

        saveButton.setOnClickListener(v -> {
            Log.d("JINCHI", "Saving...");
            blockAllComponents();
            viewModel.update(namesEditText.getText().toString(),
                    phoneEditText.getText().toString(),
                    genderSpinner.getSelectedItem().toString());
            unblockAllComponents();
        });

        if (this.viewModel.getUser().getValue() == null) {
            alertRegister();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel.class);
        viewModel.getUser().observe(this, user -> updateUI(user));
    }

    // -----------------
    // UI
    // -----------------

    private void updateUI(User user) {
        Log.d("JINCHI", "BEGIN updateUI()");
        Log.d("JINCHI", "Is user null in updateUI?" + (user == null));
        if (user != null) {
            namesEditText.setText(user.getNames());
            phoneEditText.setText(user.getPhoneNum());
            //wrong code
            genderSpinner.setSelection(0);
            //genderSpinner.setSelection(user.getGenderCode());
            //genderSpinner.setSelection(userInfo.getGender());
        } else
        {
            Log.d("JINCHI", "User is null");
        }
        Log.d("JINCHI", "END updateUI()");
    }

    private void alertRegister() {
        new AlertDialog.Builder(getContext())
                .setTitle("First Time Using the App")
                .setMessage("Complete your profile and save it to continue, please.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void blockAllComponents() {
        namesEditText.setEnabled(false);
        phoneEditText.setEnabled(false);
        genderSpinner.setEnabled(false);
        saveButton.setEnabled(false);
    }

    private void unblockAllComponents() {
        namesEditText.setEnabled(true);
        phoneEditText.setEnabled(true);
        genderSpinner.setEnabled(true);
        saveButton.setEnabled(true);
    }
}
