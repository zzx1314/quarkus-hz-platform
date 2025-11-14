package org.hzai.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUtil {

    private static String readNextLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = in.read()) != -1) {
            if (ch == '\n')
                break;
            sb.append((char) ch);
        }
        return sb.toString().trim();
    }

    public static void pgmToPng(String pgmFilePath, String pngFilePath) {
        try (InputStream fis = new FileInputStream(pgmFilePath)) {
            // 1. 读取 magic
            String magic;
            while (true) {
                magic = readNextLine(fis);
                if (!magic.startsWith("#") && !magic.isEmpty())
                    break;
            }
            if (!magic.equals("P5"))
                throw new IOException("仅支持 P5 PGM");

            // 2. 读取宽高
            String whLine;
            while (true) {
                whLine = readNextLine(fis);
                if (!whLine.startsWith("#") && !whLine.isEmpty())
                    break;
            }
            String[] wh = whLine.split("\\s+");
            int width = Integer.parseInt(wh[0]);
            int height = Integer.parseInt(wh[1]);

            // 3. 读取 maxVal
            String maxValLine;
            while (true) {
                maxValLine = readNextLine(fis);
                if (!maxValLine.startsWith("#") && !maxValLine.isEmpty())
                    break;
            }
            int maxVal = Integer.parseInt(maxValLine.trim());
            boolean is16bit = maxVal > 255;

            System.out.printf("检测到 P5 图像: %d×%d, %d-bit 深度%n", width, height, is16bit ? 16 : 8);

            // 4. 读取像素数据
            int pixelBytes = is16bit ? 2 : 1;
            byte[] data = fis.readNBytes(width * height * pixelBytes);
            if (data.length < width * height * pixelBytes)
                throw new IOException("像素数据长度不足，PGM 文件可能损坏");

            // 5. 生成图像
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            int idx = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int val;
                    if (is16bit) {
                        int hi = data[idx++] & 0xFF;
                        int lo = data[idx++] & 0xFF;
                        val = (hi << 8) | lo;
                    } else {
                        val = data[idx++] & 0xFF;
                    }
                    int gray = (val * 255) / maxVal;
                    int rgb = (gray << 16) | (gray << 8) | gray;
                    image.setRGB(x, y, rgb);
                }
            }

            ImageIO.write(image, "png", new File(pngFilePath));
            System.out.println("转换完成！输出文件: " + pngFilePath);
        } catch (IOException e) {
            log.error("转换 PGM 文件失败: {}", e.getMessage());
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
