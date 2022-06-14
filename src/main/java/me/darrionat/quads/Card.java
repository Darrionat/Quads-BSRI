package me.darrionat.quads;

import me.darrionat.matrixlib.algebra.sets.Quantity;
import me.darrionat.matrixlib.algebra.sets.Rational;

import java.util.Random;

public class Card {
    public final int intValue;
    private final boolean zero;

    public Card(int value) {
        this.intValue = value;
        this.zero = value == 0;
    }

    public static Card getRandomCard(int dim) {
        Random r = new Random();
        return new Card(r.nextInt((int) Math.pow(2, dim)));
    }

    public static Card parseCard(String binaryString) {
        return new Card(Integer.parseInt(binaryString, 2));
    }

    public static Card fromBitArray(Quantity[] bits) {
        StringBuilder builder = new StringBuilder();
        for (Quantity bit : bits) {
            builder.append(bit.toString());
        }
        return new Card(Integer.parseInt(builder.toString(), 2));
    }

    public Card add(Card B) {
        return new Card(intValue ^ B.intValue);
    }

    /**
     * Check if two cards are equal
     *
     * @param obj The card
     * @return {@code true} if both cards have equal bits and dimension; {@code false} otherwise
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Card B))
            return false;
        return intValue == B.intValue;
    }

    public int hashCode() {
        return intValue;
    }

    public String toString() {
        return intValue + "";
    }

    public String toBinary() {
        return Integer.toBinaryString(intValue);
    }

    /**
     * Whether the card is equal to the zero vector
     */
    public boolean zero() {
        return zero;
    }

    public Quantity[] getBits(int dim) {
        Quantity[] toReturn = new Quantity[dim];
        StringBuilder builder = new StringBuilder(toBinary());
        while (builder.length() != dim) {
            builder.insert(0, "0");
        }
        String s = builder.toString();
        for (int i = 0; i < s.length(); i++) {
            toReturn[i] = Rational.parseNumber(s.charAt(i) + "");
        }
        return toReturn;
    }
}