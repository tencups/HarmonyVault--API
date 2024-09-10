package com.example.HarmonyVault.resource;


import com.example.HarmonyVault.domain.Artist;
import com.example.HarmonyVault.resource.ArtistResource;
import com.example.HarmonyVault.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ArtistResourceTest {

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private ArtistResource artistResource;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(artistResource).build();
    }

    @Test
    public void testCreateArtist() throws Exception {
        // Given
        Artist artist = new Artist("123", "Artist Name", null, "Country", "Biography", "PhotoUrl");
        when(artistService.createArtist(any(Artist.class))).thenReturn(artist);

        // When / Then
        mockMvc.perform(post("/artists")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Artist Name\",\"country\":\"Country\",\"biography\":\"Biography\",\"photoUrl\":\"PhotoUrl\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Artist Name"));
    }

    @Test
    public void testGetArtist() throws Exception {
        // Given
        Artist artist = new Artist("123", "Artist Name", null, "Country", "Biography", "PhotoUrl");
        when(artistService.getArtist("123")).thenReturn(artist);

        // When / Then
        mockMvc.perform(get("/artists/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Artist Name"));
    }

    @Test
    public void testGetArtist_NotFound() throws Exception {
        // Given
        when(artistService.getArtist("123")).thenThrow(new RuntimeException("Artist not found."));

        // When / Then
        mockMvc.perform(get("/artists/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteArtist() throws Exception {
        // Given
        Artist artist = new Artist("123", "Artist Name", null, "Country", "Biography", "PhotoUrl");
        when(artistService.getArtist("123")).thenReturn(artist);
        doNothing().when(artistService).deleteArtist(artist);

        // When / Then
        mockMvc.perform(delete("/artists/123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUploadPhoto() throws Exception {
        // Given
        when(artistService.uploadPhoto(eq("123"), any())).thenReturn("http://localhost:8080/artists/image/123photo.jpg");

        // When / Then
        mockMvc.perform(put("/artists/photo")
                .param("id", "123")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content("file content".getBytes()))
                .andExpect(status().isOk())
                .andExpect(content().string("http://localhost:8080/artists/image/123photo.jpg"));
    }
}
