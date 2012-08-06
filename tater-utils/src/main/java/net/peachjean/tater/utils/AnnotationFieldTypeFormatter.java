package net.peachjean.tater.utils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import java.util.List;

class AnnotationFieldTypeFormatter extends SimpleAnnotationValueVisitor6<String, Void> {

    public static final AnnotationFieldTypeFormatter INSTANCE = new AnnotationFieldTypeFormatter();

    private AnnotationFieldTypeFormatter() {}

    @Override
    public String visitString(String s, Void aVoid) {
        return String.class.getName();
    }

    @Override
    public String visitBoolean(boolean b, Void aVoid) {
        return boolean.class.getName();
    }

    @Override
    public String visitByte(byte b, Void aVoid) {
        return byte.class.getName();
    }

    @Override
    public String visitChar(char c, Void aVoid) {
        return char.class.getName();
    }

    @Override
    public String visitDouble(double d, Void aVoid) {
        return double.class.getName();
    }

    @Override
    public String visitFloat(float f, Void aVoid) {
        return float.class.getName();
    }

    @Override
    public String visitInt(int i, Void aVoid) {
        return int.class.getName();
    }

    @Override
    public String visitLong(long i, Void aVoid) {
        return long.class.getName();
    }

    @Override
    public String visitShort(short s, Void aVoid) {
        return short.class.getName();
    }

    @Override
    public String visitType(TypeMirror t, Void aVoid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String visitEnumConstant(VariableElement c, Void aVoid) {
        return c.getEnclosingElement().toString();
    }

    /**
     * Duplicates the functionality of {@link #visitEnumConstant}.
     */
    public <E extends Enum> String formatEnum(E value) {
        return value.getClass().getName();
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
