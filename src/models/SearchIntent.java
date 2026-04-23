package models;

public class SearchIntent {
    private final int queryId;
    private final String headline;
    private final String summary;
    private final String context;

    public SearchIntent(int queryId, String headline, String summary, String context) {
        this.queryId = queryId;
        this.headline = headline;
        this.summary = summary;
        this.context = context;
    }

    public int getQueryId() { return queryId; }

    public String buildSearchString(int depthLevel) {
        StringBuilder builder = new StringBuilder(headline);
        if (depthLevel >= 2) builder.append(" ").append(summary);
        if (depthLevel >= 3) builder.append(" ").append(context);
        return builder.toString();
    }
}