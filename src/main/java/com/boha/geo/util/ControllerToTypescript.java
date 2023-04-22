package com.boha.geo.util;

import com.boha.geo.controllers.DataController;
import com.boha.geo.controllers.ListController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControllerToTypescript {
    static final Logger logger = LoggerFactory.getLogger(ControllerToTypescript.class);

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Path dirPath = Files.createDirectory(Path.of("controllers_" + sdf.format(new Date())));


        convertToTypeScript(dirPath);

    }

    private static void convertToTypeScript(Path dirPath) throws Exception {

        DataController dataController = new DataController(null, null, null, null,
                null, null, null
        );
        ListController listController = new ListController(null,null,null);

        try {
            logger.info("\n\n\uD83D\uDD35\uD83D\uDD35 DataController \n\n");
            Method[] methods = dataController.getClass().getDeclaredMethods();
            logger.info("\n\n\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 " +
                    "data methods : " + methods.length + "\n");
            convertController(methods, dataController, dirPath);
            //
            logger.info("\n\n\uD83D\uDD35\uD83D\uDD35 ListController \n\n");
            Method[] listMethods = listController.getClass().getDeclaredMethods();
            logger.info("\n\n\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 " +
                    "list methods : " + listMethods.length + "\n");
            convertController(listMethods, listController, dirPath);
        } catch (Exception e) {
            logger.info("Unable to convert : " + e.getMessage());
            e.printStackTrace();
        }
    }
/*
public ping = async (req: Request, res: Response, next: NextFunction) => {
    try {
      res.status(200).json({ data: 'Gio', message: 'Ping back to you!' });
    } catch (error) {
      next(error);
    }
  };

   const userId: string = req.params.id;
      const findOneUserData: User = await this.user.findUserById(userId);

 */
    private static void convertController(Method[] methods, Object name, Path dirPath) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nimport { NextFunction, Request, Response } from 'express';\n");
        sb.append("\n\nexport class "+name.getClass().getSimpleName()+" {\n");

        for (Method method : methods) {
            logger.info("\uD83C\uDF0E \uD83C\uDF0E Method: " + method.getName());
            Parameter[] parameters = method.getParameters();
            for (Parameter pt : parameters) {
                logger.info("\uD83C\uDF0E Parameter: \uD83C\uDF4E " + pt.getName());
            }
            sb.append("\n\n\t\tpublic ").append(method.getName()).append(" = async ")
                    .append(" (req: Request, res: Response, next: NextFunction) => {\n");
            sb.append("\t\ttry {\n");
            for (Parameter pt : parameters) {
                sb.append("\t\t\tconst ").append(pt.getName()).append(": ").append(pt.getType().getSimpleName()).append(" = ");
                if (method.getName().contains("add")
                        || method.getName().contains("register") || method.getName().contains("create")) {
                    sb.append("req.body").append(";\n");

                } else {
                    sb.append("req.params.").append(pt.getName()).append(";\n");
                }
            }
            
            sb.append("\t\t\tres.status(200).json({ });\n");
            sb.append("\t\t } catch (error) {\n");
            sb.append("\t\t\tnext(error);\n");
            sb.append("\t\t}\n};");

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> pt : parameterTypes) {
                logger.info("\uD83C\uDF0E Parameter Type: \uD83C\uDF4E " + pt.getSimpleName());
            }
            Class<?> returnType = method.getReturnType();
            logger.info("\uD83C\uDF0E Return Type: \uD83C\uDF4E " + returnType.getSimpleName());
            logger.info("\n");
        }

        sb.append("\n");

        logger.info(sb.toString());
        try {

            Path path = Path.of(dirPath + "/"
                    + name.getClass().getSimpleName().toLowerCase() + ".ts");
            Files.writeString(path, sb.toString(),
                    StandardCharsets.UTF_8);
            logger.info("\uD83C\uDF0E\uD83C\uDF0E\uD83C\uDF0E controller output path: " + path.toFile().getAbsolutePath());
        } catch (Exception e) {
            logger.info("\uD83D\uDD34\uD83D\uDD34 \uD83D\uDD34\uD83D\uDD34 unable to write file: " + e.getMessage());
        }
    }

}
