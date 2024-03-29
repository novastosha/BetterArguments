package dev.nova.betterarguments.arguments;

import dev.nova.betterarguments.parser.ArgumentParser;
import lombok.Getter;

public class Argument<T> {

    @Getter private final String name;
    @Getter private final String description;
    @Getter private final Class<? extends T> type;
    @Getter private final ValueConverter<T> converter;
    @Getter private final String[] aliases;

    private final ArgumentParser parser;

    private final boolean dataArgument;

    private Argument(String name, String description, Class<? extends T> type, ValueConverter<T> converter, String[] aliases, ArgumentParser parser, boolean dataArgument) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.converter = converter;
        this.aliases = aliases;
        this.parser = parser;
        this.dataArgument = dataArgument;
    }

    public boolean isDataArgument() {
        return dataArgument;
    }

    public T get(){
        return parser.get(this);
    }

    public static class Builder<T> {

        private final String name;
        private final Class<? extends T> type;
        private String description = "No description provided";
        private ValueConverter<T> converter;
        private String[] aliases = new String[0];
        private boolean dataArgument = false;

        private Builder(String name, Class<? extends T> clazz) {
            this.name = name;
            this.type = clazz;
            if(type == null){
                dataArgument = true;
            }
        }

        public static <T> Builder<T> create(String name, Class<? extends T> clazz) {
            return new Builder<>(name, clazz);
        }

        public static <Void> Builder<Void> create(String name) {
            return new Builder<>(name, null);
        }

        public Builder<T> withDescription(String description) {
            if (description == null) return this;

            this.description = description;
            return this;
        }

        public Builder<T> withValueConverter(ValueConverter<T> converter) {
            if(type == null) {
                return this;
            }

            this.converter = converter;
            return this;
        }

        public Builder<T> withAliases(String... otherAliases) {
            if (aliases == null) return this;

            this.aliases = otherAliases;
            return this;
        }

        public Argument<T> build(ArgumentParser parser) {
            Argument<T> argument = new Argument<>(name, description, type, converter, aliases,parser,dataArgument);
            parser.addArgument(argument);
            return argument;
        }
    }

    @FunctionalInterface
    public interface ValueConverter<T> {

        T convert(String raw);

    }
}
