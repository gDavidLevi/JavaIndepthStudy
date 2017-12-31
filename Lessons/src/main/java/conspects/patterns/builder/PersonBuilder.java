package conspects.patterns.builder;

/**
 * Класс PersonBuilder (только конструктор)
 */
public class PersonBuilder {
    private String name;
    private String surname;
    private String middlename;
    private String phone;
    private String email;

    /* Конструктор */
    public PersonBuilder() {
    }

    public PersonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public PersonBuilder setMiddlename(String middlename) {
        this.middlename = middlename;
        return this;
    }

    public PersonBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public PersonBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public Person createPerson() {
        //return new Person(name, surname, middlename, phone, email); //  расскоментировать, если надо для варианта 1
        return null;
    }
}
