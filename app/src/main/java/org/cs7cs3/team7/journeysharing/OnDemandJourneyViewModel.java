package org.cs7cs3.team7.journeysharing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OnDemandJourneyViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> from;
    private MutableLiveData<String> to;
    private MutableLiveData<Boolean> isDestination;

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


    public void init() {
        from = new MutableLiveData<>();
        from.setValue("default");
        to = new MutableLiveData<>();
        to.setValue("default");
        isDestination = new MutableLiveData<>();
        isDestination.setValue(false);
    }


}
