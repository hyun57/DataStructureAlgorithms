import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

//Sparse Matrix ADT
public class SparseMatrix {

	// nested class for each non-zero entry in Sparse Matrix
    class Entry {
        public int row, col, value;

        @Override
        public String toString() {
            return "Entry{" +
                "row=" + row +
                ", col=" + col +
                ", value=" + value +
                '}';
        }

        public Entry(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }
    }

    private int rowCount;
    private int colCount;
    private int entryCount;
    private Entry[] data;
    private int nextSlot;

    private SparseMatrix(int rowCount, int colCount, int entryCount) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.entryCount = entryCount;

        data = new Entry [entryCount];
        nextSlot = 0;
    }

    /*
	 * Construct a sparse matrix by reading data from a specified file
	 */
    public static SparseMatrix create(String filename) throws IOException {
        Path path = Paths.get(filename);
        if (!Files.exists(path))
            throw new IllegalArgumentException("No such file");

        SparseMatrix matrix = null;
        int row, col, count, value;
        try (Scanner scanner = new Scanner(path)) {
            row = scanner.nextInt();
            col = scanner.nextInt();
            count = scanner.nextInt();
            matrix = new SparseMatrix(row, col, count);

            for (int i = 0; i < count; i++) {
                row = scanner.nextInt();
                col = scanner.nextInt();
                value = scanner.nextInt();
                matrix.put(row, col, value);
            }
        };

        return matrix;
    }

    /*
	 * You can define additional private fields and/or methods here, if necessary.
	 */

    private void put(int row, int col, int value) {
    	data[nextSlot++] = new Entry(row, col, value);
	}

    /*
	 * Add this matrix with another matrix other.
	 * Assume that the dimensions of two matrices are always compatible.
	 */
   public SparseMatrix add(SparseMatrix other) {
     if (!(rowCount == other.rowCount && colCount == other.colCount)) return null;
     int sumEntryCount = other.entryCount + entryCount;
 		SparseMatrix sum = new SparseMatrix(other.rowCount, other.colCount, sumEntryCount);
 		for(int a = 0; a < other.entryCount; a++){ //put other.data's Value
       sum.put(other.data[a].row, other.data[a].col, other.data[a].value);}
     int i = 0;
     while(i < entryCount){
       int check = 0;
       for(int j = 0; j < other.entryCount; j++){
 				if(sum.data[j].row == data[i].row && sum.data[j].col == data[i].col){
 					sum.data[j].value = data[i].value + other.data[j].value; check++;
         }}
       if(check==0){ // case : each row, col is not same
           sum.put(data[i].row, data[i].col, data[i].value);
         }
         i++;
     }
     Zero_rm(sum);
     sum.entryCount = sum.nextSlot; //think not necessary setEntryCount

     Isort(sum);
     return sum;
   }

   /*
    * [Isort] with swapEntry, setEntry
    * [Zero_rm] if val==0 : remove
    */
     public static void Isort(SparseMatrix mt) {
       int j = 1;
       while(j < mt.nextSlot){
         for (int i = 1; i < mt.nextSlot; i++){
           if(mt.data[i-1].row > mt.data[i].row){
             swapEntry(mt.data, i-1, i);
           }else if (mt.data[i-1].row == mt.data[i].row && mt.data[i-1].col > mt.data[i].col) {
             swapEntry(mt.data, i-1, i);
           }} j++;}
       }
     public static void Zero_rm(SparseMatrix mt){
       for(int i = 0; i < mt.nextSlot; i++){
         if(mt.data[i].value ==0){
           mt.nextSlot--;
           setEntry(mt.data[i], mt.data[mt.nextSlot].row, mt.data[mt.nextSlot].col, mt.data[mt.nextSlot].value);
           mt.data[mt.nextSlot] = null;
         }
       }
     }
     public static void swapEntry(Entry[] swap, int i, int j) {
         int a,b,c;
         a = swap[i].row;
         b = swap[i].col;
         c = swap[i].value;
         setEntry(swap[i], swap[j].row, swap[j].col, swap[j].value);
         setEntry(swap[j], a,b,c);
       }
     private static void setEntry(Entry set, int row, int col, int value){
       set.row = row;
       set.col = col;
       set.value = value;
     }
 	/*
 	 * Transpose matrix
 	 */
     public SparseMatrix transpose() {
       SparseMatrix trans = new SparseMatrix(colCount, rowCount, entryCount);
       for(int a = 0; a < trans.entryCount; a++){
         trans.put(data[a].col, data[a].row,data[a].value);}
       Isort(trans);
       return trans;
     }

    public void print() {
    	System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(rowCount + ", " + colCount + ", " + entryCount + "\n");
        for (int i = 0; i < entryCount; i++) {
            builder.append(data[i].row + ", " + data[i].col + ", " + data[i].value + "\n");
        }
        return builder.toString();
    }

		/*
		 * DO NOT MODIFY CODE BELOW!!!!!
		 */
    public static void main(String... args) throws IOException {
        if (args[0].equals("p")) { // Print current matrix
            System.out.println("Printing a matrix: " + args[1]);
            SparseMatrix m = SparseMatrix.create(args[1]);
            m.print();
        } else if (args[0].equals("a")) { // Add two matrices
            System.out.println("Adding two matrices: " + args[1] + " and " + args[2] + "\n");
            SparseMatrix A = SparseMatrix.create(args[1]);
            System.out.println("Matrix A = \n" + A);
            SparseMatrix B = SparseMatrix.create(args[2]);
            System.out.println("Matrix B = \n" + B);

            System.out.println("Matrix A + B = \n" + A.add(B));
        } else if (args[0].equals("t")) {   // Transpose a matrix
            System.out.println("Transposing a matrix: " + args[1]);
            SparseMatrix matrix = SparseMatrix.create(args[1]);
            System.out.println(matrix);
            SparseMatrix transposedMatrix = matrix.transpose();
            System.out.println("Transposed Matrix = \n" + transposedMatrix);
        } else {
            System.err.println("Unknown operation ...");
        }
    }
}
