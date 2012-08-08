$! http://www.antlr.org/wiki/display/ST/StringTemplate+cheat+sheet !$
$if(annotationType.packageName)$package $annotationType.packageName$;$endif$

import org.apache.commons.lang3.AnnotationUtils;

@SuppressWarnings({"UnusedDeclaration", "ClassExplicitlyAnnotation"})
public class $annotationType.simpleName$Impl implements $annotationType.simpleName$ {

$annotationType.fields:{field|
    private final $field.type$ $field.name$;

    public $field.type$ $field.name$() {
      $if(field.array)$
        return $field.name$.clone();
      $else$
        return $field.name$;
      $endif$
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

    @SuppressWarnings("SimplifiableConditionalExpression")
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
    @SuppressWarnings("unchecked")
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
      @SuppressWarnings("unchecked")
      private $field.type$ $field.name$ = $field.defaultValue$;

      @SuppressWarnings("unchecked")
      public Builder $field.name$(final $field.parameterType$ v) {
        this.$field.name$ = v;
        return this;
      }
    }$
    }
}