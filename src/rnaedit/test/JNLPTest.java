/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rnaedit.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Denny Chen Dai
 */
public class JNLPTest {

    public static void main(String[] args) {



        try {
            JNLPTest jtest = new JNLPTest();
            jtest.executeRemote();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public JNLPTest() {
    }

    public void executeRemote() throws IOException {

        this.downloadJarFile("http://www.sfu.ca/~cda18/rna/lib/energy.jar", "test.jar");
        extractZipFileAnother("test.jar", "");
        
    }

    public void extractJarFile(String jarname) {
        try {

            FileOutputStream out = null;
            /*extract resource in a jar file*/
            JarResources jr = new JarResources(jarname);



            byte[] code = jr.getResource("vienna/RNAeval");

            /*create the new file on local disk*/
            File tmpFile1 = new File("RNAeval");
            out = new FileOutputStream(tmpFile1);
            out.write(code, 0, code.length);
            out.close();

            /*change to executable*/
            runCommand("chmod +x " + "RNAeval");

        } catch (IOException ex) {
            Logger.getLogger(JNLPTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void extractZipFileAnother(String filename, String destinationname) {
        try {

            ZipFile zipFile = new ZipFile(filename);

            Enumeration entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();

                if (entry.isDirectory()) {
                    // Assume directories are stored parents first then children.
                    System.err.println("Extracting directory: " + entry.getName());
                    // This is not robust, just for demonstration purposes.
                    (new File(entry.getName())).mkdir();
                } else {
                    System.err.println("Extracting file: " + entry.getName());
                    extractFile(zipFile, entry);
                }
            }

            zipFile.close();
        } catch (IOException ioe) {
            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
            return;
        }
    }

    /**
     * Extract single file into disk
     * 
     * @param filename Full path file name
     */
    private void extractFile(ZipFile zipFile, ZipEntry src) {

        String filename = src.getName();

        /*check existence of subdirectory*/
        String[] paths = filename.split("/");
        String curP = "";
        for (int i = 0; i < paths.length; i++) {
            if (paths[i] != null && !paths[i].equals("") && i < paths.length - 1) {
                /*check existience of directory, if not, create it*/
                curP += paths[i] + "/";
                if (!(new File( curP )).exists()) {
                    (new File( curP )).mkdir();
                }
                
            }
        }


        /*real single file name*/
        //filename = paths[paths.length - 1];
        try {
            /*now extract the real single files*/
            copyInputStream(zipFile.getInputStream(src),
                    new BufferedOutputStream(new FileOutputStream(filename)));
        } catch (IOException ex) {
            Logger.getLogger(JNLPTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Extract files to disk
     * 
     * @param in
     * @param out
     * @throws java.io.IOException
     */
    private void copyInputStream(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }

    public void extractZipFiles(String filename, String destinationname) {
        try {

            //String destinationname = "d:\\servlet\\testZip\\";
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(
                    new FileInputStream(filename));

            zipentry = zipinputstream.getNextEntry();
            while (zipentry != null) {
                //for each entry to be extracted
                String entryName = zipentry.getName();
                System.out.println("entryname " + entryName);
                int n;
                FileOutputStream fileoutputstream;
                File newFile = new File(entryName);
                String directory = newFile.getParent();

                if (directory == null) {
                    if (newFile.isDirectory()) {
                        break;
                    }
                }

                fileoutputstream = new FileOutputStream(
                        destinationname + entryName);

                while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                    fileoutputstream.write(buf, 0, n);
                }
                fileoutputstream.close();
                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();

            }//while

            zipinputstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadJarFile(String urlF, String output) {
        try {
            URL url = new URL(urlF);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);


            connection.connect();
            //get jar file length from response header
            int length = connection.getContentLength();

            InputStream in = connection.getInputStream();
            FileOutputStream out = new FileOutputStream(output);


            int bytesRead = 0;
            byte[] bytes = new byte[4096];

            while ((bytesRead = in.read(bytes)) != -1) {
                //put the progress code here
                out.write(bytes, 0, bytesRead);
            }

            in.close();
            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(JNLPTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String execute() throws IOException {
        String fileName = "code";

        /*extract resource in a jar file*/
        JarResources jr = new JarResources("energy.jar");
        byte[] code = jr.getResource("vienna/RNAeval");

        /*create the new file on local disk*/
        File tmpFile1 = new File(fileName);
        FileOutputStream output1 = new FileOutputStream(tmpFile1);
        output1.write(code, 0, code.length);
        output1.close();

        /*change to executable*/
        runCommand("chmod +x " + fileName);


        /*execute the command*/
        String outputMessage1 = runCommand(fileName);








        ClassLoader loader = this.getClass().getClassLoader();
        /* jnitest is an binary program running on Solaris OS.
        It is packaged in a Jar file of libjnitest.jar */
        URL url = loader.getResource("jnitest");
        /* Note: the size of the buffer must not less than the size of jnitest */
        byte[] buf = new byte[20000];
        int len = 0;
        JarURLConnection uc = (JarURLConnection) url.openConnection();
        uc.connect();
        InputStream in = new BufferedInputStream(uc.getInputStream());
        len = in.read(buf);
        in.close();



        File tmpFile = new File(fileName);

        FileOutputStream output = new FileOutputStream(tmpFile);

        output.write(buf, 0, len);

        output.close();

        runCommand("chmod +x " + fileName);

        /* Execute the executive native program, and get its output message */

        String outputMessage = runCommand(fileName);

        return outputMessage;

    }

    private String runCommand(String command) {

        String[] cmd0 = {"bash", "-c", command};

        try {

            Process ps = Runtime.getRuntime().exec(cmd0);

            String str1 = loadStream(ps.getInputStream());

            return str1;

        } catch (IOException ioe) {

            System.err.println("Error: running command on linux.");
        }

        return null;

    }

    private String loadStream(InputStream input) throws IOException {

        int count;

        int BUFFER = 20;

        byte[] data = new byte[BUFFER];

        input = new BufferedInputStream(input);

        StringBuffer buff = new StringBuffer();

        while ((count = input.read(data, 0, BUFFER)) != -1) {
            buff = buff.append(new String(data, 0, count));
        }
        return buff.toString();

    }
}
