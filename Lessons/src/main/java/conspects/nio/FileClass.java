package conspects.nio;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class FileClass {
    private static final String PATH = "src/main/java/conspects/nio/";

    public static void main(String[] args) throws Exception {
        /**
         * Класс File описывает путь к файлу, определяет методы работы с фалами.
         */
        {
            File file = new File(PATH + "1.txt");
            file.length(); // размер в байтах
            file.getName(); // получить имя
            file.isFile(); // это файл?
            file.isDirectory(); // это папка?
            file.deleteOnExit(); // удалить файл после выхода из приложения
            file.getAbsolutePath(); // полный путь файла от корня
            file.getCanonicalPath(); // вычисленный путь из относительного "c://windows//..//distr"
            file.isHidden(); // скрытый?
        }

        /**
         * Класс File описывает путь к папке, определяет методы работы с папками.
         */
        {
            File dir = new File(PATH + "MyNewDir");
            dir.mkdir();
            //dir.mkdirs(); // создаст вложенную структуру если File dir = new File("MyNewDir/1/2/3");
            dir.list(); // листинг каталога в виде String[]
            dir.listFiles(); // листинг только файлов
            dir.delete();
        }

        /**
         * Интерфейс FilenameFilter позволяет фильтровать файлы и папки.
         */
        {
            File dirD = new File(PATH);
            String[] list = dirD.list(new FilenameFilter() { // фильтр имен файлов/папок
                @Override
                public boolean accept(File dir, String name) {
                    return name.matches("[a-zA-Z]+");
                }
            });
            System.out.println(Arrays.toString(list));
        }
    }
}