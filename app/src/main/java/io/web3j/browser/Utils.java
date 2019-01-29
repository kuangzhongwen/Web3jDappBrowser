package io.web3j.browser;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils.
 *
 * @author kuang on 2018/01/08.
 */
public class Utils {

    private Utils() {
    }

    /**
     * Splits a String based on a single character, which is usually faster than regex-based
     * String.split().
     */
    static String[] fastSplit(String string, char delimiter) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        List<String> list = new ArrayList<>();
        int size = string.length();
        int start = 0;

        for (int i = 0; i < size; i++) {
            if (string.charAt(i) == delimiter) {
                if (start < i) {
                    // substring在jdk 1.7后已解决了内存泄漏问题
                    list.add(string.substring(start, i));
                } else {
                    list.add("");
                }
                start = i + 1;
            } else if (i == size - 1) {
                list.add(string.substring(start, size));
            }
        }

        String[] elements = new String[list.size()];
        list.toArray(elements);
        return elements;
    }
}
