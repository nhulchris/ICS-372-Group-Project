package com.brewbite;

/**
 * Plain Java entry point for the executable JAR.
 *
 * This class exists as a workaround so the application can be launched
 * from a fat JAR using `java -jar brewbite-1.0-SNAPSHOT.jar`. Without
 * this indirection, the JVM rejects launching a class that extends
 * javafx.application.Application directly when JavaFX is on the
 * classpath rather than the module path.
 *
 * For development (mvn javafx:run), BrewBiteApp is the entry point
 * directly because the javafx-maven-plugin handles module setup.
 */
public class Launcher {
    public static void main(String[] args) {
        BrewBiteApp.main(args);
    }
}
