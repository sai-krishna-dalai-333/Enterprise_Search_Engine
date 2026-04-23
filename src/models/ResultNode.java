package models;

public class ResultNode {
    private final String docName;
    private final double matchScore;

    public ResultNode(String docName, double matchScore) {
        this.docName = docName;
        this.matchScore = matchScore;
    }

    public String getDocName() { return docName; }
    public double getMatchScore() { return matchScore; }
}