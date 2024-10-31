/**
 * Cameron Glenn
 * 10/31/2024
 * COSC-3310.001 Digital Computer Organization
 * Unsigned Bit Project 1
 */

import java.util.Arrays;

/**
 * <h1>UInt</h1>
 * Represents an unsigned integer using a boolean array to store the binary representation.
 * Each bit is stored as a boolean value, where true represents 1 and false represents 0.
 *
 * @author Tim Fielder
 * @version 1.0 (Sept 30, 2024)
 */
public class UInt {

    // The array representing the bits of the unsigned integer.
    protected boolean[] bits;

    // The number of bits used to represent the unsigned integer.
    protected int length;
    
    /**
     * Determines the number of bits needed to store int in binary format.
     * 
     * @param i The desired integer to determine the required amount of bits.
     * @return The required length to store the integer in binary.
     */
     public static int getLength(int i) {
         int req_length;
         
         if (i == 0) {
            req_length = 1;
        }
        else {
            req_length = (int)(Math.ceil(Math.log(i)/Math.log(2.0)) + 1);
        }
        
        return req_length;
     }
     
    /**
     * Constructs a new UInt by cloning an existing UInt object.
     *
     * @param toClone The UInt object to clone.
     */
    public UInt(UInt toClone) {
        this.length = toClone.length;
        this.bits = Arrays.copyOf(toClone.bits, this.length);
    }

    /**
     * Constructs a new UInt from an integer value.
     * The integer is converted to its binary representation and stored in the bits array.
     *
     * @param i The integer value to convert to a UInt.
     */
    public UInt(int i) {
        // Determine the number of bits needed to store i in binary format.
        // Added logic so this doesn't break if we want zero -Cam
        length = UInt.getLength(i);
        bits = new boolean[length];

        // Convert the integer to binary and store each bit in the array.
        for (int b = length-1; b >= 0; b--) {
            // We use a ternary to decompose the integer into binary digits, starting with the 1s place.
            bits[b] = i % 2 == 1;
            // Right shift the integer to process the next bit.
            i = i >> 1;

            // Deprecated analog method
            /*int p = 0;
            while (Math.pow(2, p) < i) {
                p++;
            }
            p--;
            bits[p] = true;
            i -= Math.pow(2, p);*/
        }
    }
    
    /**
     * Constructs a new UInt from a string value containing the desired bits.
     * For testing purposes while I was building out the methods.
     * 
     * @param bits The string representation of the bits we want,
     */
    public UInt(String bin) {
        length = bin.length();
        bits = new boolean[length];
        
        for (int i = 0; i < length; i++) {
            bits[i] = (int)bin.charAt(i) % 2 == 1;
        }
    }
    
    /**
     * Constructs a new UInt from a boolean array containing the desired bits.
     * For use in the mul() function.
     * 
     * @param arr The boolean array containing our bits.
     */
    public UInt(boolean[] arr) {
        length = arr.length;
        bits = new boolean[length];
        
        for (int i = 0; i < length; i++) {
            bits[i] = arr[i];
        }
    }

    /**
     * Creates and returns a copy of this UInt object.
     *
     * @return A new UInt object that is a clone of this instance.
     */
    @Override
    public UInt clone() {
        return new UInt(this);
    }

    /**
     * Creates and returns a copy of the given UInt object.
     *
     * @param u The UInt object to clone.
     * @return A new UInt object that is a copy of the given object.
     */
    public static UInt clone(UInt u) {
        return new UInt(u);
    }

    /**
     * Converts this UInt to its integer representation.
     *
     * @return The integer value corresponding to this UInt.
     */
    public int toInt() {
        int t = 0;
        // Traverse the bits array to reconstruct the integer value.
        for (int i = 0; i < length; i++) {
            // Again, using a ternary to now re-construct the int value, starting with the most-significant bit.
            t = t + (bits[i] ? 1 : 0);
            // Shift the value left for the next bit.
            t = t << 1;
        }
        return t >> 1; // Adjust for the last shift.
    }

    /**
     * Static method to retrieve the int value from a generic UInt object.
     *
     * @param u The UInt to convert.
     * @return The int value represented by u.
     */
    public static int toInt(UInt u) {
        return u.toInt();
    }

