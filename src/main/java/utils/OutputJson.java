package utils;

import java.util.Collection;

public class OutputJson {
    private long initTime;
    private Collection<SearchResult> result;

    public OutputJson(long initTime, Collection<SearchResult> result) {
        this.initTime = initTime;
        this.result = result;
    }

    public long getInitTime() {
        return initTime;
    }

    public Collection<SearchResult> getResult() {
        return result;
    }
}

