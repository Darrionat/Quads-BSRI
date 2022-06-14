package me.darrionat.quads;

import me.darrionat.quads.interfaces.QuadState;

import java.util.HashSet;

/**
 * Represents a set of cards that contain no quads.
 */
public class Cap implements QuadState {
    protected final Card[] cards;

    public Cap(Card[] cards) {
        if (!formsCap(cards))
            throw new IllegalArgumentException("Cards form a quad or contain a duplicate card");
        this.cards = cards;
    }

    /**
     * Checks to see if a cap is contained within an array of cards.
     * <p>
     * Essentially, this algorithm looks at all possible 2-term sums of the cards. If any two sums are equal, there must
     * exist a quad in the set of cards.
     * <p>
     * Note that this method also ensures that the cards are unique.
     *
     * @param cards The cards to check
     * @return {@code true} if the cards form a cap; {@code false} otherwise
     */
    public static boolean formsCap(Card[] cards) {
        HashSet<Card> allSums = new HashSet<>();
        for (int i = 0; i < cards.length; i++) {
            for (int j = i + 1; j < cards.length; j++) {
                allSums.add(cards[i].add(cards[j]));
            }
        }
        int s = cards.length;
        // (size choose 2)  = s*s-1/2
        return allSums.size() == (s * (s - 1)) / 2;
    }

    public Card[] getCards() {
        return cards;
    }
}