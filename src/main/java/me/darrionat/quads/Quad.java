package me.darrionat.quads;

import me.darrionat.matrixlib.algebra.sets.Rational;
import me.darrionat.quads.interfaces.QuadState;

import java.util.Arrays;
import java.util.HashSet;

public class Quad implements QuadState {
    private final Card[] cards;
    private String asString;

    public Quad(Card A, Card B, Card C, Card D) {
        if (!formsQuad(A, B, C, D))
            throw new IllegalArgumentException("Cards do not form a quad");
        cards = new Card[]{A, B, C, D};
    }

    public static Card completeQuad(Card A, Card B, Card C) {
        return A.add(B).add(C);
    }

    public static Quad randomQuad(int dim) {
        if (dim < 2) {
            throw new IllegalArgumentException("Dimension must be greater than 1");
        }
        Card[] unique = getThreeUniqueCards(dim);
        Card A = unique[0], B = unique[1], C = unique[2];
        Card D = completeQuad(A, B, C);
        return new Quad(A, B, C, D);
    }

    static Card[] getThreeUniqueCards(int dim) {
        Card A = Card.getRandomCard(dim);
        Card B = Card.getRandomCard(dim);
        while (A.equals(B)) {
            B = Card.getRandomCard(dim);
        }
        Card C = Card.getRandomCard(dim);
        while (A.equals(C) || B.equals(C)) {
            C = Card.getRandomCard(dim);
        }
        return new Card[]{A, B, C};
    }

    public static boolean formsQuad(Card A, Card B, Card C, Card D) {
        if (A.equals(B) || A.equals(C) || A.equals(D) || B.equals(C) || B.equals(D) || C.equals(D))
            return false;
        return A.add(B).add(C).add(D).zero();
    }

    public static void main(String[] args) {
        // todo test equals method
        Card A = new Card(0, 0);
        Card B = new Card(0, 1);
        Card C = new Card(1, 0);
        Card D = new Card(1, 1);
        Card E = new Card(1, 1);
        Rational r = new Rational(1);
        Rational r2 = new Rational(5, 5);
        System.out.println(r.hashCode() == r2.hashCode());
        System.out.println(D.equals(E));
        System.out.println(new Quad(A, B, C, D).equals(new Quad(C, B, D, A)));
        System.out.println(new Quad(A, B, C, D).equals(new Quad(C, B, E, A)));
    }

    public Card[] getCards() {
        return cards;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Quad)) {
            return false;
        }
        Quad ABCD = (Quad) obj;
        HashSet<Card> set1 = new HashSet<>(Arrays.asList(cards));
        HashSet<Card> set2 = new HashSet<>(Arrays.asList(ABCD.cards));
        boolean x = set1.equals(set2);
        return set1.equals(set2);
    }

    public String toString() {
        if (asString != null)
            return asString;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cards.length; i++) {
            builder.append(cards[i]);
            if (i != cards.length - 1)
                builder.append(";");
        }
        return asString = builder.toString();
    }

    public int hashCode() {
        int product = 1;
        for (Card c : cards) {
            int p = PrimeLookup.PRIMES[Integer.valueOf(c.toString(), 2)];
            product *= p;
        }
        return product;
    }
}