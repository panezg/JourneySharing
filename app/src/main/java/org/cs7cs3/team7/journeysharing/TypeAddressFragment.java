package org.cs7cs3.team7.journeysharing;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//

public class TypeAddressFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MainViewModel mViewModel;
    private Button addressSaveButton;
    private EditText inputAddress;
    private static final float DEFAULT_ZOOM = 15f;

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
        inputAddress = getView().findViewById(R.id.input_search);

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
        SupportMapFragment mapFragment=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Load fragment
    private void loadFragment(Fragment fragment) {
        //TODO: Exception here should be handled.
        FragmentTransaction transaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void init() {
        inputAddress=getView().findViewById(R.id.input_search);
        inputAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == event.ACTION_DOWN
                        || event.getAction() == event.KEYCODE_ENTER) {
                    geolacation();
                    return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                }
                return false;
            }
        });
    }


    private void geolacation() {
        Log.d("geolacation", "getlocate:geolocating");
        String searchString = inputAddress.getText().toString();
        Geocoder geocoder;
        geocoder = new Geocoder(getActivity().getApplicationContext());
        Log.d("Geolo","getContext"+getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d("MAP Place", address.toString());
            moveCamer(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    private void moveCamer(LatLng latLng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(markerOptions);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        init();
    }


}
