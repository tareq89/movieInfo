import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by tareq.aziz on 6/18/15.
 */
public class FileServiceUtility {



    private static Set<Path> movieFolders = new HashSet<Path>();



    public static Set<Path> makeDirectoryList(String dirPath) {

        File dir = new File(dirPath);
        File[] firstLevelFiles = dir.listFiles();


        if (firstLevelFiles != null && firstLevelFiles.length > 0) {
            for (File movieFile : firstLevelFiles) {
                if (movieFile.isDirectory()){

                    String moviePathString = movieFile.getPath();
                    Path moviePath = Paths.get(moviePathString);
                    movieFolders.add(moviePath);

                    makeDirectoryList(movieFile.getAbsolutePath());  // recursion
                }
            }
        }
        return movieFolders;
    }






    public static boolean writeFile(String filepath, String toBeWritten) {

        File file = new File(filepath);
        boolean writeSuccess = false;
        try {

            writeSuccess = writeFile(file, toBeWritten);

        } catch (FileNotFoundException e) {
            System.out.println("Error : could not found file " + filepath);
        } catch (IOException e) {
            System.out.println("Error : could not create file " + filepath);
        }

        return writeSuccess;
    }






    private static boolean writeFile(File file, String toBeWritten) throws IOException {

        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(toBeWritten.getBytes());
        outputStream.close();

        return true;
    }
}
