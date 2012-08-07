package net.peachjean.tater.utils;

import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

class Utils {
    private final Elements elements;
    private final Types types;

    Utils(Elements elements, Types types) {
        this.elements = elements;
        this.types = types;
    }

    public Elements getElements() {
        return elements;
    }

    public Types getTypes() {
        return types;
    }
}
