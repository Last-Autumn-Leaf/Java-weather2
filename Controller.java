package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.core.OWM.Language;
import net.aksingh.owmjapis.model.CurrentWeather;

import java.text.SimpleDateFormat;
import java.util.Date;

import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.getKeyCode;


public class Controller {

    private String lieu;

    @FXML
    private Pane box_pane;

    @FXML
    public void initialize() throws APIException{
        search_bar.setVisible(true);
        box_pane.setVisible(false);
        cls.setVisible(true);
        BG_pane.setImage(new Image("sample/images/cold-bg.jpg"));
        cls.setImage(new Image("sample/images/close_btn.png"));
    cls.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            if (event.getTarget() == cls) {
              Stage stage = (Stage) cls.getScene().getWindow();
              stage.close();
            }
          }
        });

        search_bar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()== ENTER) {
                    try {
                        SendRequest();
                    } catch (APIException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Lieu non trouvé ?", ButtonType.CANCEL);
                        alert.setHeaderText("Bad Query");
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.YES) {
                            //do stuff
                        }
                    }
                }
            }
        });

    }

    @FXML
    private TextField search_bar;

    @FXML
    private ImageView BG_pane,cls;

    @FXML
    private Label loc,date,temperature,temps;



    public void SendRequest() throws APIException {
      lieu = search_bar.getText();

      OWM owm = new OWM("811758ba9113bdc93f0c9389caad2648");
      owm.setLanguage(Language.FRENCH);
      owm.setUnit(OWM.Unit.METRIC);

      // -----get Current Weather
      CurrentWeather cwd = owm.currentWeatherByCityName(lieu);

      // -----City Name
        if(cwd.hasCityName()) {
            loc.setText(cwd.getCityName());
        }else{
            loc.setText("Ville NA");
        }

      // ----temperature
        if(cwd.hasMainData()&&cwd.getMainData().hasTemp()) {

            temperature.setText( Math.round(cwd.getMainData().getTemp()) + " °C"); // converted to Celsius

            if(cwd.getMainData().getTemp()>20){//warm
                BG_pane.setImage(new Image("sample/images/warm-bg.jpg"));
                temps.setTextFill(Color.WHITE);
                date.setTextFill(Color.WHITE);
                loc.setTextFill(Color.WHITE);
            }else{
                BG_pane.setImage(new Image("sample/images/cold-bg.jpg"));
                temps.setTextFill(Color.BLACK);
                date.setTextFill(Color.BLACK);
                loc.setTextFill(Color.BLACK);
            }
        }else{
            temperature.setText("Temp NA");
        }

      //------------Date Locale
        if(cwd.hasDateTime()) {
            Date Calender= cwd.getDateTime();
            SimpleDateFormat ft = new SimpleDateFormat("EEEE, dd MMMM y");
            date.setText(ft.format(Calender));
        }else{
            date.setText("Date NA");
        }



      // ----------Set le temps
        if(cwd.hasWeatherList()) {
            String weatherList = "";
              for (int i = 0; i < cwd.getWeatherList().size(); i++)
                weatherList += cwd.getWeatherList().get(i).getDescription() + "/";
              weatherList = weatherList.substring(0, weatherList.length() - 1);
              temps.setText(weatherList);
      }else{
            temps.setText("???");
        }

      box_pane.setVisible(true);

    }

}
