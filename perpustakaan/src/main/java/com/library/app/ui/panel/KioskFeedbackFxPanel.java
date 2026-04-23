package com.library.app.ui.panel;

import com.library.app.service.FeedbackService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class KioskFeedbackFxPanel {
    private final FeedbackService feedbackService = new FeedbackService();
    private static final double FEEDBACK_CARD_WIDTH = 560;
    private static final double FEEDBACK_FORM_WIDTH = 400;
    private int selectedRating = 0;
    private Label messageLabelRef;

    public Node createContent(Runnable onBack) {
        VBox content = new VBox(10);
        content.getStyleClass().add("visit-content");
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(640);

        // Logo tetap ada
        Node icon = KioskIconFactory.createFeedbackIcon(Color.web("#D97706"));
        StackPane iconShell = new StackPane(icon);
        iconShell.getStyleClass().add("visit-icon-shell");
        iconShell.setAlignment(Pos.CENTER);
        iconShell.setMinSize(46, 46);
        iconShell.setPrefSize(46, 46);
        iconShell.setMaxSize(46, 46);

        Label title = new Label("Beri Feedback");
        title.getStyleClass().add("visit-title");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);

        Label subtitle = new Label("Sampaikan saran dan masukan Anda");
        subtitle.getStyleClass().add("visit-subtitle");
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(500);
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setTextAlignment(TextAlignment.CENTER);

        Label ratingLabel = new Label("Rating");
        ratingLabel.getStyleClass().add("visit-label");

        HBox ratingStars = new HBox(8);
        ratingStars.setAlignment(Pos.CENTER_LEFT);

        StackPane[] starBoxes = new StackPane[5];
        Label[] stars = new Label[5];

        for (int i = 0; i < 5; i++) {
            final int ratingValue = i + 1;

            Label star = new Label("★");
            star.setFont(Font.font(16));
            stars[i] = star;

            StackPane starBox = new StackPane(star);
            starBox.setMinSize(36, 36);
            starBox.setPrefSize(36, 36);
            starBox.setMaxSize(36, 36);
            starBox.setAlignment(Pos.CENTER);
            starBox.setCursor(Cursor.HAND);

            starBox.setOnMouseClicked(event -> {
                selectedRating = ratingValue;
                updateStars(starBoxes, stars, selectedRating);
            });

            starBoxes[i] = starBox;
        }

        ratingStars.getChildren().addAll(starBoxes);
        updateStars(starBoxes, stars, selectedRating);

        Label subjectLabel = new Label("Subjek");
        subjectLabel.getStyleClass().add("visit-label");

        TextField subjectField = new TextField();
        subjectField.getStyleClass().add("visit-input");
        subjectField.setPromptText("Topik feedback Anda");

        Label messageLabel = new Label("Pesan (0/500)");
        messageLabel.getStyleClass().add("visit-label");
        this.messageLabelRef = messageLabel;

        TextArea messageArea = new TextArea();
        messageArea.getStyleClass().addAll("visit-input", "visit-message-area");
        messageArea.setPromptText("Tuliskan masukan Anda...");
        messageArea.setWrapText(true);
        messageArea.setPrefRowCount(5);
        messageArea.setMaxWidth(Double.MAX_VALUE);

        messageArea.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > 500) {
                messageArea.setText(oldText);
            } else {
                messageLabelRef.setText("Pesan (" + newText.length() + "/500)");
            }
        });

        Button submitButton = new Button("Kirim Feedback");
        submitButton.getStyleClass().add("visit-submit-button");
        subjectField.setPrefWidth(FEEDBACK_FORM_WIDTH);
        subjectField.setMaxWidth(FEEDBACK_FORM_WIDTH);

        messageArea.setPrefWidth(FEEDBACK_FORM_WIDTH);
        messageArea.setMaxWidth(FEEDBACK_FORM_WIDTH);

        submitButton.setPrefWidth(FEEDBACK_FORM_WIDTH);
        submitButton.setMaxWidth(FEEDBACK_FORM_WIDTH);

        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("visit-status-label");
        statusLabel.setTextAlignment(TextAlignment.CENTER);
        statusLabel.setWrapText(true);
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
        statusLabel.setPrefWidth(FEEDBACK_FORM_WIDTH);
        statusLabel.setMaxWidth(FEEDBACK_FORM_WIDTH);

        submitButton.setOnAction(event ->
                submitFeedback(subjectField, messageArea, statusLabel, starBoxes, stars)
        );

        VBox headerBox = new VBox(8, iconShell, title, subtitle);
        headerBox.setAlignment(Pos.CENTER);

        VBox formFields = new VBox(
            8,
            ratingLabel, ratingStars,
            subjectLabel, subjectField,
            messageLabel, messageArea,
            submitButton,
            statusLabel
        );
        formFields.setAlignment(Pos.CENTER_LEFT);
        formFields.setFillWidth(true);
        formFields.setPrefWidth(FEEDBACK_FORM_WIDTH);
        formFields.setMaxWidth(FEEDBACK_FORM_WIDTH);

        VBox formBox = new VBox(
                8,
            headerBox,
            formFields
        );
        formBox.getStyleClass().add("visit-form-box");
        formBox.setAlignment(Pos.CENTER);
        formBox.setFillWidth(false);
        formBox.setPrefWidth(FEEDBACK_CARD_WIDTH);
        formBox.setMaxWidth(FEEDBACK_CARD_WIDTH);

        Label backLabel = new Label("Kembali");
        backLabel.getStyleClass().add("visit-back-link");
        backLabel.setCursor(Cursor.HAND);
        backLabel.setOnMouseClicked(event -> onBack.run());

        HBox backRow = new HBox(backLabel);
        backRow.setAlignment(Pos.CENTER);
        formBox.getChildren().add(backRow);

        VBox.setMargin(headerBox, new Insets(2, 0, 8, 0));
        VBox.setMargin(backRow, new Insets(8, 0, 0, 0));

        content.getChildren().addAll(
            formBox
        );

        StackPane wrapper = new StackPane(content);
        wrapper.setPadding(new Insets(20, 16, 22, 16));
        return wrapper;
    }

    private void updateStars(StackPane[] starBoxes, Label[] stars, int selectedRating) {
        for (int i = 0; i < stars.length; i++) {
            boolean active = i < selectedRating;

            if (active) {
                starBoxes[i].setStyle(
                        "-fx-background-color: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #F2B233;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-width: 1.2;"
                );

                stars[i].setStyle(
                        "-fx-text-fill: #F4A300;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
                );
            } else {
                starBoxes[i].setStyle(
                        "-fx-background-color: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #E5E7EB;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-width: 1;"
                );

                stars[i].setStyle(
                        "-fx-text-fill: #C9CED6;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
                );
            }
        }
    }

    private void submitFeedback(TextField subjectField,
                                TextArea messageArea,
                                Label statusLabel,
                                StackPane[] starBoxes,
                                Label[] stars) {
        statusLabel.getStyleClass().removeAll("visit-status-success", "visit-status-error");

        try {
            feedbackService.registerFeedback(
                    "",
                    subjectField.getText(),
                    selectedRating,
                    messageArea.getText()
            );

            statusLabel.getStyleClass().add("visit-status-success");
            statusLabel.setText("Feedback berhasil dikirim. Terima kasih atas masukan Anda.");

            subjectField.clear();
            messageArea.clear();
            selectedRating = 0;
            messageLabelRef.setText("Pesan (0/500)");
            updateStars(starBoxes, stars, selectedRating);
        } catch (Exception exception) {
            statusLabel.getStyleClass().add("visit-status-error");
            statusLabel.setText(exception.getMessage());
        }

        statusLabel.setManaged(true);
        statusLabel.setVisible(true);
    }
}