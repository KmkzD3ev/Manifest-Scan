package com.example.emissormdfe.ApiService;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "response", strict = false)
public class ServerResponse {
    @Element(name = "status", required = false)
    private String status;

    @Element(name = "code", required = false)
    private String code;

    @Element(name = "message", required = false)
    private String message;

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
