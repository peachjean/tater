package net.peachjean.tater.utils;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.google.common.io.OutputSupplier;
import com.google.common.io.Resources;
import net.peachjean.commons.test.junit.TmpDir;
import org.junit.Rule;
import org.junit.Test;
import pl.ncdc.differentia.DifferentiaAssert;

import java.io.*;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ImplementedDescriptorTest {

    @Rule
    public TmpDir tmpDir = new TmpDir();

    @Test
    public void testGenerateSource() throws Exception {

        AnnotationFieldDefaultValueFormatter defaultValueFormatter = AnnotationFieldDefaultValueFormatter.INSTANCE;
        AnnotationFieldTypeFormatter typeFormatter = AnnotationFieldTypeFormatter.INSTANCE;

        ImplementedDescriptor underTest = new ImplementedDescriptor("com.example.testa", "MyAnnotation",
                ImmutableList.of(
                        new FieldDescriptor("field1", String.class.getName(), "\"value1\""),
                        new FieldDescriptor("field2", int.class.getName(), "5"),
                        new FieldDescriptor("field3", float.class.getName(), "5.5f"),
                        new FieldDescriptor("field4", long.class.getName(), "5l"),
                        new FieldDescriptor("field5", double.class.getName(), "5.5d"),
                        new FieldDescriptor("field6", Class.class.getName(), "com.example.testa.MyAnnotation.class"),
                        new FieldDescriptor("field7",  typeFormatter.formatEnum(MyEnum.ONE), defaultValueFormatter.formatEnum(MyEnum.ONE)),
                        new FieldDescriptor("field8", byte.class.getName(), "5"),
                        new FieldDescriptor("field9", char.class.getName(), "'c'"),
                        new FieldDescriptor("field10", short.class.getName(), "3"),
                        new FieldDescriptor("field11", boolean.class.getName(), "true"),
                        new FieldDescriptor("array1", String.class.getName() + "[]", "new String[] {\"value1\"}"),
                        new FieldDescriptor("array2", int.class.getName() + "[]", "new int[] {5}"),
                        new FieldDescriptor("array3", float.class.getName() + "[]", "new float[] {5.5f}"),
                        new FieldDescriptor("array4", long.class.getName() + "[]", "new long[] {5l}"),
                        new FieldDescriptor("array5", double.class.getName() + "[]", "new double[] {5.5d}"),
                        new FieldDescriptor("array6", Class.class.getName() + "[]", "new Class[] {com.example.testa.MyAnnotation.class}"),
//                        new FieldDescriptor("array7", typeFormatter.formatEnum(new MyEnum[0]) MyEnum.class.getName() + "[]", "new MyEnum[] {ONE}"),
                        new FieldDescriptor("array8", byte.class.getName() + "[]", "new byte[] {5}"),
                        new FieldDescriptor("array9", char.class.getName() + "[]", "new char[] {'c'}"),
                        new FieldDescriptor("array10", short.class.getName() + "[]", "new short[] {3}"),
                        new FieldDescriptor("array11", boolean.class.getName() + "[]", "new boolean[] {true}")
        ));

        final StringWriter output = new StringWriter();
        underTest.generateSource(new OutputSupplier<Writer>() {
            @Override
            public Writer getOutput() throws IOException {
                return output;
            }
        });

        final URL resource = Resources.getResource(this.getClass(), "generateSource.expected");
        if(System.getProperties().keySet().contains("regenerateExpectedCode")) {
            File expectedOutput = new File("src/test/resources/net/peachjean/tater/utils/generateSource.expected");
            Files.write(output.toString(), expectedOutput, Charsets.UTF_8);
        }

        Reader expected = Resources.newReaderSupplier(resource,
                Charsets.UTF_8).getInput();

        DifferentiaAssert.assertSourcesEqual(expected, new StringReader(output.toString()), false, true);
    }

    public static enum MyEnum {
        ONE, TWO, THREE
    }
}
