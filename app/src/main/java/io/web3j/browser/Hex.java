package io.web3j.browser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Hex utils.
 *
 * @author kuang on 2018/01/08.
 */
public final class Hex {

    public Hex() {
    }

    public static int hexToInteger(String input, int def) {
        Integer value = hexToInteger(input);
        return value == null ? def : value;
    }

    @Nullable
    public static Integer hexToInteger(String input) {
        try {
            return Integer.decode(input);
        } catch (NumberFormatException var2) {
            return null;
        }
    }

    public static long hexToLong(String input, int def) {
        Long value = hexToLong(input);
        return value == null ? (long) def : value;
    }

    @Nullable
    public static Long hexToLong(String input) {
        try {
            return Long.decode(input);
        } catch (NumberFormatException var2) {
            return null;
        }
    }

    @Nullable
    public static BigInteger hexToBigInteger(String input) {
        if (TextUtils.isEmpty(input)) {
            return null;
        } else {
            try {
                boolean isHex = containsHexPrefix(input);
                if (isHex) {
                    input = cleanHexPrefix(input);
                }

                return new BigInteger(input, isHex ? 16 : 10);
            } catch (NumberFormatException | NullPointerException var2) {
                return null;
            }
        }
    }

    @NonNull
    public static BigInteger hexToBigInteger(String input, BigInteger def) {
        BigInteger value = hexToBigInteger(input);
        return value == null ? def : value;
    }

    @Nullable
    public static BigDecimal hexToBigDecimal(String input) {
        return new BigDecimal(hexToBigInteger(input));
    }

    @NonNull
    public static BigDecimal hexToBigDecimal(String input, BigDecimal def) {
        return new BigDecimal(hexToBigInteger(input, def.toBigInteger()));
    }

    public static boolean containsHexPrefix(String input) {
        return input.length() > 1 && input.charAt(0) == '0' && input.charAt(1) == 'x';
    }

    @Nullable
    public static String cleanHexPrefix(@Nullable String input) {
        if (input != null && containsHexPrefix(input)) {
            input = input.substring(2);
        }

        return input;
    }

    @Nullable
    public static String hexToDecimal(@Nullable String value) {
        BigInteger result = hexToBigInteger(value);
        return result == null ? null : result.toString(10);
    }

    @NonNull
    public static byte[] hexStringToByteArray(@Nullable String input) {
        String cleanInput = cleanHexPrefix(input);
        if (TextUtils.isEmpty(cleanInput)) {
            return new byte[0];
        } else {
            int len = cleanInput.length();
            byte[] data;
            byte startIdx;
            if (len % 2 != 0) {
                data = new byte[len / 2 + 1];
                data[0] = (byte) Character.digit(cleanInput.charAt(0), 16);
                startIdx = 1;
            } else {
                data = new byte[len / 2];
                startIdx = 0;
            }

            for (int i = startIdx; i < len; i += 2) {
                data[(i + 1) / 2] = (byte) ((Character.digit(cleanInput.charAt(i), 16) << 4) + Character.digit(cleanInput.charAt(i + 1), 16));
            }

            return data;
        }
    }

    @NonNull
    public static String byteArrayToHexString(@NonNull byte[] input, int offset, int length, boolean withPrefix) {
        StringBuilder stringBuilder = new StringBuilder();
        if (withPrefix) {
            stringBuilder.append("0x");
        }

        for (int i = offset; i < offset + length; ++i) {
            stringBuilder.append(String.format("%02x", input[i] & 255));
        }

        return stringBuilder.toString();
    }

    @Nullable
    public static String byteArrayToHexString(byte[] input) {
        return input != null && input.length != 0 ? byteArrayToHexString(input, 0, input.length, true) : null;
    }

    public static String decodeMessageData(Message<String> message) {
        if (cleanHexPrefix((String) message.value).length() == 64) {
            return (String) message.value;
        } else {
            return containsHexPrefix((String) message.value) ? new String(hexStringToByteArray((String) message.value)) : (String) message.value;
        }
    }
}
