package com.tutorfinder.model;
import java.io.Serializable;

public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String postId;
    private String parentId;
    private String tutorId;

    public Enrollment(String postId, String parentId, String tutorId) {
        this.postId = postId;
        this.parentId = parentId;
        this.tutorId = tutorId;
    }
    public String getPostId() { return postId; }
    public String getParentId() { return parentId; }
    public String getTutorId() { return tutorId; }
}