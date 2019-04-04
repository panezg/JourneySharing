package org.cs7cs3.team7.journeysharing;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.cs7cs3.team7.journeysharing.Models.HTTPResponse;
import org.cs7cs3.team7.journeysharing.Models.JourneyRequest;
import org.cs7cs3.team7.journeysharing.Models.ScheduleRequest;
import org.cs7cs3.team7.journeysharing.database.entity.User;
import org.cs7cs3.team7.journeysharing.httpservice.HTTPClient;
import org.cs7cs3.team7.journeysharing.httpservice.HTTPService;
import org.cs7cs3.team7.journeysharing.repositories.UserRepository;
import org.cs7cs3.team7.wifidirect.CommsManagerFactory;
import org.cs7cs3.team7.wifidirect.ICommsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.cs7cs3.team7.journeysharing.database.entity.User.GENDER_FEMALE;
import static org.cs7cs3.team7.journeysharing.database.entity.User.GENDER_MALE;

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
        Log.d("JINCHI", "Creating MainViewModel");
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
        //offlineRecord = new MutableLiveData<>();
        listOfHistory = new MutableLiveData<>();
        selectedIndex = new MutableLiveData<>();
        resultsOfOnlineModel = new MutableLiveData<>();
        isOnlineModel = new MutableLiveData<>();
        isOnlineModel.setValue(true);
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
//    private MutableLiveData<JourneyRequest> offlineRecord;

    // Hold history data get from backend.
    // journeyID --> journeyRequest
    private MutableLiveData<Map<String ,JourneyRequest>> listOfHistory;

    private MutableLiveData<String> selectedIndex;

    private MutableLiveData<Boolean> isOnlineModel;

    // TODO: need to finalize the result data format
    // journeyID --> matchingResult
    private MutableLiveData<Map<String, List<User>>> resultsOfOnlineModel;

//    public MutableLiveData<JourneyRequest> getOfflineRecord() {
//        return offlineRecord;
//    }

//    public void setOfflineRecord(JourneyRequest offlineRecord) {
//        this.offlineRecord.setValue(offlineRecord);
//    }

