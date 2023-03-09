package dev.nova.betterarguments.parser;

import dev.nova.betterarguments.arguments.Argument;

import java.util.*;


public class ArgumentParser {

    private enum Type {
        DATA;
    }

    private final String[] args;
    private final ArrayList<Argument<?>> arguments;
    private final HashMap<Argument<?>, Object> data;
    private boolean parsed = false;

    public ArgumentParser(String[] args) {
        this.args = args;
        this.arguments = new ArrayList<Argument<?>>();
        this.data = new HashMap<>();
    }

    public void parse() {

        if (parsed) return;

        int index = 0;
        for (String argumentRAW : args) {
            if (argumentRAW.startsWith("--")) {
                argumentRAW = argumentRAW.replaceFirst("--", "");


                Argument<?> argument = getArgument(argumentRAW);

                boolean usingEquals = false;
                String[] detection = argumentRAW.split("=", 2);

                if (detection.length > 1) {
                    argument = getArgument(detection[0]);
                    usingEquals = true;
                }

                if (argument == null) {
                    continue;
                }

                if (argument.isDataArgument()) {
                    data.put(argument, Type.DATA);
                    continue;
                }

                String argsRaw;

                if (!usingEquals) {
                    try {
                        argsRaw = args[index + 1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                } else {
                    argsRaw = detection[1];
                }


                Object converted = argument.getConverter() == null ? null : argument.getConverter().convert(argsRaw);


                if (argument.getType().isAssignableFrom(String.class)) {
                    converted = argsRaw;
                } else if (argument.getType().isAssignableFrom(Integer.class)) {
                    converted = Integer.parseInt(argsRaw);
                } else if (argument.getConverter() == null) {
                    throw new RuntimeException("Argument passed without converter!");
                }

                data.put(argument, converted);
            }
            index++;
        }

        parsed = true;

    }

    public List<Argument<?>> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    public <T> void addArgument(Argument<T> argument) {
        if (!validateArgument(argument)) return;
        arguments.add(argument);
    }

    private <T> boolean validateArgument(Argument<T> argument) {
        for (Argument<?> argumentA : arguments) {
            if (argumentA.getName().equalsIgnoreCase(argument.getName()) || Arrays.asList(argumentA.getAliases()).contains(argument.getName()))
                return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public <T> Argument<T> getArgument(String name, Class<? extends T> clazz) {
        for (Argument<?> argument : arguments) {
            if (argument.getName().equalsIgnoreCase(name) || Arrays.asList(argument.getAliases()).contains(name)) {
                return (Argument<T>) argument;
            }
        }
        return null;
    }

    private Argument<?> getArgument(String name) {
        for (Argument<?> argument : arguments) {
            if (argument.getName().equalsIgnoreCase(name) || Arrays.asList(argument.getAliases()).contains(name)) {
                return argument;
            }
        }
        return null;
    }


    public boolean hasArgument(String name) {
        if (!parsed) return false;
        for (Argument<?> argument : data.keySet()) {
            if (argument.getName().equalsIgnoreCase(name) || Arrays.asList(argument.getAliases()).contains(name)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Argument<T> argument) {
        if (!arguments.contains(argument) || !parsed || argument.isDataArgument()) return null;

        return (T) data.get(argument);
    }
}
