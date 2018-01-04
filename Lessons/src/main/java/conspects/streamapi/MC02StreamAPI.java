package conspects.streamapi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * StreamAPI
 */
public class MC02StreamAPI {
    public static void main(String[] args) throws IOException {
        // Ящик с фруктами
        ArrayList<Fruit> fruitsBox = new ArrayList<>(Arrays.asList(
                new Fruit(1, FruitType.ORANGE, 6),
                new Fruit(2, FruitType.APPLE, 4),
                new Fruit(3, FruitType.CHERRY, 2),
                new Fruit(4, FruitType.ORANGE, 13),
                new Fruit(5, FruitType.APPLE, 4),
                new Fruit(6, FruitType.ORANGE, 10)
        ));

// -================================================================================-
// До использования StreamAPI
// -================================================================================-
        System.out.println("Из ящика с фруктами выбрать ORANGE, отсортировать от большего к меньшему, получить список id-шников:");
        // Добавим с список oranges только ORANGE
        ArrayList<Fruit> oranges = new ArrayList<>();
        for (Fruit fruit : fruitsBox)
            if (fruit.getFruitType() == FruitType.ORANGE)
                oranges.add(fruit);

        // Отсортируем список oranges по весам
        oranges.sort(new Comparator<Fruit>() {
            @Override
            public int compare(Fruit o1, Fruit o2) {
                return o2.getWeight() - o1.getWeight(); // от большего к меньшему
            }
        });

        // В список fruitIDs поместим только идентификаторы (id)
        ArrayList<Integer> fruitIDs = new ArrayList<>();
        for (Fruit orange : oranges)
            fruitIDs.add(orange.getId());

        System.out.println("- без StreamAPI: " + fruitIDs);

        // StreamAPI
        List<Integer> result = fruitsBox.stream()
                .filter(fruit -> fruit.getFruitType() == FruitType.ORANGE)  // только ORANGE
                .sorted((o1, o2) -> o2.getWeight() - o1.getWeight())  // от большего к меньшему
                .map(Fruit::getId)  // получить новый список, но уже из id-фруктов
                .collect(Collectors.toList());  // вернут список Integer'ов
        System.out.println("- используя StreamAPI: " + result);

// -================================================================================-
// Примеры:
// -================================================================================-

        // Пример 1
        List<Integer> data = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        List<Integer> out = data.stream()
                .filter(integer -> integer % 2 == 0)  // только четные
                .map(integer -> integer * integer)  // получить новый список, но уже квадратов чисел
                .collect(Collectors.toList());  // вернуть список Integer'ов
        System.out.println("\n1. Список из четных чисел возведенных в квадрат: " + out);

        // Пример 2
        List<String> data2 = Arrays.asList("22", "55", "123");
        List<Integer> out2 = data2.stream()
                .map(s -> Integer.valueOf(s)) // преобразовать из String в Integer
                .collect(Collectors.toList());  // вернуть список Integer'ов
        System.out.println("2. Преобразование из String в Integer: " + out2);

        // Пример 3
        List<Integer> data3 = Arrays.asList(5, 6, 7, 8, 9, 3);
        boolean result1 = data3.stream()
                // выбрать всё по условию и вернуть boolean результат
                .allMatch(integer -> integer > 2); // если число > 2
        // выбрать любой по условию и вернуть boolean результат
        //.anyMatch(integer -> integer > 2);
        System.out.println("3. Все значения больше 2?: " + result1);

        // Пример 4
        Fruit result2 = fruitsBox.stream()
                .filter(fruit -> fruit.getFruitType() == FruitType.ORANGE)
                // найти любой
                .findAny()
                // иначе null
                .orElse(null);
        // найти первое вхождение
        //.findFirst();
        System.out.println("4. Найти любой ORANGE: " + result2);

        // Пример 5
        List<Integer> fruitWeights = fruitsBox.stream()
                .map(Fruit::getWeight)  // получить список весов фруктов
                .collect(Collectors.toList()); // вернуть список весов
        System.out.println("5. Список из весов каждого фрукта в ящике: " + fruitWeights);

        // Пример 6
        String text = "A B C D A A M X X M F D";
        List<String> out3 = Arrays.stream(text
                .split("\\s"))  // разделитель "пробел"
                .distinct() // только уникальные элементы
                .sorted() // отсортировать элементы
                .collect(Collectors.toList()); // вернуть список строк
        System.out.println("6. Преобразование текста в поток, затем отсортировать его и получить список строк: " + out3);

        // Пример 7
        List<String> words = Arrays.asList("Java", "Hello", "World", "Geekbrains", "Street");
        List<Integer> wordsLength = words.stream()
                .map(String::length)  // получить новый список, но уже длин (Integer) слов
                .sorted()  // отсортировать
                .collect(Collectors.toList());  // вернуть список длин (Integer'ов)
        System.out.println("7. Преобразование списка строк в поток, затем получить длинну строк и вернуть список длин: " + wordsLength);

        // Пример 8
        int[] numbers = {1, 2, 3, 4};
        int sum = 0;
        for (int number : numbers) sum += number;
        // Сократим выражение выше с помощью StreamAPI
        int result3 = Arrays.stream(numbers)
                //.reduce(0, Integer::sum);  // сложить элементы массива; 0 - начальное значение
                .reduce(0, ((left, right) -> left + right));  // сложить элементы массива; 0 - начальное значение
        System.out.println("8. Сумма элементов массива: " + result3);
        // Альтернативное решение
        List<Integer> dex = Arrays.asList(1, 2, 3, 4);
        int result4 = dex.stream()
                .mapToInt(n -> n) // возвращает Integer-поток
                .sum(); // возвращает сумму элементов в потоке; аналог: reduce(0, Integer::sum)
        System.out.println("Сумма элементов массива (другой способ): " + result4);

        // Пример 9
        List<Integer> data4 = IntStream // нужен поток Integer
                .rangeClosed(10, 30)  // сгенерировать диапазон [10,30]
                .boxed()  // сформировать поток из диапазона
                .collect(Collectors.toList());  // вернуть список Integer'ов
        System.out.println("9. Сгенерировать диапазон значений [10,30]: " + data4);

        // Пример 10
        List<String> data5 = Arrays.asList("A", "B", "C");
        System.out.print("10. Перебрать каждый элемент списка строк: ");
        data5.stream()
                .forEach(System.out::print);
        System.out.println();

        // Пример 11
        long linesInFile = Files
                .lines(Paths.get("server_settings.json")) // строки из файла
                .count();  // посчитать
        System.out.println("11. Количество строк в файле: " + linesInFile);

        // Пример 12
        System.out.println("12. Уникальные слова в файле: " + linesInFile);
        Files
                .lines(Paths.get("server_settings.json"))  // строки из файла
                .map(s -> s.split("\\s"))  // создать новый поток из строк, но разделитель будет "пробел"
                .flatMap(Arrays::stream)  // (создать из одного элемента несколько) => создать из массива строковый поток
                .distinct()  // только уникальные элементы
                .forEach(System.out::println);  // каждый элемент вывести в консоль

        // Пример 13
        System.out.println("13. Уникальные слова в файле:");
        Stream
                .iterate(1, integer -> 2 * integer)
                .limit(5)
                .forEach(System.out::println);  // каждый элемент вывести в консоль


        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<String> stringList = Arrays.asList("a1a", "b2b", "c3c", "d4d", "e5a");

        // Пример 13 (Java 9)
        System.out.println("13. Игнорировать в потоке числа < 3:");
        integerList.stream()
                .dropWhile(integer -> integer < 3) // пропускать пока условие выполняется
                .forEach(System.out::println);  // каждый элемент вывести в консоль

        // Пример 14 (Java 9)
        System.out.println("14. Выводит числа < 3: ");
        integerList.stream()
                .takeWhile(integer -> integer < 3)  // выводить пока условие выполняется
                .forEach(System.out::println);  // каждый элемент вывести в консоль

        // Пример 15
        System.out.println("15. Выводит 2^1, 2^2, 2^3, 2^4, 2^5: ");
        Stream
                .iterate(1, integer -> 2 * integer) // возводить 2 в степень n начиная с 1
                .limit(5) // до 5 включительно
                .forEach(System.out::println);  // каждый элемент вывести в консоль


        // Пример 16
        System.out.print("16. Найти в списке слова начинающиеся на 'с', изменить регистр: ");
        stringList.stream()
                .filter(s -> s.startsWith("c")) // отобрать начинающиеся на "с"
                .map(String::toUpperCase) // перевести в верхний регистр
                .forEach(System.out::println); // вывести в консоль

        // Пример 17
        System.out.print("17. Показать первый в списке элемент: ");
        stringList.stream()
                .findFirst()
                .ifPresent(System.out::println);

        // Пример 18
        System.out.println("18. Вывести в консоль диапазон [1,8): ");
        IntStream
                .range(1, 8)
                .forEach(System.out::println);

        // Пример 19
        System.out.print("19. Вывести среднее значение вычисленное по формуле 2*n+1 для int-списка: ");
        integerList.stream()
                .mapToInt(n -> 2 * n + 1)
                .average()
                .ifPresent(System.out::println);

        // Пример 20
        System.out.print("20. Отпарсим из строк цифры, увеличим на 1, возьмем максимум: ");
        stringList.stream()
                .map(s -> s.substring(1, 2)) // выделим из строки только цифры
                .mapToInt(Integer::parseInt) // преобразуем текст в Integer
                .map(x -> x + 1) // увеличим на единицу
                .max() // максимальное число
                .ifPresent(System.out::println); // выведем в консоль

        // Пример 21
        System.out.println("21. Преобразовать int-поток в String-поток: ");
        IntStream
                .range(1, 9)  // [1,9)
                .mapToObj(s -> "Num: " + s)  // преобразовать int-поток в поток объектов
                .forEach(System.out::println);  // вывод каждого

        // Пример 22
        System.out.println("22. Порядок обработки (filter-map-forEach): ");
        stringList.stream()
                .filter(s -> s.endsWith("a"))  // заканчивается на 'a'
                .map(m -> m.substring(1, 2))  // извлечь второй символ
                .mapToInt(Integer::parseInt)  // преобразовать символ в Integer
                .forEach(System.out::println);  // вывод каждого

        // Пример 23
        boolean result5 = stringList.stream()
                .filter(s -> s.endsWith("z")) // отфиртровать наканчивающиеся на 'z'
                .anyMatch(s -> true);  // любое вхождение
        System.out.println("23. Есть ли в списке строка заканчиваеющаяся на 'z'?: " + result5);

        // Пример 24
        System.out.println("24. parallelStream: ");
        integerList.parallelStream()
                .filter(integer -> integer > 2)
                .map(integer -> "Value: " + integer)
                .map(s -> s.substring(7, 8))
                .mapToInt(Integer::parseInt)
                .filter(i -> i < 9)
                .forEach(System.out::println);
    }
}

/**
 * Класс Fruit
 */
class Fruit {
    private FruitType fruitType;
    private int weight;
    private int id;

    Fruit(int id, FruitType fruitType, int weight) {
        this.id = id;
        this.fruitType = fruitType;
        this.weight = weight;
    }

    public FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Fruit{" + "fruitType=" + fruitType + ", weight=" + weight + ", id=" + id + '}';
    }
}

/**
 * Типы фруктов
 */
enum FruitType {
    APPLE,
    ORANGE,
    CHERRY;
}