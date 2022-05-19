import com.github.sarxos.webcam.Webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.Converter;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Recorder {
    public static void main(String[] args) {
        Recorder videoWriter = new Recorder();
        videoWriter.startVideoRecording();
    }
    private void startVideoRecording() {
        File saveFile = new File("D:\\Java\\AAA\\Project_2\\Videos\\saved_3.mp4");

        // Инициализация
        IMediaWriter writer = ToolFactory.makeWriter(saveFile.getAbsolutePath());
        // Размер
        Dimension size = WebcamResolution.VGA.getSize();

        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, 640, 480);

        System.out.println("Size: " + size.width);
        System.out.println("Size: " + size.height);

        long start = System.currentTimeMillis();
        Webcam webcam = openWebcam(size);

        for (int i = 0; i < 100; i++) {
            BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
            IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

            IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
            frame.setKeyFrame(i == 0);
            frame.setQuality(100);

            writer.encodeVideo(0, frame);

            try {
                Thread.sleep(2 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writer.close();
        System.out.println("Video recorded to the file: " + saveFile.getAbsolutePath());
    }
    private Webcam openWebcam(Dimension size) {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(size);
        webcam.open();
        return webcam;
    }
}
