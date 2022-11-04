package org.atostest.services.impl;

import org.atostest.cache.CacheUtils;
import org.atostest.services.DocumentService;
import org.atostest.services.dto.ProfileDTO;
import org.jvnet.hk2.annotations.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Override
    public String upload(String username, String filename, InputStream file) throws IOException {

        ProfileDTO profile = getProfile(username);

        String generatedName = getGeneratedName(filename, profile);

        String uploadedFileLocation = "src/main/java/org/atostest/documents/" + generatedName;

        File targetFile = new File(uploadedFileLocation);

        Files.copy(
                file,
                targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        file.close();

        CacheUtils.addDocument(username, generatedName);

        return uploadedFileLocation;
    }

    @Override
    public byte[] retrieve(String username, String document) throws IOException {

        ProfileDTO profile = getProfile(username);
        String generatedName = getGeneratedName(document, profile);

        List<String> documents = CacheUtils.getDocuments(username);
        if (isNull(documents) || documents.stream().noneMatch(doc -> doc.equals(generatedName))) {
            throw new RuntimeException("Document not found");
        }

        File file = new File("src/main/java/org/atostest/documents/" + generatedName);

        try {
            InputStream inputStream = new FileInputStream(file);
            int lenght = inputStream.available();
            byte[] data = new byte[lenght];
            int offset = 0;
            while (offset < lenght) {
                int read = inputStream.read(data, offset, data.length - offset);
                if (read < 0) {
                    break;
                }
                offset += read;
            }
            if (offset < lenght) {
                throw new IOException(
                        String.format("Read %d bytes; expected %d", offset, lenght));
            }
            inputStream.close();
            return data;

        } catch (FileNotFoundException fileNotFoundException) {
            throw new RuntimeException("Document not found");
        }
    }

    @Override
    public void delete(String username, String document) throws IOException {

        ProfileDTO profile = getProfile(username);

        CacheUtils.deleteDocument(username, getGeneratedName(document, profile));

        File file = new File("src/main/java/org/atostest/documents/" + getGeneratedName(document, profile));

        file.delete();

    }

    private static ProfileDTO getProfile(String username) {
        ProfileDTO profile = CacheUtils.getProfile(username);

        if (isNull(profile)) {
            throw new RuntimeException("User not registered.");
        }
        return profile;
    }

    private static String getGeneratedName(String filename, ProfileDTO profile) {
        return profile.getIdentification() + "_" + filename;
    }
}

