package lecturer;

import com.example.project_management_system.PMS_Controller;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.io.File;

public class ReportInfoController implements Initializable {
    @FXML
    private Label Intake;
    @FXML
    private ImageView pdf;

    @FXML
    private Label asessmentID;

    @FXML
    private Button closeBtn;

    @FXML
    private TextField feedback;

    @FXML
    private Label reportID;

    @FXML
    private Label reportName;

    @FXML
    private Label studentID;

    @FXML
    private Button updateBtn;
    public String cReportID;
    public String cReportName;
    public String cStudentID;
    public String cIntake;
    public String cAssessmentID;
    public String cFeedback;
    public String filepath;

    public void Close(ActionEvent event){
        Stage stage = (Stage)this.closeBtn.getScene().getWindow();
        stage.close();
    }

    private void setTextFields(){
        this.reportID.setText(this.cReportID);
        this.reportName.setText(this.cReportName);
        this.studentID.setText(this.cStudentID);
        this.Intake.setText(this.cIntake);
        this.asessmentID.setText(this.cAssessmentID);
        this.feedback.setText(this.cFeedback);
    }
    private void loadReportData()throws IOException {
        String line = null;

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
        while ((line = reader.readLine()) != null ) {
            String[] info = line.split(",");
            System.out.println(Arrays.toString(info));
            if (info[0].equals(ReportController.dummyReportID.getValue())) {
                this.cReportID = info[0];
                this.cReportName = info[1];
                this.cStudentID = info[2];
                this.cIntake = info[3];
                this.cAssessmentID = info[4];
                if (info[9].equals("")|| info[9].equals("null")){
                    this.cFeedback = "";
                }else {
                    this.cFeedback = info[9];
                }
                this.filepath = info[5];
                loadPDF(filepath);

            }
        }
    }

    private List<Image> pageImages;

    public void loadPDF(String filePath) {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            PDFRenderer renderer = new PDFRenderer(document);
            int numPages = document.getNumberOfPages();
            pageImages = new ArrayList<>(numPages);

            for (int i = 0; i < numPages; i++) {
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, 300);
                javafx.scene.image.Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                pageImages.add(image);
            }

            // Display the first page initially
            if (!pageImages.isEmpty()) {
                pdf.setImage(pageImages.get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showNextPage() {
        int currentIndex = pageImages.indexOf(pdf.getImage());
        if (currentIndex >= 0 && currentIndex < pageImages.size() - 1) {
            pdf.setImage(pageImages.get(currentIndex + 1));
        }
    }

    public void showPreviousPage() {
        int currentIndex = pageImages.indexOf(pdf.getImage());
        if (currentIndex > 0) {
            pdf.setImage(pageImages.get(currentIndex - 1));
        }
    }

    public void update (ActionEvent event)throws IOException{
        ArrayList<String> oldContent2 = new ArrayList<>();
        ArrayList<String> oldContent = new ArrayList<>();
        String line = null;
        String line2 = null;

        BufferedReader reader2 = new BufferedReader(new FileReader("src/main/resources/database/report.txt"));
        while ((line2= reader2.readLine())!= null){
            String[] info2 = line2.split(",");
            if (info2[0].equals(ReportController.dummyReportID.getValue())) {
                System.out.println("Report Find in Report.text");
                info2[7] = "Graded";
                info2[9] = this.feedback.getText();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(info2[0]).append(",");
            sb2.append(info2[1]).append(",");
            sb2.append(info2[2]).append(",");
            sb2.append(info2[3]).append(",");
            sb2.append(info2[4]).append(",");
            sb2.append(info2[5]).append(",");
            sb2.append(info2[6]).append(",");
            sb2.append(info2[7]).append(",");
            sb2.append(info2[8]).append(",");
            sb2.append(info2[9]).append(",");
            sb2.append(info2[10]).append(",");
            sb2.append(info2[11]);

            oldContent2.add(String.valueOf(sb2));
            System.out.println("oldContent2: "+oldContent2);
        }


        File file = new File("src/main/resources/database/report.txt");
        FileWriter writer = new FileWriter(file);
        BufferedWriter br = new BufferedWriter(writer);
        for (String s : oldContent2) {
            br.write(s);
            br.newLine();
        }
        br.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText((String)null);
        alert.setContentText("Report is Graded");
        alert.showAndWait();
        this.updateBtn.getScene().getWindow().hide();
        Parent reserve = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/lecturer/Report.fxml")));
        Scene scene = new Scene(reserve);
        PMS_Controller.primaryStage.setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.loadReportData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setTextFields();
    }
}
