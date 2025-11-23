package com.infosys.seatsync.model;

import java.util.Objects;

public class EmailDetails {

	private String recipient;
	private String messageBody;
	private String subject;
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@Override
	public int hashCode() {
		return Objects.hash(messageBody, recipient, subject);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailDetails other = (EmailDetails) obj;
		return Objects.equals(messageBody, other.messageBody) && Objects.equals(recipient, other.recipient)
				&& Objects.equals(subject, other.subject);
	}
	@Override
	public String toString() {
		return "EmailDetails [recipient=" + recipient + ", messageBody=" + messageBody + ", subject=" + subject + "]";
	}
	
      
}
