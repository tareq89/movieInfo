import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tareq.aziz on 6/18/15.
 */
public class DirectoryNames {

    public Set<Path> makeDirectoryList(String dirPath) {

        Set<Path> movieFolders = new HashSet<Path>();
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
