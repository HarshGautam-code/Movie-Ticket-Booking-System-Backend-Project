package com.bms.bms_backend.services;

import com.bms.bms_backend.models.Artist;
import com.bms.bms_backend.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistService {

    ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> fetchAllArtists(List<String> artistsNames) {

       List<Artist> artists = new ArrayList<>();

       for(int i = 0; i < artistsNames.size(); i++){
           String artistName = artistsNames.get(i);

           // We want to check that is this artistName present in the artist table or not
           Artist artist = artistRepository.findByName(artistName);

           // if not present put in the artist table
           if(artist == null){
                artist = new Artist();
                artist.setName(artistName);
                artist = artistRepository.save(artist);
           }

           artists.add(artist);

       }
       return artists;
    }


    public Artist fetchArtistByName(String name) {

        Artist artist = artistRepository.findByName(name);
        if(artist == null){
            artist = new Artist();
            artist.setName(name);
            artist = artistRepository.save(artist);
        }
        return artist;
    }
}
