package de.hssfds;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;

public class ZweiScrollbareBilder extends Application {

    private double zoomFactor = 1.0;
    private final double zoomStep = 0.1;
    private final double zoomMin = 0.2;
    private final double zoomMax = 5.0;

    @Override
    public void start(Stage stage) {
        // Bilder laden
        ImageView imageView1 = new ImageView(new Image(new File("bild1.jpg").toURI().toString()));
        ImageView imageView2 = new ImageView(new Image(new File("bild2.jpg").toURI().toString()));

        // Optional: Bildgröße auf Original belassen
        imageView1.setPreserveRatio(true);
        imageView2.setPreserveRatio(true);

        // ScrollPanes anlegen
        ScrollPane scrollPane1 = new ScrollPane(imageView1);
        ScrollPane scrollPane2 = new ScrollPane(imageView2);

        // Optional: Viewport-Größe
        scrollPane1.setPrefViewportWidth(400);
        scrollPane1.setPrefViewportHeight(400);

        scrollPane2.setPrefViewportWidth(400);
        scrollPane2.setPrefViewportHeight(400);

        // 🎯 Scroll-Synchronisation
        scrollPane1.vvalueProperty().bindBidirectional(scrollPane2.vvalueProperty());
        scrollPane1.hvalueProperty().bindBidirectional(scrollPane2.hvalueProperty());

        // 💡 Zoom-Event-Handler
        scrollPane1.addEventFilter(ScrollEvent.SCROLL, e -> handleZoom(e, imageView1, imageView2, scrollPane1, scrollPane2));
        scrollPane2.addEventFilter(ScrollEvent.SCROLL, e -> handleZoom(e, imageView1, imageView2, scrollPane1, scrollPane2));

        // 💡 Bild zentrieren nach Layout
        Platform.runLater(() -> {
            scrollPane1.setHvalue(0.5);
            scrollPane1.setVvalue(0.5);
            scrollPane2.setHvalue(0.5);
            scrollPane2.setVvalue(0.5);
        });

        // Layout
        HBox root = new HBox(10, scrollPane1, scrollPane2);

        // Szene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Zwei scrollbare Bilder");
        stage.show();
    }

    private void handleZoom(ScrollEvent e, ImageView iv1, ImageView iv2, ScrollPane sp1, ScrollPane sp2)
    {
        if (e.isControlDown()) {
            double delta = e.getDeltaY();
            if (delta > 0 && zoomFactor < zoomMax) {
                zoomFactor += zoomStep;
            } else if (delta < 0 && zoomFactor > zoomMin) {
                zoomFactor -= zoomStep;
            }

            iv1.setScaleX(zoomFactor);
            iv1.setScaleY(zoomFactor);
            iv2.setScaleX(zoomFactor);
            iv2.setScaleY(zoomFactor);

            Platform.runLater(() -> {
                sp1.setHvalue(0.5);
                sp1.setVvalue(0.5);
                sp2.setHvalue(0.5);
                sp2.setVvalue(0.5);
            });

            e.consume(); // Event nicht weiterreichen//
        }

    }


    public static void main(String[] args) {
        launch();
    }
}