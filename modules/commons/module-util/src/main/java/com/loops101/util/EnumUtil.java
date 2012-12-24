package com.loops101.util;

public class EnumUtil {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <E extends Enum<?>> E valueOf(Class clazz, String name) {
        return (E) Enum.valueOf(clazz, name);
    }

    public static <E extends Enum<?>> String valueOf(E num) {
        return num == null ? null : num.toString();
    }
}
