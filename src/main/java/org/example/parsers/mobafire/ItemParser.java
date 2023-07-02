package org.example.parsers.mobafire;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

public class ItemParser {
    private static final String urlItems = "https://www.mobafire.com/league-of-legends/items";
    private static final String urlRunes = "https://www.mobafire.com/league-of-legends/reforged-runes";
    private static final String baseUrl = "https://www.mobafire.com";
    private final Document docItems;
    private final Document docRunes;

    public ItemParser() throws IOException {
        try {
            this.docItems = Jsoup.connect(urlItems).get();
        } catch (IOException e) {
            throw new IOException("Something went wrong accessing the site");
        }

        try {
            this.docRunes = Jsoup.connect(urlRunes).get();
        } catch (IOException e) {
            throw new IOException("Something went wrong accessing the sites");
        }
    }

    public String[][] getItemInformation(String[] items) {
        String[][] itemInformation = new String[items.length][2];
        Elements itemsEl = this.docItems.select("div.item-list--list").select("a[href]");

        for (int i = 0; i < items.length; i++) {
            for (Element el : itemsEl) {
                if (Objects.requireNonNull(Objects.requireNonNull(el.select("span").first()).firstChild()).toString().equalsIgnoreCase(items[i])) {
                    itemInformation[i][0] = baseUrl + el.select("img").get(1).attr("src");
                    itemInformation[i][1] = baseUrl + el.attr("src");
                }
            }
        }

        return itemInformation;
    }

    public String[][] getRuneInformation(String[] runes) {
        String[][] runeInformation = new String[runes.length - 3][2];
        Elements itemsEl = this.docRunes.select("div.rune-list  ").select("a[href]");

        for (int i = 0; i < runes.length - 3; i++) {
            for (Element el : itemsEl) {
                if (Objects.requireNonNull(Objects.requireNonNull(el.select("span.rune-list__item__info").first()).firstChild()).toString().equalsIgnoreCase(" " + runes[i] + " ")) {
                    runeInformation[i][0] = baseUrl + el.select("span.rune-list__item__pic").select("img").attr("src");
                    runeInformation[i][1] = baseUrl + el.attr("src");
                }
            }
        }
        return runeInformation;
    }
}
