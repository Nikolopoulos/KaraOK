package shark.tornado;

import java.io.File;
import java.util.StringTokenizer;
import javax.swing.filechooser.FileFilter;

public class xmlandtextfilter extends FileFilter {

    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        try {
            if (f.isDirectory()) {
                return true;
            }
            String s = "";
            StringTokenizer st = new StringTokenizer(f.getName(), ".");
            while (st.hasMoreTokens()) {
                s = st.nextToken();
                break;
            }
            s = st.nextToken();
            String extension = s;
            if (extension != null) {
                if (extension.equals("txt") || extension.equals("xml")) // || extension.equals(Utils.tif)
                //  || extension.equals(Utils.gif)
                // || extension.equals(Utils.jpeg)
                //  || extension.equals(Utils.jpg)
                //  || extension.equals(Utils.png)) 
                {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    //The description of this filter
    public String getDescription() {
        return "Text and XML files";
    }
}
