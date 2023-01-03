package com.boha.geo.services;

import com.boha.geo.models.GCSBlob;
import com.boha.geo.util.E;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
public class StorageService {
    private static final Logger LOGGER = Logger.getLogger(StorageService.class.getSimpleName());

    public StorageService() {
        LOGGER.info(xx +
                " StorageService constructed: ");
    }

    private Storage storage;
    @Value("${bucketName}")

    private String bucketName;
    @Value("${projectId}")
    private String projectId;

    private static final String xx = E.COFFEE+E.COFFEE+E.COFFEE;


    public String downloadObject(
            String objectName) {

        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();
        byte[] content = storage.readAllBytes(bucketName, objectName);
        return new String(content, StandardCharsets.UTF_8);
    }
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public List<GCSBlob> listObjects(int hours) throws Exception {
        LOGGER.info(E.AMP + E.AMP + E.AMP + " Starting to list bucket blobs: "
                + bucketName + " projectId: " + projectId);

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Page<Blob> blobs = storage.list(bucketName);
        List<GCSBlob> list = new ArrayList<GCSBlob>();

        long now = DateTime.now().minusHours(hours).getMillis();
        for (Blob blob : blobs.iterateAll()) {
            if (blob.getName().contains("events/page")) {
                if (blob.getCreateTime() > now) {
                    GCSBlob g = new GCSBlob();
                    g.setCreateTime(new DateTime(blob.getCreateTime()).toDateTimeISO().toString());
                    g.setName(blob.getName());
                    g.setSize(blob.getSize().intValue());
                    list.add(g);

                }
            }

        }

        Collections.sort(list);
        return list;
    }
}
