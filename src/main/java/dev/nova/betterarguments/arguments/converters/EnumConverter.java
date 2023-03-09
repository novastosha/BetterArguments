package dev.nova.betterarguments.arguments.converters;

import dev.nova.betterarguments.arguments.Argument;

public abstract class EnumConverter<T extends Enum<T>> implements Argument.ValueConverter<T> {

    private final Class<T> clazz;
    private final boolean handleUppercase;

    public EnumConverter(Class<T> clazz) {
        this(clazz, false);
    }

    public EnumConverter(Class<T> clazz, boolean handleUppercase) {
        this.clazz = clazz;
        this.handleUppercase = handleUppercase;
    }

    @Override
    public T convert(String raw) {
        try {
            return T.valueOf(clazz, raw);
        } catch (IllegalArgumentException exception) {
            if(handleUppercase) {
                try {
                    return T.valueOf(clazz, raw.toUpperCase());
                } catch (IllegalArgumentException exception1) {
                    return handleException(raw, exception);
                }
            }
            else return handleException(raw, exception);
        }
    }

    protected abstract T handleException(String input, IllegalArgumentException exception);
}
