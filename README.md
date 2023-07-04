Discord bot example. 
<br>__Check out /help command to see commands available__
<br>What this bot can: 
* search youtube videos
* display Champion Builds from the game League of Legends.
_NOTE_
This bot is intended to be used only on one server from a working machine. Therefore it saves the chat, where all the commands should be ran, to optimize the working process. 

Recources used:
<br>https://developers.google.com/youtube/v3
<br>https://github.com/discord-jda/JDA
<br>https://jda.wiki/introduction/jda/
<br>https://jsoup.org

Maven Dependencies used:
```
<dependency>
            <groupId>org.yaml</groupId>
            <artifactId>snakeyaml</artifactId>
            <version>2.0</version>
</dependency>

<dependency>
            <groupId>net.dv8tion</groupId>
            <artifactId>JDA</artifactId>
            <version>5.0.0-beta.10</version>
</dependency>

<dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.7</version>
        </dependency>

 <dependency>
            <groupId>com.google.apis</groupId>
            <artifactId>google-api-services-youtube</artifactId>
            <version>v3-rev20230521-2.0.0</version>
</dependency>

<dependency>
            <groupId>com.google.api-client</groupId>
            <artifactId>google-api-client-jackson2</artifactId>
            <version>1.20.0</version>
</dependency>

<dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.16.1</version>
</dependency>
```
