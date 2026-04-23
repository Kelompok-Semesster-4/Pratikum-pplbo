package com.library.app.ui.panel;

import com.library.app.model.Member;
import com.library.app.service.MemberService;
import com.library.app.service.ProcurementService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class KioskProcurementFxPanel {
    private final MemberService memberService = new MemberService();
    private final ProcurementService procurementService = new ProcurementService();

    private TextField memberCodeField;
    private Label memberNameHint;
    private TextField titleField;
    private TextField authorField;
    private TextField publisherField;
    private TextField publicationYearField;
    private TextField isbnField;
    private TextArea reasonArea;

    private Member selectedMember;

    public Node createContent(Runnable onBack) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(8, 12, 8, 12));

        ScrollPane scrollPane = new ScrollPane(buildFormCard(onBack));
        scrollPane.getStyleClass().add("app-scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        root.setCenter(scrollPane);
        return root;
    }

    private Node buildFormCard(Runnable onBack) {
        VBox wrapper = new VBox(18);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(10, 0, 24, 0));

        VBox card = new VBox(16);
        card.getStyleClass().add("procurement-card");
        card.setMaxWidth(860);
        card.setPrefWidth(860);
        card.setPadding(new Insets(28));

        Node icon = KioskIconFactory.createRequestIcon(Color.web("#7C3AED"));
        StackPane iconShell = new StackPane(icon);
        iconShell.getStyleClass().addAll("visit-icon-shell", "procurement-icon-shell");
        iconShell.setAlignment(Pos.CENTER);
        iconShell.setMinSize(46, 46);
        iconShell.setPrefSize(46, 46);
        iconShell.setMaxSize(46, 46);

        Label title = new Label("Usul Buku");
        title.getStyleClass().addAll("visit-title", "procurement-title");
        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);

        Label subtitle = new Label(
                "Ajukan buku yang ingin tersedia di perpustakaan. Lengkapi data buku agar admin mudah meninjau permintaan.");
        subtitle.setWrapText(true);
        subtitle.getStyleClass().addAll("visit-subtitle", "procurement-subtitle");
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setTextAlignment(TextAlignment.CENTER);
        subtitle.setMaxWidth(560);

        VBox headerBox = new VBox(8, iconShell, title, subtitle);
        headerBox.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.getStyleClass().add("procurement-grid");
        grid.setHgap(18);
        grid.setVgap(14);

        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(50);
        left.setHgrow(Priority.ALWAYS);

        ColumnConstraints right = new ColumnConstraints();
        right.setPercentWidth(50);
        right.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(left, right);

        memberCodeField = createField("Masukkan NIM / NIS / NIDN");
        memberCodeField.textProperty().addListener((obs, oldValue, newValue) -> {
            selectedMember = null;
            memberNameHint.setText("Cocokkan kode anggota untuk mengambil nama anggota.");
            memberNameHint.getStyleClass().setAll("procurement-member-hint");
        });

        memberNameHint = new Label("Cocokkan kode anggota untuk mengambil nama anggota.");
        memberNameHint.getStyleClass().add("procurement-member-hint");

        Button checkMemberButton = new Button("Cek Anggota");
        checkMemberButton.getStyleClass().addAll("visit-submit-button", "procurement-check-button");
        checkMemberButton.setOnAction(event -> lookupMember());

        HBox memberActionRow = new HBox(10, memberCodeField, checkMemberButton);
        HBox.setHgrow(memberCodeField, Priority.ALWAYS);

        VBox memberBlock = new VBox(8,
                createFieldLabel("NIM / NIS / NIDN"),
                memberActionRow,
                memberNameHint);

        titleField = createField("Contoh: Clean Code");
        authorField = createField("Contoh: Robert C. Martin");
        publisherField = createField("Opsional");
        publicationYearField = createField("Opsional");
        isbnField = createField("10 atau 13 digit, opsional");
        reasonArea = createArea("Jelaskan alasan buku ini dibutuhkan");

        grid.add(memberBlock, 0, 0);
        grid.add(buildFieldBox("Judul Buku", titleField), 1, 0);
        grid.add(buildFieldBox("Pengarang", authorField), 0, 1);
        grid.add(buildFieldBox("Penerbit", publisherField), 1, 1);
        grid.add(buildFieldBox("Tahun Terbit", publicationYearField), 0, 2);
        grid.add(buildFieldBox("ISBN", isbnField), 1, 2);

        VBox reasonBox = buildFieldBox("Alasan Permintaan", reasonArea);
        VBox.setVgrow(reasonArea, Priority.ALWAYS);

        Label helper = new Label(
                "Catatan: judul, pengarang, dan alasan permintaan sebaiknya diisi sejelas mungkin agar proses review lebih cepat.");
        helper.setWrapText(true);
        helper.getStyleClass().add("procurement-helper");

        Button submitButton = new Button("Ajukan Usulan");
        submitButton.getStyleClass().addAll("visit-submit-button", "procurement-submit-button");
        submitButton.setOnAction(event -> submit());

        HBox actionRow = new HBox(submitButton);
        actionRow.setAlignment(Pos.CENTER);

        Label backLabel = new Label("Kembali");
        backLabel.getStyleClass().add("visit-back-link");
        backLabel.setCursor(Cursor.HAND);
        backLabel.setOnMouseClicked(event -> onBack.run());

        HBox backRow = new HBox(backLabel);
        backRow.setAlignment(Pos.CENTER);

        card.getChildren().addAll(headerBox, grid, reasonBox, helper, actionRow, backRow);
        wrapper.getChildren().add(card);
        return wrapper;
    }

    private void lookupMember() {
        try {
            selectedMember = memberService.findByCode(memberCodeField.getText());
            memberNameHint.setText("Anggota ditemukan: " + selectedMember.getName());
            memberNameHint.getStyleClass().setAll("procurement-member-hint", "procurement-member-hint-success");
        } catch (IllegalArgumentException exception) {
            selectedMember = null;
            memberNameHint.setText(exception.getMessage());
            memberNameHint.getStyleClass().setAll("procurement-member-hint", "procurement-member-hint-error");
        }
    }

    private void submit() {
        try {
            if (selectedMember == null) {
                lookupMember();
            }

            if (selectedMember == null) {
                throw new IllegalArgumentException("Kode anggota harus valid dan terdaftar.");
            }

            Integer publicationYear = parseYear(publicationYearField.getText());

            procurementService.registerRequest(
                    selectedMember.getId(),
                    selectedMember.getName(),
                    titleField.getText(),
                    authorField.getText(),
                    publisherField.getText(),
                    publicationYear,
                    isbnField.getText(),
                    reasonArea.getText());

            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Usulan buku berhasil dikirim. Admin akan meninjau permintaan Anda.",
                    ButtonType.OK).showAndWait();

            clearForm();
        } catch (IllegalArgumentException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private Integer parseYear(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(rawValue.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Tahun terbit harus berupa angka.");
        }
    }

    private void clearForm() {
        memberCodeField.clear();
        titleField.clear();
        authorField.clear();
        publisherField.clear();
        publicationYearField.clear();
        isbnField.clear();
        reasonArea.clear();

        selectedMember = null;
        memberNameHint.setText("Cocokkan kode anggota untuk mengambil nama anggota.");
        memberNameHint.getStyleClass().setAll("procurement-member-hint");
    }

    private VBox buildFieldBox(String labelText, Node input) {
        VBox box = new VBox(8, createFieldLabel(labelText), input);
        VBox.setVgrow(input, Priority.ALWAYS);
        return box;
    }

    private Label createFieldLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("procurement-field-label");
        return label;
    }

    private TextField createField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.getStyleClass().addAll("visit-input", "procurement-input");
        return field;
    }

    private TextArea createArea(String promptText) {
        TextArea area = new TextArea();
        area.setPromptText(promptText);
        area.setPrefRowCount(6);
        area.setWrapText(true);
        area.getStyleClass().addAll("visit-input", "procurement-input", "procurement-reason-area");
        return area;
    }

}