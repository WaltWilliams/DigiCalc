package com.woodlandcoders.digicalc;

import java.util.HashMap;
import java.util.Map;


public class Base16Maths {
    public static Base16Maths mInstance;
    static public synchronized Base16Maths getInstance(){
        if(mInstance == null){
            mInstance = new Base16Maths();
        }
        return mInstance;
    }

    // Singleton constructor.
    private Base16Maths() {
    }

    CommonUtils cu = CommonUtils.getInstance();
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Hexadecimal math ///////////////////////////////////////////////////////////////////////////
    protected String hexMath(String value1, String value2, String operatorFromUI){
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


        boolean isOneLarger = isValueLarger16(values[0], values[1]);
        boolean isTwoLarger = isValueLarger16(values[1], values[0]);

        if(operatorFromUI.compareTo("Add") == 0){
            // Now add based on 6 different state.
            // State 1. Both values positive.  It doesn't matter which larger.
            // ----------->>>  isOneNeg and isTwoNeg default to false
            //  So to be true isOneNeg and isTwoNeg have the "!" in front.
            if((!isOneNeg && !isTwoNeg) || (isOneNeg && isTwoNeg)){
                result.insert(0, hexAdd(values[0], values[1], false));
                if(isOneNeg && isTwoNeg){
                    result.insert(0, "-");
                }
                if((result.length() == 2) && (result.charAt(0) == '-') & (result.charAt(1) == '0')){
                    result.deleteCharAt(0);
                }
            }
            // State 2. Large negative value - smaller positive value
            if(isOneLarger && isOneNeg && !isTwoNeg){
                String sixteenCompx = deriveSixteensCompliment(values[0]);
                // While converting to 16s compliment a digit might get dropped.
                String sixteenComp = cu.padValue(sixteenCompx, values[1]);
                result.insert(0, hexAdd(sixteenComp, values[1], true));
                result.replace(0, result.length(), deriveSixteensCompliment(result.toString()));
                result.insert(0, "-");
            }
            // State 3. Small negative value - larger positive value
            if(isOneNeg && isTwoLarger && !isTwoNeg){
                String sixteenCompx = deriveSixteensCompliment(values[0]);
                // While converting to 16s compliment a digit might get dropped.
                String sixteenComp = cu.padValue(sixteenCompx, values[1]);
                result.insert(0, hexAdd(sixteenComp, values[1], true));
            }
            // State 4. Smaller positive  value - larger negative value.
            if(!isOneNeg && isTwoLarger && isTwoNeg){
                String sixteenCompx = deriveSixteensCompliment(values[1]);
                // While converting to 16s compliment a digit might get dropped.
                String sixteenComp = cu.padValue(sixteenCompx, values[0]);
                result.insert(0, hexAdd(values[0], sixteenComp, true));
                result.replace(0, result.length(), deriveSixteensCompliment(result.toString()));
                result.insert(0, "-");
            }
            // State 5. Larger positive  value - small negative value.
            if(!isOneNeg && isOneLarger && isTwoNeg){
                String sixteenCompx = deriveSixteensCompliment(values[1]);
                // While converting to 16s compliment a digit might get dropped.
                String sixteenComp = cu.padValue(sixteenCompx, values[0]);
                result.insert(0, hexAdd(values[0], sixteenComp, true));
            }
        }
        else if(operatorFromUI.compareTo("Mult") == 0){
            result.insert(0, hexMult(values[0], values[1]));
            result.replace(0, result.length(), cu.trimLeadingZeros(result.toString()));
            if((isOneNeg & !isTwoNeg) | (!isOneNeg & isTwoNeg)){
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
            else if(isValueLarger16(values[1], values[0])){
                return "The dividend must be larger than the divisor";
            }

            // The third parameter is the length of the divisor derived from the "prepValues" method.
            result.insert(0, hexDiv(values[0], values[1], Integer.parseInt(values[4])));
            result.replace(0, result.length(), cu.trimLeadingZeros(result.toString()));
            if((isOneNeg & !isTwoNeg) | (!isOneNeg & isTwoNeg)){
                result.insert(0, "-");
            }
        }
        else{
            result.insert(0, "Bad operator");
        }

        return result.toString();
    }



    private String hexAdd(String value1, String value2, boolean subSwitch){
        StringBuilder result = new StringBuilder();
        int v1 = 0;
        int v2 = 0;
        int tmp = 0;
        int carry = 0;

        for(int i = value1.length()-1; i >= 0; i--){
            // Grab the first character on the right and convert to a String
            // so it can be feed to the hex to decimal converter. The for loop
            // with move to the next character on the left.
            String cv1 = String.valueOf(value1.charAt(i));
            String cv2 = String.valueOf(value2.charAt(i));
            v1 = getDecimalFromHex(cv1);
            v2 = getDecimalFromHex(cv2);

            // Do the math. Once finished perform the hex magic on it
            // and place it in the result string.
            tmp = v1 + v2 + carry;

            // States. The first 16 are simple with no carry.
            // Once at 16 we have to revert back to zero and carry one.
            // Because FF == 30 with the possibility of a carry we
            // only have to go up 31.
            if(tmp > -1 && tmp < 10){
                result.insert(0, getHexFromDecimal(tmp));
                carry = 0;
            }
            else if(tmp == 10){
                result.insert(0, getHexFromDecimal(10));
                carry = 0;
            }
            else if(tmp == 11){
                result.insert(0, getHexFromDecimal(11));
                carry = 0;
            }
            else if(tmp == 12){
                result.insert(0, getHexFromDecimal(12));
                carry = 0;
            }
            else if(tmp == 13){
                result.insert(0, getHexFromDecimal(13));
                carry = 0;
            }
            else if(tmp == 14){
                result.insert(0, getHexFromDecimal(14));
                carry = 0;
            }
            else if(tmp == 15){
                result.insert(0, getHexFromDecimal(15));
                carry = 0;
            }
            else if(tmp == 16){
                result.insert(0, getHexFromDecimal(0));
                carry = 1;
            }
            else if(tmp == 17){
                result.insert(0, getHexFromDecimal(1));
                carry = 1;
            }
            else if(tmp == 18){
                result.insert(0, getHexFromDecimal(2));
                carry = 1;
            }
            else if(tmp == 19){
                result.insert(0, getHexFromDecimal(3));
                carry = 1;
            }
            else if(tmp == 20){
                result.insert(0, getHexFromDecimal(4));
                carry = 1;
            }
            else if(tmp == 21){
                result.insert(0, getHexFromDecimal(5));
                carry = 1;
            }
            else if(tmp == 22){
                result.insert(0, getHexFromDecimal(6));
                carry = 1;
            }
            else if(tmp == 23){
                result.insert(0, getHexFromDecimal(7));
                carry = 1;
            }
            else if(tmp == 24){
                result.insert(0, getHexFromDecimal(8));
                carry = 1;
            }
            else if(tmp == 25){
                result.insert(0, getHexFromDecimal(9));
                carry = 1;
            }
            else if(tmp == 26){
                result.insert(0, getHexFromDecimal(10));
                carry = 1;
            }
            else if(tmp == 27){
                result.insert(0, getHexFromDecimal(11));
                carry = 1;
            }
            else if(tmp == 28){
                result.insert(0, getHexFromDecimal(12));
                carry = 1;
            }
            else if(tmp == 29){
                result.insert(0, getHexFromDecimal(13));
                carry = 1;
            }
            else if(tmp == 30){
                result.insert(0, getHexFromDecimal(14));
                carry = 1;
            }
            else if(tmp == 31){
                result.insert(0, getHexFromDecimal(15));
                carry = 1;
            }
        }

        if(carry != 0 && !subSwitch){
            result.insert(0, getHexFromDecimal(new Integer(carry)));
        }

        return result.toString();
    }


    private String hexMult(String value1, String value2){
        // If both values are zero, screw it I don't want to bother process them.
        if((value1.compareTo("0") == 0) && (value2.compareTo("0") == 0)){
            return "0";
        }
        StringBuilder results = new StringBuilder();
        StringBuilder value1x = new StringBuilder(value1);
        StringBuilder value2x = new StringBuilder(value2);
        if(value1x.charAt(0)== '-'){
            value1x.deleteCharAt(0);
        }
        if(value2x.charAt(0)== '-'){
            value2x.deleteCharAt(0);
        }
        String value1a = value1x.toString();
        String value2a = value2x.toString();

        int l = value1a.length();
        int carry = 0;
        // First setup a 2D array to hold the multiplied items.
        int s = 0;
        int arr[][] = cu.createMultiArray(l);

        // "i" steps through value2 from right to left.
        // "h" steps through value1 from right to left.
        // "j" steps down vertically the container array.
        // "k" steps across horizontally the container array from left to right.
        int k, h = 0;
        for(int i = l-1, j = 0; j < l; i--, j++){
            int v2 = Character.getNumericValue(value2a.charAt(i));   // Examine the for loop parameters.
            for(k = (l*2)-1, h = l-1; h >= 0; k--, h--){
                int v1 = Character.getNumericValue(value1a.charAt(h));
                // multiply v2 with v1 and include the carry
                int intermediate = (v2 * v1) + carry;
                arr[j][k-s] = intermediate%16;
                carry = intermediate/16;
            }
            arr[j][k-s] = carry;
            s++;
            carry = 0;
        }

        if(arr.length > 1) {
            // Now adding up the rows of the container array.

            // Starting off by adding up the first two rows
            // and place the results in a single dimension
            // array, then add each consecutive row to that
            // array.
            carry = 0;
            int[] tmpResults = new int[(l * 2)];
            for (int i = (l * 2) - 1; i >= 0; i--) {
                int tmp = arr[0][i] + arr[1][i] + carry;
                tmpResults[i] = tmp % 16;
                if (tmp > 15) {
                    carry = tmp / 16;
                } else {
                    carry = 0;
                }
            }

            // Now taking the tmpResults and adding it with
            // each row in 'arr[][]' placing the sum back into
            // same element of arr that was just added.
            carry = 0;
            // The outer for loop traverses down the 'arr' rows.
            for (int g = 2; g < l; g++) {
                for (int j = (l * 2) - 1; j >= 0; j--) {
                    int x1 = tmpResults[j];
                    int x2 = arr[g][j];
                    // This should automatically include the final carry in tmpResults.
                    int tmp = tmpResults[j] + arr[g][j] + carry;
                    tmpResults[j] = tmp % 16;
                    if (tmp > 15) {
                        carry = tmp / 16;
                    } else {
                        carry = 0;
                    }
                }
            }


            for (int r = (l * 2) - 1; r >= 0; r--) {
                results.insert(0, getHexFromDecimal(tmpResults[r]));
            }

            if (results.charAt(0) == '0') {
                results.replace(0, results.length(), results.toString());
            }
        }
        else{
            results.insert(0, getHexFromDecimal(arr[0][1]));
            results.insert(0, getHexFromDecimal(arr[0][0]));
        }

        return results.toString();
    }




    private String hexDiv(String dividend, String divisor, int divisorLength) {
        // The string to store the quotient with remainder for returning.
        StringBuilder results = new StringBuilder();

        // If the divisor is 1 (one), it is the same as dividing any decimal value
        // by one which will produces the value itself. So lets not use the clock cycles
        // and skip the process and return the dividend.
        if(divisorLength >= 1) {

            // It was too big of a hassle to leave the divisor in its un-padded state
            // before entering this method.
            StringBuilder divisorSB = new StringBuilder(cu.trimLeadingZeros(divisor));

            int h = 0;
            for(int d = 0; d< divisorLength; d++){
                h = h + getDecimalFromHex(cu.trimLeadingZeros(String.valueOf(divisorSB.charAt(d))));
            }

            // So we that have made use of the trimming back divisor in the
            // statement above we now have adding a single zero back on the
            // left end of it to get it to the proper length prior to taking
            // the 2s compliment.
            divisorSB.insert(0, '0');

            if (h == 1) {
                return dividend + " R: 0";
            }
            else {
                //StringBuilder divisor16s = new StringBuilder(deriveEightsCompliment(divisor));
                StringBuilder remainder = new StringBuilder(dividend.substring(0, divisorLength));
                remainder.insert(0, '0');

                // Now setup the dividend in a StringBuilder and remove the values
                // that were placed in the remainder, so the next value at the
                // beginning of the string can be grabbed and included in the
                // remainder after each calculation.
                StringBuilder dividendSB = new StringBuilder(dividend);
                dividendSB.delete(0, divisorLength);

                // Temporary place holder for the divisor. <--- Don't change
                // Its the divisorSB that gets change in the end.
                StringBuilder tmpDivisor = new StringBuilder(divisorSB.toString());

                // Typically a divisor can divide a dividend one digit larger that it's self.
                // There are occasions where to the initial N+1 digits extracted from the dividend
                // will be 2 times larger that the divisor. This is a problem. To compensate for
                // this the initial digits from with dividend will be the same quantity of
                // digits as the divisor, both padded with a zero on the left to give it the
                // desired divisor.length()+1 working length.

                // Setup the quotient with an initial length.
                StringBuilder quotient = new StringBuilder();

                // Initializing the 16s compliment object but not loading it yet as the
                // contents will change on each pass.
                StringBuilder divisor16s = new StringBuilder();

                // Creating an 8s compliment zero
                StringBuilder ONE = new StringBuilder("1");
                ONE.replace(0, ONE.length(), cu.padValue(ONE.toString(), remainder.toString()));
                ONE.replace(0, ONE.length(), deriveSixteensCompliment(ONE.toString()));

                // Let the fun begin.
                while (dividendSB.length() != 0) {
                    // We first need to establish what the divisor scalar is.
                    // Start at 15 and decrement the scalar until the just slightly
                    // smaller than the divisor-fragment. This scalar is the
                    // factor that the divisor will need to be multiplied by
                    // (scaled) for the proper "subtraction" with the divisor-fragment.
                    // This scalar will become part of the quotient.
                    StringBuilder divisorMultiple = new StringBuilder("F");
                    // Padding the divisorMultiple.
                    divisorMultiple.replace(0, divisorMultiple.length(), cu.padValue(divisorMultiple.toString(), remainder.toString()));
                    //====================================================================================================
                    divisorSB.replace(0, divisorSB.length(), hexMult(divisorMultiple.toString(), tmpDivisor.toString()));
                    // The multiplication method leaves excess leading zeros. Lets trim them back.
                    for (int i = 0; i < divisorLength + 1; i++) {
                        divisorSB.deleteCharAt(0);
                    }

                    // Step down the divisor to be "just" less than the dividend.
                    while (isValueLarger16(divisorSB.toString(), remainder.toString())) {
                        divisorMultiple.replace(0, divisorMultiple.length(), hexAdd(divisorMultiple.toString(), ONE.toString(), true));
                        divisorSB.replace(0, divisorSB.length(), hexMult(divisorMultiple.toString(), tmpDivisor.toString()));
                        // The multiplication method leaves excess leading zeros. Lets trim them back.
                        for (int i = 0; i < divisorLength + 1; i++) {
                            divisorSB.deleteCharAt(0);
                        }
                    }
                    //====================================================================================================
                    // Derive the 16s compliment and then add.
                    // Hexadecimal values can include multiple letters. We need to take each Hexadecimal
                    // digit and find its equivalent value. Here we can get away with just adding the
                    // digit values up so there is an indication of something greater than zero.
                    int dm = 0;
                    for(int m = 0; m < divisorMultiple.length(); m++){
                        dm = dm + getDecimalFromHex(String.valueOf(divisorMultiple.charAt(m)));
                    }
                    if (dm > 0) {
                        divisor16s.replace(0, divisor16s.length(), deriveSixteensCompliment(divisorSB.toString()));
                        remainder.replace(0, remainder.length(), hexAdd(remainder.toString(), divisor16s.toString(), true));
                    }

                    // Append the quotient
                    divisorMultiple.replace(0, divisorMultiple.length(), cu.trimLeadingZeros(divisorMultiple.toString()));
                    quotient.append(divisorMultiple.toString());
                    // Bring down the next value from the dividend and take out a leading zero.
                    remainder.append(dividendSB.charAt(0));
                    dividendSB.deleteCharAt(0);
                    remainder.deleteCharAt(0);
                }

                // picking up the last digit in the dividend.
                StringBuilder divisorMultiple = new StringBuilder("F");
                divisorMultiple.replace(0, divisorMultiple.length(), cu.padValue(divisorMultiple.toString(), remainder.toString()));

                divisorSB.replace(0, divisorSB.length(), hexMult(divisorMultiple.toString(), tmpDivisor.toString()));
                // The multiplication method leaves excess leading zeros. Lets trim them back.
                for (int i = 0; i < divisorLength + 1; i++) {
                    divisorSB.deleteCharAt(0);
                }

                // Step down the divisor to be "just" less than the dividend.
                while (isValueLarger16(divisorSB.toString(), remainder.toString())) {
                    divisorMultiple.replace(0, divisorMultiple.length(), hexAdd(divisorMultiple.toString(), ONE.toString(), true));
                    divisorSB.replace(0, divisorSB.length(), hexMult(divisorMultiple.toString(), tmpDivisor.toString()));
                    // The multiplication method leaves excess leading zeros. Lets trim them back.
                    for (int i = 0; i < divisorLength + 1; i++) {
                        divisorSB.deleteCharAt(0);
                    }
                }
                // Derive the 16s compliment and then add.
                // Hexadecimal values can include multiple letters. We need to take each Hexadecimal
                // digit and find its equivalent value. Here we can get away with just adding the
                // digit values up so there is an indication of something greater than zero.
                int dm = 0;
                for(int m = 0; m < divisorMultiple.length(); m++){
                    dm = dm + getDecimalFromHex(String.valueOf(divisorMultiple.charAt(m)));
                }
                if (dm > 0) {
                    divisor16s.replace(0, divisor16s.length(), deriveSixteensCompliment(divisorSB.toString()));
                    remainder.replace(0, remainder.length(), hexAdd(remainder.toString(), divisor16s.toString(), true));
                }

                // Append the quotient
                divisorMultiple.replace(0, divisorMultiple.length(), cu.trimLeadingZeros(divisorMultiple.toString()));
                quotient.append(divisorMultiple.toString());

                remainder.replace(0, remainder.length(), cu.trimLeadingZeros(remainder.toString()));
                results.insert(0, cu.trimLeadingZeros(quotient.toString()) + " R: " + cu.trimLeadingZeros(remainder.toString()));
            }
        }
        return results.toString();
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // General methods ///////////////////////////////////////////////////////////////////////////////////////////////
    private int getDecimalFromHex(String key){
        Map<String, Integer> hexDecimalMap = new HashMap();
        hexDecimalMap.put("0", 0);
        hexDecimalMap.put("1", 1);
        hexDecimalMap.put("2", 2);
        hexDecimalMap.put("3", 3);
        hexDecimalMap.put("4", 4);
        hexDecimalMap.put("5", 5);
        hexDecimalMap.put("6", 6);
        hexDecimalMap.put("7", 7);
        hexDecimalMap.put("8", 8);
        hexDecimalMap.put("9", 9);
        hexDecimalMap.put("A", 10);
        hexDecimalMap.put("B", 11);
        hexDecimalMap.put("C", 12);
        hexDecimalMap.put("D", 13);
        hexDecimalMap.put("E", 14);
        hexDecimalMap.put("F", 15);

        return hexDecimalMap.get(key);
    }

    private String getHexFromDecimal(int key){
        Map<Integer, String> hexDecimalMap = new HashMap();
        hexDecimalMap.put(0, "0");
        hexDecimalMap.put(1, "1");
        hexDecimalMap.put(2, "2");
        hexDecimalMap.put(3, "3");
        hexDecimalMap.put(4, "4");
        hexDecimalMap.put(5, "5");
        hexDecimalMap.put(6, "6");
        hexDecimalMap.put(7, "7");
        hexDecimalMap.put(8, "8");
        hexDecimalMap.put(9, "9");
        hexDecimalMap.put(10, "A");
        hexDecimalMap.put(11, "B");
        hexDecimalMap.put(12, "C");
        hexDecimalMap.put(13, "D");
        hexDecimalMap.put(14, "E");
        hexDecimalMap.put(15, "F");

        return hexDecimalMap.get(key);
    }



    private String deriveSixteensCompliment(String value){
        StringBuilder result = new StringBuilder();
        int F = 15; // Equals "F"
        int v2 = 0;

        if(value.length() == 1 & value.charAt(0) == '0'){
            return "0";
        }

        for(int j = value.length()-1; j > -1; j--){
            String singleHexDigit = String.valueOf(value.charAt(j));
            v2 = getDecimalFromHex(singleHexDigit);
            int n = F - v2;
            result.insert(0, getHexFromDecimal(n));
            //result.replace(0, result.length(), result.toString());
        }
        result.replace(0, result.length(), hexAdd(result.toString(), cu.padValue("1", result.toString()), false));

        return result.toString();
    }


    // Compares suspected larger value against smaller value.
    private boolean isValueLarger16(String larger, String smaller){
        boolean TorF = false;
        for(int i = 0; i < larger.length(); i++){
            String lv = String.valueOf(larger.charAt(i));
            String sv = String.valueOf(smaller.charAt(i));
            if(getDecimalFromHex(lv) > getDecimalFromHex(sv)){
                TorF = true;
                break;
            }
            else if(getDecimalFromHex(lv) < getDecimalFromHex(sv)){
                TorF = false;
                break;
            }
        }
        return TorF;
    }

}
