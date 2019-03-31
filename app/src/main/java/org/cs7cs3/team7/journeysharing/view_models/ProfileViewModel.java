package org.cs7cs3.team7.journeysharing.view_models;

import android.util.Log;

import org.cs7cs3.team7.journeysharing.database.entity.User;
import org.cs7cs3.team7.journeysharing.repositories.UserRepository;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private LiveData<User> user;
    private UserRepository userRepository;

    @Inject
    public ProfileViewModel(UserRepository userRepository) {
        Log.d("JINCHI", "Creating ProfileViewModel");
        this.userRepository = userRepository;
        //setGenderItemIndexSelected(0);
        String login = this.userRepository.recoverUserLogin();
        if (login != null) {
            this.user = this.userRepository.getUser(login);
        } else {
            // user as LiveData won't be null, but it won't contain payload, and fragment will know
            // that means this is first time use
            this.user = new MutableLiveData<>();
        }
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void update(String login, String names, String phoneNum, String gender) {
        if (user.getValue() == null) {
            MutableLiveData<User> userTemp = new MutableLiveData<>();
            userTemp.setValue(new User(-1, login, names, phoneNum, gender));
            user = userTemp;
        } else {
            user.getValue().setLogin(login);
            user.getValue().setNames(names);
            user.getValue().setPhoneNum(phoneNum);
            user.getValue().setGender(gender);
        }
        this.userRepository.save(user.getValue());
    }
}
