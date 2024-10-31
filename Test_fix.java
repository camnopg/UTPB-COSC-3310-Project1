/**
 * <h1>Test</h1>
 * A unit-testing script for UInt.
 * Ultimately, this is intended to function as an "auto-grader" for the project.
 * It will check the UInt class to ensure it functions properly and without errors,
 *   and assign a score to signify whether the class works as intended.
 *
 * @author Tim Fielder
 * @version 1.1 (Oct 23, 2024)
 */
public class Test_fix {
    public static void main(String[] args) {
        try {
            UInt u1 = new UInt(157);
            UInt u2 = new UInt(64);
            UInt u3 = new UInt(212);
            UInt u4 = new UInt(39);

            int grade = 0;

            // Default tests
            grade += checkString(1, u1.toString(), "0b010011101");
            grade += checkInt(2, u1.toInt(), 157);
            grade += checkString(3, u2.toString(), "0b01000000");
            grade += checkInt(4, u2.toInt(), 64);
            grade += checkString(5, u3.toString(), "0b011010100");
            grade += checkInt(6, u3.toInt(), 212);
            grade += checkString(7, u4.toString(), "0b0100111");
            grade += checkInt(8, u4.toInt(), 39);

            u2 = new UInt(943);

            // AND tests
            UInt t1 = UInt.and(u1, u3);
            grade += checkInt(9, t1.toInt(), 148);
            UInt t2 = UInt.and(u1, u4);
            grade += checkInt(10, t2.toInt(), 5);
            t1 = UInt.clone(u4);
            t1.and(u3);
            grade += checkInt(11, t1.toInt(), 4);
            t2 = UInt.clone(u3);
            t2.and(u2);
            grade += checkInt(12, t2.toInt(), 132);

            // OR tests
            t1 = UInt.or(u1, u3);
            grade += checkInt(13, t1.toInt(), 221);
            t2 = UInt.or(u4, u2);
            grade += checkInt(14, t2.toInt(), 47);
            t1 = UInt.clone(u4);
            t1.or(u3);
            grade += checkInt(15, t1.toInt(), 119);
            t2 = UInt.clone(u1);
            t2.or(u4);
            grade += checkInt(16, t2.toInt(), 191);

            // XOR tests
            t1 = UInt.xor(u3, u2);
            grade += checkInt(17, t1.toInt(), 379);
            t2 = UInt.xor(u4, u1);
            grade += checkInt(18, t2.toInt(), 58);
            t1 = UInt.clone(u3);
            t1.xor(u1);
            grade += checkInt(19, t1.toInt(), 73);
            t2 = UInt.clone(u2);
            t2.xor(u4);
            grade += checkInt(20, t2.toInt(), 904);

            // ADD tests
            t1 = UInt.add(u1, u2);
            grade += checkInt(21, t1.toInt(), 1100);
            t2 = UInt.add(u3, u4);
            grade += checkInt(22, t2.toInt(), 251);
            t1 = UInt.add(u4, u1);
            grade += checkInt(23, t1.toInt(), 196);
            t2 = UInt.add(u2, u3);
            grade += checkInt(24, t2.toInt(), 1155);

            t1 = UInt.clone(u1);
            t1.add(u1);
            grade += checkInt(25, t1.toInt(), 314);
            t1.add(u2);
            grade += checkInt(26, t1.toInt(), 1257);
            t1.add(u3);
            grade += checkInt(27, t1.toInt(), 1469);
            t1.add(u4);
            grade += checkInt(28, t1.toInt(), 1508);

            // Negation tests
            t1 = UInt.clone(u1);
            t1.negate();
            grade += checkString(29, t1.toString(), "0b101100011");
            t2 = UInt.clone(u2);
            t2.negate();
            grade += checkString(30, t2.toString(), "0b10001010001");
            t1 = UInt.clone(u3);
            t1.negate();
            grade += checkString(31, t1.toString(), "0b100101100");
            t2 = UInt.clone(u4);
            t2.negate();
            grade += checkString(32, t2.toString(), "0b1011001");

            // SUB tests
            t1 = UInt.sub(u2, u1);
            grade += checkInt(33, t1.toInt(), 786);
            t2 = UInt.sub(u2, u3);
            grade += checkInt(34, t2.toInt(), 731);
            t1 = UInt.sub(u2, u4);
            grade += checkInt(35, t1.toInt(), 904);
            t2 = UInt.sub(u3, u1);
            grade += checkInt(36, t2.toInt(), 55);

            t1 = UInt.clone(u2);
            t1.sub(u1);
            grade += checkInt(37, t1.toInt(), 786);
            t1.sub(u3);
            grade += checkInt(38, t1.toInt(), 574);
            t1.sub(u4);
            grade += checkInt(39, t1.toInt(), 535);
            t1.sub(u1);
            grade += checkInt(40, t1.toInt(), 378);

            // MUL tests
            t1 = u4.clone();
            t1.mul(u4);
            grade += checkInt(41, t1.toInt(), 1521) * 2;
            t1.mul(u1);
            grade += checkInt(42, t1.toInt(), 238797) * 2;
            t1 = UInt.mul(u1, u2);
            grade += checkInt(43, t1.toInt(), 148051) * 2;
            t1 = UInt.mul(u3, u4);
            grade += checkInt(44, t1.toInt(), 8268) * 2;
            t1 = UInt.mul(u2, u3);
            grade += checkInt(45, t1.toInt(), 199916) * 2;

            System.out.printf("%nGrade on Project 1 is: %d.%n", grade);
        } catch (Exception ex) {
            System.out.printf("%nGrade on Project 1 is: 0.%n");
            ex.printStackTrace();
        }
    }

    private static int checkString(int testNum, String test, String target) {
        if (test.equals(target)) {
            System.out.printf("Test %d passed!%n", testNum);
            return 2;
        } else {
            System.out.printf("Test %d failed!  Expected %s, received %s!%n", testNum, target, test);
            return 0;
        }
    }

    private static int checkInt(int testNum, int test, int target) {
        if (test == target) {
            System.out.printf("Test %d passed!%n", testNum);
            return 2;
        } else {
            System.out.printf("Test %d failed!  Expected %d, received %d!%n", testNum, target, test);
            return 0;
        }
    }
}
