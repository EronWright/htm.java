package org.numenta.nupic.data;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Utilities to match some of the functionality found in Python's Numpy.
 * @author David Ray
 */
public class ArrayUtils {
    /**
     * Returns an array with the same shape and the contents
     * converted to doubles.
     * 
     * @param ints  an array of ints.
     * @return
     */
    public static double[] toDoubleArray(int[] ints) {
        double[] retVal = new double[ints.length];
        for(int i = 0;i < ints.length;i++) {
            retVal[i] = ints[i];
        }
        return retVal;
    }
    
    /**
     * Returns an array with the same shape and the contents
     * converted to integers.
     * 
     * @param doubs an array of doubles.
     * @return
     */
    public static int[] toIntArray(double[] doubs) {
        int[] retVal = new int[doubs.length];
        for(int i = 0;i < doubs.length;i++) {
            retVal[i] = (int)doubs[i];
        }
        return retVal;
    }
    
    /**
     * Returns a double array whose values are the maximum of the value
     * in the array and the max value argument.
     * @param doubs
     * @param maxValue
     * @return
     */
    public static double[] maximum(double[] doubs, double maxValue) {
        double[] retVal = new double[doubs.length];
        for(int i = 0;i < doubs.length;i++) {
            retVal[i] = Math.max(doubs[i], maxValue);
        }
        return retVal;
    }
    
    /**
     * Returns an array of identical shape containing the maximum
     * of the values between each corresponding index. Input arrays
     * must be the same length.
     * 
     * @param arr1
     * @param arr2
     * @return
     */
    public static int[] maxBetween(int[] arr1, int[] arr2) {
        int[] retVal = new int[arr1.length];
        for(int i = 0;i < arr1.length;i++) {
            retVal[i] = Math.max(arr1[i], arr2[i]);
        }
        return retVal;
    }
    
    /**
     * Returns an array of identical shape containing the minimum
     * of the values between each corresponding index. Input arrays
     * must be the same length.
     * 
     * @param arr1
     * @param arr2
     * @return
     */
    public static int[] minBetween(int[] arr1, int[] arr2) {
        int[] retVal = new int[arr1.length];
        for(int i = 0;i < arr1.length;i++) {
            retVal[i] = Math.min(arr1[i], arr2[i]);
        }
        return retVal;
    }
    
    /**
     * Implemented to be used as arguments in other operations.
     * see {@link ArrayUtils#retainLogicalAnd(int[], Condition[])};
     * {@link ArrayUtils#retainLogicalAnd(double[], Condition[])}.
     */
    public static interface Condition {
        /**
         * Convenience adapter to remove verbosity
         * @author metaware
         *
         */
        public class Adapter implements Condition {
            public boolean eval(int n) { return false; }
            public boolean eval(double d) { return false; }
        }
        public boolean eval(int n);
        public boolean eval(double d);
    }
    
    /**
     * Returns an array of values that test true for all of the 
     * specified {@link Condition}s.
     * 
     * @param values
     * @param conditions
     * @return
     */
    public static int[] retainLogicalAnd(int[] values, Condition[] conditions) {
        TIntArrayList l = new TIntArrayList();
        for(int i = 0;i < values.length;i++) {
            boolean result = true;
            for(int j = 0;j < conditions.length && result;j++) {
                result &= conditions[j].eval(values[i]);
            }
            if(result) l.add(values[i]);
        }
        return l.toArray();
    }
    
    /**
     * Returns an array of values that test true for all of the 
     * specified {@link Condition}s.
     * 
     * @param values
     * @param conditions
     * @return
     */
    public static double[] retainLogicalAnd(double[] values, Condition[] conditions) {
        TDoubleArrayList l = new TDoubleArrayList();
        for(int i = 0;i < values.length;i++) {
            boolean result = true;
            for(int j = 0;j < conditions.length && result;j++) {
                result &= conditions[j].eval(values[i]);
            }
            if(result) l.add(values[i]);
        }
        return l.toArray();
    }
    
