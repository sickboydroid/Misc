import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * The problem with collection was that we could not specify where to put values
 * Also Collection cannot be sorted for the same reason
 * Now in case of List we have method add(index, element)
 * </br>
 * Difference b/w ArrayList(arr.) and Vector(vec.)
 *      -> After exceeding limit, arr. increases capacity by 50% while as vec. increases by 100%
 *      -> Vectors are thread safe (theoretically) but not ArrayList
 *      -> As the result of above, Vectors as slow as compared to ArrayList
 * </br>
 * NOTE: Always prefer ArrayList over Vector unless you are doing multithreading
 * </br>
 * Difference b/w ArrayList and LinkedList
 *      -> Both provide same functionality but the difference is in there implementation
 *         ArrayList is based on the concept of Dynamic Array while as LinkedList is
 *         based on Dynamic LinkedList
 *      -> When you add elements at some index or remove element in ArrayList,
 *         everything shifts to fill or create an empty gap. This is time-consuming.
 *      -> LinkedList solves this problem at the cost of index. You cannot have
 *         random access to any element, but you can have a dynamic array.
 *
 *
 */
public class ListExample {

    public static void main(String[] args) {
        ListExample example = new ListExample();
        example.showListExample();
        example.showVectorExample();

    }

    private void showListExample() {
        List<String> countries = new ArrayList<>();
        countries.add("india");
        countries.add("usa");
        countries.add("nepal");
        countries.add("bangladesh");
        countries.add("pakistan");
        countries.add("china");
        countries.add(0, "saudi arabia");
        countries.forEach(System.out::println);
    }

    /**
     * Vector is almost same as ArrayList but not exactly same
     */
    public void showVectorExample() {
        Vector<String> subjects = new Vector<>(5); // default initialCapacity=10
        System.out.println("0 elements subjects.capacity() = " + subjects.capacity());
        subjects.add("math1");
        subjects.add("math2");
        subjects.add("math3");
        subjects.add("math4");
        System.out.println("4 elements added subjects.capacity() = " + subjects.capacity());
        subjects.add("math5");
        System.out.println("5 elements added subjects.capacity() = " + subjects.capacity());
        subjects.add("math6");
        System.out.println("6 elements added subjects.capacity() = " + subjects.capacity());
        subjects.add("math7");
        subjects.add("math8");
        subjects.add("math9");
        subjects.add("math10");
        System.out.println("10 elements added subjects.capacity() = " + subjects.capacity());
        subjects.add("math11");
        subjects.add("math13");
        subjects.add("math14");
        subjects.add("math15");
        subjects.add("math16");
        System.out.println("16 elements added subjects.capacity() = " + subjects.capacity());
        subjects.add("math17");
        subjects.add("math18");
        subjects.add("math19");
        subjects.add("math20");
        subjects.add("math21");
        System.out.println("21 elements added subjects.capacity() = " + subjects.capacity());
        subjects.add("math22");
        subjects.add("math23");
        subjects.add("math24");
    }
}
