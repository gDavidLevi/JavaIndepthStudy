package conspects.patterns.builder;

public class MainClass {
    public static void main(String[] args){
        // вариант 1. Построитель отдельно от класса Person
        PersonBuilder personBuilder = new PersonBuilder();
        Person person1 = personBuilder.setName("Rick").setEmail("fsgewthr@edf.ru").createPerson();

        // вариант 2. Построитель встроен в класс Person
        Person.Builder pBuilder = new Person.Builder();
        Person person2 = pBuilder.setName("Mory").setEmail("test@java.com").createPerson();
    }
}
