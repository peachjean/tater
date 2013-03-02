package net.peachjean.tater.utils;

import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

class ImplementedDescriptor {
    public static final String TEMPLATE_PATH =
            ImplementedDescriptor.class.getPackage().getName().replaceAll("\\.", "/") + "/";

    private final boolean isPublic;
    private final String packageName;
    private final String implName;
    private final String localName;
    private final List<FieldDescriptor> fields;
    private final FieldDescriptor valueField;
    private final String date;

    public ImplementedDescriptor(final boolean isPublic, String packageName, String implName, final String localName, List<FieldDescriptor> fields) {
        this(isPublic, packageName, implName, localName, fields, Calendar.getInstance());
    }

    public ImplementedDescriptor(final boolean isPublic, String packageName, String implName, final String localName, List<FieldDescriptor> fields, final Calendar generatedTime) {
        this.isPublic = isPublic;
        this.packageName = packageName;
        this.implName = implName;
        this.localName = localName;
        this.fields = fields;
        this.valueField = findValueField(fields);
        final TimeZone timeZone = generatedTime.getTimeZone();
        this.date = DateFormatUtils.format(
                generatedTime,
                DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern(),
                timeZone);
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
            IOUtils.closeQuietly(writer);
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

    public String getDate() {
        return date;
    }
}
