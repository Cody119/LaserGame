package com.game.main.engine.util;

import java.util.ArrayList;

/**
 * Created by AND0053 on 2/06/2016.
 */
public class Lex {

    public static String[] lex(String input) {
        ArrayList<String> tmp = new ArrayList<>();
        int flag = -1;
        boolean string = false;

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '\"') {
                if (flag != -1) {
                    tmp.add(input.substring(flag, i));
                    flag = string ? -1 : i + 1;
                    string = !string;
                } else {
                    string = true;
                    flag = i + 1;
                }
            } else if (input.charAt(i) == ' ') {
                if (flag != -1 && !string) {
                    tmp.add(input.substring(flag, i));
                    flag = -1;
                }
            } else {
                if (flag == -1) {
                    flag = i;
                }
            }
        }
        if (flag != -1 && flag != input.length()) {
            tmp.add(input.substring(flag));
        }

        String[] ret = new String[tmp.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = tmp.get(i);
        }
        return ret;
    }

}
