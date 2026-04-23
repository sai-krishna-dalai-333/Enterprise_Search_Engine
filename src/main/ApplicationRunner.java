package main;

import core.CorpusAnalyzer;
import core.SearchEvaluator;
import models.SearchIntent;
import nlp.TextCleaner;

import java.util.List;

public class ApplicationRunner {
    public static void main(String[] args) {
        try {
            System.out.println("Starting Enterprise Search Engine Initialization...");
            
            TextCleaner textCleaner = new TextCleaner();
            textCleaner.loadIgnoreList("inputs/stopwordlist.txt");
            
            CorpusAnalyzer dataAnalyzer = new CorpusAnalyzer();
            System.out.println("Parsing corpus and mapping vectors...");
            dataAnalyzer.ingestData("corpus", textCleaner);
            dataAnalyzer.generateVectorSpace();
            
            SearchEvaluator evaluator = new SearchEvaluator();
            List<SearchIntent> activeQueries = evaluator.parseQueries("inputs/topics.txt");
            
            System.out.println("Executing search ranking...");
            // Mode 3 corresponds to Narrative + Title analysis
            evaluator.runRankingPhase(activeQueries, dataAnalyzer, textCleaner, 3, "vsm_output.txt");
            
            System.out.println("Process Complete. Review vsm_output.txt for VSM rankings.");
            
        } catch (Exception e) {
            System.err.println("Critical Failure: " + e.getMessage());
            e.printStackTrace();
        }
    }
}