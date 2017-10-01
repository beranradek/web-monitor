package cz.rbe.monitor;

import cz.rbe.monitor.common.AudioPlayer;
import cz.rbe.monitor.common.selenium.WebDriverSetup;
import cz.rbe.monitor.probe.Probe;
import cz.rbe.monitor.probe.ProbeResult;
import cz.rbe.monitor.probe.ResultSeverity;
import cz.rbe.monitor.probe.impl.NewTicketProbe;
import cz.rbe.monitor.probe.impl.ProductPlayingProbe;
import cz.rbe.monitor.probe.impl.WebPageContainsTextProbe;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service providing composition and running of concrete monitoring probes.
 * @author Radek Beran
 */
@Service
public class DefaultMonitoringService implements MonitoringService, AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(DefaultMonitoringService.class);
    private static final String DATE_FORMAT = "HH:mm:ss";

    private List<Probe> probes;
    private List<ProbeResult> probeResults;
    private WebDriver webDriver;

    @Value("${app.ok-string}")
    private String okString;

    @Value("${app.web-instance.pattern}")
    private String webInstancePattern;

    @Value("${app.ticketing.user.login}")
    private String ticketingUserLogin;

    @Value("${app.ticketing.user.password}")
    private String ticketingUserPassword;

    @Value("${app.ticketing.login-url}")
    private String ticketingLoginUrl;

    @Value("${app.ticketing.list-url}")
    private String ticketingListUrl;

    @Value("${app.ticketing.tickets.count.expected}")
    private int ticketingTicketsCountExpected;

    @Value("${app.web.user.login}")
    private String webUserLogin;

    @Value("${app.web.user.password}")
    private String webUserPassword;

    @Value("${app.web.device-ids.value}")
    private String webDeviceIdsValue;

    @Value("${app.web.homepage-url}")
    private String webHomepageUrl;

    @Value("${app.web.product-url}")
    private String webProductUrl;

    public DefaultMonitoringService() {
    }

    @PostConstruct
    public void init() {
        this.webDriver = new WebDriverSetup().loadChromeDriver("http://localhost:9515", 6);

        this.probes = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            this.probes.add(new WebPageContainsTextProbe("Pub" + i + " is alive", webInstancePattern.replace("{0}", "" + i), okString));
        }

        this.probes.add(new NewTicketProbe(webDriver, ticketingUserLogin, ticketingUserPassword, ticketingTicketsCountExpected,
                ticketingLoginUrl, ticketingListUrl));
        this.probes.add(new ProductPlayingProbe(webDriver, webUserLogin, webUserPassword,
                webDeviceIdsValue,
                webHomepageUrl,
                webProductUrl));
        this.probeResults = new ArrayList<>();
    }

    @Override
    @PreDestroy
    public void close() throws Exception {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @Override
    public List<ProbeResult> getProbeResults() {
        return probeResults;
    }

    @Scheduled(fixedRate = 240000) // 60 s
    public void executeProbes() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        log.info("Running probes at time {}", dateFormat.format(new Date()));

        List<ProbeResult> updatedProbeResults = new ArrayList<>();
        for (Probe probe : probes) {
            ProbeResult probeResult = null;
            try {
                probeResult = probe.run();
            } catch (Exception ex) {
                String msg = "Probe failed: " + ex.getMessage();
                log.error(msg, ex);
                probeResult = new ProbeResult(probe.getName());
                probeResult.setSeverity(ResultSeverity.ERROR);
                probeResult.setMessage(msg);
            }
            updatedProbeResults.add(probeResult);
        }

        if (updatedProbeResults.stream().anyMatch(res -> res.getSeverity() == ResultSeverity.ERROR)) {
            runAlarm();
        }

        this.probeResults = updatedProbeResults;
        log.info("Probes finished at time {}", dateFormat.format(new Date()));
    }

    private void runAlarm() {
        try {
            AudioPlayer.play(getClass().getResourceAsStream("/fire-truck.wav"));
        } catch (Exception ex) {
            log.error("Error playing alert: " + ex.getMessage(), ex);
        }
    }
}
