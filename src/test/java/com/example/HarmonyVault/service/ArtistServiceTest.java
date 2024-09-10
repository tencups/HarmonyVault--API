package com.example.HarmonyVault.service;

import com.example.HarmonyVault.domain.Artist;
import com.example.HarmonyVault.repo.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.InputStream;
import java.util.Optional;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetArtist_Success() {
        // Given
        String artistId = "123";
        Artist artist = new Artist(artistId, "Artist Name", null, "Country", "Biography", "PhotoUrl");
        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));

        // When
        Artist foundArtist = artistService.getArtist(artistId);

        // Then
        assertEquals(artistId, foundArtist.getId());
        assertEquals("Artist Name", foundArtist.getName());
    }

    @Test
    public void testGetArtist_NotFound() {
        // Given
        String artistId = "123";
        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> artistService.getArtist(artistId));
    }

    @Test
    public void testCreateArtist_Success() {
        // Given
        Artist artist = new Artist(null, "New Artist", null, "Country", "Biography", "PhotoUrl");
        Artist savedArtist = new Artist("123", "New Artist", null, "Country", "Biography", "PhotoUrl");
        when(artistRepository.save(artist)).thenReturn(savedArtist);

        // When
        Artist result = artistService.createArtist(artist);

        // Then
        assertEquals("123", result.getId());
        assertEquals("New Artist", result.getName());
    }

    @Test
    public void testDeleteArtist_Success() {
        // Given
        String artistId = "123";
        Artist artist = new Artist(artistId, "Artist Name", null, "Country", "Biography", "PhotoUrl");
        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));

        // When
        artistService.deleteArtist(artist);

        // Then
        // Verify that the delete method was called on the repository
        verify(artistRepository).delete(artist);
    }

    @Test
public void testUploadPhoto_Success() throws Exception {
    // Given
    String artistId = "123";
    String filename = "photo.jpg";
    Artist artist = new Artist(artistId, "Artist Name", null, "Country", "Biography", "OldPhotoUrl");

    // Mock artist repository to return the artist when findById is called
    when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));

    // Mock MultipartFile to return a filename and input stream
    MultipartFile file = mock(MultipartFile.class);
    InputStream inputStream = new ByteArrayInputStream(new byte[0]); // Dummy input stream for testing
    when(file.getOriginalFilename()).thenReturn(filename);
    when(file.getInputStream()).thenReturn(inputStream);

    // Simulate the correct servlet request context
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setServerPort(8080); // Ensure the port is set correctly
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    // When
    String result = artistService.uploadPhoto(artistId, file);

    // Then
    ArgumentCaptor<Artist> artistArgumentCaptor = ArgumentCaptor.forClass(Artist.class);
    verify(artistRepository).save(artistArgumentCaptor.capture());

    Artist updatedArtist = artistArgumentCaptor.getValue();
    assertEquals("http://localhost:8080/artists/image/123.jpg", updatedArtist.getPhotoUrl());
    assertEquals("http://localhost:8080/artists/image/123.jpg", result);
}

    
}
