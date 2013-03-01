package net.peachjean.tater.utils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class Utils {
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

    public static Utils from(ProcessingEnvironment processingEnvironment) {
        return new Utils(processingEnvironment.getElementUtils(), processingEnvironment.getTypeUtils());
    }
}
