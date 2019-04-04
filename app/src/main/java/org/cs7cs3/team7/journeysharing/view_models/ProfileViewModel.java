package org.cs7cs3.team7.journeysharing.view_models;

import android.util.Log;

import org.cs7cs3.team7.journeysharing.SimpleCallback;
import org.cs7cs3.team7.journeysharing.database.entity.User;
import org.cs7cs3.team7.journeysharing.repositories.UserRepository;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    //I tried using LiveData<> and MediatorLiveData<> but they create problems regarding Room, background & UI threads as well as code placement
    //The only thing that seems to work ok is MutableLiveData
    private MutableLiveData<User> mldUser;
    private UserRepository userRepository;

    private Boolean firstTimeUse;

    @Inject
    public ProfileViewModel(UserRepository userRepository) {
        Log.d("JINCHI", "Creating ProfileViewModel");
        this.userRepository = userRepository;
        //setGenderItemIndexSelected(0);
        String login = this.userRepository.recoverUserLogin();
        this.mldUser = new MutableLiveData<>();
        userRepository.getUser(mldUser, login);
        if (login != null) {
            this.firstTimeUse = false;
        } else {
            this.firstTimeUse = true;
        }
    }

    public LiveData<User> getUser() {
        Log.d("JINCHI", "BEGIN ProfileViewModel.getUser()");
        Log.d("JINCHI", "user LiveData<User> is null? " + (mldUser == null));
        if (mldUser != null) {
            Log.d("JINCHI", "user LiveData<User>.getValue() is null? " + (mldUser.getValue() == null));
            if (mldUser.getValue() != null) {
                Log.d("JINCHI", "user.getValue().toString(): " + mldUser.getValue().toString());
            }
        }
        Log.d("JINCHI", "END ProfileViewModel.getUser()");
        return mldUser;
    }

    public Boolean isFirstTimeUse() {
        return firstTimeUse;
    }

    public void save(String names, String phoneNum, String gender, SimpleCallback<Boolean> uiDelegate) {
        User tempUser = new User(-1, phoneNum, names, phoneNum, gender);
        Log.d("JINCHI", "Before calling save");
        //this.user = this.userRepository.saveUser(tempUser, uiDelegate);
        this.userRepository.saveUser(mldUser, tempUser, uiDelegate);

        Log.d("JINCHI", "After calling save");
    }
}
