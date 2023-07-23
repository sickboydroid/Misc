import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoubleBraceInitializationExample {
    public static void main(String[] args) {
        DoubleBraceInitializationExample example = new DoubleBraceInitializationExample();
        example.showIIBExample();
        example.showIIBExampleOnList();
    }

    private void showIIBExample() {
        new IIBExample();
    }

    private void showIIBExampleOnList() {
        // List can be intialized in following ways
        List<Integer> l1 = new ArrayList<>();
        l1.add(1);
        l1.add(2);
        l1.add(3);

        List<Integer> l2 = Arrays.asList(1, 2, 3);

        List<Integer> l3 = List.of(1, 2, 3);

        // First brace represents anonymous inner class (you are creating a class that extends ArrayList but has no name)
        // 2nd block is called 'instance initialization block' (IIB) or simply 'initialization block'
        // java compiler copies the IIBs code to constructor. After super() is called, iibs execute
        List<Integer> l4 = new ArrayList<>() {{
            add(1);
            add(2);
            add(3);
        }};

        l1.forEach(System.out::print);
        System.out.println();
        l2.forEach(System.out::print);
        System.out.println();
        l3.forEach(System.out::print);
        System.out.println();
        l4.forEach(System.out::print);
    }

    abstract static class Foo {
        public Foo() {
            System.out.println("Foo() is called");
        }
    }

    static class IIBExample extends Foo {
        {
            System.out.println("IIB-1 is called");
        }

        {
            System.out.println("IIB-2 is called");
        }

        public IIBExample() {
            //super(); // it has to be first line in constructor
            System.out.println("IIBExample() is called");
        }
    }
}
