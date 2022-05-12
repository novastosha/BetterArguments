# BetterArguments
A simple, yet useful argument parser that uses builders.

## Usage

Create your argument parser supplied with your raw **string** arguments

```java
ArgumentParser parser = new ArgumentParser(args);
```

Create an argument

```java
Argument<String> name = Argument.Builder.create("name", String.class)
                // Set the aliases
                .withAliases(new String[]{"n","ln"})
                // Add a description
                .withDescription("Your name.")
                // Handle the argument conversion manually (in the case of numbers or strings, it is unnecessary but possible)
                .withValueConverter(new Argument.ValueConverterr<String>() {
                      public String convert(String raw) {
                          return raw.equalsIgnoreCase("jojo") ? "JOJO" : raw;
                      }
                })
                // Build the argument
                .build(parser);
```

***NOTE: Adding value converters is necessary in other object types***

Adding a help argument *(optional)*

```java
parser.addHelpArgument();
```

Now, to parse the arguments, call

```java
parser.parse();
```

### Checking argument presence

```java
if(parser.hasArgument("name")) {
  System.out.println("Hello, "+name.get());
}else{
  System.out.println("You didn't input your name, how sad ;-;");
}
```
