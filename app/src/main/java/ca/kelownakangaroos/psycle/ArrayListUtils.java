package ca.kelownakangaroos.psycle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ArrayListUtils {

    public static <T> void deserializeDoubleArrayList(ArrayList<ArrayList<T>> list, String fileLocation) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileLocation);
        ObjectInputStream ois = new ObjectInputStream(fis);
        list = (ArrayList<ArrayList<T>>) ois.readObject();
        ois.close();
        fis.close();
    }

    public static <T> void serializeDoubleArrayList(ArrayList<ArrayList<T>> list, String fileLocation) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileLocation);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(list);
        oos.close();
        fos.close();
    }

    public static <T> void printDoubleArrayList(ArrayList<ArrayList<T>> list) {
        for (ArrayList<T> temp_List : list) {
            for (T location : temp_List) {
                System.out.println(location);
            }
        }
    }
}
