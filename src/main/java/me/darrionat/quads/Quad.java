package me.darrionat.quads;

import me.darrionat.quads.interfaces.QuadState;
import me.darrionat.quads.statics.PrimeLookup;

import java.util.Arrays;
import java.util.HashSet;

public class Quad implements QuadState {
    private final Card[] cards;
    private String asString;

    public Quad(Card A, Card B, Card C, Card D) {
        if (!formsQuad(A, B, C, D, true))
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

    public static boolean formsQuad(Card A, Card B, Card C, Card D, boolean requiresUnique) {
        if (requiresUnique)
            if (A.equals(B) || A.equals(C) || A.equals(D) || B.equals(C) || B.equals(D) || C.equals(D))
                return false;
        return A.add(B).add(C).add(D).zero();
    }

    public static Quad parseQuad(String quadData) {
        String[] cardStrings = quadData.split(";");
        Card[] cards = new Card[cardStrings.length];
        for (int i = 0; i < cardStrings.length; i++) {
            cards[i] = Card.parseCard(cardStrings[i]);
        }
        return new Quad(cards[0], cards[1], cards[2], cards[3]);
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