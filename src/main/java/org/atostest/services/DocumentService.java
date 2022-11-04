package org.atostest.services;

import org.jvnet.hk2.annotations.Contract;

import java.io.IOException;
import java.io.InputStream;

@Contract
public interface DocumentService {

    String upload(String username, String filename, InputStream file) throws IOException;
    byte[] retrieve(String username, String document) throws IOException;
    void delete(String username, String document) throws IOException;
}
