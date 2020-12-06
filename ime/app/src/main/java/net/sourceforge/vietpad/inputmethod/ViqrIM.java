package net.sourceforge.vietpad.inputmethod;

/**
 * VIQR input method, as specified in <i><a href=http://vietunicode.sourceforge.net/inputmethod.html>Common Vietnamese Input Methods</a></i>.
 *
 *@author     Quan Nguyen
 *@version    1.1, 16 October 2005
 */
public class ViqrIM implements InputMethod {
    @Override
    public char getAccentMark(char keyChar, char curChar, String curWord) {
        char accent = '\0';

        if (!Character.isLetterOrDigit(keyChar)) {
            switch (keyChar)  {
                case '\'': accent = '1'; break;
                case '`':  accent = '2'; break;
                case '?':  accent = '3'; break;
                case '~':  accent = '4'; break;
                case '.':  accent = '5'; break;
                case '^':  accent = '6'; break;
                case '*':
                case '+':  accent = '7'; break;
                case '(':  accent = '8'; break;
                case '-':  accent = '0'; break;
            }
        } else if (keyChar == 'D' || keyChar == 'd') {
            accent = '9';
        }

        return accent;
    }
}