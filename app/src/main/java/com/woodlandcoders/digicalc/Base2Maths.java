package com.woodlandcoders.digicalc;


public class Base2Maths {
    private static Base2Maths mInstance;

    public static synchronized Base2Maths getInstance() {
        if(mInstance == null){
            mInstance = new Base2Maths();
        }
        return mInstance;
    }

    // Singleton constructor.
    private Base2Maths() {
    }

    CommonUtils cu = CommonUtils.getInstance();
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Binary math //////////////////////////////////////////////////////////////////////////////////////////////////
    protected String binMath(String value1, String value2, String operatorFromUI){
        StringBuilder result = new StringBuilder();


        // PrepValues does the following
        // 1) Records if a/the value(s) is negative.
        // 2) Strip off the negative sign(s) from the input value(s).
        // 3) Clean off the excess leading zeros. This is needed to make a couple clean determination about the input values.
        // 4) Record the length of value2 as the divisor length.
        // 5) If either input was a negative zero the negative record is reset to false in that its not negative. 0 instead of -0
        // 6) Match length both input values by padding the shorter value with leading zeros.
        String[] values = cu.prepValues(value1, value2);

        // Making a boolean from the negative record from prepValues.
        boolean isOneNeg = false;
        boolean isTwoNeg = false;
        if(values[2].compareTo("1") == 0){
            isOneNeg = true;
        }
        if(values[3].compareTo("1") == 0){
            isTwoNeg = true;
        }

        // Determine which value is larger
        // This method checks if the first parameter value is larger than second parameter value.
        // If the first parameter is larger it returns true. Else false.
        boolean isOneLarger = isValueLarger2(values[0], values[1]);
        boolean isTwoLarger = isValueLarger2(values[1], values[0]);


        if(operatorFromUI.compareTo("Add") == 0){
            // ----------->>>  isOneNeg and isTwoNeg default to false.
            //  So to be true isOneNeg and isTwoNeg have the "!" in front.
            //  Negating them says that it is true that they
            //  are not negative. EX: !isOneNeg == positive. or true.

            // Now add based on 6 state.
            // State 1 and 6. Both values positive or both negative values.
            // It doesn't matter which larger.
            if((!isOneNeg & !isTwoNeg) | (isOneNeg & isTwoNeg)){
                result.insert(0, binAdd(values[0], values[1], false));
                result.replace(0, result.length(), cu.trimLeadingZeros(result.toString()));
                if((isOneNeg & isTwoNeg)){
                    result.insert(0, "-");
                }
                if((result.length() == 2) && (result.charAt(0) == '-') & (result.charAt(1) == '0')){
                    result.deleteCharAt(0);
                }

            }
            // State 2. Large negative value + smaller positive value
            if(isOneNeg && !isTwoNeg && isOneLarger){
                String twosCompx = deriveTwosCompliment(values[0]);
                // While converting to 2s compliment a digit might get dropped.
                String twosComp = cu.padValue(twosCompx, values[1]);
                result.insert(0, binAdd(twosComp, values[1], true));
                result.replace(0, result.length(), deriveTwosCompliment(result.toString()));
                result.replace(0, result.length(), cu.trimLeadingZeros(result.toString()));
                result.insert(0, "-");
            }
            // State 3. Small negative value + larger positive value
            if(isOneNeg && !isTwoNeg && isTwoLarger){
                String twosCompx = deriveTwosCompliment(values[0]);
                // While converting to 2s compliment a digit might get dropped.
                String twosComp = cu.padValue(twosCompx, values[1]);
                result.insert(0, binAdd(twosComp, values[1], true));
                result.replace(0, result.length(), cu.trimLeadingZeros(result.toString()));
            }
            // State 4. Smaller positive value + larger negative value.
            if(!isOneNeg && isTwoNeg && isTwoLarger){
                String twosCompx = deriveTwosCompliment(values[1]);
                // While converting to 2s compliment a digit might get dropped.
                String twosComp = cu.padValue(twosCompx, values[0]);
                result.insert(0, binAdd(values[0], twosComp, false));
                result.replace(0, result.length(), deriveTwosCompliment(result.toString()));
                result.replace(0, result.length(), cu.trimLeadingZeros(result.toString()));
                result.insert(0, "-");
            }
            // State 5. Larger positive  value + small negative value.
            if(!isOneNeg && isTwoNeg && isOneLarger){
                String twosCompx = deriveTwosCompliment(values[1]);
                // While converting to 2s compliment a digit might get dropped.
                String twosComp = cu.padValue(twosCompx, values[0]);
                result.insert(0, binAdd(values[0], twosComp, true));
                result.replace(0, result.length(), cu.trimLeadingZeros(result.toString()));
            }
        }
        else if (operatorFromUI.compareTo("Mult") == 0) {
            // Is each value negative. ///////////////////////////////////
            boolean mkNeg = false;

            if(isOneNeg || isTwoNeg){
                mkNeg = true;
            }
            if(isOneNeg && isTwoNeg){
                mkNeg = false;
            }

            result.insert(0, binMult(values[0], values[1]));

            if(mkNeg){
                result.insert(0, "-");
            }
        }
        else if(operatorFromUI.compareTo("Div") == 0){
            // Thou shalt not divide by zero
            if(cu.isValueZero(values[1])){
                return "Thou Shalt Divide By Zero!";
            }
            // The dividend needs to be larger that the divisor. If not
            // dump out with a message.
            else if(isValueLarger2(values[1], values[0])){
                return "The dividend must be larger than the divisor";
            }

            // The first value divided (dividend) by the second value (divisor).
            // The third parameter is the length of the divisor derived from the "prepValues" method.
            result.insert(0, binDiv(values[0], values[1], Integer.parseInt(values[4])));

            if((isOneNeg & !isTwoNeg) | (!isOneNeg & isTwoNeg)){
                result.insert(0, "-");
            }
        }
        return result.toString();
    }

//
    //result.replace(0, result.length(), trimLeadingZeros(result.toString()));


