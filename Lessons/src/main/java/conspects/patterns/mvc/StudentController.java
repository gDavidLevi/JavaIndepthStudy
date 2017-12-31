package conspects.patterns.mvc;

/**
 * CONTROLLER (управление ПРЕДСТАВЛЕНИЕм и МОДЕЛЬю)
 */
public class StudentController {
    private StudentModel model;
    private StudentView view;

    public StudentController(StudentModel model, StudentView view) {
        this.model = model;
        this.view = view;
    }

    public void updateView() {
        view.printDetails(model.getName(), model.getNumber());
    }

    public void setName(String name) {
        model.setName(name);
    }

    public String getName() {
        return model.getName();
    }

    public void setNumber(String number) {
        model.setNumber(number);
    }

    public String getNumber() {
        return model.getNumber();
    }
}