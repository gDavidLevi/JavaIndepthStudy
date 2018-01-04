package conspects.nio;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SymbolsStream {
    private static final String PATH = "src/main/java/conspects/nio/";

    public static void main(String[] args) throws Exception {
        /**
         * FileWriter используется для записи потоков символов
         */
        {
            File file = new File(PATH + "1.txt");
            //file.createNewFile(); // не обязательно
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("stream symbols");
            fileWriter.flush();
            fileWriter.close();
        }

        /**
         * FileReader используется для чтения потоков символов
         */
        {
            File file = new File(PATH + "1.txt");
            FileReader fileReader = new FileReader(file);
            int size = (int) file.length();
            char[] chars = new char[size];
            fileReader.read(chars); // читаем в массив
            for (char c : chars)
                System.out.print(c); // вывод посимвольно
            fileReader.close();
        }

        /**
         * Читает символы из потока в буфер, потом из буфера возвращает в поток
         */
        {
            PushbackReader pushbackReader = new PushbackReader(new FileReader(PATH + "1.txt")); // aa-aa--aaa---aaaaaa
            int oneChar;
            while ((oneChar = pushbackReader.read()) != -1) {
                if (oneChar == '-') {
                    int nextChar;
                    if ((nextChar = pushbackReader.read()) == '-') {
                        System.out.print("**");
                    } else {
                        /* Pushes back a single character by copying it to the front of the pushback buffer.
                         * After this method returns, the next character to be read will have the value (char) c.
                         */
                        pushbackReader.unread(nextChar);
                        System.out.print((char) oneChar);
                    }
                } else {
                    System.out.print((char) oneChar);
                }
            }
            //aa-aa**aaa**-aaaaaa
        }

        /**
         * System.out - Вывод на стандартный вывод
         */
        {
            PrintStream printStream = new PrintStream(System.out);
            printStream.println("печать");

            OutputStream outputStream = System.out;
            outputStream.write(65);
        }

        /**
         * PrintStream - поток вывода с autoflush и кодировками
         */
        {
            OutputStream outputStream = new FileOutputStream(PATH + "1.txt");
            PrintStream printStream1 = new PrintStream(outputStream, true, "UTF-8");
            printStream1.write(65);
            printStream1.print(0.25f);
            printStream1.print(12854848465L);
            printStream1.print(12);
            printStream1.print(false);
            printStream1.printf("Цена: %d рублей", 10);
            printStream1.append('S');
            printStream1.append('S');
            printStream1.append('D');
            //printStream1.flush(); // активирован в конструкторе, см. выше
            printStream1.close();
            outputStream.flush();
            outputStream.close();
            // A0.251285484846512falseЦена: 10 рублейSSD
        }

        /**
         * CharArrayWriter(Reader) - массив символов
         */
        {
            CharArrayWriter charArrayWriter = new CharArrayWriter();
            charArrayWriter.write(65); // A
            //
            char[] a = charArrayWriter.toCharArray();
            System.out.println(a[0]); // A
            //
            char[] chars = {'a', 'b', 'c',};
            CharArrayReader charArrayReader = new CharArrayReader(chars);
            int c;
            while ((c = charArrayReader.read()) != -1) {
                System.out.println((char) c);
            }
        }

        /**
         * OutputStreamWriter(Reader) - поток-обертка для чтения/записи файлов в нужной кодировке
         */
        {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(PATH + "1.txt"), StandardCharsets.UTF_8);
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(PATH + "1.txt"), StandardCharsets.US_ASCII);
        }

        /**
         * BufferedReader читает текстовые данные из потока в буфер
         */
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(PATH + "1.txt"));
        }

        /**
         * BufferedWriter пишет из буфера в поток
         */
        {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(PATH + "1.txt"));
            bufferedWriter.write("string");
            bufferedWriter.flush();
            bufferedWriter.close();
        }

        /**
         * PrintWriter - печать в файл разных типов в разных кодировках
         */
        {
            PrintWriter printWriter = new PrintWriter(PATH + "1.txt", "UTF-8");
            printWriter.write(65);
            printWriter.print(0.25f);
            printWriter.print(12854848465L);
            printWriter.print(12);
            printWriter.print(false);
            printWriter.printf("Цена: %d рублей", 10);
            printWriter.append('S');
            printWriter.append('S');
            printWriter.append('D');
            printWriter.flush();
            printWriter.close();
            // A0.251285484846512falseЦена: 10 рублейSSD
        }

        /**
         * Класс StringReader способен преобразовывать обычную строку в считыватель потоков.
         * Для его конструктора требуется входной поток, который он преобразует в stream.
         */
        {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(PATH + "file.txt"));
            String inputLine = null;
            StringBuilder stringBuilder = new StringBuilder();

            /* Заполняем построчно буфер (StringBuilder) */
            while ((inputLine = bufferedReader.readLine()) != null)
                stringBuilder.append(inputLine);

            /* Читаем построчно из буфера stringBuilder в объект stringReader */
            StringReader stringReader = new StringReader(stringBuilder.toString());

            /* Создать токены из объекта stringReader */
            StreamTokenizer streamTokenizer = new StreamTokenizer(stringReader);

            /* Счетчик числа слов */
            int count = 0;
            /* Пока токены (слова) есть в объекте streamTokenizer: */
            while (streamTokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                /* Если тип токена СЛОВО, то увеличить счетчик на 1 */
                if (streamTokenizer.ttype == StreamTokenizer.TT_WORD)
                    ++count;
            }
            System.out.println("Всего слов в файле: " + count);
        }

        /**
         * Класс LineNumberReader представляет собой буферизованный поток ввода символов, который отслеживает
         * номера строк.
         * Строка считается завершенной любым из строки ('\n'), возврата каретки ('\r') или каретки, после чего
         * сразу же возвращается строка.
         */
        {
            LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(PATH + "file.txt"));
            String lineString;
            /* Читать пока не закончится поток */
            while ((lineString = lineNumberReader.readLine()) != null)
                System.out.printf("(%d) %s%n", lineNumberReader.getLineNumber(), lineString);

        }
    }
}