package me.darrionat.quads;

import me.darrionat.quads.interfaces.QuadState;

/**
 * Represents a qap of size four. Equivalently, an AntiQuad represents four unique cards that do not form a quad
 */
public class AntiQuad implements QuadState {
    private final Card[] cards;

    public AntiQuad(Card A, Card B, Card C, Card D) {
        if (A.equals(B) || A.equals(C) || A.equals(D) || B.equals(C) || B.equals(D) || C.equals(D))
            throw new IllegalArgumentException("Cards must be unique");
        if (Quad.formsQuad(A, B, C, D))
            throw new IllegalArgumentException("Cards must not form a quad");
        cards = new Card[]{A, B, C, D};
    }

    public Card[] getCards() {
        return cards;
    }

    public static AntiQuad randomAntiQuad(int dim) {
        if (dim < 3) {
            throw new IllegalArgumentException("Dimension must be greater than 2");
        }
        Card[] unique = Quad.getThreeUniqueCards(dim);
        Card A = unique[0], B = unique[1], C = unique[2];
        Card D = Card.getRandomCard(dim);
        while (A.equals(D) || B.equals(D) || C.equals(D)) {
            D = Card.getRandomCard(dim);
        }
        if (Quad.formsQuad(A, B, C, D))
            return randomAntiQuad(dim);
        return new AntiQuad(A, B, C, D);
    }
}