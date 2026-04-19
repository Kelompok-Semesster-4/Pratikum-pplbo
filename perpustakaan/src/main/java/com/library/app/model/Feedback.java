package com.library.app.model;

import com.library.app.model.enums.FeedbackStatus;

import java.time.LocalDateTime;

public class Feedback {
    private Long id;
    private Long memberId;
    private String senderName;
    private String subject;
    private Integer rating;
    private String message;
    private FeedbackStatus status;
    private String responseNote;
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;

    public Feedback() {
        this.status = FeedbackStatus.NEW;
        this.rating = 0;
    }

    public Feedback(Long id, Long memberId, String senderName, String message, LocalDateTime createdAt) {
        this(id, memberId, senderName, null, 0, message, FeedbackStatus.NEW, null, createdAt, null);
    }

    public Feedback(Long id,
                    Long memberId,
                    String senderName,
                    String subject,
                    Integer rating,
                    String message,
                    FeedbackStatus status,
                    String responseNote,
                    LocalDateTime createdAt,
                    LocalDateTime respondedAt) {
        this.id = id;
        this.memberId = memberId;
        this.senderName = senderName;
        this.subject = subject;
        this.rating = rating == null ? 0 : rating;
        this.message = message;
        this.status = status == null ? FeedbackStatus.NEW : status;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FeedbackStatus getStatus() {
        return status;
    }

    public void setStatus(FeedbackStatus status) {
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