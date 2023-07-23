package com.tangledbytes.collectionsexample;

import javafx.collections.ArrayChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableIntegerArray;

public class ObservableArrayExample {
    public static void main(String[] args) {
        ObservableArrayExample example = new ObservableArrayExample();
        example.showObservableIntegerArrayExample();
    }

    //TODO: Implement methods for showing examples of ObservableFloatArray and other similar interfaces
    private void showObservableIntegerArrayExample() {
        // ObservableArray<ObservableIntegerArray> numbers = FXCollections.observableIntegerArray(11, 0, 2, 32);
        //                                   OR
        // ObservableIntegerArray numbers = FXCollections.observableIntegerArray(11, 0, 2, 32);

        ObservableIntegerArray numbers = FXCollections.observableIntegerArray(11, 0, 2, 32);
        numbers.addListener(new ArrayChangeListener<ObservableIntegerArray>() {
            @Override
            public void onChanged(ObservableIntegerArray observableArray, boolean sizeChanged, int from, int to) {
                if (sizeChanged)
                    System.out.println("Size of the integer arrays changed " + from + " to " + to);
                System.out.println("New Array: " + observableArray);
            }
        });

        numbers.addAll(12, 23, 43); // size changed
        numbers.set(1, 4); // size did not change
    }
}
