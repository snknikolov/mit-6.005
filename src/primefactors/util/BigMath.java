package primefactors.util;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

public class BigMath {

    /**
     * Given a BigInteger input n, where n >= 0, returns the largest BigInteger r such that r*r <= n.
     * 
     * For n < 0, returns 0.
     * 
     * 
     * details: http://faruk.akgul.org/blog/javas-missing-algorithm-biginteger-sqrt
     * 
     * @param n BigInteger input.
     * @return for n >= 0: largest BigInteger r such that r*r <= n.
     *             n <  0: BigInteger 0
     */
    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while(b.compareTo(a) >= 0) {
          BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
          if (mid.multiply(mid).compareTo(n) > 0) 
              b = mid.subtract(BigInteger.ONE);
          else 
              a = mid.add(BigInteger.ONE);
        }
        return a.subtract(BigInteger.ONE);
    }
    
    /**
     * 
     * @param n BigInteger N such that 2 <= N
     * @param lo, hi such that 1 <= low <= hi
     * @return all prime BigIntegers x such that
     *         low <= x <= hi AND x divides N evenly.
     *         Repeated factors will be found multiple times.
     */
    public static List<BigInteger> findAllPrimeFactors
                        (BigInteger n, BigInteger lo, BigInteger hi) {
        assert n.compareTo(new BigInteger("2")) >= 0;
        assert lo.compareTo(BigInteger.ONE) >= 0;
        assert hi.compareTo(lo) >= 0;
        
        List<BigInteger> primeFactors = new LinkedList<BigInteger>();
        
        // Make a copy of the original to be divided inside the loop.
        BigInteger N = new BigInteger(n.toString());
        for (BigInteger x = lo; x.compareTo(hi) <= 0; x = x.nextProbablePrime()) {
            if (n.remainder(x).compareTo(BigInteger.ZERO) == 0) {
                while (N.remainder(x).compareTo(BigInteger.ZERO) == 0) {
                    primeFactors.add(x);
                    N = N.divide(x);
                }
                // Restore the original value of N.
                N = n;
            }
        }
        return primeFactors;
    }
}