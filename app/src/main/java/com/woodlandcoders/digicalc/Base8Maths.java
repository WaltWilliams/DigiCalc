package com.woodlandcoders.digicalc;

public class Base8Maths {
    private static Base8Maths mInstance;
    static  public synchronized Base8Maths getInstance(){

        if (mInstance == null){
            mInstance = new Base8Maths();
        }
        return mInstance;
    }

    // Singleton constructor.
    private Base8Maths() {
    }

    CommonUtils commonUtils = CommonUtils.getInstance();
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Octal math //////////////////////////////////////////////////////////////////////////////////////////////////
    protected String octalMath(String value1, String value2, String operatorFromUI){
        StringBuilder result = new StringBuilder();

        // PrepValues does the following
        // 1) Records if a/the value(s) is negative.
        // 2) Strip off the negative sign(s) from the input value(s).
        // 3) Clean off the excess leading zeros. This is needed to make a couple clean determination about the input values.
        // 4) Record the length of value2 as the divisor length.
        // 5) If either input was a negative zero the negative record is reset to false in that its not negative. 0 instead of -0
        // 6) Match length both input values by padding the shorter value with leading zeros.
        String[] values = commonUtils.prepValues(value1, value2);

        // Making a boolean from the negative record from prepValues.
        boolean isOneNeg = false;
        boolean isTwoNeg = false;
        if(values[2].compareTo("1") == 0){
            isOneNeg = true;
        }
        if(values[3].compareTo("1") == 0){
            isTwoNeg = true;
        }

        boolean isOneLarger = isValueLarger8(values[0], values[1]);
        boolean isTwoLarger = isValueLarger8(values[1], values[0]);

        if(operatorFromUI.compareTo("Add") == 0){
            // Now add based on 7 different state.
            // States 1 and 7. Both values positive.  It doesn't matter which larger.
            // ----------->>>  isOneNeg and isTwoNeg default to false
            //  So to be true isOneNeg and isTwoNeg have the "!" in front.
            if((!isOneNeg & !isTwoNeg) | (isOneNeg & isTwoNeg)){
                result.insert(0, octalAdd(values[0], values[1], false));
                if((isOneNeg & isTwoNeg)){
                    result.insert(0, "-");
                }
                if((result.length() == 2) && (result.charAt(0) == '-') & (result.charAt(1) == '0')){
                    result.deleteCharAt(0);
                }
            }
            // State 2. If both values are the same size (isOneLarger == false, isTwoLarger == false)
            // and one or the other is negative. Its subtracting the same value from it's self. It
            // will equal zero. 7 - 7 = 0
            else if((!isOneLarger & !isTwoLarger) & ((isOneNeg & !isTwoNeg) || (!isOneNeg & isTwoNeg))){
                result.insert(0, "0");
            }
            // State 3. Large negative value - smaller positive value
            else if(isOneLarger & isOneNeg & !isTwoNeg){
                String eightsCompx = deriveEightsCompliment(values[0]);
                // While converting to 8s compliment a digit might get dropped.
                String eightsComp = commonUtils.padValue(eightsCompx, values[1]);
                result.insert(0, octalAdd(eightsComp, values[1], true));
                result.replace(0, result.length(), deriveEightsCompliment(result.toString()));
                result.replace(0, result.length(), commonUtils.trimLeadingZeros(result.toString()));
                result.insert(0, "-");
            }
            // State 4. Small negative value - larger positive value
            else if(isOneNeg & isTwoLarger & !isTwoNeg){
                String eightsCompx = deriveEightsCompliment(values[0]);
                // While converting to 8s compliment a digit might get dropped.
                String eightsComp = commonUtils.padValue(eightsCompx, values[1]);
                result.insert(0, octalAdd(eightsComp, values[1], true));
                result.replace(0, result.length(), commonUtils.trimLeadingZeros(result.toString()));
            }
            // State 5. Smaller positive  value - larger negative value.
            else if(!isOneNeg & isTwoLarger & isTwoNeg){
                String eightsCompx = deriveEightsCompliment(values[1]);
                // While converting to 8s compliment a digit might get dropped.
                String eightsComp = commonUtils.padValue(eightsCompx, values[0]);
                result.insert(0, octalAdd(values[0], eightsComp.toString(), true));
                result.replace(0, result.length(), deriveEightsCompliment(result.toString()));
                result.replace(0, result.length(), commonUtils.trimLeadingZeros(result.toString()));
                result.insert(0, "-");
            }
            // State 6. Larger positive  value - small negative value.
            else if(!isOneNeg & isOneLarger & isTwoNeg){
                String eightsCompx = deriveEightsCompliment(values[1]);
                // While converting to 8s compliment a digit might get dropped.
                String eightsComp = commonUtils.padValue(eightsCompx, values[0]);
                result.insert(0, octalAdd(values[0], eightsComp, true));
                result.replace(0, result.length(), commonUtils.trimLeadingZeros(result.toString()));
            }
        }
        else if(operatorFromUI.compareTo("Mult") == 0){
            result.insert(0, octalMult(values[0], values[1]));
            result.replace(0, result.length(), commonUtils.trimLeadingZeros(result.toString()));
            if((isOneNeg & !isTwoNeg) | (!isOneNeg & isTwoNeg)){
                result.insert(0, "-");
            }
        }
        else if(operatorFromUI.compareTo("Div") == 0){
            // Thou shalt not divide by zero
            if(commonUtils.isValueZero(values[1])){
                return "Thou Shalt Divide By Zero!";
            }
            // The dividend needs to be larger that the divisor. If not
            // dump out with a message.
            else if(isValueLarger8(values[1], values[0])){
                return "The dividend must be larger than the divisor";
            }

            // The third parameter is the length of the divisor derived from the "prepValues" method.
            result.insert(0, octDiv(values[0], values[1], Integer.parseInt(values[4])));
            if((isOneNeg & !isTwoNeg) | (!isOneNeg & isTwoNeg)){
                result.insert(0, "-");
            }
        }

        return result.toString();
    }


