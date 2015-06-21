import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tareq.aziz on 6/18/15.
 */
public class PatternUtility {

    private static String FILENAME;



    public static String cleanFileName(String fileName){

        FILENAME = fileName;
        try {
            makeFileNameClean();

        } catch (FileNotFoundException e) {
            System.out.println("File not found : " + fileName);
        }

        return FILENAME;
    }






    private static String makeFileNameClean() throws FileNotFoundException {

        FILENAME = FILENAME.toLowerCase();
        Scanner scanner = new Scanner(new File("keyWordsToIgnore.csv"));
        scanner.useDelimiter(";");

        while (scanner.hasNext())
        {
            String wordToBeRemoved = scanner.next().toLowerCase();
            boolean removeWord = FILENAME.contains(wordToBeRemoved);

            if (removeWord){
                int index = FILENAME.indexOf(wordToBeRemoved);

                try {
                    FILENAME = FILENAME.substring(0, index-1);
                }catch (IndexOutOfBoundsException e){
                    continue;
                }
            }
        }
        scanner.close();
        FILENAME = FILENAME.replaceAll("\\.|\\s", "+");
        FILENAME = yearFirstBracketAdder();
        FILENAME = yearThirdBracketRemover();

        return FILENAME;
    }



    private static String yearFirstBracketAdder(){

        String bracketString = findPatternInString(FILENAME, "(\\(\\d\\d\\d\\d\\))", "year");
        if (bracketString.equals("")){

            String yearString = findPatternInString(FILENAME, "(\\d\\d\\d\\d)", "year");

            if (!yearString.equals("")){
                String replacingYearString = "(" + yearString + ")";
                FILENAME = FILENAME.replaceAll(yearString, replacingYearString);
            }
        }

        return FILENAME;
    }

    private static String yearThirdBracketRemover(){

        if (FILENAME.contains("[") || FILENAME.contains("]")){
            FILENAME = FILENAME.replaceAll("]|\\[", "");
        }
        return FILENAME;
    }


    public static String aTagCleaner(String textWithAtag) {

        String textWithoutAtag = textWithAtag.replaceAll("<a .*?</a>", "");
        return textWithoutAtag;
    }






    public static String findPatternInString(String toParse, String pattern, String option){

        String matchedString = "";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(toParse);

        try {
            while (m.find()){
                if (option.equals("genre")) {
                    matchedString += m.group(1) + " ";
                }
                else{
                    matchedString = m.group(1);
                    break;
                }
            }
        } catch (IndexOutOfBoundsException e){

        }
        return matchedString;
    }
}
