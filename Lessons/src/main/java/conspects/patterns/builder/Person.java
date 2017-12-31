package conspects.patterns.builder;

/**
 * Класс Person (со встроенным билдером)
 */
class Person {
    private final String name;
    private final String surname;
    private final String middlename;
    private final String phone;
    private final String email;

    /* Конструктор (private) */
    private Person(String name, String surname, String middlename, String phone, String email) {
        this.name = name;
        this.surname = surname;
        this.middlename = middlename;
        this.phone = phone;
        this.email = email;
    }

    /**
     * Статический класс Builder (построитель)
     */
    public static class Builder {
        private String name;
        private String surname;
        private String middlename;
        private String phone;
        private String email;

        /* Конструктор */
        Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder setMiddlename(String middlename) {
            this.middlename = middlename;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Person createPerson() {
            return new Person(name, surname, middlename, phone, email);
        }
    }
}
