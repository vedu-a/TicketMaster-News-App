package cs1302.api;

import java.util.ArrayList;
import java.net.http.HttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    /** HTTP Client */
        public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    /** TicketMaster Logo. */
    private static final String T_MASTER = "https://cdn.cookielaw.org/logos/ba6f9c5b-" +
        "dda5-43bd-bac4-4e06afccd928/002b248b-6e0e-44fd-8cb6-320ffefa48fe/" +
        "e5b998c1-73c7-46e8-a7ff-aa2ea18369bf/Ticketmaster-Logo-Azure_without_R.png";

    /** TicketMaster URI. */
    private static final String TMASTER_API = "https://app.ticketmaster.com" +
        "/discovery/v2/events.json?size=200";

    /** TicketMaster API Key. */
    private static final String configPath = "resources/config.properties";
    private static final String TMASTER_KEY = loadTicketMasterAPIKey();

    /** Wikipedia URI */
    private static final String WIKI_API = "http://en.wikipedia.org/w/api.php" +
        "?action=query&list=search&srsearch=&format=json";

    private Stage stage;
    private Scene scene;
    private VBox root;
    private ImageView logoFrame;
    private Image logo;
    private Label appDescrip;
    private HBox searchbar;
    private TextField textbox;
    private Button getEvents;
    private HBox labelHeaders;
    private Label eventsLabel;
    private Label infoLabel;
    private HBox mainContent;
    private VBox leftSide;
    private VBox eventsHolder;
    private Button[] eventsDisplayed;
    private VBox rightSide;
    private Text defaultText;
    private String title;
    private String descrip;
    private TextField urlField;
    private TextFlow tFLow;
    private String[] eventList;
    private String uriToPassIn;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        stage = null;
        scene = null;
        root = new VBox(5);
        logoFrame = new ImageView();
        logo = new Image(T_MASTER);
        appDescrip = new Label("Enter a city and click on the button " +
        "below to retreive five events, click on the event to retrieve a relevant news article");
        searchbar = new HBox(5);
        textbox = new TextField("");
        getEvents = new Button("Get Events!");
        labelHeaders = new HBox(5);
        eventsLabel = new Label("Events: ");
        infoLabel = new Label("News Article on Event: ");
        mainContent = new HBox(5);
        leftSide = new VBox(5);
        eventsHolder = new VBox(15);
        eventsDisplayed = new Button[5];
        rightSide = new VBox(5);
        title = "Title:";
        descrip = "Description:";
        urlField = new TextField("url...");
        defaultText = new Text("Title:" + "\n\n" + "Description:" );
        tFLow = new TextFlow(defaultText);
        eventList = new String[200];
    } // ApiApp

    /** {@inheritDox} */
    @Override
    public void init() {
        // creating links
        root.getChildren().addAll(logoFrame, appDescrip, searchbar, labelHeaders, mainContent);
        searchbar.getChildren().addAll(textbox, getEvents);
        labelHeaders.getChildren().addAll(eventsLabel, infoLabel);
        mainContent.getChildren().addAll(leftSide, rightSide);
        leftSide.getChildren().addAll(eventsLabel, eventsHolder);
        for (int i = 0; i < eventsDisplayed.length; i++) {
            eventsDisplayed[i] = new Button();
            eventsHolder.getChildren().add(eventsDisplayed[i]);
        }
        eventsDisplayed[0].setText("No events yet...");
        rightSide.getChildren().addAll(infoLabel, tFLow, urlField);
        urlField.setEditable(false);
        rightSide.setVgrow(tFLow, Priority.ALWAYS);

        // initializing root
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(
            new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        appDescrip.setTextFill(Color.WHITE);

        // initializing logo
        logoFrame.setImage(logo);
        logoFrame.setPreserveRatio(true);
        logoFrame.setFitWidth(300);

        // initializing searchbar
        searchbar.setAlignment(Pos.CENTER);

        // initializing labelHeaders
        labelHeaders.setHgrow(eventsLabel, Priority.ALWAYS);
        labelHeaders.setHgrow(infoLabel, Priority.ALWAYS);
        eventsLabel.setMaxWidth(Double.MAX_VALUE);
        infoLabel.setMaxWidth(Double.MAX_VALUE);
        eventsLabel.setAlignment(Pos.CENTER);
        infoLabel.setAlignment(Pos.CENTER);
        eventsLabel.setBackground(new Background(
            new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        infoLabel.setBackground(new Background(
            new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        eventsLabel.setTextFill(Color.WHITE);
        infoLabel.setTextFill(Color.WHITE);

        // initializing mainContent
        mainContent.setHgrow(leftSide, Priority.ALWAYS);
        mainContent.setHgrow(rightSide, Priority.ALWAYS);

        // setting button actions
        Runnable getEventsMethod = () -> getEventsButton();
        getEvents.setOnAction(e -> runOnNewThread(getEventsMethod));
    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        scene = new Scene(root, Color.ORANGE);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.setMinWidth(700);
        stage.setMinHeight(350);
        stage.show();

    } // start

    /**
     * Method to load properties.
     */
    private static String loadTicketMasterAPIKey () {
        try (FileInputStream configFileStream = new FileInputStream(configPath)) {
            Properties config = new Properties();
            config.load(configFileStream);
            String tmApiKey = config.getProperty("ticketmasterapi.apikey");
            return tmApiKey;
        } catch (IOException ioe) {
            System.err.println(ioe);
            ioe.printStackTrace();
        }
        return "";
    }

    /**
     * Method for the get events button.
     */
    private void getEventsButton() {
        try {
            String city = textbox.getText();
            if (city == "") {
                uriToPassIn = TMASTER_API + "&apikey=" + TMASTER_KEY;
                String msg = "No city was passed in, random events will be displayed";
                Runnable warning = () -> alertWarning(uriToPassIn, msg);
                Platform.runLater(warning);
            } else {
                for (int i = 0; i < city.length(); i++) {
                    if (city.charAt(i) ==  ' ') {
                        city = city.substring(0, i) + "%20" + city.substring(i + 1);
                    }
                }
                uriToPassIn = TMASTER_API + "&city=" + city + "&apikey=" + TMASTER_KEY;
            }
            URI tMaster = URI.create(uriToPassIn);
            HttpRequest request = HttpRequest.newBuilder().uri(tMaster).build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException(response.toString());
            }
            String responseBody = response.body();
            TicketMasterResponse tmResponse =
                GSON.fromJson(responseBody, TicketMasterResponse.class);
            eventList = filter(tmResponse.embedded.events);
            for (int i = 0; i < eventsDisplayed.length; i++) {
                final String eventName = eventList[(int)(Math.random() * eventList.length)];
                final Button eventLabel = eventsDisplayed[i];
                Runnable task = () -> eventLabel.setText(eventName);
                Platform.runLater(task);
            }
        } catch (NullPointerException npe) {
            String msg = "Not enough distinct results found or quota limit exceeded.";
            Runnable e = () -> alertError(uriToPassIn, npe, msg);
            Platform.runLater(e);
        } catch (IOException | InterruptedException e) {
            Runnable err = () -> alertError(uriToPassIn, e);
            Platform.runLater(err);
        }
    }

    /**
     * Filter method for events.
     */
    private static String[] filter (TicketMasterEvent[] eventsArray) {
        int filterIndex = 0;
        String[] filteredArray = new String[eventsArray.length];
        for (int i = 0; i < eventsArray.length; i++) {
            int counter = 0;
            for (int j = 0; j < filteredArray.length; j++) {
                if (eventsArray[i] == null) {
                    counter++;
                } else if (eventsArray[i].name.equals(filteredArray[j])) {
                    counter++;
                }
            }
            if (counter == 0) {
                filteredArray[filterIndex] = eventsArray[i].name;
                filterIndex++;
            }
        }

        String[] finalArray = new String[filterIndex];
        for (int i = 0; i < finalArray.length; i++) {
            finalArray[i] = filteredArray[i];
        }

        return finalArray;
    }

    /**
     * Method for the labels.
     */
/*    private void firstButton {
        String toSearch = eventsDisplayed[0].getText();
        for (int i = 0; i < toSearch.length(); i++) {
            String temp = toSearch;
            if (toSearch.charAt(i) == " ") {
                toSearch = toSearch.substring(0, i) + "%20" + toSearch(i + 1);
            }
        }
        URI wikiURI = URI.create(toSearch);
        HttpRequest request = HttpRequest.newBuilder().uri(wikiURI).build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, BoduHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        }
        String responseBody = response.body();

        }*/

    /**
     * Throws warning windows.
     */
    public static void alertWarning(String uri, String msg) {
        TextArea text = new TextArea(uri + "\n\n" + msg);
        text.setEditable(false);
        Alert alert = new Alert(AlertType.WARNING);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
    }

    /**
     * Throws error windows in case of exceptions.
     *
     * @param uri the url of query searched in itunes
     * @param cause the exception thrown
     */
    public static void alertError(String uri, Throwable cause) {
        TextArea text = new TextArea(uri + "\n\n" + cause.toString());
        text.setEditable(false);
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
    }

    /**
     * Throws error windows in case of exceptions.
     *
     * @param uri the url of query searched in itunes
     * @param cause the exception thrown
     * @param msg message that wants to be sent in alert box
     */
    public static void alertError(String uri, Throwable cause, String msg) {
        TextArea text = new TextArea(uri + "\n\n" + cause.toString() + "\n" + msg);
        text.setEditable(false);
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
    }

    /**
     * Runs task on new method;
     */
    private void runOnNewThread(Runnable method) {
        Thread newT = new Thread(method);
        newT.start();
    }

} // ApiApp
