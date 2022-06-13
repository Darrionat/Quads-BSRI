import me.darrionat.matrixlib.algebra.sets.Quantity;
import me.darrionat.matrixlib.algebra.sets.Rational;
import me.darrionat.matrixlib.matrices.Matrix;
import me.darrionat.quads.Card;
import me.darrionat.quads.Quad;
import me.darrionat.quads.interfaces.QuadState;

public class QuadTransformation {

    public static final int CARD_AMT = 4;

    public static void main(String[] args) {
        int dim = 6;
        Matrix A = new Matrix(CARD_AMT, dim);
        QuadState quad1 = Quad.randomQuad(dim);

        Card[] quad1Cards = quad1.getCards();
        for (int i = 0; i < CARD_AMT; i++) {
            A.setRow(i, quad1Cards[i].getBits());
        }
        System.out.println(A);
        for (int i = 0; i < Math.pow(2, dim); i++) {
            StringBuilder binaryString = new StringBuilder(Integer.toBinaryString(i));
            int length = binaryString.length();
            for (int j = 0; j < dim - length; j++) {
                binaryString.insert(0, "0");
            }
            Card transform = Card.parseCard(binaryString.toString());
            System.out.println(transform);
            Rational[] bits = transform.getBits();
            Quantity[][] quantities = new Quantity[dim][1];
            for (int j = 0; j < dim; j++) {
                quantities[j][0] = bits[j];
            }
            Matrix vec = new Matrix(dim, 1, quantities);
            System.out.println(A.multiply(vec));
        }
    }
}