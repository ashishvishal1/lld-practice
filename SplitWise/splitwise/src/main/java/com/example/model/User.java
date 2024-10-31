package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String countryCode;
    private List<String> groupIds;

    private User(UserBuilder userBuilder) {
        firstName = userBuilder.firstName;
        lastName = userBuilder.lastName;
        phoneNumber = userBuilder.phoneNumber;
        countryCode = userBuilder.countryCode;
        this.groupIds = new ArrayList<>();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public List<String> getGroupIds() {
        return groupIds;
    }

    public void addgroup(String groupId) {
        groupIds.add(groupId);
    }

    @Override
    public String toString() {
        return "Firstname "+ this.firstName
            +" Lastname "+ this.lastName
            +" CountryCode "+ this.countryCode
            + " PhoneNumber " + this.phoneNumber;
    }


    public static class UserBuilder {
        String firstName;
        String lastName;
        String phoneNumber;
        String countryCode;      

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserBuilder setCountryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        @Override
        public String toString() {
            return "Firstname "+ this.firstName
                +" Lastname "+ this.lastName
                +" CountryCode "+ this.countryCode
                + " PhoneNumber " + this.phoneNumber;
        }

        public User build() {
            System.out.println(
                "creating User "+this.toString()
            );
            return new User(this);
        }

    }

    
}
