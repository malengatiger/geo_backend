package com.boha.geo.util;

import com.boha.geo.monitor.services.DataService;
import com.boha.geo.monitor.services.ListService;
import com.boha.geo.monitor.services.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListServiceToTest {
    static final Logger logger = LoggerFactory.getLogger(ListServiceToTest.class);

//    public static void main(String[] args) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        Path dirPath = Files.createDirectory(Path.of("controllers_" + sdf.format(new Date())));
//
//
//        convertToTests(dirPath);
//
//    }

    private static void convertToTests(Path outputPath) throws Exception {

        ListService listService = new ListService(null,null, null, null,
                null, null, null, null,
                null, null, null, null,
                null,null,null, null,
                null,null,null,null,null,
                null);

        try {
            logger.info("\n\n\uD83D\uDD35\uD83D\uDD35 ListService \n\n");
            Method[] listMethods = listService.getClass().getDeclaredMethods();
            logger.info("\n\n\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 listMethods : " + listMethods.length + "\n");
            convertListService(listMethods, listService);
        } catch (Exception e) {
            logger.info("Unable to convert : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void convertListService(Method[] methods, Object name) {
        StringBuilder sb = new StringBuilder();

        for (Method method : methods) {
            logger.info("\uD83C\uDF0E \uD83C\uDF0E Method: " + method.getName());


            StringBuilder pb = new StringBuilder();
            Parameter[] parameters = method.getParameters();

            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> returnType = method.getReturnType();

            logger.info("\uD83C\uDF0E Return Type: \uD83C\uDF4E " + returnType.getSimpleName());

            if (parameters.length == 0) {
                pb.append("()");
            } else {
                pb.append("(");
                int index = 0;
                for (Parameter pt : parameters) {
                    pb.append(pt.getType().getSimpleName()).append(" ").append(pt.getName()).append(", ");
                    if (index == parameters.length - 1) {
                        pb.append(") ");
                    } else {
                        pb.append(parameterTypes[index].getSimpleName())
                                .append(" ").append(pt.getName());
                    }
                    if (parameters.length == 1) {
                        pb.append("");
                    } else {
                        if (index == parameters.length - 1) {
                            pb.append(" ");
                        } else {
                            pb.append(", ");
                        }
                    }

                    index++;
                }
                StringBuilder xb = new StringBuilder();
                int i = 0;
                xb.append(" (");
                for (Parameter pt : parameters) {
                    xb.append(pt.getName());
                    if (i < (parameters.length - 1)) {
                        xb.append(", ");
                    }
                    i++;
                }
                xb.append(") ");

                String paramsBlock = pb.toString();
                sb.append("\n@Test\n" +
                        "    public void "+method.getName()+ paramsBlock + " throws Exception {\n");
                sb.append("\t\tList<").append(returnType.getSimpleName()).append("> ");
                sb.append(" list = new ArrayList<>();\n");
                sb.append("\t\tMockito.when(listService."+method.getName()+ xb.toString() +").thenReturn(list);\n");
                sb.append("\t\tAssert.assertEquals(0, list.size());\n");
                sb.append("\t\tLOGGER.info(mm +\"ListService: "+method.getName()+"\");\n");
                sb.append("\t}\n");
            }

            logger.info("}\n");
        }

        sb.append("\n");

        logger.info(sb.toString());
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
