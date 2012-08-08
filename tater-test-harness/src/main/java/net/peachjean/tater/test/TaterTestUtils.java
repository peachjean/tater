package net.peachjean.tater.test;

import org.apache.commons.lang3.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TaterTestUtils {
    private TaterTestUtils() {}

    public static RetentionPolicy retentionPolicy(Class<? extends Annotation> annotationType) {
        Retention retention = annotationType.getAnnotation(Retention.class);
        if(retention == null) {
            return RetentionPolicy.CLASS;
        } else {
            return retention.value();
        }
    }
}
