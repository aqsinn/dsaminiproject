import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.net.URI;
import java.sql.*;


public class FXGUI extends Application {

    public void openLink(String link, Desktop desktop, ObservableList urllist) {
        try {
            desktop.browse(new URI(link));
            urllist.add(new Record(link));
        } catch (Exception ex) {
            System.out.println("Desktop not available");
        }
    }

    public void hide(Text t, int sec) {
        PauseTransition pause = new PauseTransition(Duration.seconds(sec));
        pause.setOnFinished(e -> t.setVisible(false));
        pause.play();
    }

    @Override
    public void start(Stage stage) {
        NativeHistory history = new NativeHistory();
        GridPane root_m = new GridPane();
        GridPane secondary = new GridPane();
        VBox root = new VBox();
        VBox hist_page = new VBox();
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTSTEELBLUE, new CornerRadii(0), new Insets(0))));
        hist_page.setBackground(Background.fill(Color.LIMEGREEN));
        Scene scene = new Scene(root_m, 800, 600);
        Scene hist = new Scene(secondary, 800, 600);
        ListView<Record> list = new ListView<>();
        ObservableList<Record> urllist = FXCollections.observableArrayList();
        list.setItems(urllist);
        boolean restored = true;
        list.setBackground(Background.fill(Color.LIGHTCORAL));
        try {
            ResultSet rs;
            Connection con = Database.connect();
            Database.createTable(con);
            Statement st = con.createStatement();
            rs = st.executeQuery("SELECT RECORD, DATE FROM HISTORY");
            while (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                Record re = new Record(rs.getString(1), rs.getString(2));
                System.out.println(re);
                urllist.add(re);
            }
            System.out.println();
            System.out.println(urllist);
        } catch (SQLException e) {
            restored = false;
            System.out.println("state not restored");
        }
        list.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Record item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    int number = getIndex() + 1;
                    setText(number + ".  " + item);
                }
            }
        });
        list.scrollTo(urllist.size());
        stage.setTitle("Home Page");
        Text text = new Text("Current Page: Home Page");
        Text welcome = new Text("Welcome");
        HBox hb = new HBox();
        Button previous = new Button("Previous");
        Button next = new Button("Next");
        Button home = new Button("Home");
        home.setOnAction(e -> {
            text.setText("Current Page: Home Page");
        });
        previous.setOnAction(e -> {
            text.setText(history.addPage());
        });
        next.setOnAction(e -> {
            text.setText(history.addPage());
        });
        hb.setAlignment(Pos.CENTER);
        VBox.setMargin(hb, new Insets(20, 10, 10, 10));
        HBox.setMargin(previous, new Insets(20, 10, 10, 10));
        HBox.setMargin(next, new Insets(20, 10, 10, 10));
        HBox.setMargin(home, new Insets(20, 10, 10, 10));
        home.setMaxWidth(Double.MAX_VALUE);
        previous.setMaxWidth(Double.MAX_VALUE);
        next.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(previous, Priority.ALWAYS);
        HBox.setHgrow(home, Priority.ALWAYS);
        HBox.setHgrow(next, Priority.ALWAYS);
        hb.getChildren().addAll(previous, home, next);
//        stage.heightProperty().addListener((observable, oldValue, newHeight) -> {
//        double bMargin = newHeight.doubleValue() * 0.11;
//         VBox.setMargin(welcome, new Insets(10, 10, bMargin, 10));
//        });


        text.setX(15);
        text.setY(40);
        text.setFont(Font.font("Arial", 40));
        welcome.setX(20);
        welcome.setY(80);
        welcome.setFont(Font.font("Arial", 40));
        welcome.setFill(Color.LIGHTCORAL);
        text.setFill(Color.DARKCYAN);
        TextField textfield = new TextField();
        textfield.setPromptText("Enter URL");
        textfield.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
        textfield.setBackground(Background.fill(Color.ORANGERED));
        VBox.setMargin(textfield, new Insets(10, 10, 10, 10));
        textfield.prefWidthProperty().bind(root.widthProperty().multiply(0.6));
        Button button = new Button();
        Button displayhist = new Button();
        displayhist.setText("Display History");
        Button back = new Button();
        back.setText("Back");
        VBox.setMargin(button, new Insets(10, 10, 10, 10));
        VBox.setMargin(displayhist, new Insets(10, 10, 10, 10));
        VBox.setMargin(text, new Insets(30, 10, 10, 10));
        VBox.setMargin(welcome, new Insets(10, 10, 70, 10));
        Desktop desktop = Desktop.getDesktop();
        VBox.setMargin(back, new Insets(10, 10, 10, 10));
        VBox.setMargin(list, new Insets(20, 20, 20, 20));
        list.setPadding(new Insets(10, 10, 10, 10));
        hist_page.getChildren().addAll(back, list);
        list.setOnMouseClicked(event -> {
            Record rec = list.getSelectionModel().getSelectedItem();
            if (rec != null) {
                openLink(rec.link, desktop, urllist);
            }
        });
        back.setOnAction(e -> stage.setScene(scene));
        button.setText("Open URL");
        button.disableProperty().bind(textfield.textProperty().isEmpty());
        button.setOnAction(e -> {
            String url = textfield.getText();
            textfield.clear();
            openLink(url, desktop, urllist);
        });
        button.prefWidthProperty().bind(stage.widthProperty().multiply(0.6));
        displayhist.prefWidthProperty().bind(stage.widthProperty().multiply(0.6));
        VBox.setMargin(textfield, new Insets(0, 70, 0, 70));
        root.setAlignment(Pos.BASELINE_CENTER);
        textfield.setOnAction(e -> {
            String url = textfield.getText();
            urllist.add(new Record(url));
            textfield.clear();
            try {
                desktop.browse(new URI(url));
            } catch (Exception ex) {
                System.out.println("Desktop not available");
            }
        });
        displayhist.setOnAction(e -> stage.setScene(hist));
        ColumnConstraints col1 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();
        col1.setPercentWidth(100);
        row1.setPercentHeight(100);
        root_m.getColumnConstraints().addAll(col1);
        secondary.getRowConstraints().addAll(row1);
        secondary.getColumnConstraints().addAll(col1);
        root_m.getRowConstraints().addAll(row1);
        root_m.getChildren().addFirst(root);
        String get_all = "INSERT INTO HISTORY (RECORD, DATE) VALUES (?,?)";
        secondary.getChildren().addFirst(hist_page);
        if (restored) {
            stage.setOnCloseRequest(e -> {
                Connection connection = Database.connect();
                Database.createTable(connection);
                Database.clearTable(connection);
                try (PreparedStatement ps = connection.prepareStatement(get_all)) {
                    for (Record r : urllist) {
                        ps.setString(2, r.date);
                        ps.setString(1, r.link);
                        ps.executeUpdate();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
        root.getChildren().addAll(text, welcome, textfield, hb, button, displayhist);
        hide(welcome, 3);
        stage.setScene(scene);
        stage.show();
    }
}