    /**
     * Returns an array whose members are the quotient of the dividend array
     * values and the divisor array values.
     * 
     * @param dividend
     * @param divisor
     * @param dividend adjustment
     * @param divisor adjustment
     * 
     * @return
     * @throws  IllegalArgumentException    if the two argument arrays are not the same length
     */
    public static double[] divide(double[] dividend, double[] divisor, 
        double dividendAdjustment, double divisorAdjustment) {
        
        if(dividend.length != divisor.length) {
            throw new IllegalArgumentException(
                "The dividend array and the divisor array must be the same length");
        }
        double[] quotient = new double[dividend.length];
        double denom = 1;
        for(int i = 0;i < dividend.length;i++) {
            quotient[i] = (dividend[i] + dividendAdjustment) / 
            	((denom = divisor[i] + divisorAdjustment) == 0 ? 1 : denom); //Protect against division by 0
        }
        return quotient;
    }
    
    /**
     * Returns an array whose members are the product of the multiplicand array
     * values and the factor array values.
     * 
     * @param multiplicand
     * @param factor
     * @param multiplicand adjustment
     * @param factor adjustment
     * 
     * @return
     * @throws  IllegalArgumentException    if the two argument arrays are not the same length
     */
    public static double[] multiply(double[] multiplicand, double[] factor, 
        double multiplicandAdjustment, double factorAdjustment) {
        
        if(multiplicand.length != factor.length) {
            throw new IllegalArgumentException(
                "The multiplicand array and the factor array must be the same length");
        }
        double[] product = new double[multiplicand.length];
        for(int i = 0;i < multiplicand.length;i++) {
            product[i] = (multiplicand[i] + multiplicandAdjustment) * (factor[i] + factorAdjustment);
        }
        return product;
    }
    
    /**
     * Returns an integer array containing the result of subtraction
     * operations between corresponding indexes of the specified arrays.
     * 
     * @param minuend
     * @param subtrahend
     * @return
     */
    public static int[] subtract(int[] minuend, int[] subtrahend) {
        int[] retVal = new int[minuend.length];
        for(int i = 0;i < minuend.length;i++) {
            retVal[i] = minuend[i] - subtrahend[i];
        }
        return retVal;
    }
    
    /**
     * Returns the average of all the specified array contents.
     * @param arr
     * @return
     */
    public static double average(int[] arr) {
        int sum = 0;
        for(int i = 0;i < arr.length;i++) {
            sum += arr[i];
        }
        return sum / (double)arr.length;
    }
    
    /**
     * Returns the average of all the specified array contents.
     * @param arr
     * @return
     */
    public static double average(double[] arr) {
        double sum = 0;
        for(int i = 0;i < arr.length;i++) {
            sum += arr[i];
        }
        return sum / (double)arr.length;
    }
    
    /**
     * Returns the passed in array with every value being altered
     * by the addition of the specified amount.
     * 
     * @param arr
     * @param amount
     * @return
     */
    public static int[] add(int[] arr, int amount) {
        for(int i = 0;i < arr.length;i++) {
            arr[i] += amount;
        }
        return arr;
    }
    
    /**
     * Returns the sum of all contents in the specified array.
     * @param array
     * @return
     */
    public static int sum(int[] array) {
    	int sum = 0;
    	for(int i = 0;i < array.length;i++) {
    		sum += array[i];
    	}
    	return sum;
    }
    
    /**
     * Sparse or due to the arrays containing the indexes of "on bits",
     * the <em>or</em> of which is equal to the mere combination of the two
     * arguments - eliminating duplicates and sorting.
     * 
     * @param arg1
     * @param arg2
     * @return
     */
    public static int[] sparseBinaryOr(int[] arg1, int[] arg2) {
    	TIntArrayList t = new TIntArrayList(arg1);
    	t.addAll(arg2);
    	return unique(t.toArray());
    }
    
    /**
     * Prints the specified array to a returned String.
     * 
     * @param aObject   the array object to print.
     * @return  the array in string form suitable for display.
     */
    public static String print1DArray(Object aObject) {
        if (aObject.getClass().isArray()) {
            if (aObject instanceof Object[]) // can we cast to Object[]
                return Arrays.toString((Object[]) aObject);
            else {  // we can't cast to Object[] - case of primitive arrays
                int length = Array.getLength(aObject);
                Object[] objArr = new Object[length];
                for (int i=0; i<length; i++)
                    objArr[i] =  Array.get(aObject, i);
                return Arrays.toString(objArr);
            }
        }
        return "[]";
    }
     