    private String octalAdd(String value1, String value2, boolean isSubtraction){
        StringBuilder result = new StringBuilder();
        int tmp = 0;
        int carry = 0;

        // In adding octal numbers there will be 9 possible states
        // 0 through 7 being one possibility as you will simply insert 1 through 7.
        // 8, 9, 10, 11, 12, 13, 14, 15.
        for(int i = value1.length()-1; i >= 0; i--){
            int o = Character.getNumericValue(value1.charAt(i));
            int p = Character.getNumericValue(value2.charAt(i));
            tmp = Character.getNumericValue(value1.charAt(i)) + Character.getNumericValue(value2.charAt(i)) + carry;

            if(tmp == 0 | tmp == 1 | tmp == 2 | tmp == 3 | tmp == 4 | tmp == 5 | tmp == 6 | tmp == 7){
                result.insert(0, tmp);
                carry = 0;
            }
            else if(tmp == 8){
                result.insert(0, "0");
                carry = 1;
            }
            else if(tmp == 9){
                result.insert(0, "1");
                carry = 1;
            }
            else if(tmp == 10){
                result.insert(0, "2");
                carry = 1;
            }
            else if(tmp == 11){
                result.insert(0, "3");
                carry = 1;
            }
            else if(tmp == 12){
                result.insert(0, "4");
                carry = 1;
            }
            else if(tmp == 13){
                result.insert(0, "5");
                carry = 1;
            }
            else if(tmp == 14){
                result.insert(0, "6");
                carry = 1;
            }
            else if(tmp == 15){
                result.insert(0, "7");
                carry = 1;
            }
        }
        if(carry == 1 & isSubtraction == false){
            result.insert(0, carry);
        }
        //result.replace(0, result.length(), Base2Maths.trimLeadingZeros(result.toString()));
        return result.toString();
    }


