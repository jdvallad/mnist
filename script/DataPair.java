import java.io.Serializable;
public class DataPair implements Serializable{
    public final double[] data;
    public final double[] label;

    public DataPair(double[] data, double[] label) {
        this.data = data.clone();
        this.label = label.clone();
    }
}
