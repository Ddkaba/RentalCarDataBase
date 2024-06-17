public class StringContains {
    public StringContains(){}

    public static boolean containsSubstring(String text, String pattern) {
        for (int i = 0; i <= text.length() - pattern.length(); i++) { // Проходим по всем символам текста
            boolean found = true;
            // Проверяем, совпадает ли подстрока с паттерном, начиная с текущей позиции в тексте
            for (int j = 0; j < pattern.length(); j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    found = false;
                    break;
                }
            }
            if (found)  return true; // Если подстрока найдена, возвращаем true
        }
        return false; // Если ни одно вхождение не найдено, возвращаем false
    }
}
