package main;

public class Person {
	Integer id, age;
	String firstName, lastName, street, city, state;
	
	public Person(Integer id, String firstName, String lastName, Integer age, String street, String city, String state) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.street = street;
		this.city = city;
		this.state = state;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", age=" + age +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", street='" + street + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				'}';
	}
}
