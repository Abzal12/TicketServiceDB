package org.abzal1.model.ticket;

public enum DataPath {

    PATH_TO_DATA("src/main/resources/ticketData.txt");

    private final String path;

    private DataPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