    /**
     * Another utility to account for the difference between Python and Java.
     * Here the modulo operator is defined differently.
     * 
     * @param n
     * @param divisor
     * @return
     */
    public static double positiveRemainder(double n, double divisor) {
        if (n >= 0) {
            return n % divisor;
        } else {
            double val = divisor + (n % divisor);
            return val == divisor ? 0 : val;
        }
    }
    
    /**
     * Returns an array which starts from lowerBounds (inclusive) and
     * ends at the upperBounds (exclusive).
     * 
     * @param lowerBounds
     * @param upperBounds
     * @return
     */
    public static int[] range(int lowerBounds, int upperBounds) {
        TIntList ints = new TIntArrayList();
        for(int i = lowerBounds;i < upperBounds;i++) {
            ints.add(i);
        }
        return ints.toArray();
    }
    
    /**
     * Returns a sorted unique array of integers
     * 
     * @param nums  an unsorted array of integers with possible duplicates.
     * @return
     */
    public static int[] unique(int[] nums) {
        TIntHashSet set = new TIntHashSet(nums);
        int[] result = set.toArray();
        Arrays.sort(result);
        return result;
    }
    
    /**
     * Called to merge a list of dimension arrays into a sequential row-major indexed
     * list of coordinates.
     * 
     * @param dimensions    a list of dimension arrays, each array being a dimension 
     *                      of an n-dimensional array.
     * @return  a list of n-dimensional coordinates in row-major format.
     */
    public static List<TIntList> dimensionsToCoordinateList(List<int[]> dimensions) {
        int[] depthIndexes = new int[dimensions.size()];
        for(int i = 0;i < dimensions.size();i++) {
            depthIndexes[i] = 0;
        }
        List<TIntList> retVal = new ArrayList<TIntList>();
        recurseAssembleCoordinates(0, dimensions.size(), depthIndexes, new TIntArrayList(), dimensions, retVal);
        
        return retVal;
    }
    
    /**
     * Takes a list of arrays; each array specifying a dimension of an n-dimensional array and merges
     * them into a list of row-major indexed coordinates (the right most index incrementing the fastest).
     * 
     * This method recursively calls itself to step through the depth specified by each dimension and
     * enter the index at that depth in the list specified as "resultList". The resultList is sequentially
     * populated with the incrementing coordinates and holds the final list of coordinates - the returned
     * value from this method is not meaningful to the caller due to the final results being populated
     * within the "resultList".
     * 
     * @param depth                 the current dimension depth
     * @param maxDepth              the maximum number of dimensions to the array
     * @param depthIndexes          an array holding the current index at the depth implied by that index's position.
     * @param coords                a list of coordinates at a position in the sequence of coordinates
     * @param dimensionIndexes      the array expressing the indexes of a given dimension
     * @param resultList            the container of the final list of coordinates
     * @return                      meaningless for the caller but is of interim significance during the recursion
     */
     private static TIntList recurseAssembleCoordinates(int depth, int maxDepth, int[] depthIndexes, 
        TIntList coords, List<int[]> dimensionIndexes, List<TIntList> resultList) {
        
        //Return null if we've added all indexes for each dimension
        if(depth < maxDepth) {
            //Add the index coordinate at the current depth
            coords.add(dimensionIndexes.get(depth)[depthIndexes[depth]]);
            //Go to the next dimensional coordinate and add that
            coords = recurseAssembleCoordinates(depth + 1, maxDepth, depthIndexes, coords, dimensionIndexes, resultList);
            
            //Return until we're back at the top
            if(depth > 0) {
                return null;
            }else{
                //Adjust all depth indexes by incrementing to its max then wrapping to zero
                for(int i = depthIndexes.length - 1;i >= 0;i--) {
                    if(depthIndexes[i] < dimensionIndexes.get(i).length - 1) {
                        depthIndexes[i] += 1;
                        break;
                    }else{
                        if(i == 0) return null; //if the leftmost index is at its max, we're done.
                        depthIndexes[i] = 0;
                    }
                }
                recurseAssembleCoordinates(0, dimensionIndexes.size(), depthIndexes, new TIntArrayList(), dimensionIndexes, resultList);
                //We've finished so unwind.
                return null;
            }
        }
        //Add the coordinates to the result container
        resultList.add(coords);
        //Return null if we've added all indexes for each dimension
        return null;
    }

