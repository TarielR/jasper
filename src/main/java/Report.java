import javax.faces.context.FacesContext;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.*;

public class Report {
    private DefaultTableModel tableModel;
    private List list = new ArrayList();
    private final String PATH = "C:\\Java\\projects\\jasper\\";

    public static void main(String[] args) {
        new Report().createReport();
    }

    private void createReport() {
        Report report = new Report();
        report.fillData();
        //String sourceFileName = getClass().getClassLoader().getResource("/resources/jasper/Users.jrxml").toString();
        //String sourceFileName = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/jasper/Users.jrxml");
       //String sourceFileName = Report.class.getResource("Users.jrxml").toExternalForm();
        String sourceFileName = getProperty("user.dir")+"/src/main/resources/jasper/Users.jrxml";


        try {
            JasperReport jReport = JasperCompileManager.compileReport(sourceFileName);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
            JasperPrint jPrint = JasperFillManager.fillReport(jReport, new HashMap(), new JRTableModelDataSource(report.getTableModel()));

//            JasperViewer jViewer = new JasperViewer(jPrint);
//            jViewer.setVisible(true);

            JasperExportManager.exportReportToHtmlFile(jPrint, PATH + "users.html");
            JasperExportManager.exportReportToPdfFile(jPrint, PATH + "users.pdf");
            JasperExportManager.exportReportToXmlFile(jPrint, PATH + "users.xml", false);

            JRXlsExporter exporter = new JRXlsExporter();

            exporter.setExporterInput(new SimpleExporterInput(jPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(PATH + "users.xls"));

            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
            configuration.setOnePagePerSheet(false);
            exporter.setConfiguration(configuration);

            exporter.exportReport();

        } catch (JRException e) {
            e.printStackTrace();
        }

        exit(1);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    private void fillData() {
        String[] columnNames = {"ID", "PASSWORD", "SHORT_NAME", "FULL_NAME", "ORGANIZATSIYA", "DOLZHNOST", "ID_UPPER", "PASSWORD_UNENCRYPTED", "IS_USER_LOCKED", "IS_FORCE_PASS_CHANGE", "PASSWORD_NEW"};
        String[][] data = {
                {"001", "John", "aaa", "1q1"},
                {"002", "Liz", "bbb", "2w2"}
        };

        tableModel = new DefaultTableModel(data, columnNames);
    }
}
