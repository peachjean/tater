package net.peachjean.tater.utils;

class FieldDescriptor {
    private final String name;
    private final String type;
    private final String parameterType;
    private final String defaultValue;

    FieldDescriptor(String name, String type, String defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        if(type.endsWith("[]")) {
            this.parameterType = type.substring(0, type.length() - 2) + " ...";
        } else {
            this.parameterType = type;
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getParameterType() {
        if(type.endsWith("[]")) {
            return type.substring(0, type.length() - 2) + " ...";
        } else {
            return type;
        }
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
