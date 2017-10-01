package cz.rbe.monitor;

import cz.rbe.monitor.probe.ProbeResult;

import java.util.List;

/**
 * Service that runs various monitoring probes and provides their results.
 * @author Radek Beran
 */
public interface MonitoringService {

    List<ProbeResult> getProbeResults();
}
