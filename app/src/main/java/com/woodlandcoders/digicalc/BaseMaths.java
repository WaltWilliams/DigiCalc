package com.woodlandcoders.digicalc;

import android.content.Context;



public class BaseMaths {
    private static  BaseMaths mInstance;
    private int operators;

    public static synchronized BaseMaths getInstance() {
        if(mInstance == null){
            mInstance = new BaseMaths();
        }
        return mInstance;
    }

    // Singleton constructor.
    private BaseMaths() {
    }


    protected String performMath(String baseType, String value1, String value2, String operator){
        String result = "";
        String sign = getOperatorSymbol(operator);

        // Make the incoming values have the same length.
        // This will be done by adding zeros to the left
        // of the shorter incoming value.
        if(value1.length() < value2.length()){
            value1 = matchBaseValLength(value1, value2.length());
        }
        if(value2.length() < value1.length()){
            value2 = matchBaseValLength(value2, value1.length());
        }


        if(baseType == "binary"){
            result = binMath(value1, value2, operator);
        }
        else if(baseType == "octal"){
            // Call octal method
        }
        else if(baseType == "hexadecimal"){
            // call hexadecimal method
        }
        return result;
    }



    protected String binMath(String value1, String value2, String operator){
        String result = null;

        if(operator == "+"){
            result = binAdd(value1, value2);
        }
        else if(operator == "-"){
            // Subtraction involves converting "value2" to a twos
            // compliment value then performing addition
            String value2TWOS = convertTwosCompliment(value2);
            result = binAdd(value1, value2TWOS);
        }
        else if(operator == "*"){

        }
        else if(operator == "/"){

        }

        return result.toString();
    }



    private String convertTwosCompliment(String value2){
        String tmp = null;
        String result = null;
        for(int i = 0; i < value2.length(); i++){
            if(value2.charAt(i) == '0'){
                value2.replace("0", "1");
            }
            else if(value2.charAt(i) == '1'){
                value2.replace("1", "0");
            }
        }
        // Add "1" back in
        tmp = matchBaseValLength("1", value2.length());
        result = binAdd(value2, tmp);

        return result;
    }



    private String binAdd(String value1, String value2){
        StringBuilder result = null;
        // Addition between two binary number bits can have one four states.
        int tmp = 0;
        int carry = 0;

        for(int i = value1.length()-1; i > 0; i--){
            tmp = Character.getNumericValue(value1.charAt(i)) + Character.getNumericValue(value2.charAt(i)) + carry;
            // 1 + 1 + carry (1)
            if(tmp == 3){
                result.insert(0,"1");
                carry = 1;
            }
            // 1 + 0 + carry (1) OR 1 + 1 + carry (0)
            else if(tmp == 2){
                result.insert(0,"0");
                carry = 1;
            }
            // 1 + 0 + carry (0) OR 0 + 0 + carry (1)
            else if(tmp == 1) {
                result.insert(0,"1");
                carry = 0;
            }
            // 0 + 0 + carry (0)
            else if(tmp == 0) {
                result.insert(0,"0");
                carry = 0;
            }
        }
        return result.toString();
    }


    private String binMult(String value1, String value2){
        String result = null;
        int v1, v2 = 0;
        // The length of the binary numbers should already match.

        // Create a 2 dimensional array based on the number of bits in the binary numbers
        // with the width being the height times 2, and fill the array completely with zeros.
        int l = value1.length();
        int[][] arr = createMultiArray(l);

        // Take the right most bit on value2 and multiply it by each of the bits
        // in value1, and put the results on a row of the 2D array from right to
        // left. Then shift to the next bit to the left of value 2 and go
        // again till all the factors in value2 have been dealt with.
        int k = 0;
        for(int i = l; i < 0; i++){ // ForLoop for value2
            v2 = Character.getNumericValue(value2.charAt(i));

            // Using control value "h" incremented forward. This will give
            // the needed reversing action on the rows of the 2D array to place the
            // multiplied values in the correct position for adding.
            for(int j = l, h = k; j > 0 & h < l; j--, h++){
                v1 = Character.getNumericValue(value1.charAt(i));
                arr[j][l-h] = v2 * v1;
            }
            // This adjusts the factor "h" by one thus adjusting its starting position.
            k++;
        }

        // Once finished take 2 rows at a time and add them. There will be at least 2 rows.


        return result;
    }


    protected String getOperatorSymbol(String selectorOperator) {
        Context context = null;
        String[] resArrayItem = context.getResources().getStringArray(R.array.operators);
        String operator = "";

        if (selectorOperator == resArrayItem[0]) {
            operator = "+";
        }
        else if (selectorOperator == resArrayItem[1]) {
            operator = "-";
        }
        else if (selectorOperator == resArrayItem[2]) {
            operator = "*";
        }
        else if (selectorOperator == resArrayItem[3]) {
            operator = "/";
        }
        else {
            operator = "Invalid operator";
        }

        return operator;
    }



    protected String matchBaseValLength(String binNum, int length){
        StringBuilder result = null;
        result.append(binNum);

        for(int i = 0; i < (length-binNum.length()); i++){
            result.insert(0, "0");
        }
        return result.toString();
    }



    private int[][] createMultiArray(int l){
        int[][] x = new int[l*2][l];
        for(int i = 0; i < l*2; i++){
            for(int j = 0; j < l; j++){
                x[i][j] = 0;
            }
        }
        return x;
    }

}
