package com.tutorfinder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JobPost implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id, parentId, subject;
    private double feePerSession;
    private int minutes;
    private List<String> registeredTutorIds;

    public JobPost(String id, String parentId, String subject, double fee, int minutes) {
        this.id = id; this.parentId = parentId; this.subject = subject;
        this.feePerSession = fee; this.minutes = minutes;
        this.registeredTutorIds = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getParentId() { return parentId; }
    public String getSubject() { return subject; }
    public double getFeePerSession() { return feePerSession; }
    public int getMinutes() { return minutes; }
    public List<String> getRegisteredTutorIds() { return registeredTutorIds; }
}