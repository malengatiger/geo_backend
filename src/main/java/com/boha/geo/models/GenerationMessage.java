package com.boha.geo.models;

import org.springframework.data.annotation.Id;

public class GenerationMessage implements  Comparable<GenerationMessage>{
    String type, message;
    @Id
    private String id;
    int count;
    double elapsedSeconds;

    public GenerationMessage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(double elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(GenerationMessage generationMessage) {
        return this.message.compareToIgnoreCase(generationMessage.getMessage());
    }
}
