package conspects.nio.help;

import java.io.*;

public class ObjectsStream {
    private static final String PATH = "src/main/java/conspects/nio/";

    public static void main(String[] args) throws Exception {
        /**
         * Класс для сериализации/десериализации
         */
        class Animal implements Serializable { // implements Serializable - подключить сериализацию к классу Animal
            Animal() {
                int in = 2;
            }
        }

        /**
         * Класс для сериализации/десериализации
         */
        class Ball implements Serializable { // Подключить сериализацию к классу Ball
        }

        /*
        // Интерфейс Externalizable - создание своего протокола сериализации/десериализации
        // http://www.ccfit.nsu.ru/~deviv/courses/oop/java_ser_rus.html
        class Ball1 implements Externalizable { // Используется для оптимизации производительности и безопасности
            @Override
            public void writeExternal(ObjectOutput out) throws IOException {
                // Таким образом писать:
            }

            @Override
            public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
                // Таким образом читать:
                }
            }
        }
        */

        /**
         * Класс Cat у нас относится к классу Animal и будет Serializable
         */
        class Cat extends Animal implements Serializable { // Подключить сериализацию к классу
            private String name;
            //private Ball ball;
            // transient - Не пускать это поле в сериализацию, и при десериализации это поле будет равно null
            private transient Ball ball;

            private Cat(String name, Ball ball) {
                super(); // Родитель должен быть сериализован!
                this.name = name;
                this.ball = ball; // Тип Ball должен бть сериализован!
                System.out.println("/отработал конструктор объекта " + this.name + "/");
            }

            @Override
            public String toString() {
                return this.name;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o)
                    return true;
                if (!(o instanceof Cat))
                    return false;
                Cat obj = (Cat) o;
                return (name != null ? name.equals(obj.name) : obj.name == null);
            }

            @Override
            public int hashCode() {
                return (name != null ? name.hashCode() : 0);
            }
        }

        Ball ball = new Ball(); // Кот до сериализации и после будет играть с одним мячом
        Cat cat = new Cat("Barsique", ball);

        /**
         * Сериализация объекта
         */
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(cat);
            //
            //System.out.println("Набор сериализованных байтов: " + Arrays.toString(byteArrayOutputStream.toByteArray())); // набор сериализованных байтов [-84, -19, 0, 5, 115, 114, 0, 42, 114, 117, 46, 100, 97, 118, 105...
            System.out.println(new String(byteArrayOutputStream.toByteArray(), "UTF-8"));
            //
            objectOutputStream.close();
            byteArrayOutputStream.close();
        }

        { // Сериализация c сохранением объекта в файл
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(PATH + "cat_object.txt"));
            objectOutputStream.writeObject(cat);
        }

        /**
         * Десериализация объекта
         */
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(PATH + "cat_object.txt"));
            Cat cat2 = (Cat) objectInputStream.readObject(); // кастовать обязательно!
            // конструктор при десериализации не запускается!
            objectInputStream.close();
            System.out.println("Объекты равны?: " + cat.equals(cat2));
        }
    }
}