package net.peachjean.tater.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.Transformer;
import org.kohsuke.MetaInfServices;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("net.peachjean.tater.utils.Implemented")
@MetaInfServices(Processor.class)
public class ImplementedProcessor extends AbstractProcessor {

    private Utils utils;

	@Override
	public SourceVersion getSupportedSourceVersion()
	{
		return SourceVersion.latest();
	}

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.utils = new Utils(processingEnv.getElementUtils(), processingEnv.getTypeUtils());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing @Implemented...");
        for(TypeElement annotation: annotations) {
            for(Element element: roundEnv.getElementsAnnotatedWith(annotation)) {
                TypeElement serviceElement = (TypeElement) element;
                PackageElement packageElement = getPackage(serviceElement);
                final String packageName =
                        packageElement == null ? "" : packageElement.getQualifiedName().toString();
                Implemented implemented = serviceElement.getAnnotation(Implemented.class);
                final String implName = "".equals(implemented.value()) ? serviceElement.getSimpleName().toString() + "Impl" :
                        implemented.value();
                final List<FieldDescriptor> fields = createFieldList(serviceElement);
                final String localName = determineLocalName(serviceElement);
                final boolean isPublic = serviceElement.getModifiers().contains(Modifier.PUBLIC);
                ImplementedDescriptor annotationDescriptor = new ImplementedDescriptor(isPublic, packageName, implName, localName, fields);
                createImplSourceFile(annotationDescriptor, serviceElement, packageName + "." + implName);
            }
        }

        return true;
    }

    private String determineLocalName(final TypeElement serviceElement) {
        return serviceElement.getQualifiedName().toString();
    }

    private List<FieldDescriptor> createFieldList(TypeElement serviceElement) {
        return ListUtils.unmodifiableList(CollectionUtils.collect(serviceElement.getEnclosedElements(),
                new Transformer<Element, FieldDescriptor>() {
                    @Override
                    public FieldDescriptor transform(Element enclosed) {
                        FieldDescriptor fieldDescriptor = enclosed.accept(AnnotationFieldVisitor.INSTANCE, utils);
                        return fieldDescriptor;
                    }
                }, new ArrayList<FieldDescriptor>()));
    }

    private void createImplSourceFile(final ImplementedDescriptor annotationDescriptor, final TypeElement annotationElement, final String implName)
    {
        try
        {
            final JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(implName, annotationElement);
            annotationDescriptor.generateSource(new OutputSupplier<Writer>()
            {
                @Override
                public Writer getOutput()
                {
                    try {
                        return sourceFile.openWriter();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to load source file.", e);
                    }
                }
            });
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generated source " + implName);
        }
        catch (IOException e)
        {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not create source file: " + implName + "[" + e.getMessage() + "]");
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
