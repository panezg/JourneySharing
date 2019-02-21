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
    private OnDemandJourneyViewModel mViewModel;
    private final String TAG = "myTag";

    public static TypeAddressFragment newInstance() {
       return new TypeAddressFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OnDemandJourneyViewModel.class);
        mViewModel.init();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.type_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        final Button button = view.findViewById(R.id.ok_button);

        button.setOnClickListener( (v) -> {
            EditText inputAddress = getView().findViewById(R.id.address_text);
            String address = inputAddress.getText().toString();
            if (mViewModel.getIsDestination().getValue()) {
                mViewModel.setTo(address);
            } else {
                mViewModel.setFrom(address);
            }
            Log.d(TAG, "From:" + mViewModel.getFrom().getValue());
            Log.d(TAG, "To:" + mViewModel.getTo().getValue());

            Fragment onDemandJourneyFragment = OnDemandJourneyFragment.newInstance();


            loadFragment(onDemandJourneyFragment);
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
