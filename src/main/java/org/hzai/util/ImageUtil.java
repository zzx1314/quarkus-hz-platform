package org.hzai.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
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
        try (FileInputStream fis = new FileInputStream(pgmFilePath)) {

            // ---- 读取头部 ----
            StringBuilder header = new StringBuilder();
            int newlineCount = 0;
            while (newlineCount < 3) { // P5 一般三行头部
                int ch = fis.read();
                if (ch == -1) throw new IOException("PGM 文件不完整或格式错误");
                header.append((char) ch);
                if (ch == '\n') newlineCount++;
            }

            String[] lines = header.toString().split("\n");
            String magic = lines[0].trim();
            if (!magic.equals("P5")) {
                System.err.println("当前仅支持 P5（二进制）PGM 格式");
                return;
            }

            // 第二行：宽高
            String[] wh = lines[1].trim().split("\\s+");
            int width = Integer.parseInt(wh[0]);
            int height = Integer.parseInt(wh[1]);
            int maxVal = Integer.parseInt(lines[2].trim());

            boolean is16bit = maxVal > 255;
            System.out.printf("检测到 P5 图像: %d×%d, %d-bit 深度%n", width, height, is16bit ? 16 : 8);

            // ----读取像素数据 ----
            int pixelBytes = is16bit ? 2 : 1;
            byte[] data = fis.readNBytes(width * height * pixelBytes);
            if (data.length < width * height * pixelBytes) {
                throw new IOException("像素数据长度不足，PGM 文件可能损坏");
            }

            // ----转换灰度并生成图像 ----
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            int idx = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int val;
                    if (is16bit) {
                        // PGM 标准：高字节在前（big-endian）
                        int hi = data[idx++] & 0xFF;
                        int lo = data[idx++] & 0xFF;
                        val = (hi << 8) | lo;
                    } else {
                        val = data[idx++] & 0xFF;
                    }

                    // 按 maxVal 缩放到 0–255
                    int gray = (val * 255) / maxVal;
                    int rgb = (gray << 16) | (gray << 8) | gray;
                    image.setRGB(x, y, rgb);
                }
            }

            // ----输出 PNG ----
            ImageIO.write(image, "png", new File(pngFilePath));
            System.out.println("转换完成！输出文件: " + pngFilePath);

        } catch (Exception e) {
            System.err.println("转换失败: " + e.getMessage());
            e.printStackTrace();
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
