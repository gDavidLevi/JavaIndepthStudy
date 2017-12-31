package conspects.patterns.mvc;

public class MainClass {
    public static void main(String[] args) {
        /* Модель */
        StudentModel model = new StudentModel();
        model.setName("Sarah");
        model.setNumber("10");

        /* Представление */
        StudentView view = new StudentView();

        /* Контроллер */
        StudentController controller = new StudentController(model, view);
        controller.updateView();

        /* внесем изменения */
        controller.setName("David");
        controller.setNumber("80");

        controller.updateView();
    }
}
