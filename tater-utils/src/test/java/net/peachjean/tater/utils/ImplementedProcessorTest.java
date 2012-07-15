package net.peachjean.tater.utils;

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
    public void testDefaultClass() throws IOException, ClassNotFoundException {
        JavaFileObject[] sourceFiles = {
                new JavaSourceFromText("com.example.MyAnnotation", "" +
                        "package com.example;" +
                        "import net.peachjean.tater.utils.*;" +
                        "@Implemented " +
                        "public @interface MyAnnotation {" +
                        "}"),
                new JavaSourceFromText("com.example.MyAnnotationAsserter", "" +
                        "package com.example;" +
                        "import net.peachjean.tater.test.*;" +
                        "import net.peachjean.commons.test.junit.AssertionHandler;" +
                        "public class MyAnnotationAsserter implements CompilerAsserter {" +
                        "  public void doAssertions(AssertionHandler assertionHandler) {" +
                        "    MyAnnotationImpl.build();" +
                        "  }" +
                        "}")
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
