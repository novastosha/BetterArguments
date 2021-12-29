package dev.nova.betterarguments.arguments;

import dev.nova.betterarguments.parser.ArgumentParser;

public class Argument<T> {

    private final String name;
    private final String description;
    private final Class<? extends T> type;
    private final ValueConverter<T> converter;
    private final String[] aliases;
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

    public String[] getAliases() {
        return aliases;
    }

    public String getName() {
        return name;
    }

    public Class<? extends T> getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public ValueConverter<T> getConverter() {
        return converter;
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

        public static <T> Builder<T> create(String name) {
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

        public Builder<T> withAliases(String[] aliases) {
            if (aliases == null) return this;

            this.aliases = aliases;
            return this;
        }

        public Builder<T> isDataArgument(boolean b){
            this.dataArgument = b;
            return this;
        }

        public Argument<T> build(ArgumentParser parser) {
            Argument<T> argument = new Argument<>(name, description, type, converter, aliases,parser,dataArgument);
            parser.addArgument(argument);
            return argument;
        }
    }

    public interface ValueConverter<T> {

        T convert(String raw);

    }
}
