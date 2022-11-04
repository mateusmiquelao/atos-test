package org.atostest.cache;

import org.atostest.services.dto.ProfileDTO;

import java.util.ArrayList;
import java.util.List;

public class CacheUtils {

    private static ApplicationMap<String, ProfileDTO> profiles = new ApplicationMap<>();
    private static ApplicationMap<String, List<String>> documents = new ApplicationMap<>();

    public static void addProfile(ProfileDTO profileDTO) {
        profiles.put(profileDTO.getUsername(), profileDTO);
    }

    public static ProfileDTO getProfile(String username) {
        if (profiles.isEmpty()) {
            return null;
        } else {
            return (ProfileDTO) profiles.get(username);
        }
    }

    public static void addDocument(String username, String documentName) {
        if (documents.isEmpty()) {
            List<String> list = new ArrayList<>();
            list.add(documentName);
            documents.put(username, list);
        } else {
            List<String> documentList = (List<String>) documents.get(username);

            if (documentList.stream().noneMatch(document -> document.equals(documentName))) {
                documentList.add(documentName);
                documents.put(username, documentList);
            }
        }
    }

    public static List<String> getDocuments(String username) {
        if (documents.isEmpty()) {
            return null;
        } else {
            return (List<String>) documents.get(username);
        }
    }

    public static void deleteDocument(String username, String documentName) {
        if (documents.containsKey(username) && profiles.containsKey(username)){
            List<String> documentList = (List<String>) documents.get(username);
            documentList.removeIf(document -> document.equals(documentName));
            documents.put(username, documentList);
        }
    }

}
