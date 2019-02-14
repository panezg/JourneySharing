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

public class TypeAddressFragment extends Fragment {
    private TypeAddressViewModel mViewModel;
    private static final String IS_DESTINATION = "isDestination?";
    private static final String START_POINT = "start from where?";
    private static final String DESTINATION = "where wanna to go?";

    public static TypeAddressFragment newInstance(boolean isDestination, String toAddress, String fromAddress) {
        Bundle bundle = new Bundle();
        TypeAddressFragment fragment = new TypeAddressFragment();
        bundle.putBoolean(IS_DESTINATION, isDestination);
        bundle.putString(START_POINT, toAddress);
        bundle.putString(DESTINATION, fromAddress);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.type_address, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TypeAddressViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        final Button saveAddress = view.findViewById(R.id.save_address_button);

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputAddress = getView().findViewById(R.id.input_address);
                String address = inputAddress.getText().toString();
                Bundle bundle = getArguments();
                String resetValue = bundle.getBoolean(IS_DESTINATION) ? DESTINATION : START_POINT;
                bundle.putString(resetValue, address);
                Log.d("myTag", "msg is from 'TypeAddressFragment");
                Fragment onDemandJourneyFragment = OnDemandJourneyFragment.newInstance(bundle.getString(START_POINT), bundle.getString(DESTINATION));
                Log.d("myTag", "start point: " + bundle.getString(START_POINT) + "\n destination: " + bundle.getString(DESTINATION));

                loadFragment(onDemandJourneyFragment);
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
