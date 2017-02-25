package com.example;

public class CredentialMessage {

	public String mobNo;
	public Integer destinationId;
	
	public CredentialMessage(String mobNo,Integer destinationId){
		this.mobNo = mobNo;
		this.destinationId = destinationId;
	}
	
	public CredentialMessage() {
		// TODO Auto-generated constructor stub
	}

	public String getMobNo() {
		return mobNo;
	}
	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}
	public Integer getDestinationId() {
		return destinationId;
	}
	public void setDestinationId(Integer destinationId) {
		this.destinationId = destinationId;
	}
}
