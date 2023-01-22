import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Matrix {
    private double[] cells;
    private int rows, columns;
    Random rand = new Random();
    // Matrix creation methods

    private Matrix() {
    }

    public Map<String, Object> save() {
        Map<String, Object> data = new HashMap<>();
        data.put("cells", cells);
        data.put("rows", rows);
        data.put("columns", columns);
        return data;
    }

    public static Matrix load(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        Matrix output = new Matrix();
        output.cells = (double[]) data.get("cells");
        output.rows = (int) data.get("rows");
        output.columns = (int) data.get("columns");
        return output;
    }

    private Matrix(int rows, int columns) throws Exception {
        if (rows < 1 || columns < 1) {
            throw new Exception();
        }
        this.rows = rows;
        this.columns = columns;
        this.cells = new double[this.rows * this.columns];
    }

    private Matrix(double[] cells) throws Exception {
        if (cells == null) {
            throw new Exception();
        }
        this.cells = cells.clone();
        this.rows = 1;
        this.columns = cells.length;
    }

    private Matrix(double[] cells, int rows, int columns) throws Exception {
        if (cells == null || rows < 1 || columns < 1 || rows * columns != cells.length) {
            throw new Exception();
        }
        this.cells = cells.clone();
        this.rows = rows;
        this.columns = columns;
    }

    private Matrix(int rows, int columns, double[] cells) throws Exception {
        if (cells == null || rows < 1 || columns < 1 || rows * columns != cells.length) {
            throw new Exception();
        }
        this.cells = cells.clone();
        this.rows = rows;
        this.columns = columns;
    }

    public static Matrix create(int rows, int columns) throws Exception {
        return new Matrix(rows, columns);
    }

    public static Matrix create(double[] cells) throws Exception {
        return new Matrix(cells);
    }

    public static Matrix create(double[] cells, int rows, int columns) throws Exception {
        return new Matrix(cells, rows, columns);
    }

    public static Matrix create(int rows, int columns, double[] cells) throws Exception {
        return new Matrix(rows, columns, cells);
    }

    public static Matrix identity(int n) {
        Matrix output = new Matrix();
        output.rows = n;
        output.columns = n;
        output.cells = new double[n * n];
        for (int i = 0; i < n; i++) {
            output.cells[i * (n + 1)] = 1;
        }
        return output;
    }

    public Matrix clone() {
        Matrix output = new Matrix();
        output.cells = this.cells.clone();
        output.rows = this.rows;
        output.columns = this.columns;
        return output;
    }

    // Method that prints Matrix to screen e.g.
    ///////// [1.0,2.0,3.0]
    ///////// [4.0,5.0,6.0]
    // matrix([7.0,8.0,9.0])
    public void print() throws Exception {
        for (int r = 0; r < this.rows - 1; r++) {
            System.out.print("       [");
            for (int c = 0; c < this.columns - 1; c++) {
                System.out.print("" + this.get(r, c) + ",");
            }
            System.out.print("" + this.get(r, this.columns - 1) + "]\r\n");
        }
        System.out.print("Matrix([");
        for (int c = 0; c < this.columns - 1; c++) {
            System.out.print("" + this.get(this.rows - 1, c) + ",");
        }
        System.out.print("" + this.get(this.rows - 1, this.columns - 1) + "])\r\n\r\n");
    }

    public void printDimensions() throws Exception {
        System.out.println("\r\nThis is an " + this.rows + "x" + this.columns + " Matrix.");
    }

    // Getter methods, used to access data about the Matrix without modifying it.
    public double get(int row, int col) throws Exception {
        if (row < 0 || row >= this.rows || col < 0 || col >= this.columns) {
            throw new Exception();
        }
        return this.cells[this.columns * row + col];
    }

    public Matrix get(int row, String wildcard) throws Exception {
        if (row < 0 || row >= this.rows) {
            throw new Exception();
        }
        if (wildcard.equals("*")) {
            double[] output = new double[this.columns];
            for (int i = 0; i < output.length; i++) {
                output[i] = this.get(row, i);
            }
            return new Matrix(output, 1, this.columns);
        }
        return null;
    }

    public Matrix get(String wildcard, int col) throws Exception {
        if (col < 0 || col >= this.columns) {
            throw new Exception();
        }
        if (wildcard.equals("*")) {
            double[] output = new double[this.rows];
            for (int i = 0; i < output.length; i++) {
                output[i] = this.get(i, col);
            }
            return new Matrix(output, this.rows, 1);
        }
        return null;
    }

    public Matrix get(String wildcard, String otherWildcard) {
        if (wildcard.equals("*") && otherWildcard.equals("*")) {
            return this.clone();
        }
        return null;
    }

    public double[] getCells() {
        return this.cells.clone();
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public double getSum() {
        double output = 0;
        for (double cell : this.cells) {
            output += cell;
        }
        return output;
    }

    public double getProduct() {
        double output = 1;
        for (double cell : this.cells) {
            output *= cell;
        }
        return output;
    }

    public int maxIndex() {
        int maxIndex = 0;
        for (int i = 0; i < this.cells.length; i++) {
            if (this.cells[i] > this.cells[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    // Boolean accessor methods
    public boolean isRowVector() {
        return this.rows == 1;
    }

    public boolean isColumnVector() {
        return this.columns == 1;
    }

    public boolean isVector() {
        return isColumnVector() || isRowVector();
    }

    // These are the matrix operation methods
    // must have 2 or 4 implementations.
    //
    public Matrix set(String wildcard, String otherWildcard, Matrix b) throws Exception {
        if (this.rows != b.rows || this.columns != b.columns) {
            throw new Exception();
        }
        if (wildcard.equals("*") && otherWildcard.equals("*")) {
            for (int i = 0; i < this.cells.length; i++) {
                this.cells[i] = b.cells[i];
            }
            return this;
        }
        return null;
    }

    public Matrix set(Matrix a, String wildcard, String otherWildcard, Matrix b) throws Exception {
        return this.set(wildcard, otherWildcard, b);
    }

    public static Matrix setClone(Matrix a, String wildcard, String otherWildcard, Matrix b) throws Exception {
        return Matrix.create(a.rows, a.columns).set(a, wildcard, otherWildcard, b);
    }

    public Matrix setClone(String wildcard, String otherWildcard, Matrix b) throws Exception {
        return Matrix.setClone(this, wildcard, otherWildcard, b);
    }

    //

    public Matrix set(int row, int col, double b) throws Exception {
        if (row < 0 || row >= this.rows || col < 0 || col >= this.columns) {
            throw new Exception();
        }
        this.cells[this.columns * row + col] = b;
        return this;
    }

    public Matrix set(Matrix a, int row, int col, double b) throws Exception {
        return this.set("*", "*", a).set(row, col, b);
    }

    public static Matrix setClone(Matrix a, int row, int col, double b) throws Exception {
        return Matrix.create(a.rows, a.columns).set(a, row, col, b);
    }

    public Matrix setClone(int row, int col, double b) throws Exception {
        return Matrix.setClone(this, row, col, b);
    }

    //

    public Matrix set(int row, String wildcard, Matrix b) throws Exception {
        if (row < 0 || row >= this.rows) {
            throw new Exception();
        }
        if (wildcard.equals("*")) {
            for (int i = 0; i < this.columns; i++) {
                this.set(row, i, b.get(1, i));
            }
            return this;
        }
        return null;
    }

    public Matrix set(Matrix a, int row, String wildcard, Matrix b) throws Exception {
        return this.set("*", "*", a).set(row, wildcard, b);
    }

    public static Matrix setClone(Matrix a, int row, String wildcard, Matrix b) throws Exception {
        return Matrix.create(a.rows, a.columns).set(a, row, wildcard, b);
    }

    public Matrix setClone(int row, String wildcard, Matrix b) throws Exception {
        return Matrix.setClone(this, row, wildcard, b);
    }

    //

    public Matrix set(String wildcard, int col, Matrix b) throws Exception {
        if (col < 0 || col >= this.columns) {
            throw new Exception();
        }
        if (wildcard.equals("*")) {
            for (int i = 0; i < this.rows; i++) {
                this.set(i, col, b.get(i, 0));
            }
            return this;
        }
        return null;
    }

    public Matrix set(Matrix a, String wildcard, int col, Matrix b) throws Exception {
        return this.set("*", "*", a).set(wildcard, col, b);
    }

    public static Matrix setClone(Matrix a, String wildcard, int col, Matrix b) throws Exception {
        return Matrix.create(a.rows, a.columns).set(a, wildcard, col, b);
    }

    public Matrix setClone(String wildcard, int col, Matrix b) throws Exception {
        return Matrix.setClone(this, wildcard, col, b);
    }

    //

    public Matrix zero() {
        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i] = 0;
        }
        return this;
    }

    public Matrix zero(Matrix a) {
        return this.zero();
    }

    public static Matrix zeroClone(Matrix a) throws Exception {
        return Matrix.create(a.rows, a.columns).zero(a);
    }

    public Matrix zeroClone() throws Exception {
        return Matrix.zeroClone(this);
    }

    //

    public Matrix randomize(int seed) {
        Random rand = new Random(seed);
        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i] = rand.nextGaussian();
        }
        return this;
    }

    public Matrix randomize(Matrix a, int seed) {
        return this.randomize(seed);
    }

    public static Matrix randomizeClone(Matrix a, int seed) throws Exception {
        return Matrix.create(a.rows, a.columns).randomize(a, seed);
    }

    public Matrix randomizeClone(int seed) throws Exception {
        return Matrix.randomizeClone(this, seed);
    }

    //
    public Matrix transpose() throws Exception {
        Matrix output = this.clone();
        output.rows = this.columns;
        output.columns = this.rows;
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                output.set(c, r, this.get(r, c));
            }
        }
        int temp = this.rows;
        this.rows = this.columns;
        this.columns = temp;
        this.set("*", "*", output);
        return this;
    }

    public Matrix transpose(Matrix a) throws Exception {
        return this.transpose().set("*", "*", a).transpose();

    }

    public static Matrix transposeClone(Matrix a) throws Exception {
        return Matrix.create(a.columns, a.rows).transpose(a);
    }

    public Matrix transposeClone() throws Exception {
        return Matrix.transposeClone(this);
    }

    //

    public Matrix add(Matrix b) throws Exception {
        if (this.rows != b.rows || this.columns != b.columns) {
            throw new Exception();
        }
        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i] += b.cells[i];
        }
        return this;
    }

    public Matrix add(Matrix a, Matrix b) throws Exception {
        return this.set("*", "*", a).add(b);
    }

    public static Matrix addClone(Matrix a, Matrix b) throws Exception {
        return Matrix.create(a.rows, a.columns).add(a, b);
    }

    public Matrix addClone(Matrix b) throws Exception {
        return Matrix.addClone(this, b);
    }

    //

    public Matrix elementProduct(Matrix b) throws Exception {
        if (this.rows != b.rows || this.columns != b.columns) {
            throw new Exception();
        }
        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i] *= b.cells[i];
        }
        return this;
    }

    public Matrix elementProduct(Matrix a, Matrix b) throws Exception {
        return this.set("*", "*", a).elementProduct(b);
    }

    public static Matrix elementProductClone(Matrix a, Matrix b) throws Exception {
        return Matrix.create(a.rows, a.columns).elementProduct(a, b);
    }

    public Matrix elementProductClone(Matrix b) throws Exception {
        return Matrix.elementProductClone(this, b);
    }

    //

    public Matrix product(double b) {
        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i] *= b;
        }
        return this;
    }

    public Matrix product(Matrix a, double b) throws Exception {
        return this.set("*", "*", a).product(b);
    }

    public static Matrix productClone(Matrix a, double b) throws Exception {
        return Matrix.create(a.rows, a.columns).product(a, b);
    }

    public Matrix productClone(double b) throws Exception {
        return Matrix.productClone(this, b);
    }

    //

    public Matrix add(double b) {
        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i] += b;
        }
        return this;
    }

    public Matrix add(Matrix a, double b) throws Exception {
        return this.set("*", "*", a).add(b);
    }

    public static Matrix addClone(Matrix a, double b) throws Exception {
        return Matrix.create(a.rows, a.columns).add(a, b);
    }

    public Matrix addClone(double b) throws Exception {
        return Matrix.addClone(this, b);
    }

    //

    public Matrix shape(int rows, int cols) throws Exception {
        if (rows < 1 || cols < 1 || (rows * cols) != cells.length) {
            System.out.println(cells.length);
            throw new Exception();
        }
        this.rows = rows;
        this.columns = cols;
        return this;
    }

    public Matrix shape(Matrix a, int rows, int cols) throws Exception {
        return this.set("*", "*", a).shape(rows, cols);
    }

    public static Matrix shapeClone(Matrix a, int rows, int cols) throws Exception {
        return Matrix.create(a.rows, a.columns).shape(a, rows, cols);
    }

    public Matrix shapeClone(int rows, int cols) throws Exception {
        return Matrix.shapeClone(this, rows, cols);
    }

    //

    public Matrix product(Matrix b) throws Exception {
        // Don't use this unless you know THIS and b are both
        // square matrices of the same size
        if (this.rows != this.columns || b.rows != b.columns || this.rows != b.columns) {
            throw new Exception();
        }
        return this.set("*", "*", this.productClone(b));
    }

    public Matrix product(Matrix a, Matrix b) throws Exception {
        if (a.columns != b.rows) {
            throw new Exception();
        }
        this.zero();
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < b.columns; j++) {
                for (int k = 0; k < a.columns; k++) {
                    this.set(i, j, this.get(i, j) + a.get(i, k) * b.get(k, j));
                }
            }
        }
        return this;
    }

    public static Matrix productClone(Matrix a, Matrix b) throws Exception {
        return Matrix.create(a.rows, b.columns).product(a, b);
    }

    public Matrix productClone(Matrix b) throws Exception {
        return Matrix.productClone(this, b);
    }

    //

    public Matrix outerProduct(Matrix b) throws Exception {
        // Don't use this unless you know THIS and b are both
        // square matrices of the same size
        if (this.rows != this.columns || b.rows != b.columns || this.rows != b.columns) {
            throw new Exception();
        }
        return this.set("*", "*", this.outerProductClone(b));
    }

    public Matrix outerProduct(Matrix a, Matrix b) throws Exception {
        if (a.rows != b.rows) {
            throw new Exception();
        }
        if (a == b) {
            return this.outerProduct(a, a.clone());
        }
        a.transpose();
        this.product(a, b);
        a.transpose();
        return this;
    }

    public static Matrix outerProductClone(Matrix a, Matrix b) throws Exception {
        return Matrix.create(a.columns, b.columns).outerProduct(a, b);
    }

    public Matrix outerProductClone(Matrix b) throws Exception {
        return Matrix.outerProductClone(this, b);
    }

    //

    public Matrix innerProduct(Matrix b) throws Exception {
        // Don't use this unless you know THIS and b are both
        // square matrices of the same size
        if (this.rows != this.columns || b.rows != b.columns || this.rows != b.columns) {
            throw new Exception();
        }
        return this.set("*", "*", this.innerProductClone(b));
    }

    public Matrix innerProduct(Matrix a, Matrix b) throws Exception {
        if (a.columns != b.columns) {
            a.print();
            b.print();
            throw new Exception();
        }
        if (a == b) {
            return this.innerProduct(a, a.clone());
        }
        b.transpose();
        this.product(a, b);
        b.transpose();
        return this;
    }

    public static Matrix innerProductClone(Matrix a, Matrix b) throws Exception {
        return Matrix.create(a.rows, b.rows).innerProduct(a, b);
    }

    public Matrix innerProductClone(Matrix b) throws Exception {
        return Matrix.innerProductClone(this, b);
    }

    //

    public Matrix heParameterInitialize(int previousLayer) {
        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i] = rand.nextGaussian() * Math.sqrt(2. / ((double) previousLayer));
        }
        return this;
    }

    public Matrix heParameterInitialize(Matrix a, int previousLayer) throws Exception {
        return this.set("*", "*", a).heParameterInitialize(previousLayer);
    }

    public static Matrix heParameterInitializeClone(Matrix a, int previousLayer) throws Exception {
        return Matrix.create(a.rows, a.columns).heParameterInitialize(a, previousLayer);
    }

    public Matrix heParameterInitializeClone(int previousLayer) throws Exception {
        return Matrix.heParameterInitializeClone(this, previousLayer);
    }

    //

    public Matrix activate(String activation, int nthDerivative) throws Exception {
        switch (activation) {
            case "sigmoid":
                return this.sigmoid(activation, nthDerivative);
            case "swish":
                return this.swish(activation, nthDerivative);
            case "leakyRelu":
                return this.leakyRelu(activation, nthDerivative);
            case "relu":
                return this.relu(activation, nthDerivative);
            case "tanh":
                return this.tanh(activation, nthDerivative);
            case "softmax":
                return this.softmax(activation, nthDerivative);
            default:
                throw new Exception();
        }

    }

    public Matrix activate(Matrix a, String activation, int nthDerivative) throws Exception {
        return this.set("*", "*", a).activate(activation, nthDerivative);
    }

    public static Matrix activateClone(Matrix a, String activation, int nthDerivative) throws Exception {
        return Matrix.create(a.rows, a.columns).activate(a, activation, nthDerivative);
    }

    public Matrix activateClone(String activation, int nthDerivative) throws Exception {
        return Matrix.activateClone(this, activation, nthDerivative);
    }

    //

    public Matrix sigmoid(String activation, int nthDerivative) throws Exception {
        switch (nthDerivative) {
            case 0:
                for (int i = 0; i < this.cells.length; i++) {
                    this.cells[i] = 1. / (1. + Math.exp(-this.cells[i]));
                }
                return this;
            case 1:
                for (int i = 0; i < this.cells.length; i++) {
                    double temp = 1. / (1. + Math.exp(-this.cells[i]));
                    this.cells[i] = temp * (1. - temp);
                }
                return this;
            default:
                throw new Exception();
        }
    }

    public Matrix sigmoid(Matrix a, String activation, int nthDerivative) throws Exception {
        return this.set("*", "*", a).sigmoid(activation, nthDerivative);
    }

    public static Matrix sigmoidClone(Matrix a, String activation, int nthDerivative) throws Exception {
        return Matrix.create(a.rows, a.columns).sigmoid(a, activation, nthDerivative);
    }

    public Matrix sigmoidClone(String activation, int nthDerivative) throws Exception {
        return Matrix.sigmoidClone(this, activation, nthDerivative);
    }

    //

    public Matrix swish(String activation, int nthDerivative) throws Exception {
        switch (nthDerivative) {
            case 0:
                for (int i = 0; i < this.cells.length; i++) {
                    double temp = 1. / (1. + Math.exp(-this.cells[i]));
                    this.cells[i] = this.cells[i] * temp;
                }
                return this;
            case 1:
                for (int i = 0; i < this.cells.length; i++) {
                    double temp = 1. / (1. + Math.exp(-this.cells[i]));
                    this.cells[i] = (this.cells[i] * temp) + ((temp) * (1. - (this.cells[i] * temp)));
                }
                return this;
            default:
                throw new Exception();
        }
    }

    public Matrix swish(Matrix a, String activation, int nthDerivative) throws Exception {
        return this.set("*", "*", a).swish(activation, nthDerivative);
    }

    public static Matrix swishClone(Matrix a, String activation, int nthDerivative) throws Exception {
        return Matrix.create(a.rows, a.columns).swish(a, activation, nthDerivative);
    }

    public Matrix swishClone(String activation, int nthDerivative) throws Exception {
        return Matrix.swishClone(this, activation, nthDerivative);
    }

    //

    public Matrix leakyRelu(String activation, int nthDerivative) throws Exception {
        switch (nthDerivative) {
            case 0:
                for (int i = 0; i < this.cells.length; i++) {
                    this.cells[i] = this.cells[i] >= 0 ? this.cells[i] : 0.01 * this.cells[i];
                }
                return this;
            case 1:
                for (int i = 0; i < this.cells.length; i++) {
                    this.cells[i] = this.cells[i] >= 0 ? 1 : 0.01;
                }
                return this;
            default:
                throw new Exception();
        }
    }

    public Matrix leakyRelu(Matrix a, String activation, int nthDerivative) throws Exception {
        return this.set("*", "*", a).leakyRelu(activation, nthDerivative);
    }

    public static Matrix leakyReluClone(Matrix a, String activation, int nthDerivative) throws Exception {
        return Matrix.create(a.rows, a.columns).leakyRelu(a, activation, nthDerivative);
    }

    public Matrix leakyReluClone(String activation, int nthDerivative) throws Exception {
        return Matrix.leakyReluClone(this, activation, nthDerivative);
    }

    //

    public Matrix relu(String activation, int nthDerivative) throws Exception {
        switch (nthDerivative) {
            case 0:
                for (int i = 0; i < this.cells.length; i++) {
                    this.cells[i] = Math.max(0., this.cells[i]);
                }
                return this;
            case 1:
                for (int i = 0; i < this.cells.length; i++) {
                    this.cells[i] = this.cells[i] >= 0 ? 1 : 0.;
                }
                return this;
            default:
                throw new Exception();
        }
    }

    public Matrix relu(Matrix a, String activation, int nthDerivative) throws Exception {
        return this.set("*", "*", a).relu(activation, nthDerivative);
    }

    public static Matrix reluClone(Matrix a, String activation, int nthDerivative) throws Exception {
        return Matrix.create(a.rows, a.columns).relu(a, activation, nthDerivative);
    }

    public Matrix reluClone(String activation, int nthDerivative) throws Exception {
        return Matrix.reluClone(this, activation, nthDerivative);
    }

    //

    public Matrix tanh(String activation, int nthDerivative) throws Exception {
        switch (nthDerivative) {
            case 0:
                for (int i = 0; i < this.cells.length; i++) {
                    this.cells[i] = 1. - (2. / (1. + Math.exp(2. * this.cells[i])));
                }
                return this;
            case 1:
                for (int i = 0; i < this.cells.length; i++) {
                    double temp = 1. / (1. + Math.exp(2. * this.cells[i]));
                    this.cells[i] = 4. * temp * (1. - temp);
                }
                return this;
            default:
                throw new Exception();
        }
    }

    public Matrix tanh(Matrix a, String activation, int nthDerivative) throws Exception {
        return this.set("*", "*", a).tanh(activation, nthDerivative);
    }

    public static Matrix tanhClone(Matrix a, String activation, int nthDerivative) throws Exception {
        return Matrix.create(a.rows, a.columns).tanh(a, activation, nthDerivative);
    }

    public Matrix tanhClone(String activation, int nthDerivative) throws Exception {
        return Matrix.tanhClone(this, activation, nthDerivative);
    }

    //

    public Matrix softmax(String activation, int nthDerivative) throws Exception {
        double count = 0;
        for (int i = 0; i < this.cells.length; i++) {
            count += Math.exp(this.cells[i]);
        }
        switch (nthDerivative) {
            case 0:
                for (int i = 0; i < this.cells.length; i++) {
                    this.cells[i] = Math.exp(this.cells[i]) / count;
                }
                return this;
            case 1:
                for (int i = 0; i < this.cells.length; i++) {
                    this.cells[i] = 0;
                }
                return this;
            default:
                throw new Exception();
        }
    }

    public Matrix softmax(Matrix a, String activation, int nthDerivative) throws Exception {
        return this.set("*", "*", a).softmax(activation, nthDerivative);
    }

    public static Matrix softmaxClone(Matrix a, String activation, int nthDerivative) throws Exception {
        return Matrix.create(a.rows, a.columns).softmax(a, activation, nthDerivative);
    }

    public Matrix softmaxClone(String activation, int nthDerivative) throws Exception {
        return Matrix.softmaxClone(this, activation, nthDerivative);
    }

    //

    public Matrix cost(Matrix b, String cost, int nthDerivative) throws Exception {
        switch (cost) {
            case "logLoss":
                return this.logLoss(b, nthDerivative);
            case "meanSquaredError":
                return this.meanSquaredError(b, nthDerivative);
            default:
                throw new Exception();
        }

    }

    public Matrix cost(Matrix a, Matrix b, String cost, int nthDerivative) throws Exception {
        return this.set("*", "*", a).cost(b, cost, nthDerivative);
    }

    public static Matrix costClone(Matrix a, Matrix b, String cost, int nthDerivative) throws Exception {
        return Matrix.create(a.rows, a.columns).cost(a, b, cost, nthDerivative);
    }

    public Matrix costClone(Matrix b, String cost, int nthDerivative) throws Exception {
        return Matrix.costClone(this, b, cost, nthDerivative);
    }

    //

    public Matrix logLoss(Matrix b, int nthDerivative) throws Exception {
        switch (nthDerivative) {
            case 0:
                for (int i = 0; i < this.cells.length; i++) {
                    double shiftOutput = Math.max(Math.min(this.cells[i], 1 - Math.pow(10, -15)), Math.pow(10, -15));
                    double shiftExpected = Math.max(Math.min(b.cells[i], 1 - Math.pow(10, -15)), Math.pow(10, -15));
                    this.cells[i] = -shiftExpected * Math.log(shiftOutput)
                            - (1. - shiftExpected) * Math.log(1. - shiftOutput);
                }
                return this;
            case 1:
                for (int i = 0; i < this.cells.length; i++) {
                    double shiftOutput = Math.max(Math.min(this.cells[i], 1 - Math.pow(10, -15)), Math.pow(10, -15));
                    double shiftExpected = Math.max(Math.min(b.cells[i], 1 - Math.pow(10, -15)), Math.pow(10, -15));
                    this.cells[i] = -shiftExpected / shiftOutput + (1 - shiftExpected) / (1 - shiftOutput);
                }
                return this;
            default:
                throw new Exception();
        }

    }

    public Matrix logLoss(Matrix a, Matrix b, int nthDerivative) throws Exception {
        return this.set("*", "*", a).logLoss(b, nthDerivative);
    }

    public static Matrix logLossClone(Matrix a, Matrix b, int nthDerivative) throws Exception {
        return Matrix.create(a.rows, a.columns).logLoss(a, b, nthDerivative);
    }

    public Matrix logLossClone(Matrix b, int nthDerivative) throws Exception {
        return Matrix.logLossClone(this, b, nthDerivative);
    }

    //

    public Matrix meanSquaredError(Matrix b, int nthDerivative) throws Exception {
        switch (nthDerivative) {
            case 0:
                for (int i = 0; i < this.cells.length; i++) {
                    this.cells[i] = Math.pow(this.cells[i] - b.cells[i], 2);
                }
                return this;
            case 1:
                for (int i = 0; i < this.cells.length; i++) {
                    this.cells[i] = 2.0 * (this.cells[i] - b.cells[i]);
                }
                return this;
            default:
                throw new Exception();
        }

    }

    public Matrix meanSquaredError(Matrix a, Matrix b, int nthDerivative) throws Exception {
        return this.set("*", "*", a).meanSquaredError(b, nthDerivative);
    }

    public static Matrix meanSquaredErrorClone(Matrix a, Matrix b, int nthDerivative) throws Exception {
        return Matrix.create(a.rows, a.columns).meanSquaredError(a, b, nthDerivative);
    }

    public Matrix meanSquaredErrorClone(Matrix b, int nthDerivative) throws Exception {
        return Matrix.meanSquaredErrorClone(this, b, nthDerivative);
    }
}