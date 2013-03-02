package net.peachjean.tater.utils;

import net.peachjean.commons.test.junit.TmpDir;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import pl.ncdc.differentia.DifferentiaAssert;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class ImplementedDescriptorTest {

    @Rule
    public TmpDir tmpDir = new TmpDir();

    @Test
    public void testGenerateSource() throws Exception {

        AnnotationFieldDefaultValueFormatter defaultValueFormatter = AnnotationFieldDefaultValueFormatter.INSTANCE;
        AnnotationFieldTypeFormatter typeFormatter = AnnotationFieldTypeFormatter.INSTANCE;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
        calendar.setTimeInMillis(10);
        ImplementedDescriptor underTest = new ImplementedDescriptor(true, "com.example.testa", "MyAnnotationImpl",
                "com.example.testa.MyAnnotation", immutableListOf(
                new FieldDescriptor("field1", String.class.getName(), "\"value1\""),
                new FieldDescriptor("field2", int.class.getName(), "5"),
                new FieldDescriptor("field3", float.class.getName(), "5.5f"),
                new FieldDescriptor("field4", long.class.getName(), "5l"),
                new FieldDescriptor("field5", double.class.getName(), "5.5d"),
                new FieldDescriptor("field6", Class.class.getName(), "com.example.testa.MyAnnotation.class"),
                new FieldDescriptor("field7", typeFormatter.formatEnum(MyEnum.ONE), defaultValueFormatter.formatEnum(MyEnum.ONE)),
                new FieldDescriptor("field8", byte.class.getName(), "5"),
                new FieldDescriptor("field9", char.class.getName(), "'c'"),
                new FieldDescriptor("field10", short.class.getName(), "3"),
                new FieldDescriptor("field11", boolean.class.getName(), "true"),
                new FieldDescriptor("noDefault", String.class.getName(), "null"),
                new FieldDescriptor("array1", arrayName(String.class), "new String[] {\"value1\"}"),
                new FieldDescriptor("array2", arrayName(int.class), "new int[] {5}"),
                new FieldDescriptor("array3", arrayName(float.class), "new float[] {5.5f}"),
                new FieldDescriptor("array4", arrayName(long.class), "new long[] {5l}"),
                new FieldDescriptor("array5", arrayName(double.class), "new double[] {5.5d}"),
                new FieldDescriptor("array6", arrayName(Class.class), "new Class[] {com.example.testa.MyAnnotation.class}"),
                new FieldDescriptor("array7", arrayName(MyEnum.class), "new MyEnum[] {ONE}"),
                new FieldDescriptor("array8", arrayName(byte.class), "new byte[] {5}"),
                new FieldDescriptor("array9", arrayName(char.class), "new char[] {'c'}"),
                new FieldDescriptor("array10", arrayName(short.class), "new short[] {3}"),
                new FieldDescriptor("array11", arrayName(boolean.class), "new boolean[] {true}")
        ), calendar);

        final StringWriter output = new StringWriter();
        underTest.generateSource(new OutputSupplier<Writer>() {
            @Override
            public Writer getOutput() {
                return output;
            }
        });

        final URL resource = this.getClass().getResource("generateSource.expected");
        if(System.getProperties().keySet().contains("regenerateExpectedCode")
                && Boolean.valueOf(System.getProperty("regenerateExpectedCode"))) {
            File expectedOutput = new File("src/test/resources/net/peachjean/tater/utils/generateSource.expected");
            FileUtils.write(expectedOutput, output.toString(), Charsets.UTF_8);
        }

        Reader expected = new InputStreamReader(resource.openStream(), Charsets.UTF_8);

        DifferentiaAssert.assertSourcesEqual(expected, new StringReader(output.toString()), false, true);
    }

    private <T> List<T> immutableListOf(T ... members) {
        return ListUtils.unmodifiableList(Arrays.asList(members));
    }

    private String arrayName(final Class<?> type) {
        return (type.getName() + "[]").replace("$", ".");
    }

    public static enum MyEnum {
        ONE, TWO, THREE
    }
}
