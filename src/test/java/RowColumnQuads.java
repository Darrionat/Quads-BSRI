import me.darrionat.matrixlib.matrices.Matrix;
import me.darrionat.quads.AntiQuad;
import me.darrionat.quads.Card;
import me.darrionat.quads.Quad;
import me.darrionat.quads.interfaces.QuadState;

public class RowColumnQuads {

    public static final int CARD_AMT = 4;

    public static void main(String[] args) {
        for (int test = 0; test < 100; test++) {
            int dim = 6;
            Matrix A = new Matrix(CARD_AMT, dim);
            QuadState quad1 = Quad.randomQuad(dim);

            Card[] quad1Cards = quad1.getCards();
            for (int i = 0; i < CARD_AMT; i++) {
                A.setRow(i, quad1Cards[i].getBits(dim));
            }
            System.out.println(A);
            System.out.println(A.multiply(A.transpose()));

        }
    }

    public static void main2(String[] args) {
        int N = 100000;
        System.out.println("dim\t" +
                "tests\t" +
                "cards1Quad\t" +
                "cards2Quad\t" +
                "transpose\t" +
                "noQuads\t" +
                "doubleQuads\t" +
                "rowQuads\t" +
                "colQuads");
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
        int totalRowQuads = 0;
        int totalColQuads = 0;
        int totalDoubleQuads = 0; // when both a row and col quad are formed
        int totalNoQuads = 0;
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
                A.setRow(i, quad1Cards[i].getBits(dim));
                B.setColumn(i, quad2Cards[i].getBits(dim));
            }

            if (transpose)
                B = A.transpose();
            Matrix AB = A.multiply(B);
            boolean[] quads = rowColumnQuads(AB, quadRequiresUnique);
            totalRowQuads += quads[0] ? 1 : 0;
            totalColQuads += quads[1] ? 1 : 0;
            totalDoubleQuads += quads[0] && quads[1] ? 1 : 0;
            totalNoQuads += !quads[0] && !quads[1] ? 1 : 0;
        }
        System.out.println(dim + "\t" +
                tests + "\t" +
                quad1Quad + "\t" +
                quad2Quad + "\t" +
                transpose + "\t" +
                totalNoQuads + "\t" +
                totalDoubleQuads + "\t" +
                totalRowQuads + "\t" +
                totalColQuads);
    }

    /**
     * Checks to see if the rows and columns of a 4 by 4 matrix form a quad.
     *
     * @param matrix A four by four matrix over Z_2.
     * @return An array of length 2. The first and second slots represent if a row or column quad can be formed,
     *         respectively.
     */
    private static boolean[] rowColumnQuads(Matrix matrix, boolean requiresUnique) {
        Card[] rowCards = new Card[CARD_AMT];
        for (int row = 0; row < matrix.getRowAmount(); row++) {
            rowCards[row] = Card.fromBitArray(matrix.getRow(row));
        }
        Card[] colCards = new Card[CARD_AMT];
        for (int col = 0; col < matrix.getRowAmount(); col++) {
            colCards[col] = Card.fromBitArray(matrix.getColumn(col));
        }
        boolean rowQuad = Quad.formsQuad(rowCards[0], rowCards[1], rowCards[2], rowCards[3], requiresUnique);
        boolean colQuad = Quad.formsQuad(colCards[0], colCards[1], colCards[2], colCards[3], requiresUnique);
        return new boolean[]{rowQuad, colQuad};
    }
}