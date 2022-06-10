package analysis;

import me.darrionat.quads.Quad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class QuadProductsOutputAnalysis {
    private static HashMap<Integer, HashMap<Quad, List<DimQuadData>>> map = new HashMap<>();

    public static void main(String[] args) throws IOException {
        read("data/quadProductsOutput_unique.csv");
        for (int dim = 3; dim < 12; dim++) {
            saveData(dim, "data/productsAnalysis_dim_" + dim + "_unique.csv");
        }
    }

    private static void read(String dataFile) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(dataFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] cases = sc.nextLine().split(",");
        // dim,cards1Quad,cards2Quad,transpose
        int dim = Integer.parseInt(cases[0]);
        boolean cards1Quad = Boolean.parseBoolean(cases[1]);
        boolean cards2Quad = Boolean.parseBoolean(cases[2]);
        boolean transpose = Boolean.parseBoolean(cases[3]);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            // While case has no quads
            while (line.equals("")) {
                // skip and read cases line
                cases = sc.nextLine().split(",");
                // dim,cards1Quad,cards2Quad,transpose
                dim = Integer.parseInt(cases[0]);
                cards1Quad = Boolean.parseBoolean(cases[1]);
                cards2Quad = Boolean.parseBoolean(cases[2]);
                transpose = Boolean.parseBoolean(cases[3]);
                line = sc.nextLine();
            }
            String[] quadData = line.split(",");
            // quad,rowOccurrences,colOccurrences,doubleOccurrences
            Quad quad = Quad.parseQuad(quadData[0]);
            int rowCount = Integer.parseInt(quadData[1]);
            int colCount = Integer.parseInt(quadData[2]);
            int doubleCount = Integer.parseInt(quadData[3]);
            addData(dim, quad, cards1Quad, cards2Quad, transpose,
                    rowCount, colCount, doubleCount);
        }
    }

    // HashMap<Integer, HashMap<Quad, List<DimQuadData>>>
    private static void addData(int dim, Quad quad, boolean cards1Quad, boolean cards2Quad, boolean transpose, int rowCount, int colCount, int doubleCount) {
        if (!map.containsKey(dim))
            map.put(dim, new HashMap<>());
        HashMap<Quad, List<DimQuadData>> dimQuadDataHashMap = map.get(dim);
        if (!dimQuadDataHashMap.containsKey(quad)) {
            dimQuadDataHashMap.put(quad, new ArrayList<>());
        }
        List<DimQuadData> quadData = dimQuadDataHashMap.get(quad);
        quadData.add(new DimQuadData(cards1Quad, cards2Quad, transpose, rowCount, colCount, doubleCount));
    }

    private static void saveData(int dim, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        HashMap<Quad, List<DimQuadData>> dimQuadDataHashMap = map.get(dim);
        for (Map.Entry<Quad, List<DimQuadData>> quadEntry : dimQuadDataHashMap.entrySet()) {
            Quad quad = quadEntry.getKey();
            writer.write(quad + "\n");
            for (DimQuadData dimQuadData : quadEntry.getValue()) {
                writer.write("\t" + dimQuadData + "\n");
            }
        }
        writer.close();
    }

    private static class DimQuadData {
        boolean cards1Quad;
        boolean cards2Quad;
        boolean transpose;
        int rowCount;
        int colCount;
        int doubleCount;

        DimQuadData(boolean cards1Quad, boolean cards2Quad, boolean transpose, int rowCount, int colCount, int doubleCount) {
            this.cards1Quad = cards1Quad;
            this.cards2Quad = cards2Quad;
            this.transpose = transpose;
            this.rowCount = rowCount;
            this.colCount = colCount;
            this.doubleCount = doubleCount;
        }

        public String toString() {
            return cards1Quad + "," + cards2Quad + "," + transpose + "," + rowCount + "," + colCount + "," + doubleCount;
        }
    }
}