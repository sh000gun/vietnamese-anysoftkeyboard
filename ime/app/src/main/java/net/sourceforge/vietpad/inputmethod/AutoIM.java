package net.sourceforge.vietpad.inputmethod;

/**
 * Auto input method
 *
 *@author     Phi Nguyen
 *@version    1.1, 31 July 2008
 */
public class AutoIM implements InputMethod {
    @Override
    public char getAccentMark(char keyChar, char curChar, String curWord) {
        char accent = '\0';

        if (Character.isDigit(keyChar)) {
            accent = keyChar;
        } else if (Character.isLetter(keyChar)) {
            switch (keyChar) {
                case 'S':
                case 's': accent = '1'; break;
                case 'F':
                case 'f': accent = '2'; break;
                case 'R':
                case 'r': accent = '3'; break;
                case 'X':
                case 'x': accent = '4'; break;
                case 'J':
                case 'j': accent = '5'; break;
                case 'A':
                case 'a':
                case 'E':
                case 'e':
                case 'O':
                case 'o': accent = '6'; break;
                case 'W':
                case 'w': accent = '7'; break;
                case 'D':
                case 'd': accent = '9'; break;
                case 'Z':
                case 'z': accent = '0'; break;
            }

            // Determine accent for common keys shared among a, e, o, w
            if (accent == '6' || accent == '7') {
                accent = VietKeyInput.getAccentInTelex(curWord, keyChar, accent);
            }
        } else {
            switch (keyChar) {
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
        }

        return accent;
    }
}