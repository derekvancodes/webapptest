package com.proquest.interview.phonebook;

public class Person {
	private String name;
	private String phoneNumber;
	private String address;

	public Person(String name, String phoneNumber, String address) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		Person person = (Person) o;

		return new org.apache.commons.lang3.builder.EqualsBuilder()
			.append(name, person.name)
			.append(phoneNumber, person.phoneNumber)
			.append(address, person.address)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
			.append(name)
			.append(phoneNumber)
			.append(address)
			.toHashCode();
	}
}
