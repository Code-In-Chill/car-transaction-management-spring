package fis.baolm2.ctm.spring.services;

public interface ReportService {
    byte[] exportTransactionPDF(String userId);

    byte[] exportTransactionExcelPreview(String userId);

    byte[] exportTransactionExcel(String userId);
}
