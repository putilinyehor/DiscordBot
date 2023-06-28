package org.example.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MobafireWebsiteParser {
    private static final String baseSearchUrl = "https://www.mobafire.com/league-of-legends/champion/";
    private static final String baseUrl = "https://www.mobafire.com";
    private final String championUrl;

    /**
     * Creates an object to access champion builds if the champion name is correct
     *
     * @param champion String, name of the champion to search for
     *
     * @throws IllegalArgumentException if a non-existing champion was passed
     */
    public MobafireWebsiteParser(String champion) {
        if (getChampionUrl(champion).equalsIgnoreCase(""))
            throw new IllegalArgumentException("You entered a wrong champion name");

        this.championUrl = baseSearchUrl + champion;
    }

    /**
     * Returns true if the elements in
     *
     * @param build String, build details to check for
     * @return boolean, true if build is null
     */
    public static boolean isEmpty(String[] build) {
        return build[2] == null;
    }

    /**
     * Gets url to champion build to check, if the champion exists
     *
     * @param champion String, champion name
     * @return String, champion link
     */
    private String getChampionUrl(String champion) {
        champion = champion.replace("'","")
                .toLowerCase();

        Document doc;
        try {
            doc = Jsoup.connect(baseSearchUrl + champion).get();
        } catch (IOException e) {
            return "";
        }

        Element el = doc.select("div.champ__splash__title").first();
        if (el == null)
            return "";

        return baseSearchUrl + champion;
    }

    // ---------------------------------------------------------------------------------------------
    // Functions to display build variations

    /**
     * Returns an array with champion builds details
     *
     * @param numberOfBuilds int, number of builds to display
     * @return String[][], if less than given amount - return empty
     *                      0 - title
     *                      1 - link
     *                      2 - img
     *                      3 - rating
     *                      4 - likes
     *                      5 - dislikes
     */
    public String[][] getChampionBuildsInformation(int numberOfBuilds) {
        Document doc;
        try {
            doc = Jsoup.connect(this.championUrl).get();
        } catch (IOException e) {
            return null;
        }

        String[][] buildDescriptionList = new String[numberOfBuilds][6];

        // Get all builds
        Element listings = doc.select("div.mf-listings").first();
        Elements builds = Objects.requireNonNull(listings).select("a[href]");

        int i = 0;
        for (Element build : builds) {
            Element ratingCircle = build.select("div.mf-listings__item__rating__circle__inner").first();
            if (Objects.requireNonNull(ratingCircle).select("span").first() == null)
                continue;

            String rating = Objects.requireNonNull(ratingCircle.select("span").first()).text();

            // Show items that have rating more than given
            if (Float.parseFloat(rating) > 8.5) {
                String[] details = getBuildDetails(build);
                buildDescriptionList[i][0] = details[0];        // title
                buildDescriptionList[i][1] = details[1];        // link
                buildDescriptionList[i][2] = details[2];        // img
                buildDescriptionList[i][3] = rating;            // rating
                buildDescriptionList[i][4] = details[3];        // likes
                buildDescriptionList[i][5] = details[4];        // dislikes
            }

            i++;
            if (i >= numberOfBuilds)
                break;
        }

        return buildDescriptionList;
    }

    /**
     * Gets build details that will be displayed to user
     * @param build Element, core element to parse
     * @return String[] - list of elements to be displayed
     */
    private String[] getBuildDetails(Element build) {
        String[] buildDetails = new String[5];

        // Add position if present
        String positionImage = build.select("h3").select("img").attr("data-original");
        positionImage = getMapPosition(positionImage);
        buildDetails[0] = positionImage + build.select("h3") // title
                .text();

        buildDetails[1] = baseUrl + build.select("a[href]") // link
                .attr("href");

        buildDetails[2] = baseUrl + build.select("img").attr("data-original"); // image

        String votes = build.select("div.mf-listings__item__rating__info").text();

        buildDetails[3] = votes.substring(0, votes.indexOf("V")); // likes
        buildDetails[4] = votes.substring(votes.indexOf("s") + 2); // dislikes

        return buildDetails;
    }

    /**
     * Gets map position according to IMG data-original value, class mf-listings__item__info__title
     * @param positionImage String, original image to parse
     * @return String, build's position
     */
    private String getMapPosition(String positionImage) {
        switch (positionImage) {
            case "" -> {
                return "No position specified: ";
            }
            case "/images/lanes/top.png" -> {
                return "TOP: ";
            }
            case "/images/lanes/jungle.png" -> {
                return "JG: ";
            }
            case "/images/lanes/middle.png" -> {
                return "MID: ";
            }
            case "/images/lanes/bottom.png" -> {
                return "ADC: ";
            }
            case "/images/lanes/support.png" -> {
                return "SUP: ";
            }
        }
        return "";
    }

    public static class BuildParser {
        private final String url;
        private final Document doc;

        /**
         * Creates everything to retrieve information from build page
         *
         * @param url String, page url
         */
        public BuildParser(String url) {
            this.url = url;
            try {
                this.doc = Jsoup.connect(this.url).get();
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong accessing the site");
            }
        }

        /**
         * Gets core items that are listed as a List
         *
         * @return coreItems List<String>, returns a List with items
         */
        public List<String> getCoreItems() {
            List<String> coreItems = new ArrayList<>();

            Elements items = this.doc.select("div.view-guide__build__core__inner").select("a");
            for (Element el : items) {
                System.out.println(getItemName(el.attr("href")));
            }

            return coreItems;
        }

        private String getItemName(String url) {
            Document docItem;
            try {
                docItem = Jsoup.connect(baseUrl + url).get();
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong accessing the site");
            }
            return Objects.requireNonNull(docItem.select("div.mf-responsive__leftCol").select("span").first()).text();
            // TODO: get items from file, not from website and compare time spent
        }
    }
}