    /**
     * Returns a String representation of this binary object with a leading 0b.
     *
     * @return The constructed String.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("0b");
        // Construct the String starting with the most-significant bit.
        for (int i = 0; i < length; i++) {
            // Again, we use a ternary here to convert from true/false to 1/0
            s.append(bits[i] ? "1" : "0");
        }
        return s.toString();
    }

    /**
     * Performs a logical AND operation using this.bits and u.bits, with the result stored in this.bits.
     *
     * @param u The UInt to AND this against.
     */
    public void and(UInt u) {
        // We want to traverse the bits arrays to perform our AND operation.
        // But keep in mind that the arrays may not be the same length.
        // So first we use Math.min to determine which is shorter.
        // Then we need to align the two arrays at the 1s place, which we accomplish by indexing them at length-i-1.
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] &
                            u.bits[u.length - i - 1];
        }
        // In the specific case that this.length is greater, there are additional elements of
        //   this.bits that are not getting ANDed against anything.
        // Depending on the implementation, we may want to treat the operation as implicitly padding
        //   the u.bits array to match the length of this.bits, in which case what we actually
        //   perform is simply setting the remaining indices of this.bits to false.
        // Note that while this logic is helpful for the AND operation if we want to use this
        //   implementation (implicit padding), it is never necessary for the OR and XOR operations.
        if (this.length > u.length) {
            for (int i = u.length; i < this.length; i++) {
                this.bits[this.length - i - 1] = false;
            }
        }
    }

    /**
     * Accepts a pair of UInt objects and uses a temporary clone to safely AND them together (without changing either).
     *
     * @param a The first UInt
     * @param b The second UInt
     * @return The temp object containing the result of the AND op.
     */
    public static UInt and(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.and(b);
        return temp;
    }
    
    /**
     * Performs a logical OR operation using this.bits and u.bits, with the result being stored in this.bits.
     * 
     * @param u The UInt to OR this against.
     */
    public void or(UInt u) {
        // Using the same method to align the bits as in the and method
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] |
                            u.bits[u.length - i - 1];
        }
    }
    
    /**
     * Accepts a pair of UInt objects and uses a temporary clone to safely OR them together.
     * 
     * @param a The first UInt to complete the OR operation on.
     * @param b The second UInt to complete the OR operation on.
     * @return The temporary object containing the result of the OR operation.
     */
    public static UInt or(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.or(b);
        return temp;
    }
    
    /**
     * Performs a logic XOR operation using this.bits and u.bits, with the result store in this.bits.
     * 
     * @param u The UInt to XOR this against.
     */
    public void xor(UInt u) {
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] ^
                            u.bits[u.length - i - 1];
        }
    }
    
    /**
     * Accepts a pair of UInt objects and uses a temporary clone to safely XOR them together.
     * 
     * @param a The first UInt to complete the XOR operation on.
     * @param b The second UInt to complete the XOR operation on.
     * @return The temporary object containing the result of the XOR operation.
     */
    public static UInt xor(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.xor(b);
        return temp;
    }
    
    /**
     * Performs addition on two bits using a full adder.
     * 
     * @param a The first boolean bit to perform the add on.
     * @param b The second boolean bit to perform the add on.
     * @param c_in The third boolean bit to perform the add on. The carry bit.
     * @return A boolean tuple containing the sum and c_out (carry bit).
     */
    public static boolean[] fullAdd(boolean a, boolean b, boolean c_in) {
        boolean axb = a ^ b;
        boolean sum = axb ^ c_in;
        boolean c_out = (axb & c_in) | (a & b);
        boolean[] output = {sum, c_out};
        return output;
    }
    
    /**
     * Performs addition on two UInt objects.
     * 
     * @param u The UInt to add to this.
     */
    public void add(UInt u) {
        // We will use this string builder to build our output (so it can have variable length)
        StringBuilder s = new StringBuilder("");
        int min = Math.min(this.length, u.length);
        int max = Math.max(this.length, u.length);
        int bit;
        boolean[] out = new boolean[2];
        boolean c_in = false;
        
        // Build our bit-string
        for (int i = 0; i < max; i++) {
            if (i < min) {
                // Do a full add on our two current bits
                out = UInt.fullAdd(this.bits[this.length - i - 1],
                u.bits[u.length - i - 1], c_in);
                // Assign the carry bit
                c_in = out[1];
                // Assign the sum
                bit = out[0] ? 1 : 0;
                s.append(bit);
            }
            else {
                // This else statement is needed if one of the UInts is larger than the other
                UInt larger_num = (this.length == max) ? this : u;
                // We directly input false here because the other number is too short
                // AKA There is a "zero" there. We still need to continue for c_in
                out = UInt.fullAdd(larger_num.bits[larger_num.length - i - 1], false, c_in);
                // Assign the carry bit
                c_in = out[1];
                // Assign the sum
                bit = out[0] ? 1 : 0;
                s.append(bit);
            }
            
        }
        // Append the final carry
        if(c_in) {
            s.append(1);
        }


        // Convert our bit string into bools in bits[]
        this.bits = new boolean[s.toString().length()];
        this.length = s.toString().length();
        for (int i = 0; i <= s.toString().length() - 1; i++) {
            this.bits[i] = (s.toString().charAt(s.toString().length() - 1 - i) == '1') ? true : false;
        }
        
    }
    
    /**
     * Accepts a pair of UInt objects and uses a temporary clone to safely add them together.
     * 
     * @param a The first UInt to perform the add operation on.
     * @param b The second UInt to perform the add operation on.
     * @return The temporary object containing the result of the add operation.
     */
    public static UInt add(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.add(b);
        return temp;
    }
    
    /**
     * Negates the UInt this is called on. Uses 2's complement.
     */
    public void negate() {
        // First we complement the number
        for (int i = 0; i < this.length; i++) {
            this.bits[this.length - i - 1] = this.bits[this.length - i - 1] ? false : true;
        }
        // Then we add one
        UInt temp = new UInt(1);
        this.add(temp);
    }
    
    /**
     * Pads the UInt this is called on with leading zeroes. Matches the length of the provided UInt.
     * 
     * @param a The UInt we want to pad this to match lengths.
     */
    public void pad(UInt a) {
        // No padding is necessary
        if (this.bits.length > a.bits.length || this.bits.length == a.bits.length) {
            // I had code here before, now I'm leaving the logic in case I want
            // to add anything back.
        }
        else {
            // Pad with extra zero's
            boolean[] original = Arrays.copyOf(this.bits, this.bits.length);
            boolean[] padding = new boolean[a.bits.length - this.bits.length];
            this.bits = new boolean[a.bits.length];
            this.length = a.bits.length;
            System.arraycopy(padding, 0, this.bits, 0, padding.length);
            System.arraycopy(original, 0, this.bits, padding.length, original.length);
        }
        
    }
    
    /**
     * Determines whether this UInt is greater than the passed UInt.
     * 
     * @param b The UInt we are comparing this UInt against. this > b
     * @return True if this UInt is greater than UInt b.
     */
    public boolean greaterThan(UInt b) {
        // I added this method because simply converting two unsigned bit numbers
        // into ints using toInt() to check which was greater felt like cheating. 
        // Instead, we will do it the "real" way.
        boolean output = false;
        
        // First, pad both binary numbers.
        this.pad(b); //a
        b.pad(this); //b
        
        // Now compare, starting from the left.
        for (int i = 0; i < this.bits.length; i++) {
            if (this.bits[i] != b.bits[i]) {
                // One char was 1, one was 0. The one with 1 is greater.
                output = this.bits[i];
                break;
            }
        }
        return output;
    }
    
    /**
     * Performs subtraction on two UInt objects using 2's complement subtraction. 
     * Subtracts the passed variable from the called UInt. 
     * Coerces negative values into zero.
     * 
     * @param u The variable we will be subtracting from this.
     */
    public void sub(UInt u) {
        
        if (u.greaterThan(this) || Arrays.equals(this.bits, u.bits)) {
            // Coerce to 0
            this.length = 1;
            this.bits = new boolean[1];
            this.bits[0] = false;
        }
        else {
            UInt tempU = u.clone();
            tempU.negate();
            this.add(tempU);
            // Omit the carry
            boolean[] temp = this.bits;
            this.length -= 1;
            this.bits = Arrays.copyOfRange(temp, 1, this.bits.length);
        }
    
    }
    
    /**
     * Accepts a pair of UInt objects and uses a temporary clone to safely subtract them from one another.
     * 
     * @param a The first UInt we want to subtract from.
     * @param b The second UInt we want to subtract from the first.
     * @return The temporary object containing the difference.
     */
    public static UInt sub(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.sub(b);
        return temp;
    }
    
    /**
     * Performs a right arithmetic shift on this UInt.
     */
    public void shiftRight() {
        // Start from the right, reference bit to left
        // last bit is left unchanged (right shift duplicates MSB).
        for (int i = this.length - 1; i > 0; i--) {
            this.bits[i] = this.bits[i - 1];
        }
    }
    
    /**
     * Pads our UInt with a zero if there is a leading 1.
     * Had to make this because we are using booth's algorithm (?) for unsigned bits.
     */
    public void unsign() {
        if (this.bits[0]) {
            boolean[] temp = new boolean[this.bits.length + 1];
            System.arraycopy(this.bits, 0, temp, 1, this.bits.length);
            this.length += 1;
            this.bits = temp;
        }
    }
    
    /**
     * Performs multiplication on two UInt objects using Booth's Algorithm.
     * 
     * @param u The UInt we want to multiply this by.
     */
    public void mul(UInt u) {
        // TODO Using Booth's algorithm, perform multiplication
        // This one will require that you increase the length of bits, up to a maximum of X+Y.
        // Having negate() and add() will obviously be useful here.
        // Also note the Booth's always treats binary values as if they are signed,
        //   while this class is only intended to use unsigned values.
        // This means that you may need to pad your bits array with a leading 0 if it's not already long enough.
        
        // We will probably need to pad our binary values here
        this.unsign();
        u.unsign();
        
        int x = this.length;
        int y = u.length;
        
        boolean[] arrA = new boolean[x + y + 1];
        boolean[] arrS = new boolean[x + y + 1];
        boolean[] arrP = new boolean[x + y + 1];
        
        UInt negM = this.clone();
        negM.negate();
        
        // Determine the initial values of A, S, and P.
        // Initial value of A
        System.arraycopy(this.bits, 0, arrA, 0, this.bits.length);
        UInt A = new UInt(arrA);
        // Initial value of S
        System.arraycopy(negM.bits, 0, arrS, 0, negM.bits.length);
        UInt S = new UInt(arrS);
        // Initial value of P
        System.arraycopy(u.bits, 0, arrP, x, u.bits.length);
        UInt P = new UInt(arrP);
        
        for (int i = 0; i < y; i++) {
            // Two least significant bits of P
            boolean first = P.bits[P.bits.length-2];
            boolean second = P.bits[P.bits.length-1];
            
            if (!first && second) {
                P.add(A);
            }
            else if (first && !second) {
                P.add(S);
            }
            
            // Remove any overflow here (we must ignore it)
            if (P.bits.length > x + y + 1) {
                int overflow = P.bits.length - (x + y + 1);
                P.length = x + y + 1;
                boolean[] temp = Arrays.copyOfRange(P.bits, overflow, P.bits.length);
                P.bits = temp;
            }
            
            P.shiftRight();
        }
        boolean[] temp = Arrays.copyOfRange(P.bits, 0, P.bits.length - 1);
        this.length = P.length - 1;
        this.bits = new boolean[this.length];
        this.bits = temp;
    }
    
    /**
     * Accepts a pair of UInt objects and uses a temporary clone to safely multiply them.
     * 
     * @param a The first UInt we want to multiply.
     * @param b The second UInt we want to multiply.
     * @return The temporary object containing the product.
     */
    public static UInt mul(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.mul(b);
        return temp;
    }
    
    // For testing individual methods
    public static void main(String args[]) {
    UInt a = new UInt(212);
    UInt b = new UInt(157);
    UInt c = UInt.sub(a, b);
    System.out.println(c.toInt());
    System.out.println(a.toInt());
    System.out.println(b.toInt());

    }
}
