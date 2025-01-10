package fis.baolm2.ctm.spring.controllers;

import fis.baolm2.ctm.spring.dtos.response.ErrorResponse;
import fis.baolm2.ctm.spring.services.ReportService;
import fis.baolm2.ctm.spring.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/export/transaction/pdf")
    public ResponseEntity<?> exportTransactionPdf() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userID = UserUtil.getUserId(authentication);

            byte[] reportContent = reportService.exportTransactionPDF(userID);

            if (reportContent == null || reportContent.length == 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("No Data", "No data available for report generation"));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "pdf_report_" + UUID.randomUUID() + ".pdf");

            return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            "Error generating report",
                            e.getMessage()
                    ));
        }
    }

    @GetMapping("/export/transaction/excel/preview")
    public ResponseEntity<?> exportExcelPreview() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userID = UserUtil.getUserId(authentication);

            byte[] reportContent = reportService.exportTransactionExcelPreview(userID);

            if (reportContent == null || reportContent.length == 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("No Data", "No data available for report generation"));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "pdf_excel_preview" + UUID.randomUUID() + ".pdf");

            return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            "Error generating report",
                            e.getMessage()
                    ));
        }
    }

    @GetMapping("/export/transaction/excel")
    public ResponseEntity<?> exportExcel() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userID = UserUtil.getUserId(authentication);

            byte[] reportContent = reportService.exportTransactionExcel(userID);

            if (reportContent == null || reportContent.length == 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("No Data", "No data available for report generation"));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("filename", "excel_report_" + UUID.randomUUID() + ".xlsx");

            return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            "Error generating report",
                            e.getMessage()
                    ));
        }
    }
}
