package net.peachjean.tater.utils;

import com.google.common.collect.ObjectArrays;
import net.peachjean.commons.test.junit.CumulativeAssertionRule;
import net.peachjean.commons.test.junit.TmpDir;
import net.peachjean.tater.test.CompilerHarness;
import net.peachjean.tater.test.CompilerResults;
import net.peachjean.tater.test.JavaSourceFromText;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;

public class ImplementedProcessorTest {
    @Rule
    public TmpDir tmpDir = new TmpDir();

    @Rule
    public CumulativeAssertionRule accumulator = new CumulativeAssertionRule();

    @Test
    public void testDefaultClass() throws Exception {
        JavaFileObject[] sourceFiles = {
                JavaSourceFromText.builder("com.example.MyAnnotation")
                        .line("package com.example;")
                        .line("import net.peachjean.tater.utils.*;")
                        .line("import java.lang.annotation.*;")
                        .line("@Implemented ")
                        .line("public @interface MyAnnotation {")
                        .line("  String value() default \"default\";")
                        .line("  int intVal() default 1;")
                        .line("  Class<? extends java.util.List> listType() default java.util.ArrayList.class;")
                        .line("  MyEnum enumVal() default MyEnum.ONE;")
                        .line("  String[] valueArray() default {\"default\"};")
                        .line("  int[] intValArray() default 1;")
                        .line("  Class<? extends java.util.List>[] listTypeArray() default java.util.ArrayList.class;")
                        .line("  MyEnum[] enumValArray() default MyEnum.ONE;")
                        .line("  Retention retentionPolicy() default @Retention(RetentionPolicy.SOURCE);")
                        .line("  Retention[] retentionPolicyArray() default {@Retention(RetentionPolicy.SOURCE)};")
                        .line("}")
                .build(),
                JavaSourceFromText.builder("com.example.MyAnnotationAsserter")
                        .line("package com.example;")
                        .line("import java.lang.annotation.*;")
                        .line("import net.peachjean.tater.test.*;")
                        .line("import net.peachjean.commons.test.junit.AssertionHandler;")
                        .line("public class MyAnnotationAsserter implements CompilerAsserter {")
                        .line("  public void doAssertions(AssertionHandler assertionHandler) {")
                        .line("    MyAnnotation a1 = MyAnnotationImpl.build();")
                        .line("    assertionHandler.assertEquals(\"default\", a1.value());")
                        .line("    assertionHandler.assertEquals(1, a1.intVal());")
                        .line("    assertionHandler.assertEquals(java.util.ArrayList.class, a1.listType());")
                        .line("    assertionHandler.assertEquals(MyEnum.ONE, a1.enumVal());")
                        .line("    assertionHandler.assertEquals(RetentionPolicy.SOURCE, a1.retentionPolicy().value());")
                        .line("    assertionHandler.assertEquals(1, a1.retentionPolicyArray().length);")
                        .line("    assertionHandler.assertEquals(RetentionPolicy.SOURCE, a1.retentionPolicyArray()[0].value());")
                        .line("    MyAnnotation a2 = MyAnnotationImpl.value(\"someValue\").intVal(2).build();")
                        .line("    assertionHandler.assertEquals(\"someValue\", a2.value());")
                        .line("    assertionHandler.assertEquals(2, a2.intVal());")
                        .line("    MyAnnotation a3 = MyAnnotationImpl.build(\"someValue\");")
                        .line("    assertionHandler.assertEquals(\"someValue\", a3.value());")
                        .line("  }")
                        .line("}")
                .build(),
                JavaSourceFromText.builder("com.example.MyEnum")
                        .line("package com.example;")
                        .line("public enum MyEnum {")
                        .line("  ONE, TWO, THREE")
                        .line("}")
                .build()
        };
        CompilerResults results = new CompilerHarness(tmpDir.getDir(), accumulator, sourceFiles)
                .addProcessor(new ImplementedProcessor()).invoke();

        for (Diagnostic<? extends JavaFileObject> diagnostic : results.getDiagnostics()) {
            System.out.println(diagnostic);
        }
        results.assertNoOutput();
        results.assertNumberOfDiagnostics(Diagnostic.Kind.ERROR, 0);
        results.assertNumberOfDiagnostics(Diagnostic.Kind.WARNING, 0);

        results.runAssertion("com.example.MyAnnotationAsserter");
    }

    @Test
    public void testStandardMethods() throws Exception {
        JavaFileObject[] sourceFiles = {
                JavaSourceFromText.builder("com.example.MyAnnotation")
                        .line("package com.example;")
                        .line("import net.peachjean.tater.utils.*;")
                        .line("import java.lang.annotation.*;")
                        .line("@Implemented @Retention(RetentionPolicy.RUNTIME)")
                        .line("public @interface MyAnnotation {")
                        .line("  String value() default \"default\";")
                        .line("  int intVal() default 1;")
                        .line("  Class<? extends java.util.List> listType() default java.util.ArrayList.class;")
                        .line("  MyEnum enumVal() default MyEnum.ONE;")
                        .line("  String[] valueArray() default {\"default\"};")
                        .line("}")
                        .build(),
                JavaSourceFromText.builder("com.example.MyAnnotationAsserter")
                        .line("package com.example;")
                        .line("import net.peachjean.tater.test.*;")
                        .line("import net.peachjean.commons.test.junit.AssertionHandler;")
                        .line("public class MyAnnotationAsserter extends StandardMethodTestSupport<MyAnnotation> {")
                        .line("  public MyAnnotationAsserter() {")
                        .line("    super(MyAnnotation.class);")
                        .line("  }")
                        .line("  @MyAnnotation")
                        .line("  public MyAnnotation allDefaults() {")
                        .line("    return MyAnnotationImpl.build();")
                        .line("  }")
                        .line("}")
                        .build(),
                JavaSourceFromText.builder("com.example.MyEnum")
                        .line("package com.example;")
                        .line("public enum MyEnum {")
                        .line("  ONE, TWO, THREE")
                        .line("}")
                        .build()
        };
        CompilerResults results = new CompilerHarness(tmpDir.getDir(), accumulator, sourceFiles)
                .addProcessor(new ImplementedProcessor()).invoke();

        for (Diagnostic<? extends JavaFileObject> diagnostic : results.getDiagnostics()) {
            System.out.println(diagnostic);
        }
        results.assertNoOutput();
        results.assertNumberOfDiagnostics(Diagnostic.Kind.ERROR, 0);
        results.assertNumberOfDiagnostics(Diagnostic.Kind.WARNING, 0);

        results.runAssertion("com.example.MyAnnotationAsserter");
    }

    @Test
    public void testSupportedAnnotationTypes() {
        SupportedAnnotationTypes annotation = ImplementedProcessor.class.getAnnotation(SupportedAnnotationTypes.class);
        Assert.assertNotNull("No supported annotation types specified.", annotation);
        String[] types = annotation.value();

        boolean found = false;
        for (String type : types) {
            if (type.equals(Implemented.class.getName())) {
                found = true;
                break;
            } else if(
                    (type.endsWith(".*") && Implemented.class.getName().startsWith(type.substring(0, type.length() - 2)))) {
                found = true;
                break;
            }
        }
        if (!found) {
            Assert.fail(ImplementedProcessor.class.getName() + " does not support " + Implemented.class.getName());
        }

    }
}
