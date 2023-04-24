package com.boha.geo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JavaToTypescript {
    static final Logger logger = LoggerFactory.getLogger(JavaToTypescript.class);

//    public static void main(String[] args) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        Path dirPath = Files.createDirectory(Path.of("conversions_" + sdf.format(new Date())));
//
//
//        convertToTypeScriptInterfaces(dirPath);
//
//    }

    private static void convertToTypeScriptInterfaces(Path outputPath) throws Exception {

        Path inputDirPath = Paths.get("src/main/java/com/boha/geo/monitor/data/");
        logger.info(inputDirPath.toString());
        File inputDirectory = inputDirPath.toFile();
        if (!inputDirectory.isDirectory()) {
            throw new Exception("Directory bad, Senor!");
        }

        File outDir = outputPath.toFile();
        if (outDir.exists()) {
            logger.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E Out Directory: " + outDir.getAbsolutePath());

            Path interfacesDirPath = Files.createDirectory(Path.of(outDir.getPath() + "/interfaces"));
            Path modelsDirPath = Files.createDirectory(Path.of(outDir.getPath() + "/models"));


            File[] files = inputDirectory.listFiles();
            logger.info("\uD83C\uDF4E\uD83C\uDF4E\uD83C\uDF4E Files: " + files.length);
            for (File file : files) {
                logger.info("Java File: \uD83C\uDF4E " + file.getName());
                int index = file.getName().indexOf(".");
                String m = file.getName().substring(0, index);
                Class<?> myClass1 = Class.forName("com.boha.geo.monitor.data." + m);
                try {
                    Constructor<?> constructor1 = myClass1.getConstructor();
                    Object instanceOfMyClass = constructor1.newInstance();
                    createInterfaceFile(interfacesDirPath, instanceOfMyClass);
                    createModelsFile(modelsDirPath, instanceOfMyClass);
                } catch (Exception e) {
                    logger.info("Unable to convert : " + e.getMessage());
                }

            }
        }
    }

    private static void createInterfaceFile(Path dirPath, Object am) {
        Field[] fields = am.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append("export interface ");
        sb.append(am.getClass().getSimpleName()).append(" {\n");
        sb.append("\t_id?: string;\n");
        for (Field field : fields) {
            sb.append("\t");
            sb.append(field.getName()).append(":");
            if (field.getType() == String.class) {
                sb.append(" string;\n");
            } else if (field.getType() == double.class) {
                sb.append(" Number;\n");
            } else if (field.getType() == int.class) {
                sb.append(" Number;\n");
            } else {
                sb.append(" ");
                sb.append(field.getType().getSimpleName());
                sb.append(";\n");
            }
        }
        sb.append("}");

        logger.info(sb.toString());

        try {
            Path path = Path.of(dirPath + "/"
                    + am.getClass().getSimpleName().toLowerCase() + ".ts");
            Files.writeString(path, sb.toString(),
                    StandardCharsets.UTF_8);
            logger.info("\uD83D\uDD35 \uD83D\uDD35 \uD83D\uDD35 interfaces output path: " + path.toFile().getAbsolutePath());
        } catch (Exception e) {
            logger.info("\uD83D\uDD34\uD83D\uDD34 \uD83D\uDD34\uD83D\uDD34 unable to write file: " + e.getMessage());
        }
    }

    private static void createModelsFile(Path dirPath, Object am) {
        Field[] fields = am.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append("import { model, Schema, Document } from 'mongoose';\n");
        sb.append("import { ").append(am.getClass().getSimpleName()).append(" } from '@/interfaces/").append(am.getClass().getSimpleName().toLowerCase()).append(".interface';\n");
        sb.append("\nconst ").append(am.getClass().getSimpleName()).append("Schema = new Schema (\n");
        sb.append(" {\n");
        for (Field field : fields) {
            sb.append("\t");
            sb.append(field.getName()).append(": {\n");
            if (field.getType() == String.class) {
                sb.append("\t\ttype: String,\n\t\t},\n");
            } else if (field.getType() == double.class) {
                sb.append("\t\ttype: Number,\n\t\t},\n");
            } else if (field.getType() == int.class) {
                sb.append("\t\ttype: Number,\n\t\t},\n");
            } else if (field.getType() == List.class) {
                sb.append("\t\ttype: [],\n\t\t},\n");
            } else {
                sb.append("\t\ttype: Object,\n\t\t},\n");
            }

        }
        sb.append("}, \n");
        sb.append("\t{ collection: '").append(am.getClass().getSimpleName().toLowerCase()).append("s' },\n");
        sb.append(");\n\n");
        sb.append("export const ").append(am.getClass().getSimpleName())
                .append("Model = model<").append(am.getClass().getSimpleName())
                .append(" & Document>('").append(am.getClass().getSimpleName())
                .append("', ").append(am.getClass().getSimpleName()).append("Schema);\n");
        logger.info(sb.toString());

        try {
            Path path = Path.of(dirPath + "/"
                    + am.getClass().getSimpleName().toLowerCase() + ".ts");
            Files.writeString(path, sb.toString(),
                    StandardCharsets.UTF_8);
            logger.info("\uD83C\uDF0E\uD83C\uDF0E\uD83C\uDF0E models output path: " + path.toFile().getAbsolutePath());
        } catch (Exception e) {
            logger.info("\uD83D\uDD34\uD83D\uDD34 \uD83D\uDD34\uD83D\uDD34 unable to write file: " + e.getMessage());
        }
    }

}
