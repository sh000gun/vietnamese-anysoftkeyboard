package net.sourceforge.vietpad.inputmethod;

/**
 * Telex input method, as specified in <i><a href=http://vietunicode.sourceforge.net/inputmethod.html>Common Vietnamese Input Methods</a></i>.
 *
 *@author     Quan Nguyen
 *@version    1.1, 16 October 2005
 */
public class TelexIM implements InputMethod {
    @Override
    public char getAccentMark(char keyChar, char curChar, String curWord) {
        char accent = '\0';
        
        if (Character.isLetter(keyChar)) {
            switch (keyChar)  {
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
        }
        
        return accent;
    }
}
