package edu.cuit.lushan.utils;

import java.util.Random;

public class CodeUtil {
    private static final String CHAR_SETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static String createCodeString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char ch = CHAR_SETS.charAt(new Random().nextInt(CHAR_SETS.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
}
