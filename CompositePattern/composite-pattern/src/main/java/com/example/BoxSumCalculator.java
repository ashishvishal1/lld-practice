package com.example;

import com.example.model.Product;
import com.example.model.Item;
import com.example.model.Box;
import java.util.List;
/**
 * Hello world!
 *
 */
public class BoxSumCalculator 
{
    public static void main( String[] args ) {
        Product item1 = new Item(10L, "item-a");
        Product item2 = new Item(20L, "item-b");
        Product item3 = new Item(30L, "item-c");
        Product item4 = new Item(40L, "item-d");
        Product item5 = new Item(50L, "item-e");
        Product item6 = new Item(60L, "item-f");
        Product item7 = new Item(70L, "item-g");
        Product item8 = new Item(80L, "item-h");

        Product box1 = new Box(List.of(item1, item2));
        Product box2 = new Box(List.of(item3, item4));
        Product box3 = new Box(List.of(box1, box2, item5, item6));
        Product box4 = new Box(List.of(box3, item7, item8));


        System.out.println("box1 Sum: " + box1.calculateSum());
        System.out.println("box2 Sum: " + box2.calculateSum());
        System.out.println("box3 Sum: " + box3.calculateSum());
        System.out.println("box4 Sum: " + box4.calculateSum());
    }
}
