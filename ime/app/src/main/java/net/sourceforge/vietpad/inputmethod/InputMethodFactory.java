package net.sourceforge.vietpad.inputmethod;

/**
 * Factory method to instantiate the selected input method.
 *
 *@author     Quan Nguyen
 *@version    1.2, 9 September 2008
 */
public class InputMethodFactory {  
    /**
     * Creates the selected input method.
     *
     * @param inputMethod one of the supported input methods: VNI, VIQR, or Telex (default)
     */
    public static InputMethod createInputMethod(InputMethods inputMethod) {
        InputMethod im;
        
        if (inputMethod == InputMethods.VNI) {
            im = new VniIM();
        } else if (inputMethod == InputMethods.VIQR) {
            im = new ViqrIM();
//            im = new PaliSanskritDegaIM();
        } else if (inputMethod == InputMethods.Auto) {
            im = new AutoIM();        
        } else {
            im = new TelexIM();
        }
        
        return im;
    }
}
