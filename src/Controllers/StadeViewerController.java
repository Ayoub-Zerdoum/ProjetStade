/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import Modules.Concert;
import Modules.Evenement;
import Modules.Match;
import Modules.Section;
import Modules.Siege;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import static javafx.scene.shape.StrokeType.INSIDE;
import static javafx.scene.shape.StrokeType.OUTSIDE;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import static javafx.scene.text.TextAlignment.CENTER;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * FXML Controller class
 *
 * @author Ayoub Zerdoum
 */
public class StadeViewerController implements Initializable {
    
    @FXML
    private ScrollPane StadePane;
    
    @FXML
    private Pane StadeLayout;
    
    private static final String SELECT_ALL_SECTIONS = "SELECT * FROM stadeprojet.sections";
    
    
    private List<Section> sections = new ArrayList<>();
    private List<Group> sectionGroups = new ArrayList<>();
    
    // Section colors and borders
    private final Color SEC_COLOR = Color.web("#e6e6fa"); // Lavender
    private final Color SEL_SEC_COLOR = Color.PURPLE;
    private final Color SEC_BORDER_COLOR = Color.web("#a09db2"); // Slate Gray
    private final double SEC_BORDER_WIDTH = 0.5; // Replace 2.0 with your desired width
    private final StrokeType SEC_BORDER_TYPE = StrokeType.OUTSIDE; // or INSIDE if needed

    // Siege colors and borders
    private final Color SIG_COLOR = Color.WHITE;
    private final Color SEL_SIG_COLOR = Color.GREEN;
    private final Color SIG_BORDER_COLOR = Color.web("#a09db2"); // Slate Gray
    private final double SIG_BORDER_WIDTH = 0.2; // Replace 2.0 with your desired width
    private final StrokeType SIG_BORDER_TYPE = StrokeType.OUTSIDE; // or INSIDE if needed
    
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/stadeprojet";
    private static final String USER = "root";
    private static final String PASSWORD = "AyoubM";

    // JDBC variables for opening, closing, and managing connection
    private static Connection connection;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadSectionsFromDatabase();
       
        StadeLayout.setPrefWidth(1336);
        StadeLayout.setPrefHeight(738);
    }  
    
    private void loadSectionsFromDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SECTIONS)) {
            
            // Set radius and scale for the entire Sieges table
            setRadiusAndScale();

            while (resultSet.next()) {
                int id = resultSet.getInt("sectionId");
                String sectionName = resultSet.getString("sectionName");
                int shapeIndex = resultSet.getInt("shapeIndex");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double rotation = resultSet.getDouble("rotation");
                double scale = resultSet.getDouble("scale");

                Section section = new Section(id, sectionName, shapeIndex, x, y, rotation, scale);
                // Load sieges associated with the section
                // Create a new section group
                Group sectionGroup = new Group();
                //section.getSectionShape().setOnMouseClicked(event -> selectSection(section));
                applyColorAndBorder(section.getSectionShape(), SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);
                sections.add(section);
                sectionGroups.add(sectionGroup);

                // Update the transformations
                // Scale the section shape
                section.getSectionShape().setScaleX(section.getScale());
                section.getSectionShape().setScaleY(section.getScale());

                // Ajoute la forme de section




                sectionGroup.getChildren().add(section.getSectionShape());

                // Applique les transformations de rotation et de translation
                sectionGroup.getTransforms().add(new Rotate(section.getRotation(), section.getX(), section.getY()));
                //sectionGroup.getTransforms().add(new Rotate(section.getRotation()));
                sectionGroup.getTransforms().add(new Translate(section.getX(), section.getY()));


                loadAndAddSiegesForSection(connection,section,sectionGroup);


                for (Siege siege : section.getSieges()) {
                    System.out.println("Siege X: " + siege.getCenterX() + ", Siege Y: " + siege.getCenterY());
                    //siege.getTransforms().add(new Translate(-section.getX(), -section.getY()));
                    sectionGroup.getChildren().add(siege);
                }

                StadeLayout.getChildren().add(sectionGroup); 

                }


            } catch (SQLException e) {
                e.printStackTrace(); // 
            }
        }
    
    private void loadAndAddSiegesForSection(Connection connection, Section section,Group sectionGroup) {
        int sectionId = section.getSectionId();
        String sql = "SELECT * FROM Sieges WHERE sectionId = ?";
        

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, sectionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int siegeId = resultSet.getInt("siegeId");
                    int numSiege = resultSet.getInt("numSiege");
                    double x = resultSet.getDouble("x");
                    double y = resultSet.getDouble("y");

                    Siege siege = new Siege(siegeId, numSiege, sectionId, "AVAILABLE", x, y);
                    //siege.setFill(SEL_SIEGE_COLOR);
                    applyColorAndBorder(siege, SIG_COLOR, SIG_BORDER_COLOR, SIG_BORDER_WIDTH, SIG_BORDER_TYPE);
                    //siege.setOnMouseClicked(event -> selectSiege(siege));
                    section.addSiege(siege);
                    
                    
                    
                }
                
                // Add the sieges to the section group
                
            }
        } catch (SQLException e) {
            e.printStackTrace(); //
        }
    }
    
    
    //******** extrat ****************//
    private void setRadiusAndScale() {
        String query = "SELECT radius, scale FROM Sieges LIMIT 1";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                double radius = resultSet.getDouble("radius");
                double scale = resultSet.getDouble("scale");
                Siege.setStaticRadius(radius);
                Siege.setScaleAll(scale);
            } 

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    
    public void applyColorAndBorder(Shape shape, Color fillColor, Color borderColor, double borderWidth, StrokeType strokeType) {
        shape.setFill(fillColor);
        shape.setStroke(borderColor);
        shape.setStrokeWidth(borderWidth);
        shape.setStrokeType(strokeType);
    }
    
    private double zoomLevel = 1.0;
    public void zoomInButtonClicked() {
        zoomLevel *= 1.1; // You can adjust the factor as needed
        applyZoom();
        System.out.println("zoom in");
        
        StadeLayout.setPrefWidth(StadeLayout.getWidth()+200);
        StadeLayout.setPrefHeight(StadeLayout.getHeight()+200);
    }

    public void zoomOutButtonClicked() {
        zoomLevel /= 1.1; // You can adjust the factor as needed

        // Ensure the zoom level doesn't go below 1.0
        if (zoomLevel < 1.0) {
            zoomLevel = 1.0;
        }
        applyZoom();
        System.out.println("zoom out");
        
        StadeLayout.setPrefWidth(StadeLayout.getWidth()-200);
        StadeLayout.setPrefHeight(StadeLayout.getHeight()-200);
    }

    private void applyZoom() {
        StadeLayout.setScaleX(zoomLevel);
        StadeLayout.setScaleY(zoomLevel);
        
        System.out.println("zoom applied");
        
        StadeLayout.setTranslateX(StadeLayout.getWidth()*(zoomLevel - 1.0)/2);
        StadeLayout.setTranslateY(StadeLayout.getHeight()*(zoomLevel - 1.0)/2);
    }
    
}
