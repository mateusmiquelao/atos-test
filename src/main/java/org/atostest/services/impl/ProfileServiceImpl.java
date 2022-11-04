package org.atostest.services.impl;

import org.atostest.cache.CacheUtils;
import org.atostest.services.ProfileService;
import org.atostest.services.dto.ProfileDTO;
import org.jvnet.hk2.annotations.Service;

import static java.lang.String.valueOf;
import static java.util.Objects.isNull;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Override
    public ProfileDTO create(ProfileDTO profileDTO) {

        ProfileDTO profile = CacheUtils.getProfile(profileDTO.getUsername());

        if (isNull(profile)) {
            profileDTO.setIdentification(valueOf(java.util.UUID.randomUUID()));
            CacheUtils.addProfile(profileDTO);
        } else {
            throw new RuntimeException("Profile already registered.");
        }

        return profileDTO;
    }
}
