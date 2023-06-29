package org.example.parsers;

import org.example.parsers.mobafire.BuildParser;
import org.example.parsers.mobafire.BuildsParser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        BuildsParser mwp = null;
        try {
            mwp = new BuildsParser("yone");
        } catch (IllegalArgumentException e) {
            System.out.println("Champion doesn't exist, pls enter again");
        }

        String[][] builds = Objects.requireNonNull(mwp).getChampionBuildsInformation(5);
        if (builds == null) {
            System.out.println("An error occurred while trying to retrieve builds. Bot cannot display more than 10 builds, please, use a lower number. and try again.");
            System.exit(-1);
        }

        BuildParser pb = new BuildParser("https://www.mobafire.com/league-of-legends/build/s13-surfs-up-master-taric-support-guide-498036");
        List<String> coreItems = pb.getCoreItems();
        String[] runes = pb.getRunes();
        String[] spells = pb.getSpells();
        List<List<String>> items = pb.getItems();

        System.out.println(coreItems);
        System.out.println(Arrays.toString(runes));
        System.out.println(Arrays.toString(spells));
        for (List<String> row : items) {
            System.out.println(row);
        }
    }
}
