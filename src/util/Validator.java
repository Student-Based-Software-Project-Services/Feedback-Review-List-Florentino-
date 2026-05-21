package util;

public class Validator {
    public static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isAgeValid(int age) {
        return age >= 1 && age <= 100;
    }

    public static boolean isBrandSelected(boolean[] brandChecks) {
        for (boolean checked : brandChecks) {
            if (checked) return true;
        }
        return false;
    }
}
