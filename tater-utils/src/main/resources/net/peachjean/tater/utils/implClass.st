$! http://www.antlr.org/wiki/display/ST/StringTemplate+cheat+sheet !$
$if(annotationType.packageName)$package $annotationType.packageName$;$endif$

import org.apache.commons.lang3.AnnotationUtils;
import com.google.common.base.Preconditions;

@SuppressWarnings({"UnusedDeclaration", "ClassExplicitlyAnnotation"})
$if(annotationType.public)$public $endif$class $annotationType.implName$ implements $annotationType.localName$ {

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

    private $annotationType.implName$($annotationType.fields:{field|final $field.type$ $field.name$}; separator=", "$) {
      $annotationType.fields:{field|
        this.$field.name$ = Preconditions.checkNotNull($field.name$, "$field.name$ has no default value.  It must be specified.");
      }$
    }

    public Class<$annotationType.localName$> annotationType() {
      return $annotationType.localName$.class;
    }

    public int hashCode() {
      return AnnotationUtils.hashCode(this);
    }

    @SuppressWarnings("SimplifiableConditionalExpression")
    public boolean equals(Object other) {
      return other instanceof $annotationType.localName$
         ? AnnotationUtils.equals(this, ($annotationType.localName$) other)
         : false;
    }

    public String toString() {
      return AnnotationUtils.toString(this);
    }

    public static $annotationType.localName$ build() {
      return new Builder().build();
    }

$if(annotationType.valueField)$
    public static $annotationType.localName$ build(final $annotationType.valueField.parameterType$ v) {
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
      $annotationType.localName$ build() {
        return new $annotationType.implName$($annotationType.fields:{field|$field.name$}; separator=", "$);
      }

 $if(annotationType.valueField)$
      public $annotationType.localName$ build(final $annotationType.valueField.parameterType$ v) {
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