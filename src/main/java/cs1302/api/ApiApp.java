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

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    /** TicketMaster Logo. */
    private static final String T_MASTER = "https://cdn.cookielaw.org/logos/ba6f9c5b-" +
        "dda5-43bd-bac4-4e06afccd928/002b248b-6e0e-44fd-8cb6-320ffefa48fe/" +
        "e5b998c1-73c7-46e8-a7ff-aa2ea18369bf/Ticketmaster-Logo-Azure_without_R.png";

    /** Request URI header */
    private static final String TMASTER_API = "https://app.ticketmaster.com" +
        "/discovery/v2/events.json?size=1&apikey=";

    /** TicketMaster API Key */
    private static final String tmApiKey = config.getProperty("ticketmasterapi.apikey");

    /** HTTP Client */
        public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();


    private Stage stage;
    private Scene scene;
    private VBox root;
    private ImageView logoFrame;
    private Image logo;
    private Label appDescrip;
    private Button getEvents;
    private HBox labelHeaders;
    private Label eventsLabel;
    private Label infoLabel;
    private HBox mainContent;
    private VBox leftSide;
    private VBox eventsHolder;
    private Label[] eventsDisplayed;
    private VBox rightSide;
    private TextFlow wikiTextFlow;
    private ArrayList<String> eventList;

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
        appDescrip = new Label("Click on the button below to retreive five events, " +
        "click on an event for more information.");
        getEvents = new Button("Get Events!");
        labelHeaders = new HBox(5);
        eventsLabel = new Label("Events: ");
        infoLabel = new Label("Information on event: ");
        mainContent = new HBox(5);
        leftSide = new VBox(5);
        eventsHolder = new VBox(5);
        eventsDisplayed = new Label[5];
        rightSide = new VBox(5);
        wikiTextFlow = new TextFlow();
        eventList = new ArrayList<>();
    } // ApiApp

    /** {@inheritDox} */
    @Override
    public void init() {

        // creating links
        root.getChildren().addAll(logoFrame, appDescrip, getEvents, labelHeaders, mainContent);
        labelHeaders.getChildren().addAll(eventsLabel, infoLabel);
        mainContent.getChildren().addAll(leftSide, rightSide);
        leftSide.getChildren().addAll(eventsLabel, eventsHolder);
        for (int i = 0; i < eventsDisplayed.length; i++) {
            eventsDisplayed[i] = new Label();
            eventsHolder.getChildren().add(eventsDisplayed[i]);
        }
        eventsDisplayed[0].setText("No events yet...");
        rightSide.getChildren().addAll(infoLabel, wikiTextFlow);

        // initializing root
        root.setAlignment(Pos.CENTER);

        // initializing logoFrame
        logoFrame.setImage(logo);
        logoFrame.setPreserveRatio(true);
        logoFrame.setFitWidth(300);

        // initializing labelHeaders
        labelHeaders.setAlignment(Pos.BASELINE_RIGHT);
        labelHeaders.setHgrow(eventsLabel, Priority.ALWAYS);
        labelHeaders.setHgrow(infoLabel, Priority.ALWAYS);

        // initializing mainContent
        mainContent.setHgrow(leftSide, Priority.ALWAYS);
        mainContent.setHgrow(rightSide, Priority.ALWAYS);

    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

    /**
     * Method for the get events button.
     */
    public void getEventsButton() {
        String uriToPassIn = TMASTER_API + tmApiKey;
        URI tMaster = URI.create(uriToPassIn);
        HttpRequest request = HttpRequest.newBuilder().uri(tMaster).build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        }
        String responseBody = response.body();
        TicketMasterResponse tmResponse = GSON.fromJson(responseBody, TicketMasterResponse.class);
        for (int i = 0; i < tmResponse.events.length; i++) {
            if (eventList.contains(tmResponse.events[i].name) == false) {
                eventList.add(tmResponse.events[i].name);
            }
        }
        for (int i = 0; i < eventsDisplayed.length; i++) {
            eventsDisplayed[i].setText(eventList.get((int)(Math.random() * 20)));
        }
    }

} // ApiApp
