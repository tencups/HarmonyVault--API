package com.example.HarmonyVault.resource;

import com.example.HarmonyVault.domain.Artist;
import com.example.HarmonyVault.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.example.HarmonyVault.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;



@RestController // class handles HTTP requests
@RequestMapping("/artists") // base URL
@RequiredArgsConstructor // @RequiredArgsConstructor
public class ArtistResource {
    private final ArtistService artistService;

    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        return ResponseEntity.created(URI.create("/artists/userID")).body(artistService.createArtist(artist));
        // @RequestBody binds incoming http request's body from json format to Artist object
        // .created() builds a response that includes the Location header, pointing to the newly created resource's URI.
        // Returns spring class that represents HTTP response with status code, headers, body etc
    }

    @GetMapping
    public ResponseEntity<Page<Artist>> getArtists(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(artistService.getAllArtists(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(artistService.getArtist(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Artist> deleteArtist(@PathVariable(value = "id") String id) {
        try {
            Artist artist = artistService.getArtist(id);
            artistService.deleteArtist(artist);
            return ResponseEntity.ok().body(artist);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            // The .build() method finalizes the construction of the ResponseEntity object without a response body
        }
    }


    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(artistService.uploadPhoto(id, file));
    }

    @GetMapping(path ="/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE}) 
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }

}
