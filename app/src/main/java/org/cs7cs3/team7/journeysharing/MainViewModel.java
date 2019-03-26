package org.cs7cs3.team7.journeysharing;

import android.util.Log;

import org.cs7cs3.team7.journeysharing.Models.UserInfo;
import org.cs7cs3.team7.journeysharing.httpservice.HTTPService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainViewModel extends ViewModel {
    /*
    ------------------------------------ ViewModel For OnDemandJourneyFragment -----------------------------------
     */

    //Data for OnDemandJourneyFragment
    private MutableLiveData<String> from;
    private MutableLiveData<String> to;
    private MutableLiveData<Boolean> isDestination;

    private MutableLiveData<UserInfo> sender;
    private MutableLiveData<String> Time;
    private MutableLiveData<String> Date;
    private MutableLiveData<Map<String, UserInfo>> membersList;

    // Preferences
    private MutableLiveData<String> genderPreference;
    private MutableLiveData<Integer> preGenderItemIndexSelected;
    private MutableLiveData<Integer> preMethodItemIndexSelected;
    private MutableLiveData<String> methodPreference;

    public MutableLiveData<UserInfo> getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender.setValue(sender);
    }

    public MutableLiveData<Map<String, UserInfo>> getMembersList() {
        return membersList;
    }

    public void setMembersList(Map<String, UserInfo>membersList) {
        this.membersList.setValue(membersList);
    }

    //Getter and Setter of OnDemandJourneyFragment.
    public LiveData<String> getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from.setValue(from);
    }

    public LiveData<String> getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to.setValue(to);
    }

    public LiveData<Boolean> getIsDestination() {
        return isDestination;
    }

    public void setIsDestination(boolean isDestination) {
        this.isDestination.setValue(isDestination);
    }

    public MutableLiveData<String> getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time.setValue(time);
    }

    public MutableLiveData<String> getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date.setValue(date);
    }

    public MutableLiveData<String> getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference.setValue(genderPreference);
    }

    public MutableLiveData<String> getMethodPreference() {
        return methodPreference;
    }

    public void setMethodPreference(String methodPreference) {
        this.methodPreference.setValue(methodPreference);
    }

    public MutableLiveData<Integer> getPreGenderItemIndexSelected() {
        return preGenderItemIndexSelected;
    }

    public void setPreGenderItemIndexSelected(int preGenderItemIndexSelected) {
        this.genderItemIndexSelected.setValue(preGenderItemIndexSelected);
    }

    public MutableLiveData<Integer> getPreMethodItemIndexSelected() {
        return preMethodItemIndexSelected;
    }

    public void setPreMethodItemIndexSelected(Integer preMethodItemIndexSelected) {
        this.preMethodItemIndexSelected.setValue(preMethodItemIndexSelected);
    }

    /*
    ------------------------------------ ViewModel For ScheduledJourney Fragment-----------------------------------
     */
    // TODO:

    /*
    ------------------------------------ ViewModel For ProfileFragment -----------------------------------
     */

    //Data for ProfileFragment.
    private MutableLiveData<String> names;
    private MutableLiveData<String> phone;
    private MutableLiveData<Integer> genderItemIndexSelected;
    private MutableLiveData<String> gender;

    // For sending Http request
    private MutableLiveData<Integer> uniqueID;

    //Getter and Setter of ProfileFragment.
    public MutableLiveData<String> getNames() {
        return names;
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public MutableLiveData<Integer> getGenderItemIndexSelected() {
        return genderItemIndexSelected;
    }

    public void setNames(String names) {
        this.names.setValue(names);
    }

    public void setPhone(String phone) {
        this.phone.setValue(phone);
    }

    public void setGenderItemIndexSelected(int genderItemIndexSelected) {
        this.genderItemIndexSelected.setValue(genderItemIndexSelected);
    }

    public MutableLiveData<Integer> getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(Integer uniqueID) {
        this.uniqueID.setValue(uniqueID);
    }

    public MutableLiveData<String> getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.setValue(gender);
    }

    /*
    ------------------------------------ Initialization -----------------------------------
     */

    public void init() {

        // Initialization of data in OnDemandJourneyFragment
        from = new MutableLiveData<>();
        from.setValue("Default");
        to = new MutableLiveData<>();
        to.setValue("Default");
        isDestination = new MutableLiveData<>();
        isDestination.setValue(false);
        Time = new MutableLiveData<>();
        Time.setValue("Date");
        Date = new MutableLiveData<>();
        Date.setValue("Time");

        // Initialization of data in ProfileFragment.
        names = new MutableLiveData<>();
        names.setValue("");
        genderItemIndexSelected = new MutableLiveData<>();
        genderItemIndexSelected.setValue(0);
        phone = new MutableLiveData<>();
        phone.setValue("");

        sender = new MutableLiveData<>();
        // TODO: Need to check if 0 represents Male.
        sender.setValue(new UserInfo());
        membersList = new MutableLiveData<>();
        membersList.setValue(new HashMap<>());
    }

    public void saveUserProfile(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://35.190.162.77:8080")
                .build();
        HTTPService httpService = retrofit.create(HTTPService.class);
        Call<ResponseBody> repos = httpService.test();
        repos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                JsonObject post = new JsonObject().get(response.body().toString()).getAsJsonObject();
//                if (post.get("Level").getAsString().contains("Administrator")) {
//
//                }

                try {
                    Log.d("JINCHIServer", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });

    }

}
