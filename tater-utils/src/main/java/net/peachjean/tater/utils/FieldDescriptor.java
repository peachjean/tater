package net.peachjean.tater.utils;

import com.google.common.base.Optional;

class FieldDescriptor {
    private final String name;
    private final String type;
    private final String parameterType;
    private final String defaultValue;
    private final boolean isArray;

    FieldDescriptor(String name, String type, Optional<String> defaultValue) {
	    this(name, type, defaultValue.or("null"));
    }

    FieldDescriptor(String name, String type, String defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        if(type.endsWith("[]")) {
            this.parameterType = type.substring(0, type.length() - 2) + " ...";
            this.isArray = true;
        } else {
            this.parameterType = type;
            this.isArray = false;
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

    public boolean isArray() {
        return isArray;
    }
}
