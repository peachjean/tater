package net.peachjean.tater.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.io.OutputSupplier;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("net.peachjean.tater.utils.Implemented")
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
            FieldDescriptor fieldDescriptor = enclosed.accept(AnnotationFieldVisitor.INSTANCE, utils);
            fieldListBuilder.add(fieldDescriptor);
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
