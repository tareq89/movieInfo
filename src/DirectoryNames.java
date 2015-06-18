import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by tareq.aziz on 6/18/15.
 */
public class DirectoryNames {

    private Set<Path> movieFolders = new HashSet<Path>();
    public Set<Path> makeDirectoryList(String dirPath) {

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
}
