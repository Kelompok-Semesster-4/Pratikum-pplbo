package com.library.app.model;

import com.library.app.model.enums.RequestStatus;

import java.time.LocalDateTime;

public class ProcurementRequest {
    private Long id;
    private Long memberId;
    private String requesterName;
    private String title;
    private String author;
    private String publisher;
    private Integer publicationYear;
    private String isbn;
    private String note;
    private RequestStatus status;
    private String responseNote;
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;

    public ProcurementRequest() {
    }

    public ProcurementRequest(Long id,
                              Long memberId,
                              String requesterName,
                              String title,
                              String author,
                              String note,
                              RequestStatus status,
                              String responseNote,
                              LocalDateTime createdAt,
                              LocalDateTime respondedAt) {
        this(id, memberId, requesterName, title, author, null, null, null, note, status, responseNote, createdAt,
                respondedAt);
    }

    public ProcurementRequest(Long id,
                              Long memberId,
                              String requesterName,
                              String title,
                              String author,
                              String publisher,
                              Integer publicationYear,
                              String isbn,
                              String note,
                              RequestStatus status,
                              String responseNote,
                              LocalDateTime createdAt,
                              LocalDateTime respondedAt) {
        this.id = id;
        this.memberId = memberId;
        this.requesterName = requesterName;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.note = note;
        this.status = status;
        this.responseNote = responseNote;
        this.createdAt = createdAt;
        this.respondedAt = respondedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getResponseNote() {
        return responseNote;
    }

    public void setResponseNote(String responseNote) {
        this.responseNote = responseNote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }
}