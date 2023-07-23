import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Set does not allow duplicate values*/
public class SetExample {
    public static void main(String[] args) {
        SetExample example = new SetExample();
        example.showExample();
    }

    private void showExample() {
        Set<Integer> rollNos =new HashSet<>();
        System.out.println("adding 1: " + rollNos.add(1));
        System.out.println("adding 12: " + rollNos.add(12));
        System.out.println("adding 3: " + rollNos.add(3));
        System.out.println("adding 6: " + rollNos.add(6));
        System.out.println("adding 7: " + rollNos.add(7));
        System.out.println("adding 1: " + rollNos.add(1));
        System.out.println("adding 3: " + rollNos.add(3));
        System.out.println("adding 6: " + rollNos.add(6));
        System.out.println("adding 12: " + rollNos.add(12));
        rollNos.forEach(System.out::println); // look at the output, it is sorted. you can use tree set to preserve order
    }
}
