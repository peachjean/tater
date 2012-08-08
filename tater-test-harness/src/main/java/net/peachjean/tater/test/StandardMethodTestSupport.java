package net.peachjean.tater.test;

import com.google.common.base.Preconditions;
import net.peachjean.commons.test.junit.AssertionHandler;

import java.lang.annotation.Annotation;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

public class StandardMethodTestSupport<A extends Annotation> implements CompilerAsserter {

    private final Class<A> annotationType;

    public StandardMethodTestSupport(Class<A> annotationType) {
        Preconditions.checkArgument(RetentionPolicy.RUNTIME.equals(TaterTestUtils.retentionPolicy(annotationType)),
                "Annotation %s is does not have RetentionPolicy of RUNTIME.  This test will test nothing and likely produce false positives.",
                annotationType);
        this.annotationType = annotationType;
    }

    @Override
    public void doAssertions(AssertionHandler assertionHandler) throws Exception{
        for(Method method: this.getClass().getDeclaredMethods()) {
            if(method.isAnnotationPresent(annotationType)) {
                assertionHandler.assertTrue("Annotated method " + method.getName() + " must have return type of " + annotationType,
                        method.getReturnType().equals(annotationType));
                assertionHandler.assertEquals("Annotated method " + method.getName() + " parameter list.", 0, method.getParameterTypes().length);

                A declared = method.getAnnotation(annotationType);
                A built = (A) method.invoke(this);
                validateStandardMethods(declared, built, assertionHandler);
            }
        }
    }

    protected void validateStandardMethods(A declared, A built, AssertionHandler assertionHandler) {
        assertionHandler.assertEquals("equals method", declared, built);
        assertionHandler.assertEquals("equals method reversed", built, declared);
        assertionHandler.assertEquals("hashCode method", declared.hashCode(), built.hashCode());
        assertionHandler.assertEquals("annotationType method", declared.annotationType(), built.annotationType());
    }
}
