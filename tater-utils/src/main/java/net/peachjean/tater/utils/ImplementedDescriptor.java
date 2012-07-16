package net.peachjean.tater.utils;

import com.google.common.io.Closeables;
import com.google.common.io.OutputSupplier;
import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

class ImplementedDescriptor {
    public static final String TEMPLATE_PATH =
            ImplementedDescriptor.class.getPackage().getName().replaceAll("\\.", "/") + "/";

    private final String packageName;
    private final String simpleName;
    private final List<FieldDescriptor> fields;

    public ImplementedDescriptor(String packageName, String simpleName, List<FieldDescriptor> fields) {
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.fields = fields;
    }

    public void generateSource(OutputSupplier<Writer> writerSupplier)
            throws IOException
    {
        StringTemplateGroup stGroup = new StringTemplateGroup("parameterResolver")
        {
            @Override
            public String getFileNameFromTemplateName(final String templateName)
            {
                return TEMPLATE_PATH + super.getFileNameFromTemplateName(templateName);
            }
        };
        StringTemplate typeTemplate = stGroup.getInstanceOf("implClass");
        typeTemplate.setAttribute("annotationType", this);
        Writer writer = writerSupplier.getOutput();
        try
        {
            typeTemplate.write(new AutoIndentWriter(writer, "\n"));
        }
        finally
        {
            Closeables.close(writer, false);
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public List<FieldDescriptor> getFields() {
        return fields;
    }
}
