package nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class TextCleaner {
    private final Set<String> ignoreList = new HashSet<>();
    private final Porter stemTool = new Porter();

    public void loadIgnoreList(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNextLine()) {
            ignoreList.add(scanner.nextLine().trim().toLowerCase());
        }
        scanner.close();
    }

    public List<String> extractKeywords(String rawText) {
        return Arrays.stream(rawText.toLowerCase().split("[^a-z0-9]+"))
                .filter(word -> !word.isEmpty())
                .filter(word -> !ignoreList.contains(word))
                .map(stemTool::stripAffixes)
                .filter(stemmed -> !stemmed.isEmpty())
                .collect(Collectors.toList());
    }
}