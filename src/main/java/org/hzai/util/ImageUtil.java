package org.hzai.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUtil {

    /**
     * 将 PGM 格式的图像转换为 PNG 格式
     * 
     * @param pgmFilePath PGM 文件路径
     * @param pngFilePath 目标 PNG 文件路径
     */
    public static void pgmToPng(String pgmFilePath, String pngFilePath) {
        try {
            File pgmFile = new File(pgmFilePath);
            BufferedImage image = ImageIO.read(pgmFile); // 读取 PGM
            if (image == null) {
                log.error("无法读取 PGM 文件，可能格式不受支持: {}", pgmFilePath);
                return;
            }
            File pngFile = new File(pngFilePath);
            ImageIO.write(image, "png", pngFile); // 保存为 PNG
            log.info("转换完成！");
        } catch (IOException e) {
            log.error("转换失败: {}", e.getMessage());
        }
    }

    /**
     * 读取 PNG 文件并转换为 Base64 编码的字符串
     * 
     * @param pngFilePath PNG 文件路径
     * @return Base64 编码的字符串，格式为 "data:image/png;base64,...."
     */
    public static String getPngBase64String(String pngFilePath) {
        try {
            File pngFile = new File(pngFilePath);
            BufferedImage image = ImageIO.read(pngFile);
            if (image == null) {
                log.error("无法读取 PNG 文件，可能格式不受支持: {}", pngFilePath);
                return null;
            }
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            String base64String = java.util.Base64.getEncoder().encodeToString(imageBytes);
            String base64Str = "data:image/png;base64," + base64String;
            return base64Str;
        } catch (IOException e) {
            log.error("读取 PNG 文件失败: {}", e.getMessage());
            return null;
        }
    }
}
