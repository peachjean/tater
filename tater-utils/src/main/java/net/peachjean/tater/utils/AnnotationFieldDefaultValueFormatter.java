package net.peachjean.tater.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import java.util.List;

class AnnotationFieldDefaultValueFormatter extends SimpleAnnotationValueVisitor6<String, AnnotationFieldDefaultValueFormatter.TypeAndUtils> {

    public static AnnotationFieldDefaultValueFormatter INSTANCE = new AnnotationFieldDefaultValueFormatter();

    private AnnotationFieldDefaultValueFormatter() {}

    @Override
    public String visitString(String s, TypeAndUtils utils) {
        return String.format("\"%s\"", s);
    }

    @Override
    public String visitBoolean(boolean b, TypeAndUtils utils) {
        return String.valueOf(b);
    }

    @Override
    public String visitByte(byte b, TypeAndUtils utils) {
        return String.valueOf(b);
    }

    @Override
    public String visitChar(char c, TypeAndUtils utils) {
        return String.valueOf(c);
    }

    @Override
    public String visitDouble(double d, TypeAndUtils utils) {
        return String.valueOf(d);
    }

    @Override
    public String visitFloat(float f, TypeAndUtils utils) {
        return String.valueOf(f);
    }

    @Override
    public String visitInt(int i, TypeAndUtils utils) {
        return String.valueOf(i);
    }

    @Override
    public String visitLong(long l, TypeAndUtils utils) {
        return String.valueOf(l);
    }

    @Override
    public String visitShort(short s, TypeAndUtils utils) {
        return String.valueOf(s);
    }

    @Override
    public String visitType(TypeMirror t, TypeAndUtils typeAndUtils) {
        return t.accept(TypeSourceFormatter.INSTANCE, typeAndUtils.getUtils()) + ".class";
    }

    @Override
    public String visitEnumConstant(VariableElement c, TypeAndUtils utils) {
        return c.getEnclosingElement().toString() + "." + c.toString();
    }

    /**
     * Duplicates the functionality of {@link #visitEnumConstant}.
     */
    public <E extends Enum> String formatEnum(E value) {
        return value.toString();
    }

    @Override
    public String visitAnnotation(AnnotationMirror a, TypeAndUtils utils) {
        final String annotationType = a.getAnnotationType().accept(TypeSourceFormatter.INSTANCE, utils.getUtils());
        StringBuilder sb = new StringBuilder("net.peachjean.tater.utils.AnnotationInvocationHandler.<")
                .append(annotationType)
                .append(">implement(")
                .append(annotationType)
                .append(".class)");
        for(ExecutableElement element : a.getElementValues().keySet()) {
            sb.append(".withMemberValue(\"")
                    .append(element.getSimpleName().toString())
                    .append("\", ")
                    .append(a.getElementValues().get(element).accept(this, utils))
                    .append(")");
        }
        return sb.append(".build()").toString();
    }

    @Override
    public String visitArray(List<? extends AnnotationValue> vals, TypeAndUtils utils) {
        return String.format("(%s) new %s { %s }", utils.getType(), rawType(utils.getType()), StringUtils.join(transformValues(vals, utils), ", "));
    }

    private String rawType(String type) {
        int genericStart = type.indexOf("<");
        if(genericStart >= 0) {
            int genericEnd = type.lastIndexOf(">");
            String genericPart = type.substring(genericStart, genericEnd + 1);
            return type.replace(genericPart, "");
        } else {
            return type;
        }
    }

    private Iterable<String> transformValues(List<? extends AnnotationValue> vals, final TypeAndUtils utils) {
        return CollectionUtils.collect(vals, new Transformer<AnnotationValue, String>() {
            @Override
            public String transform(AnnotationValue input) {
                return input.accept(AnnotationFieldDefaultValueFormatter.this, utils);
            }
        });
    }

    public static class TypeAndUtils {
        private final Utils utils;
        private final String type;


        TypeAndUtils(Utils utils, String type) {
            this.utils = utils;
            this.type = type;
        }

        public Utils getUtils() {
            return utils;
        }

        public String getType() {
            return type;
        }
    }
}
