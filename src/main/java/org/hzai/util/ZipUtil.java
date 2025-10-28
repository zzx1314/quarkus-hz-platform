package org.hzai.util;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class ZipUtil {

    public static void unzip(Path zipFilePath, Path destDir) throws IOException {
        if (!Files.exists(destDir)) {
            Files.createDirectories(destDir);
        }
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFilePath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path newPath = destDir.resolve(entry.getName()).normalize();

                if (!newPath.startsWith(destDir)) {
                    // 防止 Zip Slip 漏洞
                    throw new IOException("Bad zip entry: " + entry.getName());
                }
                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    Files.createDirectories(newPath.getParent());
                    try (OutputStream os = Files.newOutputStream(newPath)) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            os.write(buffer, 0, len);
                        }
                    }
                }
            }
        }
    }
}

