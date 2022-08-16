package com.hq.es;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class PicYasuo {

    @Test
    public void testya() throws IOException {


    }

    public static void main(String[] args) throws IOException {
     /*   FileInputStream fileInputStream = new FileInputStream("C:\\Users\\heqin11\\Desktop\\优化前.jpg");
        Thumbnails.of(fileInputStream).scale(1f).outputQuality(0.1).toFile(new File("C:\\Users\\heqin11\\Desktop\\优化后.jpg"));*/

/*
        try {
            //图片所在路径
            BufferedImage templateImage = ImageIO.read(new File("C:\\Users\\heqin11\\Desktop\\优化前.png"));


            //原始图片的长度和宽度
            int height = templateImage.getHeight();
            int width = templateImage.getWidth();

            //通过比例压缩
            float scale = 0.5f;



            //压缩之后的长度和宽度
            int doWithHeight = (int) (scale * height);
            int dowithWidth = (int) (scale * width);



            BufferedImage finalImage = new BufferedImage(dowithWidth, doWithHeight, BufferedImage.TYPE_INT_RGB);

            finalImage.getGraphics().drawImage(templateImage.getScaledInstance(dowithWidth, doWithHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);


            //图片输出路径，以及图片名
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\heqin11\\Desktop\\优化后.png");
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
            encoder.encode(finalImage);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


}
