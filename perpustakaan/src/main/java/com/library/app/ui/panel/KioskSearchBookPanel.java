package com.library.app.ui.panel;

import java.util.List;

import com.library.app.model.BookCatalogItem;
import com.library.app.service.BookService;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class KioskSearchBookPanel {
    private final BookService bookService = new BookService();

    public Node createContent(Runnable onBack) {
        VBox content = new VBox(10);
        content.getStyleClass().add("visit-content");
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(480);

        Label title = new Label("Pencarian Buku");
        title.getStyleClass().add("visit-title");

        Label subtitle = new Label("Cari berdasarkan judul, pengarang, atau kategori");
        subtitle.getStyleClass().add("visit-subtitle");
        subtitle.setTextAlignment(TextAlignment.CENTER);

        Node searchIcon = KioskIconFactory.createSearchIcon(Color.web("#9ca3af"));
        searchIcon.setScaleX(1.0);
        searchIcon.setScaleY(1.0);

        TextField searchField = new TextField();
        searchField.setPromptText("Ketik judul atau pengarang...");
        searchField.getStyleClass().add("visit-input");
        searchField.setMaxWidth(Double.MAX_VALUE);
        searchField.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-border-color: transparent; " +
            "-fx-focus-color: transparent; " + 
            "-fx-faint-focus-color: transparent; " +
            "-fx-font-size: 15px; " +
            "-fx-prompt-text-fill: #94a3b8;"
        );

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(8, 15, 8, 15));
        searchBox.setMaxWidth(700);
        String defaultStyle = "-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #cbd5e1; -fx-border-radius: 8; -fx-border-width: 1.5;";
        String focusedStyle = "-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #3b82f6; -fx-border-radius: 8; -fx-border-width: 1.5;";
        searchBox.setStyle(defaultStyle);
        searchBox.getChildren().addAll(searchIcon, searchField);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchField.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (isFocused) {
                searchBox.setStyle(focusedStyle);
            } else {
                searchBox.setStyle(defaultStyle);
            }
        });

        Node bigSearchIcon = KioskIconFactory.createSearchIcon(Color.web("#9ca3af"));
        bigSearchIcon.setScaleX(1.5);
        bigSearchIcon.setScaleY(1.5);

        StackPane bigSearchIconShell = new StackPane(bigSearchIcon);
        bigSearchIconShell.getStyleClass().add("search-icon-shell");
        bigSearchIconShell.setAlignment(Pos.CENTER);
        bigSearchIconShell.setMinSize(50, 50);
        bigSearchIconShell.setPrefSize(50, 50);
        bigSearchIconShell.setMaxSize(50, 50);
        StackPane.setAlignment(bigSearchIconShell, Pos.CENTER);

        Label hint = new Label("Ketik minimal 2 karakter untuk mencari");
        hint.getStyleClass().add("visit-subtitle");
        hint.setTextAlignment(TextAlignment.CENTER);

        VBox emptyBox = new VBox(15);
        emptyBox.setAlignment(Pos.CENTER);
        emptyBox.setPadding(new Insets(40, 0, 40, 0));
        emptyBox.getChildren().addAll(bigSearchIconShell, hint);

        VBox resultsContainer = new VBox(15);
        resultsContainer.setPadding(new Insets(10, 5, 20, 5));

        ScrollPane scroller = new ScrollPane(resultsContainer);
        scroller.setFitToWidth(true);
        scroller.getStyleClass().add("search-book-scroller");
        scroller.getStyleClass().add("edge-to-edge");

        VBox bookLists = new VBox(emptyBox);
        bookLists.setAlignment(Pos.CENTER);
        VBox.setVgrow(bookLists, Priority.ALWAYS);

        PauseTransition pause = new PauseTransition(Duration.millis(500));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.setOnFinished(event -> {
                if (newValue == null || newValue.trim().length() < 2) {
                    bookLists.getChildren().setAll(emptyBox);
                    resultsContainer.getChildren().clear();
                    return;
                }

                Task<Void> search = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        List<BookCatalogItem> items = bookService.searchCatalog(newValue);

                        Platform.runLater(() -> {
                            resultsContainer.getChildren().clear();

                            for (BookCatalogItem item : items) {
                                HBox card = createBookCard(
                                    item.getIsbn(), 
                                    item.getTitle(), 
                                    item.getAuthor(), 
                                    item.getPublisher(), 
                                    String.valueOf(item.getPublicationYear()), 
                                    item.getCategory(), 
                                    item.getShelfCode(), 
                                    item.getAvailableCopies());
                                
                                resultsContainer.getChildren().add(card);
                            }

                            bookLists.getChildren().setAll(scroller);
                        });

                        return null;
                    }
                };
                new Thread(search).start();
            });

            pause.playFromStart();
        });
        
        Label back = new Label("Kembali");
        back.getStyleClass().add("visit-back-link");
        back.setCursor(Cursor.HAND);
        back.setOnMouseClicked(event -> onBack.run());
        
        VBox.setMargin(title, new Insets(8, 0, 0, 0));
        VBox.setMargin(subtitle, new Insets(2, 0, 16, 0));
        VBox.setMargin(back, new Insets(18, 0, 0, 0));

        content.getChildren().addAll(
            title,
            subtitle,
            searchBox,
            bookLists,
            back
        );

        StackPane wrapper = new StackPane(content);
        wrapper.setPadding(new Insets(20, 16, 22, 16));
        return wrapper;
    }

    private HBox createBookCard(String isbn,
                                String title,
                                String author,
                                String publisher,
                                String year,
                                String category,
                                String shelf,
                                int availableCopies
                                ) {
        StackPane cover = createCover(isbn);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("book-card-title");

        Label subtitleLabel = new Label(author + " • " + publisher + " • " + year);
        subtitleLabel.getStyleClass().add("book-card-subttile");

        VBox details = new VBox(8);

        Label categoryPill = new Label(category);
        categoryPill.getStyleClass().add("book-card-category");

        Label shelfLabel = new Label(shelf);
        shelfLabel.getStyleClass().add("book-card-shelf");

        Label availableCopiesLabel = new Label(availableCopies + " tersedia");
        availableCopiesLabel.getStyleClass().add("book-card-copies");
        String color = availableCopies > 0 ? "#10b981" : "#ef4444";
        availableCopiesLabel.setStyle("-fx-text-fill: " + color);

        HBox infoRow = new HBox(15);
        infoRow.setAlignment(Pos.CENTER_LEFT);

        infoRow.getChildren().addAll(categoryPill, shelfLabel, availableCopiesLabel);
        details.getChildren().addAll(titleLabel, subtitleLabel, infoRow);

        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("search-book-card");
        card.getChildren().addAll(cover, details);
        return card;
    }

    private StackPane createCover(String isbn) {
        StackPane coverArea = new StackPane();
        coverArea.setMinSize(60, 80);
        coverArea.setPrefSize(60, 80);
        coverArea.getStyleClass().add("book-card-cover");

        Node fallbackIcon = KioskIconFactory.createBookIcon(Color.web("#3b82f6"));
        coverArea.getChildren().add(fallbackIcon);

        if (isbn != null && !isbn.trim().isEmpty()) {
            String url = "https://covers.openlibrary.org/b/isbn/" + isbn + "-M.jpg?default=false";
            Image coverImage = new Image(url, true);

            coverImage.progressProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.doubleValue() == 1.0 && !coverImage.isError()) {
                    Platform.runLater(() -> {
                        ImageView img = new ImageView(coverImage);
                        img.setFitWidth(60);
                        img.setFitHeight(80);
                        img.setPreserveRatio(true);

                        coverArea.getChildren().clear();
                        coverArea.getChildren().add(img);
                    });
                }
            });
        }

        return coverArea;
    }
}
