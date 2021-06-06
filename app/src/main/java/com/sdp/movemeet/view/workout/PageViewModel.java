package com.sdp.movemeet.view.workout;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private final MutableLiveData<Integer> index = new MutableLiveData<>();
    private final LiveData<String> text = Transformations.map(index, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            switch (input) {
                case 1:
                    return "Text-based descriptions of workouts";
                case 2:
                    return "Images of workouts";
                case 3:
                    return "Workouts in PDF format";
                default:
                    return "New workout tab";
            }
        }
    });

    public void setIndex(int index) {
        this.index.setValue(index);
    }

    public LiveData<String> getText() {
        return text;
    }
}