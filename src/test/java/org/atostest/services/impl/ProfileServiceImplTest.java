package org.atostest.services.impl;

import org.atostest.cache.ApplicationMap;
import org.atostest.cache.CacheUtils;
import org.atostest.services.dto.ProfileDTO;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProfileServiceImplTest {

    private final ProfileServiceImpl profileService = new ProfileServiceImpl();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @After
    public void reset() {
        Whitebox.setInternalState(CacheUtils.class, "profiles", new ApplicationMap<>());
        Whitebox.setInternalState(CacheUtils.class, "documents", new ApplicationMap<>());
    }

    @Test
    public void createProfile(){
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("peterkoln");
        profileDTO.setName("Peter Koln");
        profileDTO.setEmail("peterkoln@gmail.com");
        profileDTO.setAge(23);

        ProfileDTO profileCreated = profileService.create(profileDTO);

        assertEquals(profileDTO.getUsername(), profileCreated.getUsername());
        assertEquals(profileDTO.getName(), profileCreated.getName());
        assertEquals(profileDTO.getAge(), profileCreated.getAge());
        assertEquals(profileDTO.getEmail(), profileCreated.getEmail());
        assertNotNull(profileCreated.getIdentification());
        assertNotNull(CacheUtils.getProfile(profileDTO.getUsername()));
    }

    @Test
    public void errorWhenCreatingDuplicateProfile(){
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUsername("peterkoln");
        profileDTO.setName("Peter Koln");
        profileDTO.setEmail("peterkoln@gmail.com");
        profileDTO.setAge(23);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Profile already registered.");

        ProfileDTO profileCreated = profileService.create(profileDTO);
        ProfileDTO secondProfile = profileService.create(profileDTO);
    }

}
