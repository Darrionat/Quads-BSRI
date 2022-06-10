package me.darrionat.quads;

import me.darrionat.matrixlib.algebra.sets.Quantity;
import me.darrionat.matrixlib.algebra.sets.Rational;

import java.math.BigInteger;
import java.util.Random;

public class Card {
    private final Rational[] bits;
    private boolean zero = true;
    private String asString;

    protected Card(int... bits) {
        this(toRationals(bits));
    }

    /**
     * Creates a card from the given rationals.
     * <p>
     * The given array of rationals will be taken mod 2 to form a bit string.
     *
     * @param bitString The bit string.
     */
    public Card(Rational[] bitString) {
        bits = new Rational[bitString.length];
        for (int i = 0; i < bitString.length; i++) {
            Rational bit = bitString[i].mod(BigInteger.TWO);
            this.bits[i] = bit;
            if (!bit.zero())
                zero = false;
        }
    }

    public Card(Quantity[] bitString) {
        bits = new Rational[bitString.length];
        for (int i = 0; i < bitString.length; i++) {
            int intBit = bitString[i].intValue() % 2;
            Rational bit = new Rational(intBit);
            this.bits[i] = bit;
            if (!bit.zero())
                zero = false;
        }
    }

    private static Rational[] toRationals(int... bits) {
        Rational[] toReturn = new Rational[bits.length];
        for (int i = 0; i < bits.length; i++) {
            toReturn[i] = new Rational(bits[i]);
        }
        return toReturn;
    }

    public static Card getRandomCard(int dim) {
        Random r = new Random();
        Rational[] bits = new Rational[dim];
        for (int i = 0; i < dim; i++) {
            // random value between 0 (inclusive) and 2 (exclusive) => value in {0,1}
            bits[i] = r.nextInt(2) == 0 ? Rational.ZERO : Rational.ONE;
        }
        return new Card(bits);
    }

    public static Card parseCard(String cardString) {
        char[] bits = cardString.toCharArray();
        return new Card(
                Integer.valueOf(bits[0] + ""),
                Integer.valueOf(bits[1] + ""),
                Integer.valueOf(bits[2] + ""),
                Integer.valueOf(bits[3] + ""));
    }

    public Rational[] getBits() {
        return bits;
    }

    public Card add(Card B) {
        Rational[] C = new Rational[dimension()];
        for (int i = 0; i < dimension(); i++) {
            // We don't need to mod 2 here because the constructor does for us
            C[i] = bits[i].addRational(B.bits[i]);
        }
        return new Card(C);
    }

    public int dimension() {
        return bits.length;
    }

    /**
     * Check if two cards are equal
     *
     * @param obj The card
     * @return {@code true} if both cards have equal bits and dimension; {@code false} otherwise
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Card))
            return false;
        Card B = (Card) obj;
        if (dimension() != B.dimension())
            return false;
        for (int i = 0; i < dimension(); i++) {
            if (!bits[i].equals(B.bits[i]))
                return false;
        }
        return true;
    }

    public int hashCode() {
        return Integer.parseInt(toString(), 2);
    }

    public String toString() {
        if (asString != null)
            return asString;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bits.length; i++) {
            builder.append(bits[i]);
        }
        return asString = builder.toString();
    }

    /**
     * Whether the card is equal to the zero vector
     */
    public boolean zero() {
        return zero;
    }
}