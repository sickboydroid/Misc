import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Collection is at the top of collection api hierarchy. It is a type of
 * array. for example:
 * <code>Collection collection = new ArrayList(); </code>
 * Collection is an interface, its object acts the same way as you expect an
 * expandable array (mutable array) </br>
 * <p>
 * Collection also extends @Iterable, so you can use it in foreach loop as well
 * NOTE: Iterable interface is not part of Collection api </br>
 * <p>
 * Collection does not work with indices
 * Values from Collection can fetch in three ways: a) using iterator b) using enhanced for loop c) using foreach
 */
public class CollectionsExample {
    public static void main(String[] args) {
        CollectionsExample collectionsExample = new CollectionsExample();
        collectionsExample.showExample();
    }

    public void showExample() {
        Collection<String> collectionOfStrings = new ArrayList<>();
        collectionOfStrings.add("1st added value");
        collectionOfStrings.add("2nd added value");
        collectionOfStrings.add("3rd added value");

        // First way of fetching values
        for (var String : collectionOfStrings) {
            System.out.println("String = " + String);
        }

        // 2nd way of fetching values
        Iterator<String> iterator = collectionOfStrings.iterator();
        while (iterator.hasNext())
            System.out.println(iterator.next());

        // 3rd way of fetching values
        collectionOfStrings.forEach((value) -> System.out.println("value = " + value));

        // You can remove elements as well
        collectionOfStrings.remove("1st added value");
        collectionOfStrings.forEach(System.out::println); // 1st value would be gone
    }
}
