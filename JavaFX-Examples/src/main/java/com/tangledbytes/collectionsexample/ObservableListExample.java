package com.tangledbytes.collectionsexample;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * In this example we will take look at ObservableList.
 * It is same as java.collections.List but it can be (you guessed it) observed. That means
 * whenever you add remove, add, replace or do any other operation on the list, a listener will
 * be monitoring those changes.
 * It has very heavy use in javafx so don't miss anything
 * TIP: javafx.collections.FXCollections is ditto copy of java.util.Collections
 * TIP: FXCollections is also used to instantiate Observable classes/interfaces
 */
public class ObservableListExample {
    public static void main(String[] args) {
        ObservableListExample example = new ObservableListExample();
        example.showExample();
    }

    public void showExample() {
        ObservableList<String> countries =
                FXCollections.observableArrayList("India", "Pakistan", "Bangladesh");
        // Listener helps us to track changes
        countries.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                // There can be multiple changes at the time this method is called.
                // So we need to always call (even if only one change is there) change.next() to get to the change
                // change.next() will return false if the current change is the last change
                int i = 0;
                System.out.println("==========================");
                while (change.next()) {
                    System.out.println("Change number: " + ++i);
                    if (change.wasReplaced()) { // some items of list were replaced by new items
                        //NOTE: The items which are removed can be received by change.getRemoved()
                        // The items by which above items were replaced can be received by change.getAddedSubList()
                        //NOTE: change.wasReplaced() is same as change.wasAdded() && change.wasRemoved()
                        for (int j = 0; j < change.getRemoved().size(); j++) {
                            String replacedValue = change.getRemoved().get(j);
                            String replacementValue = change.getAddedSubList().get(j);
                            System.out.println("You replaced " + replacedValue + " by " + replacementValue);
                        }
                    } else if (change.wasAdded()) { // something was added to list
                        for (String addedValue : change.getAddedSubList())
                            System.out.println("You added: " + addedValue);
                    } else if (change.wasRemoved()) { // something was remove from list
                        for (String removedValue : change.getRemoved())
                            System.out.println("You removed: " + removedValue);
                    } else if (change.wasPermutated()) { // permutation/order of list was changed
                        // change.getPermutation(index) returns the new index of element which was previously at
                        // passed argument 'index'
                        ObservableList<?> parentList = change.getList();
                        for (int j = change.getFrom(); j < change.getTo(); j++) {
                            int oldIndex = j;
                            int newIndex = change.getPermutation(j);
                            String item = parentList.get(newIndex).toString();
                            System.out.println("Item " + item + " was moved from index " + oldIndex + " to " + newIndex);
                        }
                    }
                }
            }
        });

        // Make some changes to the list
        countries.add("Nepal"); // add
        countries.addAll("Bhutan", "Russia", "Algeria"); // add multiple items
        countries.add(2, "China"); // add
        countries.remove("Pakistan"); // remove
        countries.set(2, "Finland"); // replace
        FXCollections.sort(countries); // permute (aka change order of items)
        System.out.println(countries);
    }

}
