package net.peachjean.tater.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.io.OutputSupplier;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;
import javax.lang.model.util.SimpleElementVisitor7;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("net.peachjean.tater.utils.Implemented")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ImplementedProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for(TypeElement annotation: annotations) {
            for(Element element: roundEnv.getElementsAnnotatedWith(annotation)) {
                TypeElement serviceElement = (TypeElement) element;
                PackageElement packageElement = getPackage(serviceElement);
                final String packageName =
                        packageElement == null ? "" : packageElement.getQualifiedName().toString();
                final String simpleName = serviceElement.getSimpleName().toString();
                final List<FieldDescriptor> fields = createFieldList(serviceElement);
                ImplementedDescriptor annotationDescriptor = new ImplementedDescriptor(packageName, simpleName, fields);
                createImplSourceFile(annotationDescriptor, serviceElement);
            }
        }

        return true;
    }

    private List<FieldDescriptor> createFieldList(TypeElement serviceElement) {
        ImmutableList.Builder<FieldDescriptor> fieldListBuilder = ImmutableList.builder();
        for(Element enclosed: serviceElement.getEnclosedElements()) {
            enclosed.accept(new SimpleElementVisitor7<Void, ImmutableList.Builder<FieldDescriptor>>() {
                @Override
                public Void visitExecutable(ExecutableElement e, ImmutableList.Builder<FieldDescriptor> fieldListBuilder) {
                    TypeMirror returnType = e.getReturnType();
                    AnnotationValue defaultValue = e.getDefaultValue();
                    String defaultValueRep = defaultValue.accept(new SimpleAnnotationValueVisitor7<String, Void>() {
                        @Override
                        public String visitString(String s, Void aVoid) {
                            return String.format("\"%s\"", s);
                        }
                    }, null);
                    String typeRep = defaultValue.accept(new SimpleAnnotationValueVisitor7<String, Void>() {
                        @Override
                        public String visitString(String s, Void aVoid) {
                            return String.class.getName();
                        }
                    }, null);
                    String name = e.getSimpleName().toString();

                    FieldDescriptor element = new FieldDescriptor(name, typeRep, defaultValueRep);
                    fieldListBuilder.add(element);
                    return null;
                }
            }, fieldListBuilder);
        }
        return fieldListBuilder.build();
    }

    private void createImplSourceFile(final ImplementedDescriptor annotationDescriptor, final TypeElement annotationElement)
    {
        String className = processingEnv.getElementUtils().getBinaryName(annotationElement) + "Impl";
        try
        {
            final JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(className, annotationElement);
            annotationDescriptor.generateSource(new OutputSupplier<Writer>()
            {
                @Override
                public Writer getOutput() throws IOException
                {
                    return sourceFile.openWriter();
                }
            });
        }
        catch (IOException e)
        {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not create source file: " + className + "[" + e.getMessage() + "]");
        }
    }

    private PackageElement getPackage(final Element serviceElement)
    {
        if(serviceElement == null)
        {
            return null;
        }
        Element enclosing = serviceElement.getEnclosingElement();
        if(enclosing instanceof PackageElement)
        {
            return (PackageElement) enclosing;
        }
        else
        {
            return getPackage(enclosing);
        }
    }
}
