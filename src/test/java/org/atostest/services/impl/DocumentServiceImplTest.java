package org.atostest.services.impl;

import org.atostest.cache.ApplicationMap;
import org.atostest.cache.CacheUtils;
import org.atostest.services.dto.ProfileDTO;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.reflect.Whitebox;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DocumentServiceImplTest {

    private static final String FILE_NAME_TXT = "fileName.txt";
    private final DocumentServiceImpl documentService = new DocumentServiceImpl();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @After
    public void reset() {
        Whitebox.setInternalState(CacheUtils.class, "profiles", new ApplicationMap<>());
        Whitebox.setInternalState(CacheUtils.class, "documents", new ApplicationMap<>());
    }

    @Test
    public void successfulUpload() throws IOException {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("peterkoln");
        profileDTO.setName("Peter Koln");
        profileDTO.setEmail("peterkoln@gmail.com");
        profileDTO.setIdentification("123");
        profileDTO.setAge(23);

        CacheUtils.addProfile(profileDTO);

        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());

        String fileLocation = documentService.upload(profileDTO.getUsername(), FILE_NAME_TXT, inputStream);

        assertEquals(getFileLocation(getDocumentName(profileDTO.getIdentification())), fileLocation);

        assertFalse(CacheUtils.getDocuments(profileDTO.getUsername()).isEmpty());

        assertEquals(getDocumentName(profileDTO.getIdentification()), CacheUtils.getDocuments(profileDTO.getUsername()).get(0));

        File file = new File(getFileLocation(getDocumentName(profileDTO.getIdentification())));
        assertTrue(file.delete());
    }

    @Test
    public void userNotRegisteredWhenUploadingFile() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Profile not registered.");

        documentService.upload("joaquin", "file", inputStream);
    }

    @Test
    public void successfulRetrieve() throws IOException {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("peterkoln");
        profileDTO.setName("Peter Koln");
        profileDTO.setEmail("peterkoln@gmail.com");
        profileDTO.setIdentification("123");
        profileDTO.setAge(23);

        CacheUtils.addProfile(profileDTO);

        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());

        documentService.upload(profileDTO.getUsername(), FILE_NAME_TXT, inputStream);

        byte[] response = documentService.retrieve(profileDTO.getUsername(), FILE_NAME_TXT);

        assertNotNull(response);

        File file = new File(getFileLocation(getDocumentName(profileDTO.getIdentification())));
        assertTrue(file.delete());
    }

    @Test
    public void errorWhenDocumentIsNotFoundOnCache() throws IOException {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("peterkoln");
        profileDTO.setName("Peter Koln");
        profileDTO.setEmail("peterkoln@gmail.com");
        profileDTO.setIdentification("123");
        profileDTO.setAge(23);

        CacheUtils.addProfile(profileDTO);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Document not found");

        documentService.retrieve(profileDTO.getUsername(), FILE_NAME_TXT);
    }

    @Test
    public void errorWhenDocumentIsNotFound() throws IOException {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("peterkoln");
        profileDTO.setName("Peter Koln");
        profileDTO.setEmail("peterkoln@gmail.com");
        profileDTO.setIdentification("123");
        profileDTO.setAge(23);

        CacheUtils.addProfile(profileDTO);
        CacheUtils.addDocument(profileDTO.getUsername(), getDocumentName(profileDTO.getIdentification()));

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Document not found");

        documentService.retrieve(profileDTO.getUsername(), FILE_NAME_TXT);
    }

    @Test
    public void deleteSuccess() throws IOException {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("peterkoln");
        profileDTO.setName("Peter Koln");
        profileDTO.setEmail("peterkoln@gmail.com");
        profileDTO.setIdentification("123");
        profileDTO.setAge(23);

        CacheUtils.addProfile(profileDTO);

        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());

        documentService.upload(profileDTO.getUsername(), FILE_NAME_TXT, inputStream);

        documentService.delete(profileDTO.getUsername(), FILE_NAME_TXT);

        assertTrue(CacheUtils.getDocuments(profileDTO.getUsername()).isEmpty());

        File file = new File(getFileLocation(getDocumentName(profileDTO.getIdentification())));
        assertFalse(file.delete());

    }

    private static String getDocumentName(String identification) {
        return identification + "_" + FILE_NAME_TXT;
    }

    private static String getFileLocation(String name) {
        return "src/main/java/org/atostest/documents/" + name;
    }

}
