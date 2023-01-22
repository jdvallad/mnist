import java.util.HashMap;
import java.util.Map;

public class DataPair {
    public Matrix input;
    public Matrix label;

    public DataPair(Matrix input, Matrix label) {
        this.input = input;
        this.label = label;
    }

    private DataPair() {

    }

    public DataPair clone() {
        DataPair output = new DataPair();
        output.input = this.input.clone();
        output.label = this.label.clone();
        return output;
    }

    public Map<String, Object> save() {
        Map<String, Object> output = new HashMap<>();
        output.put("input", input.save());
        output.put("label", label.save());
        return output;
    }

    public static DataPair load(Map<String, Object> input) {
        DataPair output = new DataPair();
        output.input = Matrix.load((Map<String, Object>) input.get("input"));
        output.label = Matrix.load((Map<String, Object>) input.get("label"));
        return output;
    }
}