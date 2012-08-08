$if(annotationType.packageName)$package $annotationType.packageName$;$endif$

import org.apache.commons.lang3.AnnotationUtils;

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

    public int hashCode() {
      return AnnotationUtils.hashCode(this);
    }

    public boolean equals(Object other) {
      return other instanceof $annotationType.simpleName$
         ? AnnotationUtils.equals(this, ($annotationType.simpleName$) other)
         : false;
    }

    public String toString() {
      return AnnotationUtils.toString(this);
    }

    public static $annotationType.simpleName$ build() {
      return new Builder().build();
    }

$if(annotationType.valueField)$
    public static $annotationType.simpleName$ build(final $annotationType.valueField.parameterType$ v) {
      return new Builder().build(v);
    }
$endif$

$annotationType.fields:{field|
    public static Builder $field.name$(final $field.parameterType$ v) {
      return new Builder().$field.name$(v);
    }
}$

    public static class Builder {
      $annotationType.simpleName$ build() {
        return new $annotationType.simpleName$Impl($annotationType.fields:{field|$field.name$}; separator=", "$);
      }

 $if(annotationType.valueField)$
      public $annotationType.simpleName$ build(final $annotationType.valueField.parameterType$ v) {
        this.$annotationType.valueField.name$ = v;
        return this.build();
      }
 $endif$
   $annotationType.fields:{field|
      private $field.type$ $field.name$ = $field.defaultValue$;

      public Builder $field.name$(final $field.parameterType$ v) {
        this.$field.name$ = v;
        return this;
      }
    }$
    }
}