    private String octalMult(String value1, String value2){
        // If both values are zero, screw it I don't want to bother process them.
        if((value1.compareTo("0") == 0) && (value2.compareTo("0") == 0)){
            return "0";
        }
        int[][] arr;
        int v1 = 0;
        int v2 = 0;
        int[] octValues = {0, 0};

        // First determine if value1 is larger than value2. we want the largest value.
        // Then create a 2 dimensional array based on the number of digits in the largest octal
        // value with the width being the height times 2, and fill the array completely with zeros.
        // The larger of the input values is to be the stop case of the for loop pair just below.
        int l1 = value1.length();
        int l2 = value2.length();
        int l = 0;
        int carry = 0;
        if(l1 >= l2) {
            arr = commonUtils.createMultiArray(l1);
            l = l1;
        }
        else{
            arr = commonUtils.createMultiArray(l2);
            l = l2;
        }

        int k = 0;// 'k' is provide the mechanism to shift the multiplication in the 2D array to the left.
        // Load the 2D array with the multiplication work to be added up.
        // 'i' is needed to step from right to left of value2.
        // 'm' is for stepping from row to row of the 2D array.
        for (int i = l - 1, m = 0; i >= 0; i--, m++) {
            // Grab the first octal value on the right end of 'value2'
            // and put it in a int variable.
            int j = 0;
            int h = 0;
            v2 = Character.getNumericValue(value2.charAt(i));
            for (j = (l * 2) - 1, h = l - 1; h >= 0; j--, h--) {
                // Now grab the appropriate int from 'value1' and multiply it
                // with v2 and place it in the correct cell of the 2D array.
                v1 = Character.getNumericValue(value1.charAt(h));
                octValues = octalMultiplyHelper((v2 * v1) + carry);
                carry = octValues[0];
                arr[m][j-k] = octValues[1];
            }
            if(carry > 0){
                arr[m][j-k] = carry;
                carry = 0;
            }
            k++; // 'k' is provide the mechanism to shift the multiplication in the 2D array to the left.
        }
        StringBuilder row1 = new StringBuilder();
        if(arr.length > 1) {
            // Now take the first two multiplication results and add them together.
            // Then take the result and add the next multiplication to that. Continue
            // until all the multiplication is included.

            StringBuilder row2 = new StringBuilder();
            for (int i = (l * 2) - 1; i >= 0; i--) {
                row1.insert(0, arr[0][i]);
                row2.insert(0, arr[1][i]);
            }

            // Insert the results from row1 and row2 back into row1 for further calculation.
            row1.replace(0, (l * 2), octalAdd(row1.toString(), row2.toString(), false));
            // Because the add method trims the leading zeros before returning,
            // which is desirable for regular addition, we need to re-pad row 1
            // with leading zeros before proceeding so the values align for the
            // next addition.
            row1.replace(0, row1.length(), commonUtils.padValue(row1.toString(), row2.toString()));
            row2.delete(0, (l * 2)); // Remove the contents from row2.

            // Grab the third or . . . row and add them until there are not anymore rows.
            for (int i = 2; i < l; i++) {
                for (int j = (l * 2) - 1; j >= 0; j--) {
                    row2.insert(0, arr[i][j]);           // insert it into a string
                }
                // Now add row1 with the row you just got.
                row1.replace(0, (l * 2), octalAdd(row1.toString(), row2.toString(), false));
                // Re-padding the leading zeros in row1.
                row1.replace(0, row1.length(), commonUtils.padValue(row1.toString(), row2.toString()));
                row2.delete(0, (l * 2)); // Remove the contents from row2.
            }
        }
        else{
            row1.insert(0, String.valueOf(arr[0][1]));
            row1.insert(0, String.valueOf(arr[0][0]));
        }

        return row1.toString();
    }




