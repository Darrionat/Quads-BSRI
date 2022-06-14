import me.darrionat.quads.Cap;
import me.darrionat.quads.Card;
import utils.ProgressBar;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class CapGenerator {
    public static void main2(String[] args) {
        int caps = 0;
        for (int i = 0; i < 100000; i++) {
            if (gotRandomCap(6, 8))
                caps++;
        }
        System.out.println(caps);
    }

    public static void main(String[] args) throws IOException {
        int tests = 1000000;
        int minDim = 7;
        int maxDim = 30;
        int dimDiff = maxDim - minDim + 1;
        int minCapSize = 4;
        int maxCapSize = 30;
        int capDiff = maxCapSize - minCapSize + 1;
        System.out.println("Search Space");
        System.out.println(tests + " tests");
        System.out.println("d=" + minDim + "," + maxDim);
        System.out.println("k=" + minCapSize + "," + maxCapSize);
        System.out.println(tests * dimDiff * capDiff + " tested sets in total");

        // long totalIterations = tests * (maxDim - minDim + 1) * (maxCapSize - minCapSize + 1);
        FileWriter writer;
        try {
            writer = new FileWriter("data/cap_generator/capGenResults_" + tests + "_tests.csv");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ProgressBar progressBar = new ProgressBar(dimDiff * capDiff);

        for (int dim = minDim; dim <= maxDim; dim++) {
            int deckSize = (int) Math.pow(2, dim);
            for (int capSize = minCapSize; capSize <= maxCapSize && capSize <= deckSize; capSize++) {
                int caps = 0;
                for (int test = 1; test <= tests; test++) {
                    if (gotRandomCap(dim, capSize))
                        caps++;
                }
                // double percent = caps / (double) tests * 100;
                writer.write(dim + "," + capSize + "," + caps + "\n");
                int i = dim - minDim;
                int j = capSize - minCapSize + 1;
                progressBar.printProgress(i * capDiff + j);
            }

        }
        System.out.println("\ntests=" + tests);
        writer.close();
    }

    public static boolean gotRandomCap(int dim, int capSize) {
        return Cap.formsCap(getUniqueCards(dim, capSize));
    }

    public static Card[] getUniqueCards(int dim, int cardAmt) {
        if (Math.pow(2, dim) < cardAmt)
            throw new IllegalArgumentException("Card Amount > Possible Cards");
        Card[] cards = new Card[cardAmt];
        HashSet<Card> previouslyGenerated = new HashSet<>();
        for (int i = 0; i < cardAmt; i++) {
            Card c = Card.getRandomCard(dim);
            // Naive way to generate randomly
            while (previouslyGenerated.contains(c)) {
                c = Card.getRandomCard(dim);
            }
            previouslyGenerated.add(c);
            cards[i] = c;
        }
        return cards;
    }
}
