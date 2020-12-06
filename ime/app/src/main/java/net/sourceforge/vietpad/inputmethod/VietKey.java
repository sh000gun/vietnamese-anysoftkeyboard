package net.sourceforge.vietpad.inputmethod;

import android.view.KeyEvent;

import com.anysoftkeyboard.base.utils.Logger;

import java.text.BreakIterator;
import java.util.*;


/**
 * Listener for Vietnamese key entries. 
 * Acts as an input preprocessor to <code>VietKeyInput</code> module, the keyboard engine.
 * <p>
 * A listener object created from this class, when registered with a text component 
 * using the component's <code>addKeyListener</code> method,
 * gives the component the capability of Vietnamese text input. 
 * The class has numerous static methods to provide extra niceties 
 * to the registered text components. It includes support for shorthand, 
 * smart marking, and selection of popular Vietnamese input methods. A typical application is as follows:
 * <pre>
 *     JTextComponent textComp = new JTextArea();
 *     VietKeyListener keyLst = new VietKeyListener(textComp);
 *     textComp.addKeyListener(keyLst);
 *     VietKeyListener.setInputMethod(InputMethods.Telex);
 *     VietKeyListener.setVietModeEnabled(true);
 *     VietKeyListener.setSmartMark(true);
 * </pre>
 * A convenient <i><a href=http://prdownloads.sourceforge.net/vietpad/VietKeyInput.JAR.zip>JAR</a></i> package is provided for easy integration to your program. A practical example can be found in 
 * <i><a href=http://vietpad.sourceforge.net>VietPad</a></i>, a full-featured Vietnamese Unicode text editor.
 * 
 * @author Quan Nguyen
 * @author Quang D. Le
 *
 * @version 1.3, 11 July 2006
 */

public class VietKey {
    private static InputMethods selectedInputMethod = InputMethods.Telex; // default keyboard layout
    private static InputMethod inputMethod = InputMethodFactory.createInputMethod(selectedInputMethod);
    private static boolean smartMarkOn = true;
    private static boolean vietModeOn = true;
    private String vietWord;
    private char  vietChar;
    private char accent;
    private static boolean repeatKeyConsumed;

    private static final char ESCAPE_CHAR = '\\';
    private static final String SHIFTING_CHARS = "cmnpt"; // chars potentially cause shifting of diacritical marks
    private static final String VOWELS = "aeiouy";
    private static final String NON_ACCENTS = "!@#$%&)_={}[]|:;/>,";
    protected static final String TAG = "VKI";

    /**
     * Creates a new <CODE>VietKey</CODE>.
     *
     */
    public VietKey()  {

    }

    /**
     * Enables Vietnamese mode for key input.
     *
     * @param mode true to enable entry of Vietnamese characters (enabled by default)
     */    
    public static void setVietModeEnabled(final boolean mode) {
        vietModeOn = mode;
    }
      
    /**
     * Sets the input method.
     *
     * @param method one of the supported input methods: VNI, VIQR, or Telex
     */    
    public static void setInputMethod(final InputMethods method) {
        selectedInputMethod = method;
        inputMethod = InputMethodFactory.createInputMethod(selectedInputMethod);
    }    

    /**
     * Gets the current input method.
     *
     * @return the current input method
     */    
    public static InputMethods getInputMethod() {
        return selectedInputMethod;
    }    
    /**
     * Sets the SmartMark capability on or off.
     *
     * @param smartMark true to enable automatic placement of diacritical marks on appropriate vowels in a word;<br>
     * otherwise, they must be typed immediately after the character they qualify.
     */    
    public static void setSmartMark(final boolean smartMark) {
        smartMarkOn = smartMark;
    }

    /**
     * Sets the diacritics position to follow the classic style (\u00f2a, \u00f2e, \u00fay), as opposed to the modern style (o\u00e0, o\u00e8, u\u00fd).
     *
     * @param classic true for classic; false for modern (default)
     */
    public static void setDiacriticsPosClassic(final boolean classic) {    
        VietKeyInput.setDiacriticsPosClassic(classic);
    }

