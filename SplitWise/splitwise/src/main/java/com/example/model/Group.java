package com.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Group {
    String id;
    private List<User> users;
    List<SplitDetail> splitDetails;
    Map<User, Map<User, Double> > splitCache;

    public Group(List<User> users) {
        this.id = UUID.randomUUID().toString();
        this.users = users;
        this.splitCache = new HashMap<>();
        this.splitDetails = new ArrayList<>();
        users.forEach(u -> initUser(u));
        users.forEach(u -> u.addgroup(this.id));
    }

    public User addUser(User user) {
        users.add(user);
        initUser(user);
        return users.stream().filter(u -> u==user).findFirst().get();
    }

    public User removeuser(User user) {
        // removeUserSplitDetails();
        users.remove(user);
        return user;
    }

    public SplitDetail updateSplitDetails(SplitDetail splitDetail) {
        splitDetails.add(splitDetail);

        updateSplitCache(splitDetail);
        notifyBorrowers(splitDetail.getBorrowers(), splitDetail.moneyTobeSplited);

        return splitDetails.stream().filter(
            sd -> sd==splitDetail
        ).findFirst().get();
    }

    private void notifyBorrowers(List<User> borrowers, double cost) {
        borrowers.stream()
        .forEach(user -> System.out.println("notified user "+ user.getPhoneNumber() + " for cost " + cost));

    }


    private void updateSplitCache(SplitDetail splitDetail) {
        double share = splitDetail.getMoneyTobeSplited()/splitDetail.getBorrowers().size();
        User lender = splitDetail.getLender();
        splitDetail.getBorrowers().stream().filter(borrower -> borrower!=lender).forEach(
            borrower -> splitCache.get(lender).put(
                borrower, 
                splitCache.get(lender).get(borrower)+share
                )
            );


        splitDetail.getBorrowers().stream().filter(borrower -> borrower!=lender).forEach(
            borrower -> splitCache.get(borrower).put(
                lender, 
                splitCache.get(borrower).get(lender)-share
                )
            );
        
            printUpdatedSplitCache();
    }

    private void initUser(User user) {
        Map<User, Double> details = new HashMap<>();
        if(!splitCache.containsKey(user)) {
            this.users.stream().filter(u -> user!=u).forEach(u -> details.put(u,0.0));
            splitCache.put(user, details);
        }

    }

    @Override
    public String toString() {
        this.users.stream().forEach(u -> System.out.println(u.toString()));
        return "Printed Users of the group " + this.id;
    }

    private void printUpdatedSplitCache() {
        System.out.println(this.splitCache.entrySet().stream().map(
            entry -> "key: " + entry.getKey().getPhoneNumber() + " Value:{ " + 
            entry.getValue().entrySet().stream().map(childentry -> "Key: "+ childentry.getKey().getPhoneNumber() + " Value: "+ childentry.getValue().toString()).collect(Collectors.joining(" , ")) + "}"
        ).collect(Collectors.joining(", \n")));
    }
}
