package fis.baolm2.ctm.spring.services.impls;

import fis.baolm2.ctm.spring.services.ReportService;
import fis.baolm2.ctm.spring.utils.ReportConstant;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final DataSource dataSource;

    @Autowired
    public ReportServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public byte[] exportTransactionPDF(String userId) {
        try {
            JasperPrint jasperPrint = createJasperPrint(userId, ReportConstant.TRANSACTION_REPORT_PDF);

            if (jasperPrint == null) {
                return new byte[0];
            }

            // Export to PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Override
    public byte[] exportTransactionExcelPreview(String userId) {
        try {
            JasperPrint jasperPrint = createJasperPrint(userId, ReportConstant.TRANSACTION_REPORT_EXCEL);

            if (jasperPrint == null) {
                return new byte[0];
            }

            // Export to PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Override
    public byte[] exportTransactionExcel(String userId) {
        try {
            JasperPrint jasperPrint = createJasperPrint(userId, ReportConstant.TRANSACTION_REPORT_EXCEL);

            if (jasperPrint == null) {
                return new byte[0];
            }

            jasperPrint.setBottomMargin(0);
            jasperPrint.setTopMargin(0);
            jasperPrint.setLeftMargin(0);
            jasperPrint.setRightMargin(0);

            // Export to Excel
            JRXlsxExporter exporter = new JRXlsxExporter();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setCollapseRowSpan(true);
            configuration.setDetectCellType(true);
            exporter.setConfiguration(configuration);

            exporter.exportReport();

            return outputStream.toByteArray();
        } catch (JRException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private JasperPrint createJasperPrint(String userId, String path) {
        try (var connection = dataSource.getConnection()) {
            ClassPathResource resource = new ClassPathResource(path);
            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());

            Map<String, Object> params = new HashMap<>();
            params.put("USER_ID", userId);

            // Fill report with data using try-with-resources
            return JasperFillManager.fillReport(
                    jasperReport,
                    params,
                    connection
            );
        } catch (JRException | SQLException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
