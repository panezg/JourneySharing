package org.cs7cs3.team7.journeysharing;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.cs7cs3.team7.journeysharing.Models.JourneyRequest;
import org.cs7cs3.team7.journeysharing.database.entity.User;
import org.cs7cs3.team7.journeysharing.repositories.UserRepository;
import org.cs7cs3.team7.wifidirect.CommsManagerFactory;
import org.cs7cs3.team7.wifidirect.ICommsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    /*
    ------------------------------------ ViewModel For OnDemandJourneyFragment -----------------------------------
     */

    private Activity mainActivity;
    private SharedPreferences sharedPreferences;
    private ICommsManager commsManager;
    private UserRepository userRepository;
    private String userLoginOnLocal;

    //Data for OnDemandJourneyFragment
    private MutableLiveData<String> from;
    private MutableLiveData<String> to;
    private MutableLiveData<Boolean> isDestination;

    private MutableLiveData<User> sender;
    private MutableLiveData<String> Time;
    private MutableLiveData<String> Date;


    private MutableLiveData<Double>Longtitute;
    private MutableLiveData<Double>Latitude;
    // Only for P2P part.
    private MutableLiveData<List<User>> membersList;

    // Preferences
    private MutableLiveData<String> genderPreference;
    private MutableLiveData<Integer> preGenderItemIndexSelected;
    private MutableLiveData<Integer> preMethodItemIndexSelected;
    private MutableLiveData<String> methodPreference;

    // --- Constructor

    @Inject
    public MainViewModel(UserRepository userRepository) {
        Log.d("JINCHI", "Creating ProfileViewModel");
        this.userRepository = userRepository;
        this.userLoginOnLocal = userRepository.recoverUserLogin();
        this.commsManager = CommsManagerFactory.getCommsManager(App.context, userLoginOnLocal);
        this.init();
    }

    // --- Initialization

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
        Latitude=new MutableLiveData<>();
        setLatitude(0.0);
        Longtitute=new MutableLiveData<>();
        setLongtitute(0.0);


        sender = new MutableLiveData<>();
        // TODO: Need to check if 0 represents Male.
        membersList = new MutableLiveData<>();
        setMembersList(new ArrayList<User>());

        // Initialization of data in ScheduledJourneyFragment.
        offlineRecord = new MutableLiveData<>();
        listOfHistory = new MutableLiveData<>();
        selectedIndex = new MutableLiveData<>();
        resultsOfOnlineModel = new MutableLiveData<>();
    }

    // ---

    public MutableLiveData<User> getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender.setValue(sender);
    }

    public MutableLiveData<List<User>> getMembersList() {
        return membersList;
    }

    public void setMembersList(List<User> membersList) {
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


    public void setLongtitute(Double longtitute) { Longtitute.setValue(longtitute); }
    public MutableLiveData<Double> getLongtitute() { return Longtitute; }

    public MutableLiveData<Double> getLatitude() { return Latitude; }

    public void setLatitude(Double latitude) { Latitude.setValue(latitude); }

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
    private MutableLiveData<JourneyRequest> offlineRecord;

    // Hold history data get from backend.
    private MutableLiveData<List<JourneyRequest>> listOfHistory;

    private MutableLiveData<Integer> selectedIndex;

    // TODO: need to finalize the result data format
    private MutableLiveData<Map<String, List<User>>> resultsOfOnlineModel;

    public MutableLiveData<JourneyRequest> getOfflineRecord() {
        return offlineRecord;
    }

    public void setOfflineRecord(JourneyRequest offlineRecord) {
        this.offlineRecord.setValue(offlineRecord);
    }

    public void addRecordToList(JourneyRequest record) {
        List<JourneyRequest> tmpList = listOfHistory.getValue();
        tmpList.add(record);
        listOfHistory.setValue(tmpList);
    }


    public MutableLiveData<List<JourneyRequest>> getListOfHistory() {
        return listOfHistory;
    }

    public void setListOfHistory(List<JourneyRequest> listOfHistory) {
        this.listOfHistory.setValue(listOfHistory);
    }

    public MutableLiveData<Integer> getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(Integer selectedIndex) {
        this.selectedIndex.setValue(selectedIndex);
    }

    /*
    ----------------------------------- User Actions --------------------------------------
     */

    public void search(String genderPref, String methodPref, String destination, boolean isRealTime) {
        Log.d("JINCHI", "Search: " + userLoginOnLocal);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground( final Void ... params ) {
                User user = userRepository.getUserSync(userLoginOnLocal);
                Log.d("JINCHI", "Search: " + (user == null));
                JourneyRequest journeyRequest = new JourneyRequest(user, genderPref, methodPref, destination, isRealTime);
                journeyRequest.setState(JourneyRequest.JourneyRequestStatus.PENDING);
                commsManager.requestJourneyMatch(journeyRequest);
                // this line breaks this because it updates UI
                // setOfflineRecord(journeyRequest);

                return null;
            }
        }.execute();

        Log.d("JINCHI", "OnDemandJourneyFragment: After calling requestJourneyMatch()");
        Toast.makeText(App.context, "Request Sent! Waiting for matching...", Toast.LENGTH_SHORT).show();
    }

    /*
    ---------------------------------- UI Framework Actions ------------------------------
     */
    public void resume() {
        commsManager.onResume();
    }

    public void pause() {
        commsManager.onPause();
    }

    public void stop() {
        commsManager.onStop();
    }

    public void destroy() {
        commsManager.onDestroy();
        //commsManager.clean();
    }
}
