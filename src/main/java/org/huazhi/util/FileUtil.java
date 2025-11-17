package org.huazhi.util;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.ws.rs.core.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class FileUtil {

    /**
     * 保存上传文件
     * @param file      上传的文件对象
     * @param targetDir 保存目录
     * @return          保存后的路径
     */
    public static Path saveFile(FileUpload file, String targetDir) {
        if (file == null || file.uploadedFile() == null) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        try {
            // 确保目录存在
            Path dir = Paths.get(targetDir);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            // 保存文件
            Path target = dir.resolve(file.fileName());
            Files.copy(file.uploadedFile(), target, StandardCopyOption.REPLACE_EXISTING);

            return target;
        } catch (IOException e) {
            throw new RuntimeException("保存文件失败: " + file.fileName(), e);
        }
    }

    /**
     * 保存文件到指定目录
     *
     * @param dirPath   保存的目录，例如 "knowledge/123"
     * @param fileName  文件名，例如 "test.txt"
     * @param input     输入流
     * @return 文件保存后的路径
     * @throws IOException IO异常
     */
    public static Path saveFile(String dirPath, String fileName, InputStream input) throws IOException {
        // 确保目录存在
        Path dir = Path.of(dirPath);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        // 拼接完整路径
        Path filePath = dir.resolve(fileName);

        // 写入文件
        try (FileOutputStream out = new FileOutputStream(filePath.toFile())) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            input.close();
        }

        return filePath;
    }

    /**
     * 下载文件
     * @param filePath 文件路径
     * @return Response (带文件流)
     */
    public static Response downloadFile(String filePath) {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("文件不存在: " + filePath)
                    .build();
        }
        try {
            String fileName = path.getFileName().toString();
            return Response.ok(path.toFile())
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .build();
        } catch (Exception e) {
            return Response.serverError().entity("下载失败: " + e.getMessage()).build();
        }
    }

    /**
     * 删除单个文件
     */
    public static boolean deleteFile(String path) {
        Path p = Paths.get(path);
        try {
            return Files.deleteIfExists(p);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

     /**
     * 删除文件夹及其所有子内容（递归）
     */
    public static boolean deleteDirectory(String directoryPath) {
        Path dir = Paths.get(directoryPath);
        if (!Files.exists(dir)) {
            return true; // 认为已删除
        }

        try {
            Files.walk(dir)
                    .sorted((a, b) -> b.compareTo(a)) // 先删子目录/文件，再删目录本身
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 清空目录，但保留目录本身
     */
    public static boolean cleanDirectory(String directoryPath) {
        Path dir = Paths.get(directoryPath);
        if (!Files.exists(dir)) {
            return true;
        }

        try {
            Files.list(dir).forEach(path -> {
                try {
                    if (Files.isDirectory(path)) {
                        deleteDirectory(path.toString());
                    } else {
                        Files.deleteIfExists(path);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取文件全部内容为字符串
     * @param filePath 文件路径
     * @return 文件内容，读取失败返回 null
     */
    public static String readFileToString(String filePath) {
        Path path = Paths.get(filePath);
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 按行读取文件内容
     * @param filePath 文件路径
     * @return 每行内容列表，读取失败返回 null
     */
    public static List<String> readFileToLines(String filePath) {
        Path path = Paths.get(filePath);
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}



