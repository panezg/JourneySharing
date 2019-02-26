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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypeAddressFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment supportMapFragment;
    private TypeAddressViewModel viewModel;
    private EditText inputAddress;
    private static final String IS_DESTINATION = "isDestination?";
    private static final String START_POINT = "start from where?";
    private static final String DESTINATION = "where wanna to go?";
    private static final float DEFAULT_ZOOM = 15f;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(TypeAddressViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.type_address, container, false);
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        SupportMapFragment mapFragment=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final Button button = view.findViewById(R.id.ok_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputAddress = getView().findViewById(R.id.input_search);
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
        LatLng latLng=new LatLng(39.9046900000,116.4071700000);
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng).title("test");
        mMap.addMarker(markerOptions);
        init();
    }
}
