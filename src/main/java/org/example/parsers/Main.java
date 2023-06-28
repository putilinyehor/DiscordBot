package org.example.parsers;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        MobafireWebsiteParser mwp = null;
        try {
            mwp = new MobafireWebsiteParser("zyra");
        } catch (IllegalArgumentException e) {
            System.out.println("Champion doesn't exist, pls enter again");
        }

        String[][] builds = Objects.requireNonNull(mwp).getChampionBuildsInfo(5);
        if (builds == null) {
            System.out.println("An error occurred while trying to retrieve builds. Bot cannot display more than 10 builds, please, use a lower number. and try again.");
            System.exit(-1);
        }

        for (String[] str : builds) {
            if (mwp.isEmpty(str))
                continue;
            System.out.println("Rating:" + str[0]);
            System.out.println("Title:" + str[1]);
            System.out.println("Link:" + str[2]);
            System.out.println("Image:" + str[3]);
        }
    }
}
