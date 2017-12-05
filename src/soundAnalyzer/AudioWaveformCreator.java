package soundAnalyzer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioWaveformCreator {

    //AudioInputStream audioInputStream;
    String imageFilename;
    String imagePath;
    String soundName;
    AudioInputStream decoded;
    InputStream fileDecoded;
    byte[] data;
    int percentage = 9;
    int base = 32768;
    int sensitivity = base / percentage;
    Vector<Line2D.Double> lines = new Vector<Line2D.Double>();
    String errStr;
    Capture capture = new Capture();
    double duration, seconds;
    File file;
    String fileName;
    SamplingGraph samplingGraph;
    String waveformFilename;
    soundRetriever sr;
    Color imageBackgroundColor = new Color(235, 235, 235);  //xrwma basil
    //Color imageBackgroundColor = new Color(255, 255, 255);  //xrwma elenhs
    //AudioInputStream in;
    double durationInSeconds = 0;
    int secondsPerScreen = 10;
    int resolution = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();

    int fpx;
    File sound;

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public AudioInputStream getDecoded() {
        return decoded;
    }

    public void setDecoded(AudioInputStream decoded) {
        this.decoded = decoded;
    }

    public String getDecodedPath() {
        return sr.decodedPath;
    }

    public InputStream getFileDecoded() {
        return fileDecoded;
    }

    public void setFileDecoded(InputStream fileDecoded) {
        this.fileDecoded = fileDecoded;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    public Vector<Line2D.Double> getLines() {
        return lines;
    }

    public void setLines(Vector<Line2D.Double> lines) {
        this.lines = lines;
    }

    public String getErrStr() {
        return errStr;
    }

    public void setErrStr(String errStr) {
        this.errStr = errStr;
    }

    public Capture getCapture() {
        return capture;
    }

    public void setCapture(Capture capture) {
        this.capture = capture;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getSeconds() {
        return seconds;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return soundName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public SamplingGraph getSamplingGraph() {
        return samplingGraph;
    }

    public void setSamplingGraph(SamplingGraph samplingGraph) {
        this.samplingGraph = samplingGraph;
    }

    public String getWaveformFilename() {
        return waveformFilename;
    }

    public void setWaveformFilename(String waveformFilename) {
        this.waveformFilename = waveformFilename;
    }

    public soundRetriever getSr() {
        return sr;
    }

    public void setSr(soundRetriever sr) {
        this.sr = sr;
    }

    public Color getImageBackgroundColor() {
        return imageBackgroundColor;
    }

    public void setImageBackgroundColor(Color imageBackgroundColor) {
        this.imageBackgroundColor = imageBackgroundColor;
    }

    public float getDurationInSeconds() {
        return (float) durationInSeconds;
    }

    public void setDurationInSeconds(float durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public int getSecondsPerScreen() {
        return secondsPerScreen;
    }

    public void setSecondsPerScreen(int secondsPerScreen) {
        this.secondsPerScreen = secondsPerScreen;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int getFpx() {
        return fpx;
    }

    public void setFpx(int fpx) {
        this.fpx = fpx;
    }

    public AudioWaveformCreator(String fileName) throws UnsupportedAudioFileException, IOException {
        soundName = fileName;
        //System.out.println("Resolution is "+ resolution);
        sr = new soundRetriever(fileName);

        waveformFilename = "";

        sound = new File(sr.getDecoded());
        sound.deleteOnExit();

        //URL url = this.getClass().getClassLoader().getResource("temp\\" + sound.getName());
        //InputStream  url= this.getClass().getClassLoader().getResourceAsStream("temp\\" + sound.getName());
        // System.out.println("1");
        StringTokenizer Tok = new StringTokenizer(sound.getName(), ".");
        //   System.out.println("2");
        waveformFilename = Tok.nextToken();
        //   System.out.println("3");
        decoded = AudioSystem.getAudioInputStream(sound);
        System.out.println("right after the creation decoded.getFrameLength() is "+decoded.getFrameLength());
        //  System.out.println("4");
        AudioFormat format = decoded.getFormat();
        // System.out.println("Format: " + format.toString());
        long audioFileLength = sound.length();
        // System.out.println("File length: " + audioFileLength);
        int frameSize = format.getFrameSize();
        // System.out.println("Frame size: " + frameSize);
        float frameRate = format.getFrameRate();
        // System.out.println("Frame Rate: " + frameRate);

        durationInSeconds = (audioFileLength / (frameSize * frameRate));
        //System.out.println("Frame Rate: " + frameRate);

        // fileDecoded = this.getClass().getClassLoader().getResourceAsStream(sr.getDecoded());
        // decoded = AudioSystem.getAudioInputStream(new File(sr.getDecoded()));
        /*try {
         data = sr.rawpcm();
         } catch (LineUnavailableException ex) {
         Logger.getLogger(AudioWaveformCreator.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        this.waveformFilename = waveformFilename;

        /*  byte[] audioBytes = new byte[
         (int) (decoded.getFrameLength() 
         * decoded.getFormat().getFrameSize())];
                   
         decoded.read(audioBytes);*/
    }

    public void closeStreams() {
        try {
            decoded.close();
        }
        catch (IOException ex) {
            Logger.getLogger(AudioWaveformCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createAudioInputStream() throws Exception {

        try {
            errStr = null;
            long milliseconds = (long) ((decoded.getFrameLength() * 1000) / decoded.getFormat().getFrameRate());
            System.out.println("decoded.getFrameLength()->"+decoded.getFrameLength());
            System.out.println("millis "+milliseconds);
            duration = milliseconds / 1000.0;
            samplingGraph = new SamplingGraph();
            samplingGraph.createWaveForm(null);
           
        }
        catch (Exception ex) {
            reportStatus("TEST " + "AN EXCEPTION " + ex.toString());
            throw ex;
        }
        //sr.closeStreams();

    }

    public String getImage() {
        return imagePath;
    }

    /**
     * Render a WaveForm.
     */
    class SamplingGraph implements Runnable {

        private Thread thread;
        private Font font10 = new Font("serif", Font.PLAIN, 10);
        private Font font12 = new Font("serif", Font.PLAIN, 12);
        Color jfcBlue = new Color(000, 000, 255);
        Color pink = new Color(255, 175, 175);
        int w = 0;

        int h = 200;
        String s = "";

        public SamplingGraph() {
        }

        public void createWaveForm(byte[] audioBytes) {
            // System.out.println("frame rate "+(decoded.getFrameLength()/(decoded.getFormat().getFrameRate()/1000)));
            w = (int) ((decoded.getFrameLength() / (decoded.getFormat().getFrameRate() / 1000)) / (secondsPerScreen));
            

            System.out.println(w+" -> w");
            System.out.println("One by one\n"+decoded.getFrameLength()+"->decoded.getFrameLength()\n"+decoded.getFormat().getFrameRate()+"->\ndecoded.getFormat().getFrameRate()"+secondsPerScreen+"->secondsPerScreenn");
            lines.removeAllElements();  // clear the old vector

            AudioFormat format = decoded.getFormat();
            if (audioBytes == null) {
                try {
                    audioBytes = new byte[decoded.available()];

                    decoded.read(audioBytes);
                    //System.out.println(audioBytes);
                }
                catch (Exception ex) {
                    reportStatus(ex.getMessage() + ex);
                    return;
                }
            }
            //System.out.println(audioBytes.length);

            int[] audioData = null;
            if (format.getSampleSizeInBits() == 16) {
                int nlengthInSamples = audioBytes.length / 2;
                audioData = new int[nlengthInSamples];
                if (format.isBigEndian()) {
                    for (int i = 0; i < nlengthInSamples; i++) {
                        /* First byte is MSB (high order) */
                        int MSB = (int) audioBytes[2 * i];
                        /* Second byte is LSB (low order) */
                        int LSB = (int) audioBytes[2 * i + 1];
                        audioData[i] = MSB << 8 | (255 & LSB);
                    }
                }
                else {
                    for (int i = 0; i < nlengthInSamples; i++) {
                        /* First byte is LSB (low order) */
                        int LSB = (int) audioBytes[2 * i];
                        /* Second byte is MSB (high order) */
                        int MSB = (int) audioBytes[2 * i + 1];
                        audioData[i] = MSB << 8 | (255 & LSB);
                    }
                }
            }
            else if (format.getSampleSizeInBits() == 8) {
                int nlengthInSamples = audioBytes.length;
                audioData = new int[nlengthInSamples];
                if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
                    for (int i = 0; i < audioBytes.length; i++) {
                        audioData[i] = audioBytes[i];
                    }
                }
                else {
                    for (int i = 0; i < audioBytes.length; i++) {
                        audioData[i] = audioBytes[i] - 128;
                    }
                }
            }
            System.out.println(format.getFrameSize());
            System.out.println(w);
            int frames_per_pixel = (audioBytes.length / format.getFrameSize() / w);
             System.out.println("Got through the framesize");
            fpx = frames_per_pixel;

            byte my_byte = 0;
            double y_last = 0;
            int numChannels = format.getChannels();
            for (double x = 0; x < w && audioData != null; x++) {
                int idx = (int) (frames_per_pixel * numChannels * x);
                if (format.getSampleSizeInBits() == 8) {
                    my_byte = (byte) audioData[idx];
                }
                else {
                    my_byte = (byte) (128 * audioData[idx] / sensitivity);
                    s += my_byte + " ";
                }
                double y_new = (double) ((h * 0.5) * (250 - my_byte) / 256);
                lines.add(new Line2D.Double(x, y_last, x, y_new));
                y_last = y_new;
            }
            saveToFile(waveformFilename);

        }

        public void saveToFile(String filename) {

            int INFOPAD = 15;

            BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bufferedImage.createGraphics();

            createSampleOnGraphicsContext(w, h, INFOPAD, g2);
            //g2.dispose();
            g2.setPaint(Color.red);
            g2.setFont(new Font("Serif", Font.BOLD, 20));

            FontMetrics fm = g2.getFontMetrics();
            int x = bufferedImage.getWidth() - fm.stringWidth(s) - 5;
            int y = fm.getHeight();

            g2.dispose();
            // Write generated image to a file
            try {
                // Save as PNG
                File tempdir = new File(System.getProperty("java.io.tmpdir") + "/karaok");

                File file = File.createTempFile(filename, ".png", tempdir);
                //System.out.println("File path: "+file.getName());
                file.deleteOnExit();
                ImageIO.write(bufferedImage, "png", file);
                imageFilename = file.getName();
                imagePath = file.getAbsolutePath();
            }
            catch (IOException e) {
            }
        }

        public int getSpx() {
            return fpx;
        }

        private void createSampleOnGraphicsContext(int w, int h, int INFOPAD, Graphics2D g2) {
            g2.setBackground(imageBackgroundColor);
            g2.clearRect(0, 0, w, h);
            g2.setColor(Color.white);
            g2.fillRect(0, h - INFOPAD, w, INFOPAD);

            if (errStr != null) {
                g2.setColor(jfcBlue);
                g2.setFont(new Font("serif", Font.BOLD, 18));
                g2.drawString("ERROR", 5, 20);
                AttributedString as = new AttributedString(errStr);
                as.addAttribute(TextAttribute.FONT, font12, 0, errStr.length());
                AttributedCharacterIterator aci = as.getIterator();
                FontRenderContext frc = g2.getFontRenderContext();
                LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
                float x = 5, y = 25;
                lbm.setPosition(0);
                while (lbm.getPosition() < errStr.length()) {
                    TextLayout tl = lbm.nextLayout(w - x - 5);
                    if (!tl.isLeftToRight()) {
                        x = w - tl.getAdvance();
                    }
                    tl.draw(g2, x, y += tl.getAscent());
                    y += tl.getDescent() + tl.getLeading();
                }
            }
            else if (capture.thread != null) {
                g2.setColor(Color.black);
                g2.setFont(font12);
                //g2.drawString("Length: " + String.valueOf(seconds), 3, h-4);
            }
            else {
                g2.setColor(Color.GRAY);
                g2.setFont(font12);
                //g2.drawString("File: " + fileName + "  Length: " + String.valueOf(duration) + "  Position: " + String.valueOf(seconds), 3, h-4);
                /*int counter = 0;
                 for (float i = 0; i < (resolution * (durationInSeconds / secondsPerScreen));) {

                 g2.drawString(counter + "", i, h - 4);
                 if ((resolution / secondsPerScreen) < 50) {
                 int check = 0;
                 while (check < 50) {
                 i += (resolution / secondsPerScreen);
                 counter++;
                 check += (resolution / secondsPerScreen);
                 }
                 }

                 i += (resolution / secondsPerScreen);
                 counter++;

                 }*/
                if (decoded != null) {
                    // .. render sampling graph ..
                    g2.setColor(jfcBlue);
                    for (int i = 1; i < lines.size(); i++) {
                        g2.draw((Line2D) lines.get(i));
                    }

                    // .. draw current position ..
                    if (seconds != 0) {
                        double loc = seconds / duration * w;
                        g2.setColor(pink);
                        g2.setStroke(new BasicStroke(3));
                        g2.draw(new Line2D.Double(loc, 0, loc, h - INFOPAD - 2));
                    }
                }
            }
        }

        public void start() {
            thread = new Thread(this);
            thread.setName("SamplingGraph");
            thread.start();
            seconds = 0;
        }

        public void stop() {
            if (thread != null) {
                thread.interrupt();
            }
            thread = null;
        }

        public void run() {
            seconds = 0;
            while (thread != null) {
                if ((capture.line != null) && (capture.line.isActive())) {
                    long milliseconds = (long) (capture.line.getMicrosecondPosition() / 1000);
                    seconds = milliseconds / 1000.0;
                }
                try {
                    thread.sleep(100);
                }
                catch (Exception e) {
                    break;
                }
                while ((capture.line != null && !capture.line.isActive())) {
                    try {
                        thread.sleep(10);
                    }
                    catch (Exception e) {
                        break;
                    }
                }
            }
            seconds = 0;
        }
    } // End class SamplingGraph

    /**
     * Reads data from the input channel and writes to the output stream
     */
    class Capture implements Runnable {

        TargetDataLine line;
        Thread thread;

        public void start() {
            errStr = null;
            thread = new Thread(this);
            thread.setName("Capture");
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        private void shutDown(String message) {
            if ((errStr = message) != null && thread != null) {
                thread = null;
                samplingGraph.stop();
                System.err.println(errStr);
            }
        }

        public void run() {

            duration = 0;
            decoded = null;

            // define the required attributes for our line, 
            // and make sure a compatible line is supported.
            AudioFormat format = decoded.getFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                    format);

            if (!AudioSystem.isLineSupported(info)) {
                shutDown("Line matching " + info + " not supported.");
                return;
            }

            // get and open the target data line for capture.
            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format, line.getBufferSize());
            }
            catch (LineUnavailableException ex) {
                shutDown("Unable to open the line: " + ex);
                return;
            }
            catch (SecurityException ex) {
                shutDown("AN EXCEPTION " + ex.toString());
                //JavaSound.showInfoDialog();
                return;
            }
            catch (Exception ex) {
                shutDown("AN EXCEPTION " + ex.toString());
                return;
            }

            // play back the captured audio data
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead;

            line.start();

            while (thread != null) {
                if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                    break;
                }
                out.write(data, 0, numBytesRead);
            }

            // we reached the end of the stream.  stop and close the line.
            line.stop();
            line.close();
            line = null;

            // stop and close the output stream
            try {
                out.flush();
                out.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }

            // load bytes into the audio input stream for playback
            byte audioBytes[] = out.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
            decoded = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);

            long milliseconds = (long) ((decoded.getFrameLength() * 1000) / format.getFrameRate());
            duration = milliseconds / 1000.0;

            try {
                decoded.reset();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return;
            }

            samplingGraph.createWaveForm(audioBytes);
        }
    } // End class Capture    

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            printUsage();
            System.exit(1);
        }
        AudioWaveformCreator awc = new AudioWaveformCreator(args[0]);
        awc.createAudioInputStream();
    }

    private void reportStatus(String msg) {
        if ((errStr = msg) != null) {
            System.out.println(errStr);
        }
    }

    private static void printUsage() {
        System.out.println("AudioWaveformCreator usage: java AudioWaveformCreator.class [path to audio file for generating the image] [path to save waveform image to]");
    }
}
