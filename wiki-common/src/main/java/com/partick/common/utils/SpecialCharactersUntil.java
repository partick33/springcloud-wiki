package com.partick.common.utils;

/**
 * 特殊符号处理
 */
@SuppressWarnings("AlibabaSwitchStatement")
public class SpecialCharactersUntil {
    public static String SpecialCharactersHandler(String s) {
        switch (s) {
            case "%":
                s = "\\%";
                break;
            case "_":
                s = "\\_";
                break;
        }
        return s;
    }
}
