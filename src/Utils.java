import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import vector_creation.FISCRM;

public class Utils {
	
	
	public static void writeObjectToFile(String filename,Object obj) {
		FileOutputStream fout = null;
		ObjectOutputStream oout = null;
		try {
			fout = new FileOutputStream(filename);
			oout = new ObjectOutputStream(fout);


			oout.writeObject(obj);

			oout.close();
			fout.close();

		} catch (FileNotFoundException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static Object readObject(String filename) {
        FileInputStream fin = null;
        ObjectInputStream oin = null;
        Object obj = null;
        try {
            fin = new FileInputStream(filename);
            oin = new ObjectInputStream(fin);


             obj = oin.readObject();

            oin.close();
            fin.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
        }
		return obj;
    }

}
