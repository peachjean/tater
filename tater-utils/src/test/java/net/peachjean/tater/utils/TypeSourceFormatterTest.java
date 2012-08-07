package net.peachjean.tater.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.ObjectArrays;
import net.peachjean.commons.test.junit.CumulativeAssertionRule;
import net.peachjean.commons.test.junit.TmpDir;
import net.peachjean.tater.test.CompilerHarness;
import net.peachjean.tater.test.CompilerResults;
import net.peachjean.tater.test.JavaSourceFromText;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.theories.ParametersSuppliedBy;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.annotation.Nullable;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@RunWith(Parameterized.class)
public class TypeSourceFormatterTest {

    private static Object[] test(String fieldDeclaration, String[] imports) {
        return new Object[] { fieldDeclaration, fieldDeclaration, imports };
    }

    private static Object[] test(String fieldDeclaration) {
        return new Object[] { fieldDeclaration, fieldDeclaration, new String[0] };
    }

    private static Object[] test(String fieldDeclaration, String expectedText, String[] imports) {
        return new Object[] { fieldDeclaration, expectedText, imports };
    }

    private static Object[] test(String fieldDeclaration, String expectedText) {
        return new Object[] { fieldDeclaration, expectedText, new String[0] };
    }

    private static String[] imports(String ... imports) {
        return imports;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.<Object[]>asList(
                test("String", "java.lang.String"),
                test("int"),
                test("boolean"),
                test("byte"),
                test("char"),
                test("int"),
                test("long"),
                test("float"),
                test("double"),
                test("MyEnum", MyEnum.class.getCanonicalName(),
                        imports(MyEnum.class.getCanonicalName())),
                test("Class", "java.lang.Class"),
                test("Class<? extends List>", "java.lang.Class<? extends java.util.List>",
                        imports(List.class.getCanonicalName())),
                test("SomeClass", SomeClass.class.getCanonicalName(),
                        imports(SomeClass.class.getCanonicalName())),
                test("SomeClass<? extends List, ? super Iterable>",
                        SomeClass.class.getCanonicalName() + "<? extends java.util.List,? super java.lang.Iterable>",
                        imports(SomeClass.class.getCanonicalName(),
                                List.class.getCanonicalName(),
                                Iterable.class.getCanonicalName())),
                test("String[]", "java.lang.String[]"),
                test("int[]"),
                test("boolean[]"),
                test("byte[]"),
                test("char[]"),
                test("int[]"),
                test("long[]"),
                test("float[]"),
                test("double[]"),
                test("MyEnum[]", MyEnum.class.getCanonicalName() + "[]", imports(MyEnum.class.getCanonicalName())),
                test("Class[]", "java.lang.Class[]"),
                test("SomeClass[]", SomeClass.class.getCanonicalName() + "[]", imports(SomeClass.class.getCanonicalName())),
                test("SomeClass<? extends List, ? super Iterable>[]",
                        SomeClass.class.getCanonicalName() + "<? extends java.util.List,? super java.lang.Iterable>[]",
                        imports(SomeClass.class.getCanonicalName(),
                                List.class.getCanonicalName(),
                                Iterable.class.getCanonicalName()))
                );
    }

    @Rule
    public TmpDir tmpDir = new TmpDir();

    @Rule
    public CumulativeAssertionRule accumulator = new CumulativeAssertionRule();

    private final String fieldDeclaration;
    private final String expectedSource;
    private final String[] imports;

    public TypeSourceFormatterTest(String fieldDeclaration, String expectedSource, String... imports) {
        this.fieldDeclaration = fieldDeclaration;
        this.expectedSource = expectedSource;
        this.imports = imports;
    }

    @Test
    public void format() throws IOException {
        StringBuilder source = new StringBuilder("package com.example;");
        for(String importStatement: this.imports) {
            source.append("import ").append(importStatement).append(";");
        }
        source.append("@Deprecated public class TestHost {")
              .append("  public ").append(this.fieldDeclaration).append(" hostField;")
              .append("}");


        JavaFileObject javaFileObject = new JavaSourceFromText("com.example.TestHost", source.toString());

        CompilerResults results = new CompilerHarness(tmpDir.getDir(), accumulator, javaFileObject)
                .addProcessor(new FormattingProcessor()).invoke();

        results.assertNumberOfDiagnostics(Diagnostic.Kind.ERROR, 0);
        results.assertNumberOfDiagnostics(Diagnostic.Kind.NOTE, 1);
        results.assertDiagnosticMatches(Diagnostic.Kind.NOTE, Pattern.quote("***" + this.expectedSource + "***"), 1);

        final Collection<Diagnostic<? extends JavaFileObject>> errors = results.getDiagnosticsOfKind(Diagnostic.Kind.ERROR);
        if(!errors.isEmpty()) {
            System.out.println(errors);
        }
//        System.out.println(results.getDiagnosticsOfKind(Diagnostic.Kind.NOTE));
    }

    @SupportedAnnotationTypes("*")
    public static class FormattingProcessor extends AbstractProcessor {

        @Override
        public SourceVersion getSupportedSourceVersion() {
            return SourceVersion.latest();
        }

        @Override
        public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
            final Set<? extends Element> elements = roundEnv.getRootElements();
            final String elementName = "TestHost";
            Element testHostClass = locateNamedElement(elements, elementName);
            if(testHostClass != null) {
                Element hostField = locateNamedElement(testHostClass.getEnclosedElements(), "hostField");
                TypeMirror type = hostField.asType();

                String typeAsString = type.accept(TypeSourceFormatter.INSTANCE, Utils.from(processingEnv));
                if(typeAsString == null) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "No type as string returned.");
                } else {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "***" + typeAsString + "***");
                }
            }
            return false;
        }

        private Element locateNamedElement(Iterable<? extends Element> elements, final String elementNameString) {
            final Name elementName = processingEnv.getElementUtils().getName(elementNameString);
            return Iterables.getOnlyElement(Iterables.filter(elements, new Predicate<Element>() {
                @Override
                public boolean apply(@Nullable Element input) {
                    return elementName.equals(input.getSimpleName());
                }
            }), null);
        }
    }

    public static enum MyEnum {
        ONE, TWO, THREE
    }

    public class SomeClass<X, Y> {

    }
}
