/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundAnalyzer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author Basil
 */
public class soundRetriever {

    AudioInputStream ais;
    AudioInputStream din;
    String decodedPath;
    AudioInputStream in;
    AudioFormat decodedFormat;
    AudioInputStream pcm;

    public soundRetriever(String path) {

        try {

            int i = 0;
            File sound = new File(path);

            //InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
            //in = AudioSystem.getAudioInputStream(sound);
            in = AudioSystem.getAudioInputStream(sound);

            AudioFormat baseFormat = in.getFormat();

            decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);

            pcm = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, in);

            /* File folder = new File("temp");
             if (!folder.isDirectory()) {
             if (!folder.mkdir()) {
             System.out.println("Not enough permissions to mkdir. Exiting with code -2");
             exit(-2);
             }
             }*/
            StringTokenizer Tok = new StringTokenizer(sound.getName(), ".");

            //File tempFile = new File("temp\\"+Tok.nextToken()+".wav");
            String fileName = "";
            String tempString = "";
            while (Tok.hasMoreElements()) {
                tempString = Tok.nextToken();
                if (Tok.hasMoreElements()) {
                    fileName += tempString;
                }
                else{break;}
            }
            while(fileName.length()<3)
            {
              fileName=' '+fileName;
            }
            //File roots[]=File.listRoots();
            //System.out.println(System.getProperty("java.io.tmpdir"));
            File tempdir = new File(System.getProperty("java.io.tmpdir")+"/karaok");
            tempdir.mkdir();
            File ntf = File.createTempFile(fileName, ".wav",tempdir);
            
            ntf.deleteOnExit();
            //tempFile.deleteOnExit();
            decodedPath = ntf.getAbsolutePath();
            System.out.println(decodedPath);
            AudioSystem.write(pcm, AudioFileFormat.Type.WAVE, ntf);

            //System.out.println("Sound decoding complete. Path to file" + decodedPath);
            closeStreams();
            //din = new AudioInputStream(in, decodedFormat, (sound.length() / decodedFormat.getFrameSize()));
            //System.out.println("MP3: " + in.available() + " PCM: " + din.available());
            //rawplay(decodedFormat, din);
            //rawpcm();
            //in.close();
            /*AudioInputStream source = AudioSystem.getAudioInputStream(in);
             AudioInputStream pcm = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, in);
             AudioInputStream ulaw = AudioSystem.getAudioInputStream(AudioFormat.Encoding.ULAW, pcm);*/
            //File tempFile = new File(path + "1.wav");
            //AudioSystem.write(din, AudioFileFormat.Type.WAVE, tempFile);
        } catch (Exception e) {
            System.out.println("Exception thrown at soundReceiver " + e.getLocalizedMessage());
        }

    }

    public String getDecoded() {

        return decodedPath;
    }

    public void closeStreams() {

        try {
            pcm.close();
        } catch (IOException ex) {
            Logger.getLogger(soundRetriever.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(soundRetriever.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void rawplay(AudioFormat targetFormat, AudioInputStream din2) throws IOException, LineUnavailableException {
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(decodedFormat);
        if (line != null) {
            // Start

            line.start();
            int nBytesRead = 0, nBytesWritten = 0;
            while (nBytesRead != -1) {
                nBytesRead = din.read(data, 0, data.length);
                System.out.println("NOWER " + din.available());
                if (nBytesRead != -1) {
                    nBytesWritten = line.write(data, 0, nBytesRead);
                }
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }

    public byte[] rawpcm() throws IOException, LineUnavailableException {
        byte[] data = new byte[4096];
        System.out.println("NOW " + din.available());
        FileOutputStream out = new FileOutputStream("templar.wav");

        int nBytesRead = 0, nBytesWritten = 0;
        /*while (nBytesRead != -1) {
         din.read(data, 0, data.length);

         out.write(data);
            
         //AudioSystem.write(din, AudioFileFormat.Type.WAVE, out);
         }*/

        while (nBytesRead != -1) {
            nBytesRead = din.read(data, 0, data.length);
            System.out.println("NOWER " + din.available());
            if (nBytesRead != -1) {
                AudioSystem.write(din, AudioFileFormat.Type.WAVE, out);
            }
        }

        out.close();
        System.out.println("Complete");
        din.close();
        out.close();
        return data;
    }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }
}
