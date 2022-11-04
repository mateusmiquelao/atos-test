package org.atostest.cache;

import org.atostest.services.dto.ProfileDTO;
import org.junit.After;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.*;

public class CacheUtilsTest {

    @After
    public void reset() {
        Whitebox.setInternalState(CacheUtils.class, "profiles", new ApplicationMap<>());
        Whitebox.setInternalState(CacheUtils.class, "documents", new ApplicationMap<>());
    }

    @Test
    public void addProfileTest() {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("peterkoln");
        profileDTO.setName("Peter Koln");
        profileDTO.setEmail("peterkoln@gmail.com");
        profileDTO.setAge(23);

        assertNull(CacheUtils.getProfile(profileDTO.getUsername()));
        CacheUtils.addProfile(profileDTO);
        assertNotNull(CacheUtils.getProfile(profileDTO.getUsername()));
    }

    @Test
    public void addDocumentOnANewProfileTest() {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("peterkoln");
        profileDTO.setName("Peter Koln");
        profileDTO.setEmail("peterkoln@gmail.com");
        profileDTO.setAge(23);

        CacheUtils.addProfile(profileDTO);

        assertNull(CacheUtils.getDocuments(profileDTO.getUsername()));

        CacheUtils.addDocument(profileDTO.getUsername(), "fileName.txt");

        assertNotNull(CacheUtils.getDocuments(profileDTO.getUsername()));

    }

    @Test
    public void addDocumentExistentProfileTest() {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("peterkoln");
        profileDTO.setName("Peter Koln");
        profileDTO.setEmail("peterkoln@gmail.com");
        profileDTO.setAge(23);

        CacheUtils.addProfile(profileDTO);

        assertNull(CacheUtils.getDocuments(profileDTO.getUsername()));

        CacheUtils.addDocument(profileDTO.getUsername(), "fileName.txt");

        assertNotNull(CacheUtils.getDocuments(profileDTO.getUsername()));

        CacheUtils.addDocument(profileDTO.getUsername(), "fileName2.txt");

        assertNotNull(CacheUtils.getDocuments(profileDTO.getUsername()));

        assertEquals(2, CacheUtils.getDocuments(profileDTO.getUsername()).size());

    }

    @Test
    public void deleteDocument() {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("peterkoln");
        profileDTO.setName("Peter Koln");
        profileDTO.setEmail("peterkoln@gmail.com");
        profileDTO.setAge(23);

        CacheUtils.addProfile(profileDTO);

        assertNull(CacheUtils.getDocuments(profileDTO.getUsername()));

        CacheUtils.addDocument(profileDTO.getUsername(), "fileName.txt");

        assertNotNull(CacheUtils.getDocuments(profileDTO.getUsername()));
        assertEquals(1, CacheUtils.getDocuments(profileDTO.getUsername()).size());

        CacheUtils.deleteDocument(profileDTO.getUsername(), "fileName.txt");

        assertTrue(CacheUtils.getDocuments(profileDTO.getUsername()).isEmpty());

    }


}
