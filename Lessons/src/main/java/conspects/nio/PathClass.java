package conspects.nio;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathClass {
    public static void main(String[] args) throws IOException {
        /**
         * Пути nix и Windows, имя файла, тип файловой системы
         */
        {
            /* Cоздание объекта Path через вызов статического метода get() класса Paths в Linux, Mac */
            Path testFilePathLinux = Paths.get("/home/david/file.txt");

            /* Пример строки создания объекта Path пути для запуска в Windows */
            Path testFilePathWindows = Paths.get("C:\\Users\\d.levi\\file.txt");

            /* Вывод информации о файле */
            System.out.println("Информация о файле: ");
            System.out.println(" имя файла: " + testFilePathWindows.getFileName()); // file.txt
            System.out.println(" корневой каталог: " + testFilePathWindows.getRoot()); // C:\
            System.out.println(" родительский каталог: " + testFilePathWindows.getParent()); // C:\Users\d.levi
            System.out.println(" тип файловой системы: " + testFilePathWindows.getFileSystem().getClass().getSimpleName()); // WindowsFileSystem

            /* Вывод элементов пути */
            System.out.print("Вывод элементов пути: ");
            for (Path element : testFilePathWindows)
                System.out.print(element + "|"); // Users|d.levi|file.txt|
            System.out.println();
        }

        /**
         * Получение абсолютного пути от относительного пути. Нормализацию пути.
         */
        {
            /* Относительное местоположение */
            System.out.println("\n\nТекушее местоположение: " + Paths.get(".\\").toAbsolutePath()); // C:\Users\d.levi\IdeaProjects\JavaCoreProfessionalLevel\.

            /* Относительный путь для запуска в Linux и Mac */
            //Path pathLinux = Paths.get("./file.txt"); // Файл ./file.txt отноcительно текущего каталога

            /* Относительный путь для запуска в Windows */
            Path pathWindows = Paths.get(".\\file.txt");  // Файл .\\file.txt отноcительно текущего каталога

            /* file.txt */
            System.out.println(" имя файла: " + pathWindows.getFileName());

            /* file:///C:/Users/d.levi/IdeaProjects/JavaCoreProfessionalLevel/./file.txt */
            System.out.println(" URI-ссылка: " + pathWindows.toUri());

            /* file.txt */
            System.out.println(" нормализованный путь: " + pathWindows.normalize());

            /* C:\Users\d.levi\IdeaProjects\JavaCoreProfessionalLevel\.\file.txt */
            System.out.println(" абсолютный путь: " + pathWindows.toAbsolutePath());

            /* C:\Users\d.levi\IdeaProjects\JavaCoreProfessionalLevel\file.txt */
            System.out.println(" абсолютный нормализованный путь: " + pathWindows.normalize().toAbsolutePath());
        }
    }
}