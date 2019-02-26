package org.cs7cs3.team7.journeysharing;

import androidx.fragment.app.FragmentTransaction;
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
import android.widget.EditText;
import java.util.Objects;

public class TypeAddressFragment extends Fragment {

    private MainViewModel mViewModel;
    private Button addressSaveButton;
    private EditText inputAddress;

    private final String TAG = "myTag";

    static TypeAddressFragment newInstance() {
       return new TypeAddressFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inti the ViewModel.
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MainViewModel.class);

        //TODO: Exception here should be handled.
        // Inti the addressSaveButton and Set its onClick listener.
        addressSaveButton = getView().findViewById(R.id.ok_button);

        // Inti the EditView 'inputAddress'.
        inputAddress = getView().findViewById(R.id.address_text);

        addressSaveButton.setOnClickListener( (v) -> {
            String address = inputAddress.getText().toString();
            //TODO: Exception here should be handled.
            if (mViewModel.getIsDestination().getValue()) {
                mViewModel.setTo(address);
            } else {
                mViewModel.setFrom(address);
            }

            Log.d(TAG, "From:" + mViewModel.getFrom());
            Log.d(TAG, "To:" + mViewModel.getTo());

            Fragment onDemandJourneyFragment = OnDemandJourneyFragment.newInstance();
            loadFragment(onDemandJourneyFragment);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.type_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

    }

    // Load fragment
    private void loadFragment(Fragment fragment) {
        //TODO: Exception here should be handled.
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
