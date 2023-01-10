package com.boha.geo.monitor.data;

import lombok.Data;

@Data
public class UploadBag {
    private byte[] fileBytes, thumbnailBytes;
    private Photo photo;
}
