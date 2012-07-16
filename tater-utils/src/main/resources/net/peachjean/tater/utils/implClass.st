$if(annotationType.packageName)$package $annotationType.packageName$;$endif$

public class $annotationType.simpleName$Impl implements $annotationType.simpleName$ {

$annotationType.fields:{field|
    private final $field.type$ $field.name$;

    public $field.type$ $field.name$() {
      return $field.name$;
    }
}$

    private $annotationType.simpleName$Impl($annotationType.fields:{field|final $field.type$ $field.name$}; separator=", "$) {
      $annotationType.fields:{field|
        this.$field.name$ = $field.name$;
      }$
    }

    public Class<$annotationType.simpleName$> annotationType() {
      return $annotationType.simpleName$.class;
    }

    public static $annotationType.simpleName$ build() {
      return new Builder().build();
    }

$if(annotationType.valueField)$
    public static $annotationType.simpleName$ build(final $annotationType.valueField.type$ v) {
      return new Builder().build(v);
    }
$endif$

$annotationType.fields:{field|
    public static Builder $field.name$(final $field.type$ v) {
      return new Builder().$field.name$(v);
    }
}$

    public static class Builder {
      $annotationType.simpleName$ build() {
        return new $annotationType.simpleName$Impl($annotationType.fields:{field|$field.name$}; separator=", "$);
      }

 $if(annotationType.valueField)$
      public $annotationType.simpleName$ build(final $annotationType.valueField.type$ v) {
        this.$annotationType.valueField.name$ = v;
        return this.build();
      }
 $endif$
   $annotationType.fields:{field|
      private $field.type$ $field.name$ = $field.defaultValue$;

      public Builder $field.name$(final $field.type$ v) {
        this.$field.name$ = v;
        return this;
      }
    }$
    }
}