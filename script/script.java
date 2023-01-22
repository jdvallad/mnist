import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;


// Run this script to create serialized files used for training neural network with.
public class script {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting serialization...");
        String filePath = "../";
        String[] types = new String[] { "testing/", "training/", "validation/" };
        int[] labels = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        File[] files;
        List<Map<String, Object>> data = new ArrayList<>();
        for (String t : types) {
            data.clear();
            for (int l : labels) {
                files = new File(filePath + t + l + "/").listFiles();
                for (int i = 0; i < files.length; i++) {
                    BufferedImage image = ImageViewer.pathToImage(filePath + t + l + "/" + files[i].getName());
                    Matrix imageData = ImageViewer.greyMatrix(image);
                    double[] imageLabelList = new double[10];
                    for (int r = 0; r < 10; r++) {
                        imageLabelList[r] = 0.;
                    }
                    imageLabelList[l] = 1.;
                    Matrix imageLabel = Matrix.create(imageLabelList);
                    DataPair temp = new DataPair(imageData, imageLabel);
                    data.add(temp.save());
                }
            }
            Collections.shuffle(data);
            FileOutputStream fileOut = new FileOutputStream(filePath + t + "dataPairs.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            System.out.println("Created file at " + filePath + t + "dataPairs.ser");
            out.writeObject(data);
            out.close();
            fileOut.close();
        }
        return;
    }
}