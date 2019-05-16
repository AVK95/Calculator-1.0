/*******************************************************************************
 * ---------------------------- Accumulator class -------------------------------
 * @author adityavkhandelwal@gmail.com
 * The Accumulator class implements the string parsing operations to support:
 * 1. digits entered by the calling object are stored in the appropriate place 
 *    (whole part / decimal part)
 * 2. Handle the decimal point correctly
 * 3. Display the number in a standard easily understandable way
 *******************************************************************************/

public class Accumulator implements Comparable{
    private String whole, decimal;
    
    public Accumulator() {
        whole = null;
        decimal = null;
    }
    
    public Accumulator(String wValue, String dValue) { //copy constructor
        whole = wValue;
        decimal = dValue;
    }
    
    public void set(double value) {
        String valueString = Double.toString(value);
        int decimalPos = valueString.indexOf('.');
        whole = valueString.substring(0, decimalPos); //extract the whole part
        //an extra zero is added at the beginning of the decimal string (see append function)
        decimal = "0" + valueString.substring(decimalPos + 1); //dont include the dot and add at extra zero at the beginning
        
        if(isZero(decimal)) decimal = null;
    }
    
    public void append(int n) { //append a digit
        if (whole == null) //decimal must also be null
            whole = Integer.toString(n);
        else if (decimal == null) //whole is not null and decimal is null
            whole = whole + Integer.toString(n); //concat the digit
        else {//both are not null, meaning the digit n must be appended to decimal
            decimal = decimal + Integer.toString(n);
            //there is an extra zero at the beginning of the decimal part which must be removed by the toDouble func
            //the extra zero comes because of how the decimal string is initialized by the append (char)
            //function when the decimal button is pressed.
        }
    }
    
    public void append(char c) { //append a decimal point
        if (whole == null)
            whole = "0"; //if . is added to an empty number, it is interpreted as "0."
        if (decimal == null) //only works if decimal is not already there!!
            decimal = Integer.toString(0);
    }
    
    //isZero function checks if the string represents a zero (is "000....0")
    //useful to remove an empty decimal part from "X.000" type numbers
    private static boolean isZero(String num) {
        char [] numArray = num.toCharArray();
        for (int i = 0; i < num.length(); i++)
            if (numArray[i] != '0') return false;
        return true;
    }
    
    public String toString() {
        String completeNumber;
        if (whole == null) return "0";
        else {
            if (decimal == null)
                completeNumber = whole;
            else if (isZero(decimal) && decimal.length() == 1)
                completeNumber = whole;
            else
                completeNumber = whole + "." + decimal.substring(1); //removes the extra zero at the beginning
        }
        return completeNumber;
    }
    
    public Double toDouble() {
        return Double.parseDouble(toString());
    }
    
    public void flush() {
        whole = null;
        decimal = null;
    }
    
    public Accumulator copy() {
        return new Accumulator(whole, decimal);
    }
    
    //Not used in this project, but can be useful in more advanced calculators
    public int compareTo(Object other) {
        Accumulator temp;
        if (other == null)
            throw new NullPointerException();
        else if (getClass() != other.getClass()) 
            throw new ClassCastException();
        else {
            temp = (Accumulator) other;
            if((whole == null) || (temp.whole == null))
                throw new NullPointerException();
            else {
                if (Integer.parseInt(whole) != Integer.parseInt(temp.whole))
                    return (int) Integer.parseInt(whole) - Integer.parseInt(temp.whole);
                
                if ((decimal == null) && (temp.decimal == null)) return 0;
                else if((decimal != null) && (temp.decimal == null))
                    return (int) Integer.parseInt(decimal);
                else if((decimal == null) && (temp.decimal != null))
                    return (int) -Integer.parseInt(temp.decimal);
                else
                    return (int) Integer.parseInt(decimal) - Integer.parseInt(temp.decimal);
            }
        }
    }   
}
