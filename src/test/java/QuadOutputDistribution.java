import me.darrionat.matrixlib.matrices.Matrix;
import me.darrionat.quads.AntiQuad;
import me.darrionat.quads.Card;
import me.darrionat.quads.Quad;
import me.darrionat.quads.interfaces.QuadState;

import java.util.HashMap;
import java.util.Map;

public class QuadOutputDistribution {

    public static final int CARD_AMT = 4;

    public static void main(String[] args) {
        int N = 100000;
        System.out.println("dim\t" +
                "tests\t" +
                "cards1Quad\t" +
                "cards2Quad\t" +
                "transpose\t");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int dim = 3; dim < 12; dim++) {
                        quadMultiplication(N, dim, i == 0, j == 0, k == 0, true);
                    }
                }
            }
        }
    }

    public static void quadMultiplication(int tests, int dim, boolean quad1Quad, boolean quad2Quad, boolean transpose, boolean quadRequiresUnique) {
        if (dim < 3) {
            throw new IllegalArgumentException("requires dimension greater than 2");
        }
        HashMap<Quad, Integer> rowQuadOccurrences = new HashMap<>();
        HashMap<Quad, Integer> colQuadOccurrences = new HashMap<>();
        HashMap<Quad, Integer> doubleQuadOccurrences = new HashMap<>();
        for (int test = 0; test < tests; test++) {
            Matrix A = new Matrix(CARD_AMT, dim);
            Matrix B = new Matrix(dim, CARD_AMT);

            QuadState quad1 = quad1Quad ? Quad.randomQuad(dim) : AntiQuad.randomAntiQuad(dim);
            QuadState quad2 = quad2Quad ? Quad.randomQuad(dim) : AntiQuad.randomAntiQuad(dim);

            while (quad1.equals(quad2))
                quad2 = Quad.randomQuad(dim);

            Card[] quad1Cards = quad1.getCards();
            Card[] quad2Cards = quad2.getCards();
            for (int i = 0; i < CARD_AMT; i++) {
                A.setRow(i, quad1Cards[i].getBits());
                B.setColumn(i, quad2Cards[i].getBits());
            }

            if (transpose)
                B = A.transpose();
            Matrix AB = A.multiply(B);
            Quad[] quads = rowColumnQuads(AB, quadRequiresUnique);
            // row quad occurrence
            if (quads[0] != null) {
                if (rowQuadOccurrences.get(quads[0]) == null) {
                    rowQuadOccurrences.put(quads[0], 1);
                } else {
                    rowQuadOccurrences.put(quads[0], rowQuadOccurrences.get(quads[0]) + 1);
                }
            }
            // col quad occurrence
            if (quads[1] != null) {
                if (colQuadOccurrences.get(quads[1]) == null) {
                    colQuadOccurrences.put(quads[1], 1);
                } else {
                    colQuadOccurrences.put(quads[1], colQuadOccurrences.get(quads[1]) + 1);
                }
            }
            // DOUBLE QUAD occurrence
            if (quads[0] != null && quads[1] != null && quads[0].equals(quads[1])) {
                if (doubleQuadOccurrences.get(quads[0]) == null) {
                    doubleQuadOccurrences.put(quads[0], 1);
                } else {
                    doubleQuadOccurrences.put(quads[0], doubleQuadOccurrences.get(quads[0]) + 1);
                }
            }
        }
        System.out.println(dim + "\t" +
                tests + "\t" +
                quad1Quad + "\t" +
                quad2Quad + "\t" +
                transpose + "\t");
        System.out.println("quad\t" +
                "rowOccurrences\t" +
                "colOccurrences\t" +
                "doubleOccurrences");
        for (Map.Entry<Quad, Integer> entry : rowQuadOccurrences.entrySet()) {
            Quad quad = entry.getKey();
            int rowOccur = rowQuadOccurrences.get(quad);
            int colOccur = colQuadOccurrences.containsKey(quad) ? colQuadOccurrences.get(quad) : 0;
            int doubleOccur = doubleQuadOccurrences.containsKey(quad) ? doubleQuadOccurrences.get(quad) : 0;
            System.out.println(quad + "\t" +
                    rowOccur + "\t" +
                    colOccur + "\t" +
                    doubleOccur);
            colQuadOccurrences.remove(quad);
        }
        for (Map.Entry<Quad, Integer> entry : colQuadOccurrences.entrySet()) {
            Quad quad = entry.getKey();
            int rowOccur = rowQuadOccurrences.containsKey(quad) ? rowQuadOccurrences.get(quad) : 0;
            int colOccur = colQuadOccurrences.get(quad);
            int doubleOccur = doubleQuadOccurrences.containsKey(quad) ? doubleQuadOccurrences.get(quad) : 0;
            System.out.println(quad + "\t" +
                    rowOccur + "\t" +
                    colOccur + "\t" +
                    doubleOccur);
        }
        System.out.println();
    }

    /**
     * Checks to see if the rows and columns of a 4 by 4 matrix form a quad.
     *
     * @param matrix A four by four matrix over Z_2.
     * @return An array of length 2. The first and second slots represent if a row or column quad can be formed,
     *         respectively.
     */
    private static Quad[] rowColumnQuads(Matrix matrix, boolean requiresUnique) {
        Card[] rowCards = new Card[CARD_AMT];
        for (int row = 0; row < matrix.getRowAmount(); row++) {
            rowCards[row] = new Card(matrix.getRow(row));
        }
        Card[] colCards = new Card[CARD_AMT];
        for (int col = 0; col < matrix.getRowAmount(); col++) {
            colCards[col] = new Card(matrix.getColumn(col));
        }
        Quad rowQuad = null;
        if (Quad.formsQuad(rowCards[0], rowCards[1], rowCards[2], rowCards[3], requiresUnique))
            rowQuad = new Quad(rowCards[0], rowCards[1], rowCards[2], rowCards[3]);

        Quad colQuad = null;
        if (Quad.formsQuad(colCards[0], colCards[1], colCards[2], colCards[3], requiresUnique))
            colQuad = new Quad(colCards[0], colCards[1], colCards[2], colCards[3]);
        return new Quad[]{rowQuad, colQuad};
    }
}