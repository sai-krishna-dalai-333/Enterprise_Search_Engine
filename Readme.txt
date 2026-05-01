Enterprise Search Engine - Phase 3
==================================

DESCRIPTION
This project implements a highly modular Information Retrieval (IR) system using the Vector Space Model (VSM) and TF-IDF weighting. Unlike flat-script implementations, this system is built using Domain-Driven Design (DDD) principles and modern Java functional programming (Streams) to parse documents, calculate mathematical vector relevance, and rank results based on user search intents.

PROJECT STRUCTURE
Enterprise_Search_Engine/
├── src/
│   ├── main/
│   │   └── ApplicationRunner.java      (Main execution loop)
│   ├── core/
│   │   ├── CorpusAnalyzer.java         (Builds the TF-IDF matrix)
│   │   └── SearchEvaluator.java        (Calculates Cosine Similarity)
│   ├── models/
│   │   ├── SearchIntent.java           (Query data structure)
│   │   └── ResultNode.java             (Ranked output structure)
│   └── nlp/
│       ├── TextCleaner.java            (Stopword and token filtering)
│       └── Porter.java                 (Stemming algorithm)
├── corpus/                             (Directory for FT911 text files)
├── inputs/
│   ├── topics.txt                      (The search queries file)
│   └── stopwordlist.txt                (Terms to ignore during indexing)
├── bin/                                (Generated automatically on compile)
├── output_title.txt                    (Generated automatically on run)
├── output_title_desc.txt               (Generated automatically on run)
├── output_title_narr.txt               (Generated automatically on run)
└── Readme.txt

HOW TO COMPILE
1. Open your terminal or command prompt.
2. Navigate directly to the root "Enterprise_Search_Engine" directory:
   cd path/to/Enterprise_Search_Engine

3. Create the binary output directory (if it doesn't already exist):
   mkdir -p bin

4. Compile the modular Java packages. We use the -nowarn flag to cleanly suppress outdated API warnings from the legacy Porter.java stemmer:
   javac -d bin -nowarn src/models/*.java src/nlp/*.java src/core/*.java src/main/*.java

HOW TO RUN
1. Ensure your terminal is still located in the root directory.
2. Run the compiled application by targeting the main class inside the bin folder:
   java -cp bin main.ApplicationRunner

3. The system will ingest the corpus, process the search intents, and automatically evaluate the rankings.

OUTPUT FORMAT
To comply with project specifications, the system processes queries across three different context settings and generates three distinct output files:

1. output_title.txt      (Setting 1: Main Query/Title only)
2. output_title_desc.txt (Setting 2: Title + Description)
3. output_title_narr.txt (Setting 3: Title + Narrative)

All three files adhere to the required tab-separated batch-mode format:
TOPIC    DOCUMENT    UNIQUE#    COSINE_VALUE

Where:
- TOPIC is the query identifier.
- DOCUMENT is the retrieved XML docno.
- UNIQUE# is the rank of the document (1 to 100).
- COSINE_VALUE is the normalized similarity score.