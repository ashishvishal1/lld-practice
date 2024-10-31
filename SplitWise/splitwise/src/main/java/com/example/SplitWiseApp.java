package com.example;

import java.util.List;

import com.example.model.Group;
import com.example.model.SplitDetail;
import com.example.model.User;

/**
 * Hello world!
 *
 */
public class SplitWiseApp{
    public static void main( String[] args )
    {
        System.out.println( "Starting Splitwise Application" );

        User user1 = new User.UserBuilder().setPhoneNumber("1").build();
        User user2 = new User.UserBuilder().setPhoneNumber("2").build();
        User user3 = new User.UserBuilder().setPhoneNumber("3").build();
        User user4 = new User.UserBuilder().setPhoneNumber("4").build();
        // User user5 = new User.UserBuilder().setPhoneNumber("5").build();

        Group group1 = new Group(List.of(user1, user2, user3, user4));

        System.out.println(
            group1.toString()
        );

        SplitDetail splitDetail1 = new SplitDetail(user1, user1, List.of(user1, user2, user3), 30.0);
        SplitDetail splitDetail2 = new SplitDetail(user3, user1, List.of(user1, user2, user3), 30.0);

        System.out.println(splitDetail1.toString());



        group1.updateSplitDetails(splitDetail1);
        group1.updateSplitDetails(splitDetail2);

        
    }
}
