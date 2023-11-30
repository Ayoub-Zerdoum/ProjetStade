/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author Ayoub Zerdoum
 */
public class Concert extends Evenement{
    private Set<String> artists;
    private String posterImagePath;

    public Concert(int eventId, String eventName, String eventDate, Map<Integer, Double> sectionPrices,String description, Set<String> artists, String posterImagePath) {
        super(eventId, eventName, eventDate, sectionPrices, description);
        this.artists = new HashSet<String>(artists);
        this.posterImagePath = posterImagePath;
    }

    public Set<String> getArtists() {return artists;}
    public String getPosterImagePath() {return posterImagePath;}
    
    public void setPosterImagePath(String posterImagePath) {this.posterImagePath = posterImagePath;}

    public void addArtist(String artist) {artists.add(artist);}
    public void clearArtists() {artists.clear();}

}
