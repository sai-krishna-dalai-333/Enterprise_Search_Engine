package core;

import nlp.TextCleaner;
import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.*;

public class CorpusAnalyzer {
    
    private final Map<String, Map<String, Integer>> rawFrequencies = new HashMap<>();
    private final Map<String, Integer> globalTermCounts = new HashMap<>();
    
    private final Map<String, Map<String, Double>> tfIdfMatrix = new HashMap<>();
    private final Map<String, Double> documentMagnitudes = new HashMap<>();
    
    private int totalCorpusSize = 0;

    public void ingestData(String directoryPath, TextCleaner cleaner) throws Exception {
        File[] files = new File(directoryPath).listFiles();
        if (files == null) return;

        Pattern idPattern = Pattern.compile("<DOCNO>\\s*(.*?)\\s*</DOCNO>");
        Pattern bodyPattern = Pattern.compile("<TEXT>\\s*(.*?)\\s*</TEXT>", Pattern.DOTALL);

        for (File currentFile : files) {
            if (currentFile.getName().contains("stopword")) continue;
            
            String fileData = new String(Files.readAllBytes(currentFile.toPath()));
            Matcher idMatch = idPattern.matcher(fileData);
            Matcher bodyMatch = bodyPattern.matcher(fileData);

            while (idMatch.find() && bodyMatch.find()) {
                String dId = idMatch.group(1).trim();
                String dBody = bodyMatch.group(1).trim();
                
                List<String> validTokens = cleaner.extractKeywords(dBody);
                Map<String, Integer> currentDocMap = new HashMap<>();
                
                validTokens.forEach(token -> currentDocMap.put(token, currentDocMap.getOrDefault(token, 0) + 1));
                
                rawFrequencies.put(dId, currentDocMap);
                currentDocMap.keySet().forEach(k -> globalTermCounts.put(k, globalTermCounts.getOrDefault(k, 0) + 1));
            }
        }
        totalCorpusSize = rawFrequencies.size();
    }

    public void generateVectorSpace() {
        Iterator<Map.Entry<String, Map<String, Integer>>> iterator = rawFrequencies.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<String, Map<String, Integer>> entry = iterator.next();
            String docKey = entry.getKey();
            Map<String, Integer> localFreqs = entry.getValue();
            
            Map<String, Double> weights = new HashMap<>();
            double lengthSquared = 0.0;

            for (String term : localFreqs.keySet()) {
                int tf = localFreqs.get(term);
                int df = globalTermCounts.getOrDefault(term, 1);
                double idf = Math.log((double) totalCorpusSize / df);
                double tfidfWeight = tf * idf;
                
                weights.put(term, tfidfWeight);
                lengthSquared += (tfidfWeight * tfidfWeight);
            }
            tfIdfMatrix.put(docKey, weights);
            documentMagnitudes.put(docKey, Math.sqrt(lengthSquared));
        }
    }

    public Map<String, Map<String, Double>> getMatrix() { return tfIdfMatrix; }
    public Map<String, Double> getMagnitudes() { return documentMagnitudes; }
    public Map<String, Integer> getGlobalCounts() { return globalTermCounts; }
    public int getCorpusSize() { return totalCorpusSize; }
}