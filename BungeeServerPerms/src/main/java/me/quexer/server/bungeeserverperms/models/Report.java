package me.quexer.server.bungeeserverperms.models;

public class Report {

    private String targetUUID;
    private String reporterUUID;
    private String reason;

    public Report(String targetUUID, String reporterUUID, String reason) {
        this.targetUUID = targetUUID;
        this.reporterUUID = reporterUUID;
        this.reason = reason;
    }
}
