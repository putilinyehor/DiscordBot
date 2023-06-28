package org.example.parsers;

import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        MobafireWebsiteParser mwp = null;
        try {
            mwp = new MobafireWebsiteParser("yone");
        } catch (IllegalArgumentException e) {
            System.out.println("Champion doesn't exist, pls enter again");
        }

        String[][] builds = Objects.requireNonNull(mwp).getChampionBuildsInformation(5);
        if (builds == null) {
            System.out.println("An error occurred while trying to retrieve builds. Bot cannot display more than 10 builds, please, use a lower number. and try again.");
            System.exit(-1);
        }

        MobafireWebsiteParser.BuildParser pb = new MobafireWebsiteParser.BuildParser("https://www.mobafire.com/league-of-legends/build/yone-builds-handygamer-13-11-614460");
        List<String> str = pb.getCoreItems();
        System.out.println(str);

//        for (String[] str : builds) {
//            if (mwp.isEmpty(str))
//                continue;
//            System.out.println("Rating:" + str[0]);
//            System.out.println("Title:" + str[1]);
//            System.out.println("Link:" + str[2]);
//            System.out.println("Image:" + str[3]);
//        }
    }
}
