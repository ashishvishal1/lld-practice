package com.example.model;

import java.util.List;
import java.util.UUID;

public class User implements Comparable<User> {
    private String id;
    private String firstName;
    private String lastName;
    private String countryCode;
    private Integer phoneNumber;
    private Integer age;
    private String email;
    private Address address;
    private List<Library> subscribedLibraries;
    private UserType userType;

    private User(UserBuilder userBuilder) {
        this.id = UUID.randomUUID().toString();
        this.firstName = userBuilder.firstName;
        this.lastName = userBuilder.lastName;
        this.countryCode = userBuilder.countryCode;
        this.phoneNumber = userBuilder.phoneNumber;
        this.age = userBuilder.age;
        this.email = userBuilder.email;
        this.address = userBuilder.address;
        this.userType = userBuilder.userType;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public List<Library> getSubscribedLibraries() {
        return subscribedLibraries;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public String toString() {
        return "id: "+ this.id + " firstname: " + this.firstName;
    }

    @Override
    public int compareTo(User user) {
        return this.email.compareTo(user.getEmail());
    }

    // public int compareTo(User user) {
    //     return this.email.compareTo(user.email);
    // }


    public static class UserBuilder {
        String firstName;
        String lastName;
        String countryCode;
        Integer phoneNumber;
        Integer age;
        String email;
        Address address;
        UserType userType;

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setCountryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public UserBuilder setPhoneNumber(Integer phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserBuilder setAge(Integer age) {
            this.age = age;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setUserType(UserType userType) {
            this.userType = userType;
            return this;
        }

        public UserBuilder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public User build() {
            return new User(this);
        }
        
    }
    
}