    private String binAdd(String value1, String value2, boolean isSubtraction){
        StringBuilder result = new StringBuilder();
        // Addition between two binary number bits can have one four states.
        int tmp = 0;
        int carry = 0;
        StringBuilder value2a = new StringBuilder(cu.padValue(value2, value1));

        for(int i = value1.length()-1; i >= 0; i--){
            int f = Character.getNumericValue(value1.charAt(i));
            int g = Character.getNumericValue(value2a.charAt(i));
            tmp = Character.getNumericValue(value1.charAt(i)) + Character.getNumericValue(value2a.charAt(i)) + carry;
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
        if(carry == 1 & !isSubtraction){
            result.insert(0, carry);
        }
        // You must NOT trim the leading zeros before returning.
        return result.toString();
    }




    private String binMult(String value1, String value2){
        // If both values are zero, screw it I don't want to bother process them.
        // Simply return zero.
        if((value1.compareTo("0") == 0) && (value2.compareTo("0") == 0)){
            return "0";
        }
        StringBuilder result = new StringBuilder();
        int[][] arr;
        int v1 = 0;
        int v2 = 0;
        // The length of the binary numbers should already match.

        // First determine if value1 is larger than value2. we want the largest value.
        // Then create a 2 dimensional array based on the number of bits in the largest binary
        // number with the width being the height times 2, and fill the array completely with zeros.
        int l1 = value1.length();
        int l2 = value2.length();
        int l = 0;
        if(l1 >= l2) {
            arr = cu.createMultiArray(l1);
            l = l1;
        }
        else{
            arr = cu.createMultiArray(l2);
            l = l2;
        }

        // Take the right most bit on value2 and multiply it by each of the bits
        // in value1, and put the results on a row of the 2D array from right to
        // left. Then shift to the next bit to the left of value 2 and go
        // again till all the factors in value2 have been dealt with.
        int k = 0;
        // 'm' is to step down each row in the 2D array.
        // 'i' is the step from right to left of 'value2'.
        for(int i = l-1, m = 0; i >=0; m++, i--){ // ForLoop for value2
            v2 = Character.getNumericValue(value2.charAt(i));

            // Using control value "h" incremented forward. This will give
            // the needed reversing action on the rows of the 2D array to place the
            // multiplied values in the correct position for adding.
            // 'j' is to step from left to right in the 2D array.
            // 'h' is to step from right to left of 'value1'.
            for(int j = (l*2)-1, h = l-1; h >=0; j--,  h--){
                v1 = Character.getNumericValue(value1.charAt(h));
                arr[m][j-k] = v2 * v1;
            }
            // This adjusts the factor "j" by one thus adjusting its starting position.
            k++;
        }
        StringBuilder row1 = null;
        StringBuilder row2;
        if(arr.length > 1) {
            // Once finished take 2 rows at a time and add them. There will be at least 2 rows.
            // Get first  and second row from "arr".
            row1 = new StringBuilder();
            row2 = new StringBuilder();
            // Load first array.
            for (int z = 0; z < (l * 2); z++) {
                row1.insert(z, arr[0][z]);
                row2.insert(z, arr[1][z]);
            }
            result.insert(0, binAdd(row1.toString(), row2.toString(), false));
            row1.replace(0, l * 2, result.toString());
            row2.delete(0, l * 2);
            result.delete(0, l * 2);

            // Grab the third or . . . row and add them.
            for (int i = 2; i < l; i++) {
                for (int j = (l * 2) - 1; j >= 0; j--) {
                    row2.insert(0, arr[i][j]);
                }
                result.insert(0, binAdd(row1.toString(), row2.toString(), false));
                row1.replace(0, l * 2, result.toString());
                result.delete(0, l * 2);
                row2.delete(0, l * 2);
            }
            result.insert(0, row1.toString());
        }
        else{
            result.insert(0, arr[0][1]);
        }
        result.replace(0, result.length(), cu.trimLeadingZeros(result.toString()));
        return result.toString();
    }





    private String binDiv(String dividend, String divisor, int divisorLength){
        // The string to store the quotient with remainder for returning.
        StringBuilder result = new StringBuilder();

        // If the divisor is 1 (one), it is the same as dividing any decimal value
        // by one which will produces the value itself. So lets not use the clock cycles
        // and skip the process and return the dividend.
        // The for loop below takes the one bit in the divisor and adds them up.
        // It is obvious that if there is more than one "one" bit that the
        // divisor is not "1" (one)
        int d = 0;
        for(int i = 0; i < divisor.length(); i++){
            d = d + Character.getNumericValue(divisor.charAt(i));
        }
        if(d == 1 & divisor.charAt(divisor.length()-1) == '1'){
            return dividend + " R: 0";
        }
        else{
            // Start by extracting divisor.length number of digits from the dividend.
            StringBuilder remainder = new StringBuilder(dividend.substring(0, divisorLength));
            remainder.insert(0, '0');

            // Now setup the dividend in a StringBuilder and remove the values
            // that were placed in the remainder, so the next value at the
            // beginning of the string can be grabbed and included in the
            // remainder after each calculation.
            StringBuilder dividendSB = new StringBuilder(dividend);
            dividendSB.delete(0, divisorLength);

            // It was too big of a hassle to leave the divisor in its un-padded state
            // before entering this method. So we are trimming it back with the standard
            // trim method and then adding a single zero back on the left end
            // to get it to the proper length prior to taking the 2s compliment.
            StringBuilder divisorSB = new StringBuilder(cu.trimLeadingZeros(divisor));
            divisorSB.insert(0, '0');

            // Typically a divisor can divide a dividend one digit larger that it's self.
            // There are occasions where to the initial N+1 digits extracted from the dividend
            // will be 2 times larger that the divisor. This is a problem. To compensate for
            // this the initial digits from with dividend will be the same quantity of
            // digits as the divisor, both padded with a zero on the left to give it the
            // desired divisor.length()+1 working length.

            // We need to get a 2s compliment of the divisor
            StringBuilder divisor2s = new StringBuilder(deriveTwosCompliment(divisorSB.toString()));

            // Setup the quotient with an initial length. The length will never be longer that the dividend.
            StringBuilder quotient = new StringBuilder(dividend.length());

            // Let the fun begin.
            while (dividendSB.length() != 0) {
                // Check is the remainder is larger then the divisor.
                // If not skip to the else.
                if (isValueLarger2(remainder.toString(), divisorSB.toString())) {
                    // Add the remainder with the 2s compliment.
                    remainder.replace(0, dividend.length(), binAdd(remainder.toString(), divisor2s.toString(), true));
                    quotient.append("1"); // Increment the quotient.
                    if (remainder.charAt(0) == '0') {
                        remainder.deleteCharAt(0);  // remove the leading zero
                    }
                    // Bring down the next value from the dividend.
                    remainder.append(dividendSB.charAt(0));
                    dividendSB.deleteCharAt(0);

                } else {
                    quotient.append('0');  // Increment the quotient
                    // Bring down the next value from the dividend.
                    remainder.append(dividendSB.charAt(0));
                    dividendSB.deleteCharAt(0);
                    if(remainder.charAt(0) == '0'){
                        remainder.deleteCharAt(0);  // remove the leading zero
                    }
                }
            }
            // To pick up whats left as the dividendSB went to zero before it was finished.
            if(isValueLarger2(remainder.toString(), divisorSB.toString())) {
                remainder.replace(0, remainder.length(), binAdd(remainder.toString(), divisor2s.toString(), true));
                quotient.append("1");
                if (remainder.charAt(0) == '0') {
                    remainder.deleteCharAt(0);
                }
            }
            else{
                quotient.append("0");
                if(remainder.charAt(0) == '0'){
                    remainder.deleteCharAt(0);
                }
            }
            quotient.replace(0, quotient.length(), cu.trimLeadingZeros(quotient.toString()));
            remainder.replace(0, remainder.length(), cu.trimLeadingZeros(remainder.toString()));
            result.insert(0, quotient.toString() + " R: " + remainder.toString());
        }
        return result.toString();
    }



    // Used in subtraction.
    private String deriveTwosCompliment(String value){
        StringBuilder valueA = new StringBuilder();
        StringBuilder adjValue = new StringBuilder(value);
        String tmp = null;
        String result = null;

        // Remove "-" negative sign from input string if it exists.
        if(value.charAt(0) == '-'){
            adjValue.deleteCharAt(0);
        }

        // Start at the right end of value and build it inverse.
        for(int i = adjValue.length()-1; i >= 0; i--){
            if(adjValue.length() == 1 & adjValue.charAt(i) == '0'){
                return "0";
            }
            else if(adjValue.charAt(i) == '0'){
                valueA.insert(0, "1");
            }
            else if(adjValue.charAt(i) == '1'){
                valueA.insert(0, "0");
            }
        }
        // Add "1" back in
        tmp = "1";
        result = binAdd(valueA.toString(), tmp, false);

        return result;
    }




    // Value 1 should be larger or equal to value 2 to evaluate as true.
    private boolean isValueLarger2(String string1, String string2){
        boolean TorF = false;

        for(int i = 0; i < string1.length(); i++){
            if(Character.getNumericValue(string1.charAt(i)) > Character.getNumericValue(string2.charAt(i))){
                TorF = true;
                break;
            }
            else if(Character.getNumericValue(string1.charAt(i)) < Character.getNumericValue(string2.charAt(i))){
                TorF = false;
                break;
            }
            //else if(Character.getNumericValue(string1.charAt(i)) == Character.getNumericValue(string2.charAt(i))){
            //    TorF = true;
            //}
        }
        return TorF;
    }

}
