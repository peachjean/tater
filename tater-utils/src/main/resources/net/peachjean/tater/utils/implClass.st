$if(annotationType.packageName)$package $annotationType.packageName$;$endif$

public class $annotationType.simpleName$Impl implements $annotationType.simpleName$ {

$annotationType.fields:{field|
    public $field.type$ $field.name$() {
      return $field.defaultValue$;
    }
}$

    public Class<$annotationType.simpleName$> annotationType() {
      return $annotationType.simpleName$.class;
    }

    public static $annotationType.simpleName$ build() {
      return new Builder().build();
    }

    public static class Builder {
      $annotationType.simpleName$ build() {
        return new $annotationType.simpleName$Impl();
      }
    }
}