    private String octDiv(String dividend, String divisor, int divisorLength) {
        // The string to store the quotient with remainder for returning.
        StringBuilder results = new StringBuilder();

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
        else {
            // Java Strings are immutable so a little setup is needed
            // with StringBuilder objects.
            StringBuilder remainder = new StringBuilder(dividend.substring(0, divisorLength));
            remainder.insert(0, '0');

            // Now setup the dividend in a StringBuilder and remove the values
            // that were placed in the remainder, so the next value at the
            // beginning of the string can be grabbed and included in the
            // remainder after each calculation. If the dividend is as long as
            // the divisor it will delete the contents of dividendSB.
            StringBuilder dividendSB = new StringBuilder(dividend);
            dividendSB.delete(0, divisorLength);

            // It was too big of a hassle to leave the divisor in its un-padded state
            // before entering this method. So we are trimming it back using the standard
            // trim method and then adding a single zero back on the left end
            // to get it to the proper length prior to taking the 2s compliment.
            // "divisorSB" is the divisor-fragment mentioned below.
            StringBuilder divisorSB = new StringBuilder(commonUtils.trimLeadingZeros(divisor));
            divisorSB.insert(0, '0');
            // Place holder for the original divisor.
            // Its the divisorSB that gets change.
            String tmpDivisor = divisorSB.toString();

            // Typically a divisor can divide a dividend one digit larger that it's self.
            // There are occasions where to the initial N+1 digits extracted from the dividend
            // will be 2 times larger that the divisor. This is a problem. To compensate for
            // this the initial digits from with dividend will be the same quantity of
            // digits as the divisor, both padded with a zero on the left to give it the
            // desired divisor.length()+1 working length.

            // Setup the quotient with an initial length.
            StringBuilder quotient = new StringBuilder();

            // Initializing the 8s compliment object but not loading it yet as the
            // contents will change on each pass.
            StringBuilder divisor8s = new StringBuilder();

            // Creating an 8s compliment zero
            StringBuilder ONE = new StringBuilder("1");
            ONE.replace(0, ONE.length(), commonUtils.padValue(ONE.toString(), remainder.toString()));
            ONE.replace(0, ONE.length(), deriveEightsCompliment(ONE.toString()));

            // Let the fun begin.
            while (dividendSB.length() != 0) {
                // We first need to establish what the divisor scalar is.
                // Start at 7 and decrement the scalar until the just slightly
                // smaller than the divisor-fragment. This scalar is the
                // factor that the divisor will need to be multiplied by
                // (scaled) for the proper "subtraction" with the divisor-fragment.
                // This scalar will become part of the quotient.
                StringBuilder divisorMultiple = new StringBuilder("7");
                // Padding the divisorMultiple.
                divisorMultiple.replace(0, divisorMultiple.length(), commonUtils.padValue(divisorMultiple.toString(), remainder.toString()));
                //====================================================================================================
                // Getting the initial scaled up divisor.
                divisorSB.replace(0, divisorSB.length(), octalMult(divisorMultiple.toString(), tmpDivisor));
                // The multiplication method leaves excess leading zeros (sometimes several). Lets trim them back to just one zero.
                for(int i = 0; i < divisorLength+1; i++){
                    divisorSB.deleteCharAt(0);
                }

                // If needed (most likely) this steps down the scalar until the
                // scaled divisor is obtained that is just one factor less than the dividend.
                while(isValueLarger8(divisorSB.toString(), remainder.toString())){
                    divisorMultiple.replace(0, divisorMultiple.length(), octalAdd(divisorMultiple.toString(), ONE.toString(), true));
                    divisorSB.replace(0, divisorSB.length(), octalMult(divisorMultiple.toString(), tmpDivisor));
                    // The multiplication method leaves excess leading zeros (sometimes several). Lets trim them back to just one zero.
                    for(int i = 0; i < divisorLength+1; i++){
                        divisorSB.deleteCharAt(0);
                    }
                }
                //====================================================================================================
                // Now to do the calculation.
                // Derive the 8s compliment and then add.
                if(Integer.parseInt(divisorMultiple.toString()) > 0){
                    divisor8s.replace(0, divisor8s.length(), deriveEightsCompliment(divisorSB.toString()));
                    remainder.replace(0, remainder.length(), octalAdd(remainder.toString(), divisor8s.toString(), true));
                }

                // Append the quotient
                quotient.append(Integer.parseInt(divisorMultiple.toString()));
                // Bring down the next value from the dividend and take out a leading zero.
                remainder.append(dividendSB.charAt(0));
                dividendSB.deleteCharAt(0);
                remainder.deleteCharAt(0);
            }

            // picking up the last digit in the dividend.
            StringBuilder divisorMultiple = new StringBuilder("7");
            divisorMultiple.replace(0, divisorMultiple.length(), commonUtils.padValue(divisorMultiple.toString(), remainder.toString()));

            divisorSB.replace(0, divisorSB.length(), octalMult(divisorMultiple.toString(), tmpDivisor));
            // The multiplication method leaves excess leading zeros. Lets trim them back.
            for(int i = 0; i < divisorLength+1; i++){
                divisorSB.deleteCharAt(0);
            }
            while(isValueLarger8(divisorSB.toString(), remainder.toString())){
                divisorMultiple.replace(0, divisorMultiple.length(), octalAdd(divisorMultiple.toString(), ONE.toString(), true));
                divisorSB.replace(0, divisorSB.length(), octalMult(divisorMultiple.toString(), tmpDivisor));
                // The multiplication method leaves excess leading zeros. Lets trim them back.
                for(int i = 0; i < divisorLength+1; i++){
                    divisorSB.deleteCharAt(0);
                }
            }
            // Derive the 8s compliment and then add.
            if(Integer.parseInt(divisorMultiple.toString()) > 0){
                divisor8s.replace(0, divisor8s.length(), deriveEightsCompliment(divisorSB.toString()));
                remainder.replace(0, remainder.length(), octalAdd(remainder.toString(), divisor8s.toString(), true));
            }

            // Append the quotient
            quotient.append(Integer.parseInt(divisorMultiple.toString()));
            remainder.replace(0, remainder.length(), commonUtils.trimLeadingZeros(remainder.toString()));

            results.insert(0, commonUtils.trimLeadingZeros(quotient.toString()) + " R: " + commonUtils.trimLeadingZeros(remainder.toString()));
        }
        return results.toString();
    }


