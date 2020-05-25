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



    public static DataContainer container = new DataContainer();

    public static DataContainer etBehavior(CharSequence cs, CharSequence et1, int cp, boolean isMinus, boolean isValue, boolean isBackSpace){
        StringBuilder sb = new StringBuilder(et1);
        // Inserting or removing a minus sign. This must
        // always be at the left end of the string.
        if(isMinus){
            if(sb.length() == 0){
                sb.append(cs);
                cp++;
            }
            else if(sb.charAt(0) != '-'){
                sb.insert(0, '-');
                cp++;
            }
            else if(sb.charAt(0) == '-'){
                sb.replace(0, 1, "");
                cp--;
            }
        }

        // Delete button and value insertion controls.
        if(cp >= 0 &  isValue){
            sb.insert(cp, cs);
            cp++;
        }
        else if(cp > 0 & isBackSpace){
            cp--;
            sb.deleteCharAt(cp);
        }

        // Back and forward button controls.
        if(cs == "<" & cp > 0){
            cp--;
        }
        if(cs == ">" & cp < sb.length()){
            cp++;
        }

        container.cs = sb;  // Return the StringBuilder content. Not what was passed in.
        container.pos = cp;

        return container;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // General methods ///////////////////////////////////////////////////////////////////////////////////////////////
    // These methods used by other classes.
    ///////////////////////////////////////////////////////////////////////////////////////////////
    protected static String trimLeadingZeros(String x){
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
    protected static String[] prepValues(String value1, String value2){
        String[] result = new String[5];
        result[2] = "0";
        result[3] = "0";
        StringBuilder v1 = new StringBuilder(value1);
        StringBuilder v2 = new StringBuilder(value2);

        // Analyzing each value for negative signs.
        if(value1.charAt(0) == '-'){
            result[2] = "1"; // 1 = it is negative or true.
        }
        if(value2.charAt(0) == '-'){
            result[3] = "1"; //1 = it is negative or true.
        }

        // Removing all negative signs.
        for(int m = 0; m < v1.length(); m++){
            if(v1.charAt(m) == '-'){
                v1.deleteCharAt(m);
            }
        }
        for(int n = 0; n < v2.length(); n++){
            if(v2.charAt(n) == '-'){
                v2.deleteCharAt(n);
            }
        }

        // Trim off the excess zeros on the left so that a couple of
        //clean determinations can be made.
        if(v1.charAt(0) == '0'){
            v1.replace(0, v1.length(), trimLeadingZeros(v1.toString()));
        }
        if(v2.charAt(0) == '0'){
            v2.replace(0, v2.length(), trimLeadingZeros(v2.toString()));
        }

        // If after trimming the excess leading zeros from the value(s), and it is
        // a single zero and it was input as a negative value, it is no longer negative.
        if((v1.length() == 1) && (v1.charAt(0) == '0')){
            result[2] = "0";
        }
        if((v2.length() == 1) && (v2.charAt(0) == '0')){
            result[3] = "0";
        }

        // For division this is the divisor length after trimming off the zeros
        // before length matching with the dividend (v1)
        result[4] = String.valueOf(v2.length());

        // Now to pad the smaller value with zeros on the left.
        String value1a;
        String value2a;
        if(v1.length() < v2.length()){
            value1a = padValue(v1.toString(), v2.toString());
            value2a = v2.toString();
        }
        else if(v1.length() > v2.length()){
            value2a = padValue(v2.toString(), v1.toString());
            value1a = v1.toString();
        }
        else{
            value1a = v1.toString();
            value2a = v2.toString();
        }

        result[0] = value1a;
        result[1] = value2a;

        return result;
    }


    // This created the array for the multiplication functions.
    protected static int[][] createMultiArray(int l){
        int[][] x = new int[l][l*2];
        for(int i = 0; i < l; i++){
            for(int j = 0; j*2 < l; j++){
                x[i][j] = 0;
            }
        }
        return x;
    }


    // Pad a value so both are the same length.
    protected static String padValue(String shorter, String longer){
        StringBuilder result = new StringBuilder(shorter);

        for(int i = longer.length()-1; i >= shorter.length(); i--){
            result.insert(0, "0");
        }

        return result.toString();
    }


    protected static boolean isValueZero(String val){
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
