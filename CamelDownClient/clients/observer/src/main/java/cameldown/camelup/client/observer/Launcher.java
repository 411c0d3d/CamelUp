package cameldown.camelup.client.observer;

/**
 * Needed for Maven Shade Plugin & Debugging with javaFX, because it can't create a jar if the main Method's class extends another one.
 */
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}
