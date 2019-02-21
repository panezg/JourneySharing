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
import android.widget.TextView;

public class OnDemandJourneyFragment extends Fragment {

    private OnDemandJourneyViewModel mViewModel;

    private static final String START_POINT = "from_where";
    private static final String DESTINATION = "to_where";

    public static OnDemandJourneyFragment newInstance(String startLocation, String destination) {
        Bundle bundle = new Bundle();
        bundle.putString(START_POINT, startLocation);
        bundle.putString(DESTINATION, destination);
        OnDemandJourneyFragment fragment = new OnDemandJourneyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.on_demand_journey_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OnDemandJourneyViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.d("myTag", "msg is from 'onViewCreated': ");
        View addressLayout = getView().findViewById(R.id.address_include);
        Bundle bundle = getArguments();
        if(bundle != null) {
            if(bundle.containsKey(START_POINT)) {
                TextView fromAddress = addressLayout.findViewById(R.id.from_text);
                Log.d("myTag", "'START_POINT' is: " + bundle.getString(START_POINT));
                fromAddress.setText(bundle.getString(START_POINT));
            }
            if(bundle.containsKey(DESTINATION)) {
                TextView toAddress = addressLayout.findViewById(R.id.to_text);
                Log.d("myTag", "'DESTINATION' is: " + bundle.getString(DESTINATION));
                toAddress.setText(bundle.getString(DESTINATION));
            }
        }

        final View fromButton = addressLayout.findViewById(R.id.from_button);
        final View toButton = addressLayout.findViewById(R.id.to_button);
        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                Fragment fromFragment = TypeAddressFragment.newInstance(false, bundle.getString(START_POINT), bundle.getString(DESTINATION));
                Log.d("myTag", "msg from press from address button");
                Log.d("myTag", "start point: " + bundle.getString(START_POINT) + "\n" + "destination: " + bundle.getString(DESTINATION));
                loadFragment(fromFragment);
            }
        });
        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                Fragment fromFragment = TypeAddressFragment.newInstance(true, bundle.getString(START_POINT), bundle.getString(DESTINATION));
                Log.d("myTag", "msg from press to address button");
                Log.d("myTag", "start point: " + bundle.getString(START_POINT) + "\n" + "destination: " + bundle.getString(DESTINATION));
                loadFragment(fromFragment);
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        //transaction.addToBackStack(fragment.toString());
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
}
