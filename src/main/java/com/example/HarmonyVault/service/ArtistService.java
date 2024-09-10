package com.example.HarmonyVault.service;


import com.example.HarmonyVault.domain.Artist;
import com.example.HarmonyVault.repo.ArtistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.example.HarmonyVault.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service // Instantiate as service component/bean. Add to application context
@Slf4j // Adds logger (log.info())
@Transactional(rollbackOn =  Exception.class) // Methods executed in transaction context, if Exception then rollback
// transactions to keep data safe
@RequiredArgsConstructor // generate constructor with parameters for all final and @NonNull fields
// (ArtistRepo in this case)
public class ArtistService {

    private final ArtistRepository artistRepo;

    public Page<Artist> getAllArtists(int page, int size) {
        return artistRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Artist getArtist(String id) {
        return artistRepo.findById(id).orElseThrow(() -> new RuntimeException("Artist not found."));
    }

    public Artist createArtist(Artist artist) {
        return artistRepo.save(artist);
    }

    public void deleteArtist(Artist artist) {
        artistRepo.delete(artist);
    }

    public String uploadPhoto(String id, MultipartFile file) {
        log.info("Saving picture for user ID: {}", id);
        Artist artist = getArtist(id);
        String photoUrl = photoFunction.apply(id, file);
        artist.setPhotoUrl(photoUrl);
        artistRepo.save(artist);
        return photoUrl;
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename)
            .filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");
    
    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String extension = fileExtension.apply(image.getOriginalFilename());
        try {

            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(id + extension), REPLACE_EXISTING);
            // This line copies an image from an input stream to a specific location in the file system, 
            // creating a file with a name based on id + extension. 
            // If a file with the same name already exists, it will be replaced with the new file.
            

            // getInputStream reads the content of the uploaded file byte by byte
            // .resolve is better than string concatenation because it automatically handles separators and platform differences, ensuring valid path 
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/artists/image/" + id + extension).toUriString();

            // This line generates and returns a URI string pointing to an image resource. The URI is constructed based on the current application context, 
            // with the path /artists/image/ followed by a combination of id and extension. 
            // This URI can be used to retrieve the image from the server.

            /*
             * Base URL: http://localhost:8080 (from fromCurrentContextPath())
             * Path: /artists/image/123image.jpg (from path("/artists/image/" + id + extension))
             * Full URI: http://localhost:8080/artists/image/123image.jpg
             */ 


        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };

}
