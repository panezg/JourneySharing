package org.cs7cs3.team7.journeysharing;

import android.util.Log;

import org.cs7cs3.team7.journeysharing.Models.JourneyRequestInfo;
import org.cs7cs3.team7.journeysharing.Models.UserInfo;
import org.cs7cs3.team7.journeysharing.httpservice.HTTPService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    // Only for P2P part.
    private MutableLiveData<List<UserInfo>> membersList;

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

    public MutableLiveData<List<UserInfo>> getMembersList() {
        return membersList;
    }

    public void setMembersList(List<UserInfo> membersList) {
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
        this.preGenderItemIndexSelected.setValue(preGenderItemIndexSelected);
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

    // Only keep one single
    private MutableLiveData<JourneyRequestInfo> offlineRecord;

    // Hold history data get from backend.
    private MutableLiveData<List<JourneyRequestInfo>> listOfHistory;

    private MutableLiveData<Integer> selectedIndex;

    // TODO: need to finalize the result data format
    private MutableLiveData<Map<String, List<UserInfo>>> resultsOfOnlineModel;

    public MutableLiveData<JourneyRequestInfo> getOfflineRecord() {
        return offlineRecord;
    }

    public void setOfflineRecord(JourneyRequestInfo offlineRecord) {
        this.offlineRecord.setValue(offlineRecord);
    }

    public void addRecordToList(JourneyRequestInfo record) {
        List<JourneyRequestInfo> tmpList = listOfHistory.getValue();
        tmpList.add(record);
        listOfHistory.setValue(tmpList);
    }


    public MutableLiveData<List<JourneyRequestInfo>> getListOfHistory() {
        return listOfHistory;
    }

    public void setListOfHistory(List<JourneyRequestInfo> listOfHistory) {
        this.listOfHistory.setValue(listOfHistory);
    }

    public MutableLiveData<Integer> getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(Integer selectedIndex) {
        this.selectedIndex.setValue(selectedIndex);
    }

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
        setFrom("Default");
        to = new MutableLiveData<>();
        setTo("Default");
        isDestination = new MutableLiveData<>();
        setIsDestination(false);
        Time = new MutableLiveData<>();
        setTime("Time");
        Date = new MutableLiveData<>();
        setDate("Date");
        preGenderItemIndexSelected = new MutableLiveData<>();
        setPreGenderItemIndexSelected(0);
        genderPreference = new MutableLiveData<>();
        setGenderPreference("Male");
        preMethodItemIndexSelected = new MutableLiveData<>();
        setPreMethodItemIndexSelected(0);
        methodPreference = new MutableLiveData<>();
        setMethodPreference("Walk");

        // Initialization of data in ProfileFragment.
        names = new MutableLiveData<>();
        setNames("");
        gender = new MutableLiveData<>();
        setGender("Male");
        genderItemIndexSelected = new MutableLiveData<>();
        setGenderItemIndexSelected(0);
        phone = new MutableLiveData<>();
        setPhone("");
        uniqueID = new MutableLiveData<>();

        sender = new MutableLiveData<>();
        setSender(new UserInfo("", names.getValue(), phone.getValue(), to.getValue()));
        // TODO: Need to check if 0 represents Male.
        membersList = new MutableLiveData<>();
        setMembersList(new ArrayList<UserInfo>());

        // Initialization of data in ScheduledJourneyFragment.
        offlineRecord = new MutableLiveData<>();
        listOfHistory = new MutableLiveData<>();
        selectedIndex = new MutableLiveData<>();
        resultsOfOnlineModel = new MutableLiveData<>();
    }
}
