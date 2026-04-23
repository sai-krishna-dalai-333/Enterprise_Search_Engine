Enterprise Search Engine - Phase 3
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
├── vsm_output.txt                      (Generated automatically on run)
└── Readme.txt

HOW TO COMPILE

Open your terminal or command prompt.

Navigate directly to the root "Enterprise_Search_Engine" directory:
cd path/to/Enterprise_Search_Engine

Create the binary output directory (if it doesn't already exist):
mkdir -p bin

Compile the modular Java packages. We use the -nowarn flag to cleanly suppress outdated API warnings from the legacy Porter.java stemmer:
javac -d bin -nowarn src/models/.java src/nlp/.java src/core/.java src/main/.java

HOW TO RUN

Ensure your terminal is still located in the root directory.

Run the compiled application by targeting the main class inside the bin folder:
java -cp bin main.ApplicationRunner

The system will ingest the corpus, process the search intents, and automatically evaluate the rankings.

OUTPUT FORMAT
The search results will be saved in the root directory as "vsm_output.txt" in the exact batch-mode format required:

TOPIC    DOCUMENT    UNIQUE#    COSINE_VALUE

Where:

TOPIC is the query identifier.

DOCUMENT is the retrieved XML docno.

UNIQUE# is the rank of the document (1 to 100).

COSINE_VALUE is the normalized similarity score.