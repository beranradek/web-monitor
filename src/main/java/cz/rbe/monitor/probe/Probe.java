package cz.rbe.monitor.probe;

/**
 * Monitoring probe that checks some condition.
 * @author Radek Beran
 */
public interface Probe {

    String MSG_OK = "OK";
    int DEFAULT_CONN_TIMEOUT = 2000;
    int DEFAULT_READ_TIMEOUT = 2000;

    /**
     * Name of probe.
     * @return
     */
    String getName();

    /**
     * Executes the probe and returns result of the check.
     * @return
     */
    ProbeResult run();
}
