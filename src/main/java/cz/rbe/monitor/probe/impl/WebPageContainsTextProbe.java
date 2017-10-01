package cz.rbe.monitor.probe.impl;

import cz.rbe.monitor.common.UrlDownloader;
import cz.rbe.monitor.probe.Probe;
import cz.rbe.monitor.probe.ProbeResult;
import cz.rbe.monitor.probe.ResultSeverity;

/**
 * General purpose probe checking that web page contains given text.
 * @author Radek Beran
 */
public class WebPageContainsTextProbe implements Probe {

    private final String name;
    private final String url;
    private final String text;

    public WebPageContainsTextProbe(String name, String url, String text) {
        this.name = name;
        this.url = url;
        this.text = text;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ProbeResult run() {
        ProbeResult result = new ProbeResult(getName());
        String content = UrlDownloader.downloadString(url, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT);
        if (content.contains(text)) {
            result.setMessage(Probe.MSG_OK);
            result.setSeverity(ResultSeverity.INFO);
        } else {
            result.setMessage(text + " not found");
            result.setSeverity(ResultSeverity.ERROR);
        }
        return result;
    }
}
