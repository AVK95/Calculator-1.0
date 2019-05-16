/*******************************************************************************
 * ---------------------------- Calculator class -------------------------------
 * @author adityavkhandelwal@gmail.com
 * The Calculator class implements the functionality. It uses the Accumulator
 * objects acc and reg which serve as operands. The lastAns stores the last answer
 * obtained (used by Ans button). operator stores the last operator that was pressed
 * by the user. equalsPressed and lastAnsAvailable are self-explanatory. active
 * stores which accumulator is currently active (acc: 1 / reg: 2). lastAction keeps
 * track of whether last key pressed was a number or an operator (otherwise pressing
 * an operator like + multiple times will cause a run-time exception). display
 * stores the string to be displayed on the calculator
 *******************************************************************************/

public class Calculator {
    //acc: first operand, red: second operand
    private Accumulator acc, reg, lastAns; //Accumulator class deals with the string parsing part
    private Character operator;
    private boolean equalsPressed, lastAnsAvailable;
    private int active, lastAction; //1: acc, 2: reg; 1: number, 2: operator
    private String display;
    //private String display, tempNum; not needed in the simple implementation
    
    private boolean err; //set to true if divide by zero error occured
    
    public Calculator() {
        acc = new Accumulator();
        reg = new Accumulator();
        lastAns = new Accumulator();
        operator = '\0';
        active = 1;
        equalsPressed = false;
        display = "0"; //initial display is 0
        lastAction = 0;
        err = false;
        lastAnsAvailable = false;
    }
    
    private void calculate() {
        switch (operator) {
            case '+':
                acc.set(acc.toDouble() + reg.toDouble());
                break;
            case '-':
                acc.set(acc.toDouble() - reg.toDouble());
                break;
            case '*':
                acc.set(acc.toDouble() * reg.toDouble());
                break;
            case '/':
                if (reg.toDouble() != 0.0) acc.set(acc.toDouble() / reg.toDouble());
                else err = true;
                break;
        }
        if(!err) { //lastAns is now available. Doesn't need = being pressed.
            lastAns = acc.copy();
            lastAnsAvailable = true;
        }
    }        
    
    //adds the digit pressed by the user into the active accumulator
    public void parseDigit(int n) {
        //if an error had occured, all buttons are "soft-disabled". User must press AC
        //to reset the calculator
        if (!err) {
            //If = was pressed as the last action and user began typing a number,
            //then we must clear the display first and start accepting the new number
            if (equalsPressed) {
                acc.flush();
                equalsPressed = false;
            }
        
            //append the digit pressed into the active accumulator. Display = active acc always
            if (active == 1) {
                acc.append(n);
                display = acc.toString();
            }
            else {
                reg.append(n);
                display = reg.toString();
            }
            lastAction = 1; //lastAction = number pressed
        }
    }
    
    //first performs the previously queued operation (allows user to queue infinite
    //number of operations and then sets the operator to the new operator pressed by the 
    //user. We also flush the second operand, allowing user to queue as many operations
    //as needed. The answer is always stored in the acc (first operand) by the calculate
    //function
    public void parseOperator(char op) {
        if(lastAction == 1) {
            if(active == 2)
                calculate();
            
            if (!err) {
                reg.flush();
                active = 2;
                display = acc.toString();
                equalsPressed = false;
                lastAction = 2;
        
                if (op == '=') {
                    equalsPressed = true;
                    active = 1;
                    lastAction = 1;
                    reg.flush();
                }
            }
            else
                display = "Error: Divide by Zero. Press AC";
        }
        operator = op;
    }
    
    public void decimal() {//decimal button pressed
        if (!err) {
            if (equalsPressed) {
                acc.flush();
                equalsPressed = false;
            }
        
            if (active == 1) {
                acc.append('.');
                display = acc.toString();
            }
            else {
                reg.append('.');
                display = reg.toString();
            }
            
            if(!display.contains(".")) display += Character.toString('.');
            lastAction = 1;
        }
    }
    
    public void clear() {
        acc.flush();
        reg.flush();
        operator = '\0';
        active = 1;
        equalsPressed = false;
        display = "0";
        err = false;
        lastAns.flush();
        lastAnsAvailable = false;
    }
    
    public void softClear() {
        if (!err) {
            display = "0";
            if(active == 1)
                acc.flush();
            else
                reg.flush();
        }
    }
    
    //displays the minus sign ONLY is the current accumulator is not 0
    public void plusMinus() {
        if(!err) {
            if ((active == 1) && (acc.toDouble() != 0)) {
                acc.set(-acc.toDouble());
                display = acc.toString();
            }
            else if ((active == 2) && (reg.toDouble() != 0)) {
                reg.set(-reg.toDouble());
                display = reg.toString();
            }
            equalsPressed = false;
        }
    }
    
    public void lastAnswer() {
        if(!err && lastAnsAvailable) {
            if(active == 1) {
                acc.set(lastAns.toDouble());
                display = acc.toString();
            }
            else {
                reg.set(lastAns.toDouble());
                display = reg.toString();
            }
            equalsPressed = false;
            lastAction = 1;
        }
    }
    
    public String toString() {
        return display;
    }
}