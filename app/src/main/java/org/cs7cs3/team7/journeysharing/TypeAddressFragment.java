package org.cs7cs3.team7.journeysharing;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
//

public class TypeAddressFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private MainViewModel mViewModel;
    private Button addressSaveButton;
    private EditText inputAddress;
    private static final float DEFAULT_ZOOM = 15f;
    private static final String FINE_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private Boolean mlocationpermission=false;

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
        mViewModel= ViewModelProviders.of(getActivity(), viewModelFactory).get(MainViewModel.class);

        //TODO: Exception here should be handled.
        // Inti the addressSaveButton and Set its onClick listener.
        addressSaveButton = getView().findViewById(R.id.ok_button);

        // Inti the EditView 'inputAddress'.
        inputAddress = getView().findViewById(R.id.input_search);

        addressSaveButton.setOnClickListener((v) -> {
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
        getDeviceLocation();
        getLocationPermission();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.type_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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
        getDeviceLocation();
        inputAddress = getView().findViewById(R.id.input_search);
        inputAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == event.ACTION_DOWN
                        || event.getAction() == event.KEYCODE_ENTER
                                                                        ) {
                    geolacation();
                    return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
                }
                return false;
            }
        });
    }

    private void getLocationPermission(){
        String[] permission={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(),COURSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                mlocationpermission=true;
                Log.d("XINDI","获取位置permission成功");
            }else{
                ActivityCompat.requestPermissions(getActivity(),permission,1234);
            }
        }else{
            ActivityCompat.requestPermissions(getActivity(),permission,1234);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mlocationpermission=false;
        switch (requestCode){
            case 1234:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    for(int i=0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mlocationpermission=false;
                            Log.d("XINDI","获取位置permission失败");
                            break;
                        }
                    }
                    mlocationpermission=true;
                    Log.d("XINDI","获取位置permission成功2");
                }
            }
        }
    }

    private void getDeviceLocation(){
        Log.d("XINDI","get device loction");
        mfusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getContext());
        if(mlocationpermission==true){
            Log.d("XINDI","可以获取本机地址");
        }else {
            Log.d("XINDI","无法获取本机地址");
        }
        try {
            if(mlocationpermission){
                Task location=mfusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d("XINDI","get the device location");
                            Location currentLocation=(Location)task.getResult();
                            moveCamer(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM,"current Location");
                        }else {
                            Log.d("XINDI","the device location is null");
                        }
                    }
                });

            }
        }catch (SecurityException e){
            Log.e("XINDI",e.getMessage());
        }
    }
    private void geolacation() {
        Log.d("XINDI", "getlocate:geolocating");
        String searchString = inputAddress.getText().toString();
        Geocoder geocoder;
        geocoder = new Geocoder(getActivity().getApplicationContext());
        Log.d("XINDI", "getContext" + getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d("XINDI", address.toString());
            double Latitude=address.getLatitude();
            mViewModel.setLatitude(Latitude);
            double Longtitude=address.getLongitude();
            mViewModel.setLongtitute(Longtitude);
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
