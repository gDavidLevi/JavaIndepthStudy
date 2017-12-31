package ru.davidlevi.jis.common.notuse;

import java.io.StreamTokenizer;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Для шифрования пароля и хранении хэша в БД
 */
public class Cryptonit {
    public static void main(String arg[]) {
        try {
            StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("your secret password"));
            tokenizer.nextToken();
            /**/
            String attemptOne = byteArrayToHashString(Cryptonit.computeHash(tokenizer.sval));
            System.out.println("Хэш" + attemptOne);
            /**/
            boolean isRepeat = true;
            String attemptTwo;
            while (isRepeat) {
                System.out.print("Повторите ввод пароля: ");
                tokenizer.nextToken();
                attemptTwo = byteArrayToHashString(Cryptonit.computeHash(tokenizer.sval));
                if (attemptOne.equals(attemptTwo)) {
                    System.out.println("Пароли совпадают!");
                    isRepeat = false;
                } else System.out.println("Ошибка! Повторите ввод.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Вычисляет hash */
    private static byte[] computeHash(String x) throws NoSuchAlgorithmException {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(x.getBytes());
        return messageDigest.digest();
    }

    /* Шифрует массив байт */
    private static String byteArrayToHashString(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            int v = aByte & 0xff;
            if (v < 16) stringBuffer.append('0');
            stringBuffer.append(Integer.toHexString(v));
        }
        return stringBuffer.toString().toUpperCase();
    }
}
