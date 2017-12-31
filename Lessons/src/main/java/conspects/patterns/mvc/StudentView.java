package conspects.patterns.mvc;

/**
 * VIEW (как именно показывать)
 */
class StudentView {
    void printDetails(String name, String number) {
        System.out.println("Student: ");
        System.out.println(" name: " + name);
        System.out.println(" number: " + number);
    }
}
