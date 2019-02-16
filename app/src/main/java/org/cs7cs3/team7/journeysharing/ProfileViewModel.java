package org.cs7cs3.team7.journeysharing;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private static final String SHARE_PREFS = "sharedPrefs";
    private static final String NAME = "names";
    private static final String PHONE = "phone";
    private static final String GENDER_POSITION = "SelectedPosition";

    private MutableLiveData<String> names;
    private MutableLiveData<String> phone;
    private MutableLiveData<Integer> genderItemIndexSelected;

    private SharedPreferences sharedPreferences;

    public ProfileViewModel(Context context) {
        super();
        this.sharedPreferences = context.getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        loadData();
    }

    public MutableLiveData<String> getNames() {
        if (names == null) {
            names = new MutableLiveData<String>();
        }
        return names;
    }

    public MutableLiveData<String> getPhone() {
        if (phone == null) {
            phone = new MutableLiveData<String>();
        }
        return phone;
    }

    public MutableLiveData<Integer> getGenderItemIndexSelected() {
        if (genderItemIndexSelected == null) {
            genderItemIndexSelected = new MutableLiveData<Integer>();
        }
        return genderItemIndexSelected;
    }

    private void loadData() {
        getNames().setValue(sharedPreferences.getString(NAME, ""));
        getPhone().setValue(sharedPreferences.getString(PHONE, ""));
        getGenderItemIndexSelected().setValue(sharedPreferences.getInt(GENDER_POSITION, 0));
    }

    public void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, getNames().getValue());
        editor.putString(PHONE, getPhone().getValue());
        editor.putInt(GENDER_POSITION, getGenderItemIndexSelected().getValue());
        editor.apply();
    }
}
