package com.boha.geo.services;

import com.boha.geo.models.GCSBlob;
import com.boha.geo.monitor.data.Photo;
import com.boha.geo.monitor.data.UploadBag;
import com.boha.geo.repos.PhotoRepository;
import com.boha.geo.util.E;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;
@RequiredArgsConstructor

@Service
public class StorageService {
    private static final Logger LOGGER = Logger.getLogger(StorageService.class.getSimpleName());

    final PhotoRepository photoRepository;

    private Storage storage;
    @Value("${bucketName}")

    private String bucketName;
    @Value("${projectId}")
    private String projectId;

    @Value("${storageBucket}")
    private String storageBucket;

    private static final String xx = E.COFFEE + E.COFFEE + E.COFFEE;


    public String downloadObject(
            String objectName) {

        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();
        byte[] content = storage.readAllBytes(bucketName, objectName);
        return new String(content, UTF_8);
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

    static final String folder = "monitorPhotos/";

    public Photo uploadBag(UploadBag bag) throws Exception {
        String fileName = folder + "photo#" + bag.getPhoto().getProjectId() + "#"
                + DateTime.now().toDateTimeISO().toString() + ".jpg";
        String fileUrl = uploadPhoto(bag.getFileBytes(), fileName);

        String thumbnailName = folder + "thumbnail#" + bag.getPhoto().getProjectId() + "#"
                + DateTime.now().toDateTimeISO().toString() + ".jpg";
        String thumbUrl = uploadPhoto(bag.getThumbnailBytes(), thumbnailName);

        Photo photo = bag.getPhoto();
        photo.setUrl(fileUrl);
        photo.setThumbnailUrl(thumbUrl);

        Photo p = photoRepository.insert(photo);
        LOGGER.info(E.LEAF + E.LEAF + " Photo written to DB, file url: " + p.getUrl());
        LOGGER.info(E.LEAF + E.LEAF + " Photo written to DB, thumb url: " + p.getThumbnailUrl());

        return p;
    }

    private String uploadPhoto(byte[] photoBytes, String objectName) throws Exception {

        File file = new File(objectName);
        FileUtils.writeByteArrayToFile(file, photoBytes);

        if (file.exists()) {
            LOGGER.info(E.AMP + " File path: " + file.getPath() + " " + file.length() + " bytes");
        }
        FileInputStream stream = new FileInputStream(file);
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();

        BlobId blobId = BlobId.of(storageBucket, folder + objectName);
        final BlobInfo.Builder blobInfoBuilder = BlobInfo.newBuilder(blobId);

        String contentType = Files.probeContentType(file.toPath());
        LOGGER.info(E.AMP + E.AMP + " File contentType: " + contentType);

        blobInfoBuilder.setContentType("image/jpg")
                .build();

        BlobInfo blobInfo = blobInfoBuilder.build();
        LOGGER.info(E.AMP + E.AMP + " File contentType: " + blobInfo.getContentType()
                + " mediaLink: " + blobInfo.getSelfLink());

        Blob blob = storage.createFrom(blobInfo, stream);
        LOGGER.info(E.AMP + E.AMP + " uploadPhoto: storage blob: " + blob.getSize() + " " + blob.getBucket());

        //https://firebasestorage.googleapis.com/v0/b/thermal-effort-366015.appspot.com/o/monitorPhotos
        // %2Fmedia@454df53c-b716-4d0e-920f-0802659a90de@2023-01-08T02:08:35.723979Z.jpg?
        // alt=media&token=f7bb77c1-b8f1-4150-98ca-0d95883942d1

        //https://storage.googleapis.com/${bucket.name}/${blob.name}
//        String url = "https://firebasestorage.googleapis.com/v0/b/" + projectId + ".appspot.com/o/"+folder+"/" + blob.getName();

//        byte[] content = storage.readAllBytes(storageBucket, folder + objectName);
//        String m = new String(content, UTF_8);
//        LOGGER.info(E.LEAF+E.LEAF+E.LEAF+
//                " content from uploaded file: " + m);

        return blob.getSelfLink();
    }

    public String testUploadPhoto() throws Exception {
        byte[] bytes = ("This is a test string in bytes. really checking to see if the text uploads to cloud storage " +
                " This would show success if I can download it after uploading it!")
                .getBytes(UTF_8);
        String url = uploadPhoto(bytes, "lookingGreatSenor123.txt");
        LOGGER.info("\uD83D\uDD37 \uD83D\uDD37 \uD83D\uDD37 testUploadPhoto: url: " + url);
        return url;
    }

}
