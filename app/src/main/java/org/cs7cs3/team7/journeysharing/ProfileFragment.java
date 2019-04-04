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
import android.widget.Toast;

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

        Log.d("JINCHI", "Before configureViewModel() call");
        this.configureViewModel();
        Log.d("JINCHI", "After calling configureViewModel()");

        saveButton.setOnClickListener(v -> {
            Log.d("JINCHI", "Saving...");
            blockAllComponents();
            viewModel.save(namesEditText.getText().toString(),
                    phoneEditText.getText().toString(),
                    genderSpinner.getSelectedItem().toString(),
                    new SimpleCallback<Boolean>() {
                        @Override
                        public void accept(Boolean result) {
                            if (result) {
                                Toast.makeText(getContext(), "User information saved successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "A problem occurred when saving the information. Please, try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            unblockAllComponents();
        });


        if (this.viewModel.isFirstTimeUse()) {
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
        viewModel.getUser().observe(this, user -> {
            Log.d("JINCHI", "Noticed something changed at user from ProfileFragment");
            updateUI(user);
            Log.d("JINCHI", "END Noticed something changed at user from ProfileFragment");
        });
    }

    // -----------------
    // UI
    // -----------------

    private void updateUI(@Nullable User user) {
        Log.d("JINCHI", "BEGIN updateUI()");
        Log.d("JINCHI", "Is user null in updateUI? " + (user == null));
        if (user != null) {
            namesEditText.setText(user.getNames());
            phoneEditText.setText(user.getPhoneNum());
            //TODO: Verify this is the right gender code selection
            genderSpinner.setSelection(user.getGenderCode());
        } else {
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
