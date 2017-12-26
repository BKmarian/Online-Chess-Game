import java.io.FileNotFoundException;

public class Table {
    private String [][] matrix;
    public static final int rows = 8;
    public static final int columns = 8;


    public AbstractLogger logger;

    public Table() throws FileNotFoundException {
        logger = Logger.getChainOfLoggers();
        this.matrix = new String[][] { { "wtura", "wcal", "wnebun", "wrege", "wregina", "wnebun", "wcal", "wtura" },
                { "wpion", "wpion", "wpion", "wpion", "wpion", "wpion", "wpion", "wpion" },
                { "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
                { "0", "0", "0", "0", "0", "0", "0", "0" }, { "0", "0", "0", "0", "0", "0", "0", "0" },
                { "bpion", "bpion", "bpion", "bpion", "bpion", "bpion", "bpion", "bpion" },
                { "btura", "bcal", "bnebun", "brege", "bregina", "bnebun", "bcal", "btura" } };

    }

    public Table(String [][] matrixTemp) throws FileNotFoundException {
        logger = Logger.getChainOfLoggers();
        this.matrix = copyMatrix(matrixTemp);
    }

    public int search(String s) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j].equals(s))
                    return i * 10 + j;
            }
        return 0;
    }

    public int[][] posibleMoves(int rand, int piecePoz[][]) {
        int i, j, ii, jj;
        int table[][] = new int[rows][columns];
        int b[][] = new int[rows][columns];
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                if (piecePoz[i][j] == rand) {
                    b = Management.nextmove(matrix[i][j], i, j, rand, piecePoz);

                    for (ii = 0; ii < rows; ii++)
                        for (jj = 0; jj < columns; jj++)
                            table[ii][jj] = table[ii][jj] | b[ii][jj];
                }
            }
        }
        return table;
    }

    public void showTable() {
        int r, c;
        for (r = 0; r < rows; r++) {
            for (c = 0; c < columns; c++) {
                logger.print(matrix[r][c] + " ");
            }
            logger.logMessage(AbstractLogger.INFO, "");
        }
    }

    public int getKingPos(int rand) {
        int i, j, poz = 0;
        char culoare = (rand == 1) ? 'w' : 'b';
        for (i = 0; i < rows; i++)
            for (j = 0; j < columns; j++)
                if (matrix[i][j].substring(1).equals("rege") && matrix[i][j].charAt(0) == culoare)
                    poz = i * 10 + j;
        return poz;
    }

    public String[][] copyMatrix(String matrix[][]) {
        String matrixTemp[][] = new String[rows][columns];
        for (int i = 0; i < rows; i++)
            System.arraycopy(matrix[i], 0, matrixTemp[i], 0, columns);
        // showTable(matrixTemp);
        return matrixTemp;
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(String[][] matrix) {
        this.matrix = matrix;
    }

}