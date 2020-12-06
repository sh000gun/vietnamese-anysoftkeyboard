package net.sourceforge.vietpad.inputmethod;

/**
 * VNI input method, as specified in <i><a href=http://vietunicode.sourceforge.net/inputmethod.html>Common Vietnamese Input Methods</a></i>.
 *
 *@author     Quan Nguyen
 *@version    1.1, 16 October 2005
 */
public class VniIM implements InputMethod {
    @Override
    public char getAccentMark(char keyChar, char curChar, String curWord) {
        char accent = '\0';

        if (Character.isDigit(keyChar)) {
            accent = keyChar;
        }

        return accent;
    }
}