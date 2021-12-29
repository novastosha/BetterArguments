package dev.nova.betterarguments;

import dev.nova.betterarguments.arguments.Argument;
import dev.nova.betterarguments.arguments.converters.EnumConverter;
import dev.nova.betterarguments.parser.ArgumentParser;

import java.util.Arrays;

public class MainTest {

    public static void main(String[] args) {

        ArgumentParser parser = new ArgumentParser(args);

        Argument<Decompiler> decompilerArgument = Argument.Builder.create("decompiler", Decompiler.class)
                .withDescription("Choose a decompiler to use. Available decompilers: "+ Arrays.toString(Decompiler.values()))
                .withAliases(new String[]{"decomp"})
                .withValueConverter(new EnumConverter<>(Decompiler.class) {
                    @Override
                    protected Decompiler handleException(String input, IllegalArgumentException exception) {
                        System.out.println("Cannot find any decompiler named: \"" + input + "\", defaulting to " + Decompiler.CFR.name());
                        return Decompiler.CFR;
                    }
                })
                .build(parser);

        Argument<String> stringArgument = Argument.Builder.create("name", String.class)
                .withDescription("Choose a name.")
                .build(parser);

        parser.addHelpArgument();
        parser.parse();

        if(parser.hasArgument("help")) {
            parser.printHelp();
        }

        if (!parser.hasArgument("decompiler")) {
            System.out.println("Missing decompiler!");
            return;
        }

        System.out.println(parser.get(decompilerArgument).name());
    }


}
