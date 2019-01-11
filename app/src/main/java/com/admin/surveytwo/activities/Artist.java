package com.admin.surveytwo.activities;

public class Artist {

    String artistId;
    String artistName;
    String artistTime;

    public Artist(){

    }

    public Artist(String artistId, String artistName, String artistTime) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistTime = artistTime;



    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistTime() {
        return artistTime;
    }
}
