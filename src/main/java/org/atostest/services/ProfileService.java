package org.atostest.services;

import org.atostest.services.dto.ProfileDTO;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface ProfileService {

    ProfileDTO create(ProfileDTO profileDTO);
}
