package net.peachjean.tater.utils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@SuppressWarnings("UnusedDeclaration")
public class ImplementationHelper {

    private static final ToStringStyle TO_STRING_STYLE = new ToStringStyle() {
        /** Serialization version */
        private static final long serialVersionUID = 1L;

        {
            setDefaultFullDetail(true);
            setArrayContentDetail(true);
            setUseClassName(true);
            setUseShortClassName(true);
            setUseIdentityHashCode(false);
            setContentStart("(");
            setContentEnd(")");
            setFieldSeparator(", ");
            setArrayStart("[");
            setArrayEnd("]");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String getShortClassName(java.lang.Class<?> cls) {
            return "@" + cls.getName();
        }
    };

    public static ToStringBuilder toStringBuilder(Annotation a)
    {
        return new ToStringBuilder(a, TO_STRING_STYLE);
    }

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

    public static int hashCode(Class<?> member)
    {
        return member.hashCode();
    }

    public static <E extends Enum<E>> int hashCode(E member)
    {
        return member.hashCode();
    }

    public static int hashCode(String member)
    {
        return member.hashCode();
    }

    public static int hashCode(Annotation member)
    {
        return member.hashCode();
    }

    public static int hashCode(Boolean member)
    {
        return member.hashCode();
    }

    public static int hashCode(Byte member)
    {
        return member.hashCode();
    }

    public static int hashCode(Character member)
    {
        return member.hashCode();
    }

    public static int hashCode(Double member)
    {
        return member.hashCode();
    }

    public static int hashCode(Float member)
    {
        return member.hashCode();
    }

    public static int hashCode(Integer member)
    {
        return member.hashCode();
    }

    public static int hashCode(Long member)
    {
        return member.hashCode();
    }

    public static int hashCode(Short member)
    {
        return member.hashCode();
    }


    public static int hashCode(Class<?>[] member)
    {
        return Arrays.hashCode(member);
    }

    public static <E extends Enum<E>> int hashCode(E[] member)
    {
        return Arrays.hashCode(member);
    }

    public static int hashCode(String[] member)
    {
        return Arrays.hashCode(member);
    }

    public static int hashCode(Annotation[] member)
    {
        return Arrays.hashCode(member);
    }

    public static int hashCode(boolean[] member)
    {
        return Arrays.hashCode(member);
    }

    public static int hashCode(byte[] member)
    {
        return Arrays.hashCode(member);
    }

    public static int hashCode(char[] member)
    {
        return Arrays.hashCode(member);
    }

    public static int hashCode(double[] member)
    {
        return Arrays.hashCode(member);
    }

    public static int hashCode(float[] member)
    {
        return Arrays.hashCode(member);
    }

    public static int hashCode(int[] member)
    {
        return Arrays.hashCode(member);
    }

    public static int hashCode(long[] member)
    {
        return Arrays.hashCode(member);
    }

    public static int hashCode(short[] member)
    {
        return Arrays.hashCode(member);
    }
}
