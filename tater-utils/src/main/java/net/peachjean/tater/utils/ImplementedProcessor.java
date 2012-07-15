package net.peachjean.tater.utils;

import com.google.common.io.OutputSupplier;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
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
                ImplementedDescriptor annotationDescriptor = new ImplementedDescriptor(packageName, simpleName);
                createImplSourceFile(annotationDescriptor, serviceElement);
            }
        }

        return true;
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
