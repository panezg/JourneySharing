package org.cs7cs3.team7.journeysharing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    /*
    ------------------------------------ ViewModel For OnDemandJourneyFragment -----------------------------------
     */

    //Data for OnDemandJourneyFragment
    private MutableLiveData<String> from;
    private MutableLiveData<String> to;
    private MutableLiveData<Boolean> isDestination;

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

    /*
    ------------------------------------ ViewModel For ProfileFragment -----------------------------------
     */

    //Data for ProfileFragment.
    private MutableLiveData<String> names;
    private MutableLiveData<String> phone;
    private MutableLiveData<Integer> genderItemIndexSelected;

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

        // Initialization of data in ProfileFragment.
        names = new MutableLiveData<String>();
        names.setValue("");
        genderItemIndexSelected = new MutableLiveData<Integer>();
        genderItemIndexSelected.setValue(0);
        phone = new MutableLiveData<String>();
        phone.setValue("");
    }


}
