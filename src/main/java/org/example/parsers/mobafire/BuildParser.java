package org.example.parsers.mobafire;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuildParser {
    private final Document doc;

    /**
     * Creates everything to retrieve information from build page
     *
     * @param url String, page url
     */
    public BuildParser(String url) {
        try {
            this.doc = Jsoup.connect(url).get();
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
        for (Element el : items)
            coreItems.add(el.select("span").text());

        return coreItems;
    }

    /**
     * Gets a list of runes
     *
     * @return String[11], list of runes, where
     *                      <br>0 - Main Rune Section title
     *                      <br>1-4 - Main Runes
     *                      <br>5 - Secondary Rune Section title
     *                      <br>6-7 - Secondary Runes
     *                      <br>8-10 - Shard Runes
     */
    public String[] getRunes() {
        String[] runesArr = new String[11];
        Elements runes = doc.select("div.new-runes__item");

        runesArr[0] = Objects.requireNonNull(doc.select("div.new-runes__title").get(0)).text();
        runesArr[1] = runes.get(1).text();
        runesArr[2] = runes.get(2).text();
        runesArr[3] = runes.get(3).text();
        runesArr[4] = runes.get(4).text();
        runesArr[5] = Objects.requireNonNull(doc.select("div.new-runes__title").get(1)).text();
        runesArr[6] = runes.get(6).text();
        runesArr[7] = runes.get(7).text();

        runes =  doc.select("div.new-runes__shards").select("span");
        runesArr[8] = runes.get(0).attr("shard-type");
        runesArr[9] = runes.get(1).attr("shard-type");
        runesArr[10] = runes.get(2).attr("shard-type");

        return runesArr;
    }

    /**
     * Gets summoner spells
     *
     * @return String[2], spells
     */
    public String[] getSpells() {
        String[] spellsArr = new String[2];
        Elements spells = doc.select("div.view-guide__spells__row").select("h4");

        spellsArr[0] = spells.get(0).text();
        spellsArr[1] = spells.get(1).text();

        return spellsArr;
    }

    /**
     * Gets a 2-dimensional List, where each row represents an item list and each column represents an item
     * @return List<List<String>>, List of item variations
     *                      <br>0 - always a title
     *                      <br>between - item names
     *                      <br>the last one - always a description, if none - 0
     */
    public List<List<String>> getItems() {
        List<List<String>> items = new ArrayList<>();
        Elements itemsEl = doc.select("div.view-guide__items");
        Elements temp;

        int i = 0;
        for (Element row : itemsEl) {
            items.add(new ArrayList<>());

            temp = row.select("div.view-guide__items__bar").select("span");
            if (temp.get(0).text().equalsIgnoreCase(""))
                items.get(i).add("No name: ");
            else
                items.get(i).add(temp.get(0).text());


            temp = row.select("div.view-guide__items__content").select("a[href]").select("span");
            for (Element element : temp)
                items.get(i).add(element.text());

            items.get(i).add(row.select("div.view-guide__items__bar").select("span[title]").attr("title"));
            i++;
        }

        return items;
    }
}