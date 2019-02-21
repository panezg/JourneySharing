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
    ------------------------------------ ViewModel For local File -----------------------------------
     */

    public void init() {
        from = new MutableLiveData<>();
        from.setValue("default");
        to = new MutableLiveData<>();
        to.setValue("default");
        isDestination = new MutableLiveData<>();
        isDestination.setValue(false);
    }


}
