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

    private final boolean isPublic;
    private final String packageName;
    private final String implName;
    private final String localName;
    private final List<FieldDescriptor> fields;
    private final FieldDescriptor valueField;

    public ImplementedDescriptor(final boolean isPublic, String packageName, String implName, final String localName, List<FieldDescriptor> fields) {
        this.isPublic = isPublic;
        this.packageName = packageName;
        this.implName = implName;
        this.localName = localName;
        this.fields = fields;
        this.valueField = findValueField(fields);
    }

    private FieldDescriptor findValueField(List<FieldDescriptor> fields) {
        for(FieldDescriptor field: fields) {
            if("value".equals(field.getName())) {
                return field;
            }
        }
        return null;
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

    public boolean isPublic() {
        return isPublic;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getImplName() {
        return implName;
    }

    public String getLocalName() {
        return localName;
    }

    public List<FieldDescriptor> getFields() {
        return fields;
    }

    public FieldDescriptor getValueField() {
        return valueField;
    }
}
