package com.echo.util;

import com.echo.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Random;

public class ImgUtils {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd:HH mm ss");
    public static final Random r = new Random();

    //缩略图的的生成
    public static String generatorThumbnail(ImageHolder thumbnail, String targetAddr) {

        //图片的随机名
        String realFileName = getRadomFileName();

        //图片的扩展名
        String extention = getFileExentionName(thumbnail.getImageName());

        //创建文件夹
        makeDirPath(targetAddr);

        //图片的相对路径
        String realtiveAddr = targetAddr + realFileName + extention;

        //图片的绝对路径
        File dest = new File(PathUtils.getImgBasePath() + realtiveAddr);

        //对图片加上水印
        try {
            Thumbnails.of(thumbnail.getImageInputStream()).size(200, 200).
                    watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "\\test.JPG")), 0.5f)
                    .outputQuality(0.8).toFile(dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realtiveAddr;
    }

    //随机获取文件名5位随机数+日期
    public static String getRadomFileName() {
        int randomNum = r.nextInt(89999) + 10000;
        // String nowTime = sFormat.format(new Date());
        return String.valueOf(randomNum);
    }

    //创建文件的路径
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtils.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    //获取文件的扩展名
    public static String getFileExentionName(String imageName) {
        return imageName.substring(imageName.lastIndexOf("."));
    }

    public static void main(String[] args) throws Exception {
        Thumbnails.of(new File("D:\\javaEE\\ShopImgs\\1.PNG")).size(200, 200).
                watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "\\test.JPG")), 0.5f)
                .outputQuality(0.8).toFile(new File("D:\\javaEE\\ShopImgs\\2.PNG"));
    }

    //shop更新后图片的删除
    public static void deleteFileOrPath(String storePath) {
        File filePath = new File(PathUtils.getImgBasePath() + storePath);
        //判断该文件是目录还是文件
        if (filePath.exists()) {
            if (filePath.isDirectory()) {
                File files[] = filePath.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            //文件直接删除
            filePath.delete();
        }
    }
}
