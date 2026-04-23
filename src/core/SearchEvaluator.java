package core;

import models.ResultNode;
import models.SearchIntent;
import nlp.TextCleaner;

import java.io.*;
import java.util.*;

public class SearchEvaluator {
    
    public List<SearchIntent> parseQueries(String path) throws Exception {
        List<SearchIntent> intents = new ArrayList<>();
        Scanner scan = new Scanner(new File(path));
        
        int currId = -1;
        String head = "", sum = "", full = "";
        
        while (scan.hasNextLine()) {
            String l = scan.nextLine().trim();
            if (l.startsWith("<num>")) currId = Integer.parseInt(l.replaceAll("[^0-9]", ""));
            else if (l.startsWith("<title>")) head = l.substring(l.indexOf(">") + 1).trim();
            else if (l.startsWith("<desc>")) sum = scan.nextLine().trim();
            else if (l.startsWith("<narr>")) {
                StringBuilder b = new StringBuilder();
                while (scan.hasNextLine()) {
                    String next = scan.nextLine().trim();
                    if (next.startsWith("</top>")) break;
                    b.append(next).append(" ");
                }
                full = b.toString().trim();
                intents.add(new SearchIntent(currId, head, sum, full));
            }
        }
        scan.close();
        return intents;
    }

    public void runRankingPhase(List<SearchIntent> searchList, CorpusAnalyzer analyzer, TextCleaner cleaner, int configMode, String outPath) throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(outPath));
        
        searchList.forEach(intent -> {
            String combinedQ = intent.buildSearchString(configMode);
            List<String> qTokens = cleaner.extractKeywords(combinedQ);
            
            Map<String, Integer> qCounts = new HashMap<>();
            qTokens.forEach(t -> qCounts.put(t, qCounts.getOrDefault(t, 0) + 1));
            
            Map<String, Double> qVector = new HashMap<>();
            double qLengthSq = 0.0;
            
            for (String t : qCounts.keySet()) {
                int df = analyzer.getGlobalCounts().getOrDefault(t, 0);
                if (df == 0) continue;
                
                double weight = qCounts.get(t) * Math.log((double) analyzer.getCorpusSize() / df);
                qVector.put(t, weight);
                qLengthSq += (weight * weight);
            }
            double qMag = Math.sqrt(qLengthSq);
            
            List<ResultNode> rankings = new ArrayList<>();
            analyzer.getMatrix().forEach((dName, dVector) -> {
                double dot = 0.0;
                for (String qt : qVector.keySet()) {
                    if (dVector.containsKey(qt)) dot += (qVector.get(qt) * dVector.get(qt));
                }
                if (dot > 0) {
                    double finalSim = dot / (qMag * analyzer.getMagnitudes().get(dName));
                    rankings.add(new ResultNode(dName, finalSim));
                }
            });
            
            // Stream sorting and limiting
            List<ResultNode> topResults = rankings.stream()
                    .sorted(Comparator.comparingDouble(ResultNode::getMatchScore).reversed())
                    .limit(100)
                    .collect(java.util.stream.Collectors.toList());
            
            int placement = 1;
            for (ResultNode node : topResults) {
                writer.printf("%d\t%s\t%d\t%.6f\n", intent.getQueryId(), node.getDocName(), placement++, node.getMatchScore());
            }
        });
        writer.close();
    }
}