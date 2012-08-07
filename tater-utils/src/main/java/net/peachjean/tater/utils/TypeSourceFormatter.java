package net.peachjean.tater.utils;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import javax.annotation.Nullable;
import javax.lang.model.type.*;
import javax.lang.model.util.TypeKindVisitor6;

class TypeSourceFormatter extends TypeKindVisitor6<String,Utils> {
    public static TypeSourceFormatter INSTANCE = new TypeSourceFormatter();

    private TypeSourceFormatter() {}

    @Override
    public String visitPrimitiveAsBoolean(PrimitiveType t, Utils o) {
        return boolean.class.getName();
    }

    @Override
    public String visitPrimitiveAsByte(PrimitiveType t, Utils o) {
        return byte.class.getName();
    }

    @Override
    public String visitPrimitiveAsShort(PrimitiveType t, Utils o) {
        return short.class.getName();
    }

    @Override
    public String visitPrimitiveAsInt(PrimitiveType t, Utils o) {
        return int.class.getName();
    }

    @Override
    public String visitPrimitiveAsLong(PrimitiveType t, Utils o) {
        return long.class.getName();
    }

    @Override
    public String visitPrimitiveAsChar(PrimitiveType t, Utils o) {
        return char.class.getName();
    }

    @Override
    public String visitPrimitiveAsFloat(PrimitiveType t, Utils o) {
        return float.class.getName();
    }

    @Override
    public String visitPrimitiveAsDouble(PrimitiveType t, Utils o) {
        return double.class.getName();
    }

    @Override
    public String visitDeclared(DeclaredType t, final Utils o) {
        final String enclosing = resolveEnclosingStringPortion(t, o);

        final String base = enclosing + "." + t.asElement().getSimpleName();
        if(t.getTypeArguments().size() > 0) {
            String typeArgs = Joiner.on(",").join(Iterables.transform(t.getTypeArguments(), new Function<TypeMirror, String>() {
                @Override
                public String apply(@Nullable TypeMirror input) {
                    return input.accept(TypeSourceFormatter.this, o);
                }
            }));
            return base + "<" + typeArgs + ">";
        }
        return base;
    }

    private String resolveEnclosingStringPortion(DeclaredType t, Utils o) {
        final String value = t.getEnclosingType().accept(this, o);
        if(value == null) {
            return o.getElements().getPackageOf(t.asElement()).getQualifiedName().toString();
        }
        return value;
    }

    @Override
    public String visitArray(ArrayType t, Utils o) {
        return t.getComponentType().accept(this, o) + "[]";
    }

    @Override
    public String visitNoTypeAsPackage(NoType t, Utils aVoid) {
        return t.toString();
    }

    @Override
    public String visitTypeVariable(TypeVariable t, Utils aVoid) {
        return t.toString();
    }

    @Override
    public String visitWildcard(WildcardType t, Utils aVoid) {
        StringBuilder sb = new StringBuilder("?");
        if(t.getExtendsBound() != null) {
            sb.append(" extends " + t.getExtendsBound().accept(this, aVoid));
        }
        if(t.getSuperBound() != null) {
            sb.append(" super " + t.getSuperBound().accept(this, aVoid));
        }
        return sb.toString();
    }
}
