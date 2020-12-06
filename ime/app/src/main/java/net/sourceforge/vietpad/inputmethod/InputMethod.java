package net.sourceforge.vietpad.inputmethod;

/**
 * Interface for Vietnamese input methods.
 */
public interface InputMethod {
    /**
     * Determines the diacritical mark associated with the typed key based on the current word context.
     *
     * @param keyChar the typed key
     * @param curChar the character at current caret position
     * @param curWord the word at current caret position
     * @return the diacritical mark in numerical VNI convention, or <i>null character</i> for noncombining key
     */
    public char getAccentMark(char keyChar, char curChar, String curWord);
}