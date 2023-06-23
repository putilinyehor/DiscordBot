package org.example.youtubeapi;

import java.util.*;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;

public class YoutubeAPI {
    private String apiKey = "";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static YouTube youtube;

    /**
     * Constructor, initializes a YouTube instance
     * @param apiKey String, YouTube Data V3 Api Key
     * @throws IOException if there was an error, creating YouTube instance
     */
    public YoutubeAPI(String apiKey) throws IOException {
        this.apiKey = apiKey;
        youtube = new YouTube.Builder(YoutubeAPI.HTTP_TRANSPORT, YoutubeAPI.JSON_FACTORY, request -> {
        }).setApplicationName("youtube-cmdline-search-sample").build();
    }

    /**
     * Get results of a youtube-search
     * @param queryTerm String, user search input
     * @param NUMBER_OF_VIDEOS_RETURNED long, number of videos user wants to display
     * @return String, 2-dimensional array, [numberOfVideosReturned][2]
     *                  arr[i][0] - title of a video
     *                  arr[i][1] - link to a video
     */
    public String[][] getSearchResult(String queryTerm, long NUMBER_OF_VIDEOS_RETURNED) {
        try {
            YouTube.Search.List search = youtube.search().list(Collections.singletonList("id,snippet"));
            search.setKey(apiKey);
            search.setQ(queryTerm);
            // Search videos only
            search.setType(Collections.singletonList("video"));
            // To increase efficiency, only retrieve the fields that the application uses
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                 return getVideoItems(searchResultList.iterator(), (int) NUMBER_OF_VIDEOS_RETURNED);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Parse results of a youtube-search into an array
     * @param iteratorSearchResults List<SearchResult>, list of youtube-videos to be parsed to String[][] array
     * @param numberOfVideosReturned int, number of videos int the list
     * @return String, 2-dimensional array, [numberOfVideosReturned][2]
     *                  arr[i][0] - title of a video
     *                  arr[i][1] - link to a video
     *                  arr[i][2] - thumbnail url
     */
    private static String[][] getVideoItems(Iterator<SearchResult> iteratorSearchResults, int numberOfVideosReturned) {
        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
            return null;
        }

        String[][] videoItemsArray = new String[numberOfVideosReturned][3];

        int i = 0;
        while (iteratorSearchResults.hasNext()) {
            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();
            String link = getYoutubeLink(rId.getVideoId());

            String title = singleVideo.getSnippet().getTitle();
            // Fix to discord showing Unicode Values instead of symbols
            title = title
                    .replace("&#39;", "'")
                    .replace("&amp;", "&")
                    .replace("&quot;", "\"");

            Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
            String thumbnailUrl = thumbnail.getUrl();


            // Confirm that the result represents a video. Otherwise, the item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                videoItemsArray[i][0] = title;
                videoItemsArray[i][1] = link;
                videoItemsArray[i][2] = thumbnailUrl;
            }
            i++;
        }
        return videoItemsArray;
    }

//    public static String getSearchResultAsString(String[][] videos) {
//        StringBuilder str = new StringBuilder("Search results: \n");
//
//        for (int i = 0; i < videos.length; i++) {
//            str.append(i + 1)
//                    .append(": ")
//                    .append(videos[i][1])
//                    .append("\n")
//                    .append(videos[i][0])
//                    .append("\n")
//                    .append(videos[i][2])
//                    .append("\n");
//        }
//
//        return str.toString();
//    }

    /**
     * Creates a full YouTube link from id
     * @param id String, id of a YouTube video
     * @return String, final link
     */
    private static String getYoutubeLink(String id) {
        return "https://www.youtube.com/watch?v=" + id;
    }
}


