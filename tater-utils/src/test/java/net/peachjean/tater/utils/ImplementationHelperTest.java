package net.peachjean.tater.utils;

import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ImplementationHelperTest {
    @Test @Holder(
            one = @MyAnnotation(listType = ArrayList.class, listArrayType = { ArrayList.class }),
            two = @MyAnnotation(listType = LinkedList.class, listArrayType = { LinkedList.class }))
    public void testIsEquals() throws NoSuchMethodException {
        Holder holder = ImplementationHelperTest.class.getMethod("testIsEquals").getAnnotation(Holder.class);
        ImplementationHelper.isEquals(holder.one().listType(), holder.two().listType());
        ImplementationHelper.isEquals(holder.one().listArrayType(), holder.two().listArrayType());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation {
        Class<? extends List> listType();
        Class<? extends List>[] listArrayType();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Holder {
        MyAnnotation one();
        MyAnnotation two();
    }
}
