package cz.rbe.monitor;

import cz.rbe.monitor.probe.ProbeResult;
import cz.rbe.monitor.tpl.MainTpl;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Controller for application homepage.
 * @author Radek Beran
 */
@Controller
@RequestMapping("/")
public class MainController {

    private final MonitoringService monitoringService;

    public MainController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    String index(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        StringBuilder sb = new StringBuilder();
        for (ProbeResult result : monitoringService.getProbeResults()) {
            sb.append("<p>" + result.getProbeName() + ":\t" + result.getSeverity().name() + ":\t" + result.getMessage() + "</p>");
        }
        return MainTpl.t1 + sb.toString() + MainTpl.t2;
    }
}