     /**
      * Returns a random, sorted, and  unique array of the specified sample size of 
      * selections from the specified list of choices.
      * 
      * @param sampleSize   the number of selections in the returned sample
      * @param choices      the list of choices to select from
      * @param random       a random number generator
      * @return a sample of numbers of the specified size
      */
    public static int[] sample(int sampleSize, TIntArrayList choices, Random random) {
        TIntHashSet temp = new TIntHashSet();
        int upperBound = choices.size();
        for(int i = 0;i < sampleSize;i++) {
            int randomIdx = random.nextInt(upperBound);
            while(temp.contains(choices.get(randomIdx))) {
                randomIdx = random.nextInt(upperBound);
            }
            temp.add(choices.get(randomIdx));
        }
        TIntArrayList al = new TIntArrayList(temp);
        al.sort();
        return al.toArray();
    }
    
    /**
     * Ensures that each entry in the specified array has a min value
     * equal to or greater than the specified min and a maximum value less
     * than or equal to the specified max.
     * 
     * @param values    the values to clip
     * @param min       the minimum value
     * @param max       the maximum value
     */
    public static void clip(double[] values, double min, double max) {
        for(int i = 0;i < values.length;i++) {
            values[i] = Math.min(1, Math.max(0, values[i]));
        }
    }
    
    /**
     * Returns the count of values in the specified array that are
     * greater than the specified compare value
     * 
     * @param compare   the value to compare to
     * @param array     the values being compared
     * 
     * @return  the count of values greater
     */
    public static int valueGreaterCount(double compare, double[] array) {
        int count = 0;
        for(int i = 0;i < array.length;i++) {
            if(array[i] > compare) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Raises the values in the specified array by the amount specified
     * @param amount        the amount to raise the values
     * @param values        the values to raise
     */
    public static void raiseValuesBy(double amount, double[] values) {
        for(int i = 0;i < values.length;i++) {
            values[i] += amount;
        }
    }
    
    /**
     * Makes all values in the specified array which are less than or equal to the specified
     * "x" value, equal to the specified "y".
     * @param array
     * @param x     the comparison
     * @param y     the value to set if the comparison fails
     */
    public static void lessThanOrEqualXThanSetToY(double[] array, double x, double y) {
        for(int i = 0;i < array.length;i++) {
            if(array[i] <= x) array[i] = y;
        }
    }
    
    /**
     * Makes all values in the specified array which are less than the specified
     * "x" value, equal to the specified "y".
     * @param array
     * @param x     the comparison
     * @param y     the value to set if the comparison fails
     */
    public static void lessThanXThanSetToY(double[] array, double x, double y) {
        for(int i = 0;i < array.length;i++) {
            if(array[i] < x) array[i] = y;
        }
    }
    
    /**
     * Makes all values in the specified array which are greater than or equal to the specified
     * "x" value, equal to the specified "y".
     * @param array
     * @param x     the comparison
     * @param y     the value to set if the comparison fails
     */
    public static void greaterThanOrEqualXThanSetToY(double[] array, double x, double y) {
        for(int i = 0;i < array.length;i++) {
            if(array[i] >= x) array[i] = y;
        }
    }
    
    /**
     * Makes all values in the specified array which are greater than the specified
     * "x" value, equal to the specified "y".
     * @param array
     * @param x     the comparison
     * @param y     the value to set if the comparison fails
     */
    public static void greaterThanXThanSetToY(double[] array, double x, double y) {
        for(int i = 0;i < array.length;i++) {
            if(array[i] > x) array[i] = y;
        }
    }
    
    /**
     * Returns the maximum value in the specified array
     * @param array
     * @return
     */
    public static int max(int[] array) {
        int max = Integer.MIN_VALUE;
        for(int i = 0;i < array.length;i++) {
            if(array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
    
    /**
     * Returns the minimum value in the specified array
     * @param array
     * @return
     */
    public static int min(int[] array) {
        int min = Integer.MAX_VALUE;
        for(int i = 0;i < array.length;i++) {
            if(array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
}