//    public void addRecordToList(JourneyRequest record) {
//        List<JourneyRequest> tmpList = listOfHistory.getValue();
//        tmpList.add(record);
//        listOfHistory.setValue(tmpList);
//    }

    public MutableLiveData<Map<String, JourneyRequest>> getListOfHistory() {
        return listOfHistory;
    }

    public void setListOfHistory(Map<String, JourneyRequest> listOfHistory) {
        this.listOfHistory.setValue(listOfHistory);
    }

    public MutableLiveData<String> getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(String selectedIndex) {
        this.selectedIndex.setValue(selectedIndex);
    }

    public MutableLiveData<Map<String, List<User>>> getResultsOfOnlineModel() {
        return resultsOfOnlineModel;
    }

    public void setResultsOfOnlineModel(Map<String, List<User>> resultsOfOnlineModel) {
        this.resultsOfOnlineModel.setValue(resultsOfOnlineModel);
    }

    public MutableLiveData<Boolean> getIsOnlineModel() {
        return isOnlineModel;
    }

    public void setIsOnlineModel(Boolean isOnlineModel) {
        this.isOnlineModel.setValue(isOnlineModel);
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

    /*
      mViewModel.searchOnline(
              fromAddress.getText().toString(),
                        toAddress.getText().toString(),
                        showDate.getText().toString(),
                        showTime.getText().toString(),
    fakeStartLat,
    fakeStartLon,
    fakeEndLat,
    fakeEndLon,
            genderSpinner.getSelectedItem().toString(),
                        methodSpinner.getSelectedItem().toString()
                        );*/
    public void searchOnline(String fromAddress,
                             String toAddress,
                             String showDate,
                             String showTime,
                             String startLat,
                             String startLon,
                             String endLat,
                             String endLon,
                             String gender,
                             String method)  {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground( final Void ... params ) {
                User user = userRepository.getUserSync(userLoginOnLocal);
                HTTPService client = HTTPClient.INSTANCE.getClient();
                ScheduleRequest scheduleRequest = new ScheduleRequest();
                int genderCode = -1;
                int methodCode = -1;
                if (gender.equals("Male")) {
                    genderCode = GENDER_MALE;
                }
                else if (gender.equals("Female"))  {
                    genderCode = GENDER_FEMALE;
                }


                if (method.equals("Walking")) {
                    methodCode = JourneyRequest.METHOD_WALKING;
                }
                else if (gender.equals("Taxi"))  {
                    methodCode = JourneyRequest.METHOD_TAXI;
                }


                scheduleRequest.setCommuteType(methodCode);
                scheduleRequest.setEndPosition(toAddress);
                scheduleRequest.setEndPositionLatitude(endLat);
                scheduleRequest.setEndPositionLongitude(endLon);
                scheduleRequest.setGenderPreference(genderCode);
                scheduleRequest.setStartPosition(fromAddress);
                scheduleRequest.setStartPositionLatitude(startLat);
                scheduleRequest.setStartPositionLongitude(startLon);
                scheduleRequest.setScheduleDateTime(showDate+showTime);
                scheduleRequest.setUserId(user.getId());
                Call<HTTPResponse> res = client.addSchedule(scheduleRequest);
                res.enqueue(new Callback<HTTPResponse>() {
                    @Override
                    public void onResponse(Call<HTTPResponse> call, Response<HTTPResponse> response) {
                        if(response.isSuccessful()){
//                            System.out.println("succ");
                            Log.d("JINCHI", "succ");
                            HTTPResponse httpResponse = response.body();
//                            System.out.println(httpResponse.getStatus());
                            Log.d("JINCHI", httpResponse.getStatus());
                        }else {
                            System.out.println("fail");
                            try {
                                System.out.println(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HTTPResponse> call, Throwable t) {

                    }
                });

                return null;
            }
        }.execute();

//        HTTPService client = HTTPClient.INSTANCE.getClient();
//        ScheduleRequest scheduleRequest = new ScheduleRequest();
//        int genderCode = -1;
//        int methodCode = -1;
//        if (gender.equals("MALE")) {
//            genderCode = GENDER_MALE;
//        }
//        else if (gender.equals("FEMALE"))  {
//            genderCode = GENDER_FEMALE;
//        }
//
//
//        if (method.equals("Walking")) {
//            methodCode = JourneyRequest.METHOD_WALKING;
//        }
//        else if (gender.equals("Taxi"))  {
//            methodCode = JourneyRequest.METHOD_TAXI;
//        }
//
//
//        scheduleRequest.setCommuteType(methodCode);
//        scheduleRequest.setEndPosition(toAddress);
//        scheduleRequest.setEndPositionLatitude(endLat);
//        scheduleRequest.setEndPositionLongitude(endLon);
//        scheduleRequest.setGenderPreference(genderCode);
//        scheduleRequest.setStartPosition(fromAddress);
//        scheduleRequest.setStartPositionLatitude(startLat);
//        scheduleRequest.setStartPositionLongitude(startLon);
//        scheduleRequest.setScheduleDateTime(showDate+showTime);
//        scheduleRequest.setUserId(1);
//        Call<HTTPResponse> res = client.addSchedule(scheduleRequest);
//        res.enqueue(new Callback<HTTPResponse>() {
//            @Override
//            public void onResponse(Call<HTTPResponse> call, Response<HTTPResponse> response) {
//                if(response.isSuccessful()){
//                    System.out.println("succ");
//                    HTTPResponse httpResponse = response.body();
//                    System.out.println(httpResponse.getStatus());
//
//                }else {
//                    System.out.println("fail");
//                    try {
//                        System.out.println(response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<HTTPResponse> call, Throwable t) {
//
//            }
//        });


    }
}
