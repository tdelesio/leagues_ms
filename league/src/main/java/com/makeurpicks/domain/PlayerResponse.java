package com.makeurpicks.domain;

public class PlayerResponse extends AbstractModel {


	private String email;
	private String firstName;
	private String lastName;
	
	public PlayerResponse()
	{
		
	}
	
	public PlayerResponse(String id)
	{
		super.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "PlayerResponse [email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", id=" + id
				+ "]";
	}
	
	
}
