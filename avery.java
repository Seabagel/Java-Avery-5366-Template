
/**
 * A JavaFX program that converts integers into binary using Lambda
 * expressions and recursive methods
 *
 * @author Timothy Sebayang
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.lang.StringBuilder;
import java.nio.file.Files;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.text.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.*;
import javax.swing.JOptionPane;

public class avery extends Application {

    // VBox will be the root pane of all HBoxes
    public VBox root = new VBox();

    // Declare scene
    public Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // Set the spacing of our root Pane
        root.setPadding(new Insets(32, 0, 32, 0));

        // Create the 2 boxes inside
        VBox content = createVBox();

        // Add the boxes into the root
        root.getChildren().add(content);

        // Set the Scene, and show the stage
        scene = new Scene(root, 360, 580);
        stage.setTitle("Avery 5366 Label Generator");
        stage.setScene(scene);
        stage.show();
    }

    // A class to create the input fields of the program
    public VBox createVBox() {
        // Create the child Nodes
        final VBox vbOut = new VBox();
        final Button btn = new Button("Make Label");

        final Label[] arrLbl = new Label[15];
        final TextField[] arrTF = new TextField[15];
        final HBox[] arrHbox = new HBox[15];

        for (int i = 0; i < arrTF.length; i++) {
            arrLbl[i] = new Label(Integer.toString(i + 1));
            arrTF[i] = new TextField("");
            arrTF[i].prefWidth(500);
            arrHbox[i] = new HBox();
            arrHbox[i].getChildren().addAll(arrLbl[i], arrTF[i]);
            vbOut.getChildren().add(arrHbox[i]);
            arrHbox[i].setAlignment(Pos.CENTER);
            arrHbox[i].setSpacing(8);
            arrTF[i].setMinWidth(280);
        }

        vbOut.setAlignment(Pos.CENTER);
        vbOut.setSpacing(8);
        vbOut.getChildren().add(btn);

        // btn.setOnAction(new ButtonListener());
        btn.setOnAction(e -> {
            if (e.getSource() instanceof Button) {
                try {

                    String htmlData = "";
                    String arrayData = "";
                    int count = 0;
                    File html = new File("template.html");
                    File newFile = new File("label.html");
                    BufferedReader br = new BufferedReader(new FileReader(html));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));

                    // Write new array
                    for (Node rootBox : root.getChildren()) {
                        if (rootBox instanceof VBox) {
                            for (Node hboxes : ((VBox) rootBox).getChildren()) {
                                if (hboxes instanceof HBox) {
                                    for (Node txtField : ((HBox) hboxes).getChildren()) {
                                        if (txtField instanceof TextField) {
                                            String txt = String.format("clientNumbers[%d] = \"%s\";\n", count,
                                                    ((TextField) txtField).getText());
                                            arrayData += txt;
                                            count++;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Read line from template.html
                    for (String line = br.readLine(); line != null; line = br.readLine()) {
                        htmlData += line.trim().replaceAll(" +", " ") + "\n";
                    }

                    // Replace data with array
                    htmlData = htmlData.replace("//ZX", arrayData);

                    // Check if file exists, then write it
                    if (!newFile.exists()) {
                        newFile.createNewFile();
                    }
                    bw.write(htmlData);
                    br.close();
                    bw.close();

                } catch (Exception err) {
                    JOptionPane.showMessageDialog(null, err.getMessage());
                }
            }
        });
        // Return the VBox
        return vbOut;
    }

    // private class ButtonListener implements EventHandler<ActionEvent> {
    // @Override
    // public void handle(ActionEvent e) {

    // }
    // }

    public static void main(String[] args) {
        Application.launch(args);
    }
}