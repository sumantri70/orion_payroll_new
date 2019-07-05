package com.orionit.app.orion_payroll_new.models;

public class KirimEmailModel {
    private String email;
    private String subject;
    private String message;
    private String File;

    public KirimEmailModel(String email, String subject, String message, String file) {
        this.email = email;
        this.subject = subject;
        this.message = message;
        File = file;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFile() {
        return File;
    }

    public void setFile(String file) {
        File = file;
    }
}