    /**
     * Invoked when a key has been typed.
     *
     *
     * @return
     */    
    public String keyTyped(char keyChar, char curChar, String curWord)  {

        Logger.d(TAG, "curChar: " + curChar);

        if (curChar != ESCAPE_CHAR && !Character.isLetter(curChar))  {
            return "";
        }

        //keyChar = 's'; // register last key entry
        Logger.d(TAG, "keyChar: " + keyChar);


        if (!vietModeOn) {
            return ""; // exit processing if Viet Mode is off
        }

        if (Character.isWhitespace(keyChar) || NON_ACCENTS.indexOf(keyChar) != -1 || keyChar == '\b') {
            return ""; // skip over keys not used for accent marks for faster typing
        }

        Logger.d(TAG, "curWord: " + curWord);


        // Shift the accent to the second vowel in a two-consecutive-vowel sequence, if applicable
        if (smartMarkOn) {
            if (curWord.length() >= 2 &&
                    (SHIFTING_CHARS.indexOf(Character.toLowerCase(keyChar)) >= 0 ||
                    VOWELS.indexOf(Character.toLowerCase(keyChar)) >= 0 )) {
                try  {
                    String newWord;
                    // special case for "qu" and "gi"
                    if (curWord.length() == 2 &&
                            VOWELS.indexOf(Character.toLowerCase(keyChar)) >= 0 &&
                            (curWord.toLowerCase().startsWith("q") || curWord.toLowerCase().startsWith("g") )) {
                        newWord = VietKeyInput.shiftAccent(curWord+keyChar, keyChar);
                        if (!newWord.equals(curWord+keyChar)) {
                            Logger.d(TAG, "newWord: " + newWord);
                            return newWord;
                        }
                    }

                    newWord = VietKeyInput.shiftAccent(curWord, keyChar);
                    if (!newWord.equals(curWord)) {
                        Logger.d(TAG, "newWord: " + newWord);
                        curWord = newWord;
                    }
                } catch (StringIndexOutOfBoundsException exc)  {
                    System.err.println("Caret out of bound! (For Shifting Marks)");
                }
            }
        }

        accent = getAccentMark(keyChar, curChar, curWord);

        try  {
            if (Character.isDigit(accent))  {
                if (smartMarkOn)  {
                    vietWord = (curChar == ESCAPE_CHAR) ?
                            String.valueOf(keyChar) : VietKeyInput.toVietWord(curWord, accent);
                    if (!vietWord.equals(curWord)) {

                        if (!VietKeyInput.isAccentRemoved() || repeatKeyConsumed) {
                            // accent removed by repeat key, not '0' key
                            Logger.d(TAG, "!VietKeyInput.isAccentRemoved() || repeatKeyConsumed");
                        }


                        Logger.d(TAG, "vietWord: " + vietWord.substring(0, vietWord.length()));
                        return vietWord.substring(0, vietWord.length());
                    }
                } else {
                    vietChar = (curChar == ESCAPE_CHAR)? keyChar: VietKeyInput.toVietChar(curChar, accent);
                    if (vietChar != curChar) {

                        Logger.d(TAG, "vietChar != curChar: " + vietChar);


                        if (!VietKeyInput.isAccentRemoved() || repeatKeyConsumed) {
                            // accent removed by repeat key, not '0' key
                            Logger.d(TAG, "!VietKeyInput.isAccentRemoved() || repeatKeyConsumed");
                        }

                        return String.valueOf(vietChar);
                    }
                }
            }
        }
        catch (StringIndexOutOfBoundsException exc)  {
            System.err.println("Caret out of bound!");
        }

        return curWord + keyChar;
    }

    /**
     * Sets to consume the accent key when it is repeated to remove the diacritical mark just entered.
     *
     * @param mode true to consume the accent key when it is used to remove the diacritical mark just entered; false otherwise
     */
    public static void consumeRepeatKey(final boolean mode) {
        repeatKeyConsumed = mode;
    }

    /**
     * Returns the accent mark.
     *
     * The diacritical marks follows VNI input style:<p>
     * <code>1:', 2:`, 3:?, 4:~, 5:., 6:^, 7:+, 8:(, 9:-, 0:remove diacritics<br>
     * Also, repeating the accent key removes the accent just entered.</code>
     *
     * @param   keyChar     key input
     * @return  accent      a Vietnamese diacritical mark
     */
    private char getAccentMark(char keyChar, char curChar, String curWord) {
        return inputMethod.getAccentMark(keyChar, curChar, curWord);
    }
}