    // Base 8 functions.
    private String deriveEightsCompliment(String value) {
        StringBuilder result = new StringBuilder();
        int seven = 7;
        int x = 0;

        if(value.length() == 1 & value.charAt(0) == '0'){
            return "0";
        }

        for(int j = value.length()-1; j > -1; j--){
            int singleOctalDigit = Integer.parseInt(String.valueOf(value.charAt(j)));
            int n = seven - singleOctalDigit;
            result.insert(0, n);
        }
        result.replace(0, result.length(), octalAdd(result.toString(), commonUtils.padValue("1", result.toString()), false));

        return result.toString();
    }



    // Value 1 should be larger or equal to value 2 to evaluate as true.
    private boolean isValueLarger8(String larger, String smaller){
        boolean TorF = false;

        for(int i = 0; i < larger.length(); i++){
            if(Character.getNumericValue(larger.charAt(i)) > Character.getNumericValue(smaller.charAt(i))){
                TorF = true;
                break;
            }
            else if(Character.getNumericValue(larger.charAt(i)) < Character.getNumericValue(smaller.charAt(i))){
                TorF = false;
                break;
            }
        }
        return TorF;
    }


    // This is to take the input integer from the multiplication method
    // while adding up the various values after the multiplication is
    // completed and provide the proper octal value and carry.
    private int[] octalMultiplyHelper(int r){
        int[] result = {0, 0};

        int q = 0;
        while(r >= 8){
            r = r - 8;
            q++;
        }
        result[0] = q;
        result[1] = r;
        return result;
    }

}