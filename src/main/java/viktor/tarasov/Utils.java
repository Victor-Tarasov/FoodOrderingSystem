package viktor.tarasov;

public class Utils {

    public static void notEmpty(String name, String string) {
        throwIfInvalid(string == null, name + " can't be null.");
        throwIfInvalid(string.isEmpty(), name + " can't be empty.");
    }

    public static void throwIfInvalid(boolean isInvalid, String message) throws IllegalArgumentException {
        if (isInvalid) {
            throw new IllegalArgumentException(message);
        }
    }
}
