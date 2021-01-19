package com.woodlandcoders.digicalc;

public class CommonUtils {

    private static CommonUtils mInstance;

    public static synchronized CommonUtils getInstance() {
        if(mInstance == null){
            mInstance = new CommonUtils();
        }
        return mInstance;
    }

    // Singleton constructor.
    private CommonUtils() {
    }



    public final DataContainer container = new DataContainer();

    public DataContainer etBehavior(CharSequence wCharIn, CharSequence wCharString, int wCursorPosition, boolean wIsMinus, boolean wIsValue, boolean wIsBackSpace){
        StringBuilder sb = new StringBuilder(wCharString);
        // Inserting or removing a minus sign. This must
        // always be at the left end of the string.
        if(wIsMinus){
            if(sb.length() == 0){
                sb.append(wCharIn);
                wCursorPosition++;
            }
            else if(sb.charAt(0) != '-'){
                sb.insert(0, '-');
                wCursorPosition++;
            }
            else if(sb.charAt(0) == '-'){
                sb.replace(0, 1, "");
                if(wCursorPosition > 0){
                    wCursorPosition--;
                }
            }
        }

        // This block of code is to fix a glitch. If the cursor is arrowed
        // over to the left side of a minus sign you would still be able
        // to enter digits. This code moves the cursor to the right,
        // so the digit can be entered on the correct side of the minus sign.
        if(wIsValue){
            if(wCharString.length() > 0){
                if(wCharString.charAt(0) == '-'){
                    if(wCursorPosition == 0){
                        wCursorPosition = 1;
                    }
                }
            }
        }



        // Delete button and value insertion controls.
        if(wCursorPosition >= 0 &  wIsValue){
            sb.insert(wCursorPosition, wCharIn);
            wCursorPosition++;
        }
        else if(wCursorPosition > 0 & wIsBackSpace){
            wCursorPosition--;
            sb.deleteCharAt(wCursorPosition);
        }

        // Back and forward button controls.
        if(wCharIn == "<" & wCursorPosition > 0){
            wCursorPosition--;
        }
        if(wCharIn == ">" & wCursorPosition < sb.length()){
            wCursorPosition++;
        }

        container.cs = sb;  // Return the StringBuilder content. Not what was passed in.
        container.pos = wCursorPosition;

        return container;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // General methods ///////////////////////////////////////////////////////////////////////////////////////////////
    // These methods used by other classes.
    ///////////////////////////////////////////////////////////////////////////////////////////////
    protected String trimLeadingZeros(String x){
        StringBuilder trimmed = new StringBuilder(x);

        // Its strange how I have to extract the length to insert into the for
        // loop before hand rather than using "trimmed.length()" directly.
        int l = trimmed.length();

        for(int i = 0; i < l-1; i++){
            if(Character.getNumericValue(trimmed.charAt(0)) == 0){
                trimmed.deleteCharAt(0);
            }
            else if(Character.getNumericValue(trimmed.charAt(0)) > 0){
                break;
            }
        }

        return trimmed.toString();
    }

    // The purpose of this method is to remove the negative signs
    // and pad the left end of a value if its shorter.
    protected String[] prepValues(String value1, String value2){
        String[] result = new String[6];
        // This is the "isEmpty" element. Defaulting it to empty.
        result[5] = "0";
        // This is the "isNeg" elements. Defaulting to positive.
        result[2] = "0";
        result[3] = "0";
        StringBuilder v1 = new StringBuilder(value1);
        StringBuilder v2 = new StringBuilder(value2);

        // If an empty value was passed in don't enter this
        // group of functions.
        if(value1.length() != 0 && value2.length() != 0) {
            // Analyzing each value for negative signs.
            if (value1.charAt(0) == '-') {
                result[2] = "1"; // 1 = it is negative or true.
            }
            if (value2.charAt(0) == '-') {
                result[3] = "1"; //1 = it is negative or true.
            }

            // Removing all negative signs.
            for (int m = 0; m < v1.length(); m++) {
                if (v1.charAt(m) == '-') {
                    v1.deleteCharAt(m);
                }
            }
            for (int n = 0; n < v2.length(); n++) {
                if (v2.charAt(n) == '-') {
                    v2.deleteCharAt(n);
                }
            }
        }

        // If only a negative sign was passed in for either value
        // the negative sign was stripped off by the statements
        // above. This will make either v1 or v2 empty.
        // Don't proceed with the following statements below.
        if (v1.length() != 0 && v2.length() != 0) {
            // Trim off the excess zeros on the left so that a couple of
            // clean determinations can be made.
            if (v1.charAt(0) == '0') {
                v1.replace(0, v1.length(), trimLeadingZeros(v1.toString()));
            }
            if (v2.charAt(0) == '0') {
                v2.replace(0, v2.length(), trimLeadingZeros(v2.toString()));
            }

            // If after trimming the excess leading zeros from the value(s), and it is
            // a single zero and it was input as a negative value, it is no longer negative.
            if ((v1.length() == 1) && (v1.charAt(0) == '0')) {
                result[2] = "0";
            }
            if ((v2.length() == 1) && (v2.charAt(0) == '0')) {
                result[3] = "0";
            }

            // For division this is the divisor length after trimming off the zeros
            // before length matching with the dividend (v1)
            result[4] = String.valueOf(v2.length());

            // Now to pad the smaller value with zeros on the left.
            String value1a;
            String value2a;
            if (v1.length() < v2.length()) {
                value1a = padValue(v1.toString(), v2.toString());
                value2a = v2.toString();
            } else if (v1.length() > v2.length()) {
                value2a = padValue(v2.toString(), v1.toString());
                value1a = v1.toString();
            } else {
                value1a = v1.toString();
                value2a = v2.toString();
            }
            result[0] = value1a;
            result[1] = value2a;
            // If both values are NOT empty
            result[5] = String.valueOf(1);
        }
        return result;
    }

    // This created the array for the multiplication functions.
    protected int[][] createMultiArray(int l){
        int[][] x = new int[l][l*2];
        for(int i = 0; i < l; i++){
            for(int j = 0; j*2 < l; j++){
                x[i][j] = 0;
            }
        }
        return x;
    }


    // Pad a value so both are the same length.
    protected String padValue(String shorter, String longer){
        StringBuilder result = new StringBuilder(shorter);

        for(int i = longer.length()-1; i >= shorter.length(); i--){
            result.insert(0, "0");
        }

        return result.toString();
    }


    protected boolean isValueZero(String val){
        boolean YorN = true;
        for(int z = 0; z < val.length(); z++){
            if(Character.getNumericValue(val.charAt(z)) == 0){
                // Do nothing.
            }
            else{
                return false;
            }
        }
        return YorN;
    }

}
