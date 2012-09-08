package net.peachjean.tater.utils;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@SuppressWarnings("UnusedDeclaration")
public class ImplementationHelper {
    public static boolean isEquals(Class<?> one, Class<?> two)
    {
        return one.equals(two);
    }

    public static <E extends Enum<E>> boolean isEquals(E one, E two)
    {
        return one.equals(two);
    }

    public static boolean isEquals(String one, String two)
    {
        return one.equals(two);
    }

    public static <A extends Annotation> boolean isEquals(A one, A two)
    {
        return one.equals(two);
    }

    public static boolean isEquals(boolean one, boolean two)
    {
        return one == two;
    }

    public static boolean isEquals(byte one, byte two)
    {
        return one == two;
    }

    public static boolean isEquals(char one, char two)
    {
        return one == two;
    }

    public static boolean isEquals(double one, double two)
    {
        return one == two;
    }

    public static boolean isEquals(float one, float two)
    {
        return one == two;
    }

    public static boolean isEquals(int one, int two)
    {
        return one == two;
    }

    public static boolean isEquals(long one, long two)
    {
        return one == two;
    }

    public static boolean isEquals(short one, short two)
    {
        return one == two;
    }

    public static boolean isEquals(Class<?>[] one, Class<?>[] two)
    {
        return Arrays.equals(one, two);
    }

    public static <E extends Enum<E>> boolean isEquals(E[] one, E[] two)
    {
        return Arrays.equals(one, two);
    }

    public static boolean isEquals(String[] one, String[] two)
    {
        return Arrays.equals(one, two);
    }

    public static <A extends Annotation> boolean isEquals(A[] one, A[] two)
    {
        return Arrays.equals(one, two);
    }

    public static boolean isEquals(boolean[] one, boolean[] two)
    {
        return Arrays.equals(one, two);
    }

    public static boolean isEquals(byte[] one, byte[] two)
    {
        return Arrays.equals(one, two);
    }

    public static boolean isEquals(char[] one, char[] two)
    {
        return Arrays.equals(one, two);
    }

    public static boolean isEquals(double[] one, double[] two)
    {
        return Arrays.equals(one, two);
    }

    public static boolean isEquals(float[] one, float[] two)
    {
        return Arrays.equals(one, two);
    }

    public static boolean isEquals(int[] one, int[] two)
    {
        return Arrays.equals(one, two);
    }

    public static boolean isEquals(long[] one, long[] two)
    {
        return Arrays.equals(one, two);
    }

    public static boolean isEquals(short[] one, short[] two)
    {
        return Arrays.equals(one, two);
    }
}
