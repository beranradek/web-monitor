package cz.rbe.monitor.probe;

/**
 * Result of check.
 * @author Radek Beran
 */
public class ProbeResult {
    private final String probeName;
    private ResultSeverity severity = ResultSeverity.ERROR; // default
    private String message;

    public ProbeResult(String probeName) {
        this.probeName = probeName;
    }

    public String getProbeName() {
        return probeName;
    }

    public ResultSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(ResultSeverity severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
