package net.peachjean.tater.utils;

/**
 *
 */
public abstract class Optional<T> {

    public abstract T get() throws IllegalStateException;

    public abstract boolean isPresent();

    public abstract T or(T other);

    public static <T> Optional<T> of(final T value)
    {
        return new Optional<T>() {
            @Override
            public T get() throws IllegalStateException {
                return value;
            }

            @Override
            public boolean isPresent() {
                return true;
            }

            @Override
            public T or(T other) {
                return value;
            }
        };
    }

    public static <T> Optional<T> absent()
    {
        return new Optional<T>() {
            @Override
            public T get() throws IllegalStateException {
                throw new IllegalStateException("There is no value present.");
            }

            @Override
            public boolean isPresent() {
                return false;
            }

            @Override
            public T or(T other) {
                return other;
            }
        };
    }

    public static <T> Optional<T> fromNullable(T value)
    {
        return value == null
                ? Optional.<T>absent()
                : Optional.of(value);
    }
}
