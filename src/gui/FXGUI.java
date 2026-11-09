package gui;

import history.BrowserHistory;
import history.PageNode;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.format.DateTimeFormatter;

public class FXGUI extends Application {

    private static final String HOME = "Home Page";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // history lives in memory only
    private final BrowserHistory browser = new BrowserHistory(HOME);
    private final ObservableList<PageNode> pages = FXCollections.observableArrayList();

    private Stage stage;
    private Text text;
    private Text meta;
    private Button previous;
    private Button next;
    private ListView<PageNode> list;

    // sync every control with the model
    private void refresh() {
        PageNode cur = browser.current_page();
        text.setText("Current Page: " + cur.getUrl());
        meta.setText("Visited: " + FMT.format(cur.getTimestamp()));
        stage.setTitle(cur.getUrl());
        previous.setDisable(!browser.canGoBack());
        next.setDisable(!browser.canGoForward());
        pages.setAll(browser.getAllPages());
        list.refresh();
        list.scrollTo(pages.indexOf(cur));
    }

    private void visit(String url) {
        String clean = url.trim();
        if (clean.isEmpty()) {
            return;
        }
        browser.visit(clean);
        refresh();
    }

    public void hide(Text t, int sec) {
        PauseTransition pause = new PauseTransition(Duration.seconds(sec));
        pause.setOnFinished(e -> t.setVisible(false));
        pause.play();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        GridPane root_m = new GridPane();
        GridPane secondary = new GridPane();
        VBox root = new VBox();
        VBox hist_page = new VBox();
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTSTEELBLUE, new CornerRadii(0), new Insets(0))));
        hist_page.setBackground(Background.fill(Color.LIMEGREEN));
        Scene scene = new Scene(root_m, 800, 600);
        Scene hist = new Scene(secondary, 800, 600);

        list = new ListView<>(pages);
        list.setBackground(Background.fill(Color.LIGHTCORAL));
        list.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(PageNode item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // mark the page we are on
                    String mark = item == browser.current_page() ? "   <" : "";
                    setText((getIndex() + 1) + ".  " + item.getUrl() + "   " + FMT.format(item.getTimestamp()) + mark);
                }
            }
        });

        text = new Text();
        meta = new Text();
        Text welcome = new Text("Welcome");
        HBox hb = new HBox();
        previous = new Button("Previous");
        next = new Button("Next");
        Button home = new Button("Home");

        home.setOnAction(e -> visit(HOME));
        previous.setOnAction(e -> {
            browser.back(1);
            refresh();
        });
        next.setOnAction(e -> {
            browser.forward(1);
            refresh();
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

        text.setFont(Font.font("Arial", 30));
        text.setFill(Color.DARKCYAN);
        text.wrappingWidthProperty().bind(root.widthProperty().subtract(40));
        meta.setFont(Font.font("Arial", 16));
        meta.setFill(Color.DARKSLATEGRAY);
        welcome.setFont(Font.font("Arial", 40));
        welcome.setFill(Color.LIGHTCORAL);

        TextField textfield = new TextField();
        textfield.setPromptText("Enter URL");
        textfield.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
        textfield.setBackground(Background.fill(Color.ORANGERED));
        textfield.prefWidthProperty().bind(root.widthProperty().multiply(0.6));
        VBox.setMargin(textfield, new Insets(0, 70, 0, 70));

        Button button = new Button("Open URL");
        Button displayhist = new Button("Display History");
        Button back = new Button("Return");
        button.disableProperty().bind(textfield.textProperty().isEmpty());
        button.prefWidthProperty().bind(stage.widthProperty().multiply(0.6));
        displayhist.prefWidthProperty().bind(stage.widthProperty().multiply(0.6));
        VBox.setMargin(button, new Insets(10, 10, 10, 10));
        VBox.setMargin(displayhist, new Insets(10, 10, 10, 10));
        VBox.setMargin(text, new Insets(30, 10, 0, 10));
        VBox.setMargin(meta, new Insets(0, 10, 10, 10));
        VBox.setMargin(welcome, new Insets(10, 10, 40, 10));
        VBox.setMargin(back, new Insets(10, 10, 10, 10));
        VBox.setMargin(list, new Insets(20, 20, 20, 20));
        list.setPadding(new Insets(10, 10, 10, 10));
        root.setAlignment(Pos.BASELINE_CENTER);

        // open url from button or enter key
        Runnable submit = () -> {
            String url = textfield.getText();
            textfield.clear();
            visit(url);
        };
        button.setOnAction(e -> submit.run());
        textfield.setOnAction(e -> submit.run());

        // click a history entry to jump to it
        list.setOnMouseClicked(event -> {
            PageNode picked = list.getSelectionModel().getSelectedItem();
            if (picked == null) {
                return;
            }
            int target = pages.indexOf(picked);
            int now = pages.indexOf(browser.current_page());
            if (target < now) {
                browser.back(now - target);
            } else if (target > now) {
                browser.forward(target - now);
            }
            refresh();
        });

        displayhist.setOnAction(e -> {
            refresh();
            stage.setScene(hist);
        });
        back.setOnAction(e -> stage.setScene(scene));

        hist_page.getChildren().addAll(back, list);

        ColumnConstraints col1 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();
        col1.setPercentWidth(100);
        row1.setPercentHeight(100);
        root_m.getColumnConstraints().add(col1);
        root_m.getRowConstraints().add(row1);
        secondary.getColumnConstraints().add(col1);
        secondary.getRowConstraints().add(row1);
        root_m.getChildren().addFirst(root);
        secondary.getChildren().addFirst(hist_page);

        root.getChildren().addAll(text, meta, welcome, textfield, hb, button, displayhist);
        hide(welcome, 3);

        refresh();
        stage.setScene(scene);
        stage.show();
    }
}
