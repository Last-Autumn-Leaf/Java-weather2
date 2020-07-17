package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.aksingh.owmjapis.api.APIException;

public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    static Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Rectangle rectangle=new Rectangle(360,640);
        rectangle.setArcHeight(60.0);
        rectangle.setArcWidth(60.0);
        root.setClip(rectangle);
        controller= loader.getController();

        primaryStage.setTitle("Weather-Java");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 360, 640, Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();

        //Moveable
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
        //Moveable




    }


    public static void main(String[] args) throws   APIException{
        launch(args);



    }
}
