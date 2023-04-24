package com.boha.geo;

import com.boha.geo.controllers.DataController;
import com.boha.geo.controllers.ListController;
import com.boha.geo.monitor.services.ListService;
import com.boha.geo.services.CloudStorageUploaderService;
import com.boha.geo.services.TextTranslationService;
import com.boha.geo.services.UserBatchService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
public class SmokeTest {

    @Mock
    DataController dataController;
    @Mock
    ListController listController;

    @Mock
    TextTranslationService textTranslationService;

    @Mock
    UserBatchService userBatchService;

    @Mock
    CloudStorageUploaderService cloudStorageUploaderService;

    @Mock
    ListService listService;
    @Test
    public void dataControllerIsNotNull() {
        Assert.assertNotNull(dataController);
    }
    @Test
    public void listControllerIsNotNull() {
        Assert.assertNotNull(listController);
    }
    @Test
    public void textTranslationServiceIsNotNull() {
        Assert.assertNotNull(textTranslationService);
    }
    @Test
    public void userBatchServiceIsNotNull() {
        Assert.assertNotNull(userBatchService);
    }
    @Test
    public void cloudStorageUploaderServiceIsNotNull() {
        Assert.assertNotNull(cloudStorageUploaderService);
    }
    @Test
    public void listServiceIsNotNull() {
        Assert.assertNotNull(listService);
    }
}