package com.boha.geo.services;


import com.boha.geo.util.E;
import com.google.cloud.storage.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
public class CloudStorageUploader {
    public static final Logger LOGGER = LoggerFactory.getLogger(CloudStorageUploader.class.getSimpleName());
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    @Value("${storageBucket}")
    private String bucketName;
    @Value("${projectId}")
    private String projectId;

    @Value("${cloudStorageDirectory}")
    private String cloudStorageDirectory;

    public String getSignedUrl(String objectName, String contentType  ) throws Exception {
        LOGGER.info("%s%s%s getSignedUrl for cloud storage: %s".formatted(E.CHIPS, E.CHIPS, E.CHIPS, objectName));
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, cloudStorageDirectory
                + "/" + objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();
        URL vv = storage
                .signUrl(blobInfo, 20000, TimeUnit.DAYS, Storage.SignUrlOption.withPathStyle());
        LOGGER.info(E.CHIPS+E.CHIPS+E.CHIPS +
                "  signed url acquired. Cool! " + vv.toString());
       return vv.toString();
    }

    public String uploadFile(String objectName, File file) throws IOException {

        LOGGER.info(E.CHIPS+E.CHIPS+E.CHIPS +
                " uploadFile to cloud storage: " + objectName);
        String contentType = Files.probeContentType(file.toPath());
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, cloudStorageDirectory
                + "/" + objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        LOGGER.info(E.CHIPS+E.CHIPS+E.CHIPS +
                " uploadFile to cloud storage, contentType: " + contentType);

        Storage.BlobWriteOption precondition;
        if (storage.get(bucketName, objectName) == null) {
            precondition = Storage.BlobWriteOption.doesNotExist();
        } else {
            precondition =
                    Storage.BlobWriteOption.generationMatch(
                            storage.get(bucketName, objectName).getGeneration());
        }
        Blob blob = storage.createFrom(blobInfo, Paths.get(file.getPath()));
        URL vv = storage
                .signUrl(blobInfo, 20000, TimeUnit.DAYS, Storage.SignUrlOption.withPathStyle());
        LOGGER.info(E.CHIPS+E.CHIPS+E.CHIPS +
                " file uploaded to cloud storage, signed url acquired. Cool! " + vv.toString() );
        LOGGER.info(E.CHIPS+E.CHIPS+E.CHIPS +
                " should we use medialLink rather than the signed url?? " + blob.getMediaLink() );
        return vv.toString();
    }
}
