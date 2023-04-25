package com.boha.geo;

import com.boha.geo.controllers.DataController;
import com.boha.geo.controllers.ListController;
import com.boha.geo.monitor.services.ListService;
import com.boha.geo.services.CloudStorageUploaderService;
import com.boha.geo.services.TextTranslationService;
import com.boha.geo.services.UserBatchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
class SmokeTest {

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
    void dataControllerIsNotNull() {
        Assertions.assertNotNull(dataController);
    }
    @Test
    void listControllerIsNotNull() {
        Assertions.assertNotNull(listController);
    }
    @Test
    void textTranslationServiceIsNotNull() {
        Assertions.assertNotNull(textTranslationService);
    }
    @Test
    void userBatchServiceIsNotNull() {
        Assertions.assertNotNull(userBatchService);
    }
    @Test
    void cloudStorageUploaderServiceIsNotNull() {
        Assertions.assertNotNull(cloudStorageUploaderService);
    }
    @Test
    void listServiceIsNotNull() {
        Assertions.assertNotNull(listService);
    }
}