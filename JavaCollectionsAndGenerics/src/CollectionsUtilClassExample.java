import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CollectionsUtilClassExample {
    public static void main(String[] args) {
        CollectionsUtilClassExample example = new CollectionsUtilClassExample();
        example.showExamples();
        example.showCustomSortExample();
        example.showCustomSortExample2();
    }

    public void showExamples() {
        ArrayList<Integer> luckyNumbers = new ArrayList<>();
        luckyNumbers.add(1);
        luckyNumbers.add(4);
        luckyNumbers.add(3);
        luckyNumbers.add(23);
        luckyNumbers.add(53);
        luckyNumbers.add(263);
        luckyNumbers.add(563);
        luckyNumbers.add(78);
        luckyNumbers.add(12);
        luckyNumbers.add(2);
        luckyNumbers.add(21);

        System.out.println("Raw: " + Arrays.deepToString(luckyNumbers.toArray()));

        // Sorting list
        Collections.sort(luckyNumbers);
        System.out.println("Sorted: " + Arrays.deepToString(luckyNumbers.toArray()));

        // Randomizing list
        Collections.shuffle(luckyNumbers);
        System.out.println("Shuffled: " + Arrays.deepToString(luckyNumbers.toArray()));

    }

    /**
     * Sometimes we need our own way to compare things and this is where Comparable and Comparator
     * comes into play.
     * NOTE: These do not ask you to code your own sorting algo., they just tells
     * the java how to compare two things
     */
    private void showCustomSortExample() {
        Phone realme = new Phone("Realme", 4, "$300");
        Phone mi = new Phone("MI", 2, "$200");
        Phone motorola = new Phone("Motorola", 1, "$100");
        Phone pixel = new Phone("Pixel", 8, "$30");
        Phone samsung = new Phone("Samsung", 8, "$120");
        Phone iphone = new Phone("IPhone", 2, "$300");
        ArrayList<Phone> phones = new ArrayList<>();
        phones.add(realme);
        phones.add(mi);
        phones.add(motorola);
        phones.add(pixel);
        phones.add(samsung);
        phones.add(iphone);

        System.out.println("Phones before sorting: " + Arrays.deepToString(phones.toArray()));
        Collections.sort(phones);
        System.out.println("Phones after sorting: " + Arrays.deepToString(phones.toArray()));
    }

    private void showCustomSortExample2() {
        ArrayList<Student> students = new ArrayList<>(10);
        students.add(new Student("Muskan", 112));
        students.add(new Student("SickBoy", 2));
        students.add(new Student("Junaid", 5));
        students.add(new Student("Salman", 134));
        students.add(new Student("Khadija", 14));
        students.add(new Student("Jannati", 1));
        students.add(new Student("Sakib", 31));

        System.out.println("Students before sorting: " + Arrays.deepToString(students.toArray()));
//        students.sort(Comparator.comparingInt(student -> student.rollNo));
        students.sort((Student student1, Student student2) -> {
            return Integer.compare(student1.rollNo, student2.rollNo);
        });
        System.out.println("Students after sorting: " + Arrays.deepToString(students.toArray()));

    }

    static class Student {
        int rollNo;
        String name;

        public Student(String name, int rollNo) {
            this.name = name;
            this.rollNo = rollNo;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "name=" + name +
                    ", rollNo=" + rollNo +
                    '}';
        }
    }

    static class Phone implements Comparable<Phone> {
        public String mName;
        public int mRam;
        public String mPrice;

        public Phone(String name, int ram, String price) {
            mName = name;
            mRam = ram;
            mPrice = price;
        }

        @Override
        public int compareTo(Phone phone) {
            // phone = phone to be compared with 'this' phone
            int priceOfArg = Integer.parseInt(phone.mPrice.substring(1));
            int priceOfThis = Integer.parseInt(mPrice.substring(1));
            return Integer.compare(priceOfThis, priceOfArg);
        }

        @Override
        public String toString() {
            return mName + " " + mPrice + " " + mRam + "GB";
        }
    }
}
