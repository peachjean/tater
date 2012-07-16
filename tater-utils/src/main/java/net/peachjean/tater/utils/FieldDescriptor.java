package net.peachjean.tater.utils;

class FieldDescriptor {
    private final String name;
    private final String type;
    private final String defaultValue;

    FieldDescriptor(String name, String type, String defaultValue) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
