package id.co.butik.controller;

import id.co.butik.dto.DashboardInner;
import id.co.butik.service.DataDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DataDashboardService dataDashboardService;

    public DashboardController(DataDashboardService dataDashboardService) {
        this.dataDashboardService = dataDashboardService;
    }

    @GetMapping({"/inner","/inner/"})
    public DashboardInner inner(HttpServletRequest request) {
        return dataDashboardService.getDataDashoard(request);
    }
}
