package com.services.exception;

public class ExceptionResponse {
  private String message;
  private String status;

  public ExceptionResponse(String message, String status) {
    super();
    this.setMessage(message);
    this.setStatus(status);
  }

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}


 

}