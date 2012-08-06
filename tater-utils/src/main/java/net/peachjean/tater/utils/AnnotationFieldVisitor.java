package net.peachjean.tater.utils;

import com.google.common.collect.ImmutableList;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleElementVisitor6;

class AnnotationFieldVisitor extends SimpleElementVisitor6<FieldDescriptor, Void> {

    public static final AnnotationFieldVisitor INSTANCE = new AnnotationFieldVisitor();

    private AnnotationFieldVisitor() {}

    @Override
    public FieldDescriptor visitExecutable(ExecutableElement e, Void voidIn) {
        TypeMirror returnType = e.getReturnType();
        AnnotationValue defaultValue = e.getDefaultValue();
        String defaultValueRep = defaultValue.accept(AnnotationFieldDefaultValueFormatter.INSTANCE, null);
        String typeRep = defaultValue.accept(AnnotationFieldTypeFormatter.INSTANCE, null);
        String name = e.getSimpleName().toString();

        FieldDescriptor element = new FieldDescriptor(name, typeRep, defaultValueRep);
        return element;
    }

}
