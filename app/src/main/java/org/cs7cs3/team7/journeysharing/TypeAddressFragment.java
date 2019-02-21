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
    private OnDemandJourneyViewModel mViewModel;
    private static final String IS_DESTINATION = "isDestination?";
    private static final String START_POINT = "start from where?";
    private static final String DESTINATION = "where wanna to go?";
    private final String TAG = "myTag";

    public static TypeAddressFragment newInstance() {
       return new TypeAddressFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(OnDemandJourneyViewModel.class);
        final Button button = getView().findViewById(R.id.ok_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputAddress = getView().findViewById(R.id.address_text);
                String address = inputAddress.getText().toString();
                if(mViewModel.getIsDestination().getValue()){
                    mViewModel.setTo(address);
                }else {
                    mViewModel.setFrom(address);
                }
                Log.d(TAG, "From:"+mViewModel.getFrom().getValue());
                Log.d(TAG, "To:"+mViewModel.getTo().getValue());

                Fragment onDemandJourneyFragment = OnDemandJourneyFragment.newInstance();


                loadFragment(onDemandJourneyFragment);
            }
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

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
