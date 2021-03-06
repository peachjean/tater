package net.peachjean.tater.utils;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.UnmodifiableMap;
import org.apache.commons.lang3.AnnotationUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class AnnotationInvocationHandler implements InvocationHandler, Serializable {

    @SuppressWarnings("UnusedDeclaration") // this method is used by generated classes
    public static <A extends Annotation> A implement(Class<A> annotationType, Map<String, Object> memberValues) {
        UnmodifiableMap<String, Object> unmodifiableMap = (UnmodifiableMap<String, Object>) (memberValues instanceof UnmodifiableMap
                        ? memberValues
                        : MapUtils.unmodifiableMap(memberValues));
        return (A) Proxy.newProxyInstance(annotationType.getClassLoader(),
                new Class[]{annotationType},
                new AnnotationInvocationHandler(annotationType, unmodifiableMap));
    }

    public static <A extends Annotation> AnnotationBuilder<A> implement(final Class<A> annotationType) {
        final Map<String, Object> memberValueMap = new HashMap<String, Object>();
        return new AnnotationBuilder<A>() {
            @Override
            public A build() {
                return AnnotationInvocationHandler.implement(annotationType, MapUtils.unmodifiableMap(memberValueMap));
            }

            @Override
            public AnnotationBuilder<A> withMemberValue(String name, Object value) {
                memberValueMap.put(name, value);
                return this;
            }
        };
    }

    public static interface AnnotationBuilder<A extends Annotation> {
        A build();
        AnnotationBuilder<A> withMemberValue(String name, Object value);
    }

    private final Class<? extends Annotation> type;
    private final UnmodifiableMap<String, Object> memberValues;

    private AnnotationInvocationHandler(Class<? extends Annotation> type, UnmodifiableMap<String, Object> memberValues) {
        this.type = type;
        this.memberValues = memberValues;
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        String member = method.getName();
        Class<?>[] paramTypes = method.getParameterTypes();

        // Handle Object and Annotation methods
        if (member.equals("equals") && paramTypes.length == 1 && paramTypes[0] == Object.class) {
            if (!type.isInstance(args[0])) {
                return false;
            }

            return AnnotationUtils.equals((Annotation) proxy, (Annotation) args[0]);
        } else if(paramTypes.length == 0) {
            if (member.equals("toString")) {
                return AnnotationUtils.toString((Annotation) proxy);
            } else if (member.equals("hashCode")) {
                return AnnotationUtils.hashCode((Annotation) proxy);
            } else if (member.equals("annotationType")) {
                return type;
            } else {
                Object result = memberValues.get(member);

                if (result == null) {
                    return method.getDefaultValue();
                }

                if (result.getClass().isArray() && Array.getLength(result) != 0) {
                    result = cloneArray(result);
                }

                return result;
            }
        } else {
            throw new IllegalStateException("No implementation for method " + method);
        }
    }

    /**
     * This method, which clones its array argument, would not be necessary
     * if Cloneable had a public clone method.
     */
    private Object cloneArray(Object array) {
        Class<?> type = array.getClass();

        if (type == byte[].class) {
            byte[] byteArray = (byte[]) array;
            return byteArray.clone();
        }
        if (type == char[].class) {
            char[] charArray = (char[]) array;
            return charArray.clone();
        }
        if (type == double[].class) {
            double[] doubleArray = (double[]) array;
            return doubleArray.clone();
        }
        if (type == float[].class) {
            float[] floatArray = (float[]) array;
            return floatArray.clone();
        }
        if (type == int[].class) {
            int[] intArray = (int[]) array;
            return intArray.clone();
        }
        if (type == long[].class) {
            long[] longArray = (long[]) array;
            return longArray.clone();
        }
        if (type == short[].class) {
            short[] shortArray = (short[]) array;
            return shortArray.clone();
        }
        if (type == boolean[].class) {
            boolean[] booleanArray = (boolean[]) array;
            return booleanArray.clone();
        }

        Object[] objectArray = (Object[]) array;
        return objectArray.clone();
    }

}
