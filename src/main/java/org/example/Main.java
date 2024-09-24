package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Transformations.mainTransformations();
        OrdenationsByDate.mainOrdenationsByDate();
        OrdenationsByPrice.mainOrdenationsByPrice();
        OrdenationsByAchievements.mainOrdenationsByAchievements();

    }
}