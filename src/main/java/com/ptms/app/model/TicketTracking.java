package com.ptms.app.model;

import java.time.LocalDateTime;

public class TicketTracking {

    private int id;
    private int ticketId;
    private String status;
    private int progress;
    private String comment;
    private int updatedBy;
    private LocalDateTime updatedAt;

    public TicketTracking() {
    }

    public TicketTracking(
            int ticketId,
            String status,
            int progress,
            String comment,
            int updatedBy) {

        this.ticketId = ticketId;
        this.status = status;
        this.progress = progress;
        this.comment = comment;
        this.updatedBy = updatedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "TicketTracking{" +
                "id=" + id +
                ", ticketId=" + ticketId +
                ", status='" + status + '\'' +
                ", progress=" + progress +
                ", comment='" + comment + '\'' +
                ", updatedBy=" + updatedBy +
                ", updatedAt=" + updatedAt +
                '}';
    }
}