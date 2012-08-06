package net.peachjean.tater.utils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import java.util.List;

class AnnotationFieldDefaultValueFormatter extends SimpleAnnotationValueVisitor6<String, Void> {

    public static AnnotationFieldDefaultValueFormatter INSTANCE = new AnnotationFieldDefaultValueFormatter();

    private AnnotationFieldDefaultValueFormatter() {}

    @Override
    public String visitString(String s, Void aVoid) {
        return String.format("\"%s\"", s);
    }

    @Override
    public String visitBoolean(boolean b, Void aVoid) {
        return String.valueOf(b);
    }

    @Override
    public String visitByte(byte b, Void aVoid) {
        return String.valueOf(b);
    }

    @Override
    public String visitChar(char c, Void aVoid) {
        return String.valueOf(c);
    }

    @Override
    public String visitDouble(double d, Void aVoid) {
        return String.valueOf(d);
    }

    @Override
    public String visitFloat(float f, Void aVoid) {
        return String.valueOf(f);
    }

    @Override
    public String visitInt(int i, Void aVoid) {
        return String.valueOf(i);
    }

    @Override
    public String visitLong(long l, Void aVoid) {
        return String.valueOf(l);
    }

    @Override
    public String visitShort(short s, Void aVoid) {
        return String.valueOf(s);
    }

    @Override
    public String visitType(TypeMirror t, Void aVoid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visitEnumConstant(VariableElement c, Void aVoid) {
        return c.getEnclosingElement().toString() + "." + c.toString();
    }

    /**
     * Duplicates the functionality of {@link #visitEnumConstant}.
     */
    public <E extends Enum> String formatEnum(E value) {
        return value.toString();
    }

    @Override
    public String visitAnnotation(AnnotationMirror a, Void aVoid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visitArray(List<? extends AnnotationValue> vals, Void aVoid) {
        throw new UnsupportedOperationException();
    }
}
