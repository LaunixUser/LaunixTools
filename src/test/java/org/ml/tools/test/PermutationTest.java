/*
 * The MIT License
 *
 * Copyright 2019 Dr. Matthias Laux.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.ml.tools.test;

import java.util.HashMap;
import java.util.Map;
import org.ml.tools.math.Permutations;
import org.ml.tools.math.Tools;

/**
 * This is a test based on the following mathematical challenge:
 *
 * Take a whole positive number a and permutate its digits such that the new
 * reordered numbers is 3*a. Show that the resulting numbers is divisible by 27
 * without remainder
 *
 * @author Dr. Matthias Laux
 */
public class PermutationTest {

    public static void main(String[] args) {

        Permutations p;

        int testCase = 1;
        switch (testCase) {

            case 0:

                p = new Permutations();
                int[] input = new int[4];
                input[0] = 'L';
                input[1] = 'A';
                input[2] = 'U';
                input[3] = 'X';
                int[][] r0 = p.generate(input);
                for (int i = 0; i < r0.length; i++) {
                    System.out.println(String.format("%04d", i) + " : " + printCharArray(r0[i]));
                }
                break;

            case 1:

                p = new Permutations();
                int[] inp = new int[4];
                inp[0] = 0;
                inp[1] = 17;
                inp[2] = 28;
                inp[3] = 35;
                int[][] r1 = p.generate(inp);
                for (int i = 0; i < r1.length; i++) {
                    System.out.println(String.format("%04d", i) + " : " + printIntArray(r1[i]));
                }
                break;

            case 2:

                for (int n = 2; n < 6; n++) {

                    p = new Permutations();
                    Map<Integer, Integer> found = new HashMap<>();
                    int[][] r2 = p.generate(n);

                    //.... Loop over all numbers with n digits
                    for (int a = (int) Math.pow(10, n - 1); a < (int) Math.pow(10, n); a++) {

                        int value3 = 3 * a;   //.... Check value: the reordered number has to be 3 times the original one
                        int[] digits = Tools.getDigits(a);   //.... The digits to permutate

                        //.... Check all permutations of the digits of a
                        for (int i = 0; i < r2.length; i++) {
                            int value = 0;
                            int f = 1;
                            for (int j = 0; j < n; j++) {
                                value += digits[r2[i][j]] * f;
                                f = 10 * f;
                            }

                            //.... This means we have found one permutation
                            if (value == value3) {
                                if (!found.containsKey(a) || found.get(a) != value) {
                                    System.out.println(String.format("n = %2d : a = %6d Reordered = %6d  Remainder = %2d", n, a, value, value % 27));
                                    found.put(a, value);
                                }
                            }
                        }

                    }
                }
                break;
                
            case 3:

                p = new Permutations();
                int[][] r3 = p.generate("AUT");
                for (int i = 0; i < r3.length; i++) {
                    System.out.println(String.format("%04d", i) + " : " + printCharArray(r3[i]));
                }
                break;

        }
    }

    /**
     *
     * @param d
     * @return
     */
    public static String printIntArray(int[] d) {
        StringBuilder sb = new StringBuilder(200);
        for (int value : d) {
            sb.append(String.format("%2d ", value));
        }
        return sb.toString();
    }

    /**
     *
     * @param d
     * @return
     */
    public static String printCharArray(int[] d) {
        StringBuilder sb = new StringBuilder(200);
        for (int value : d) {
            sb.append(String.format("%2s ", (char) value));
        }
        return sb.toString();
    }

}
