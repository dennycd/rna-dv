/********************************************************************************************
*  RNA-DV Version 1.0
*  Copyright (c)  2008 September  Denny Chen Dai, Herbert H. Tsang.
*  Permission is granted to copy, distribute and/or modify this document
*  under the terms of the GNU Free Documentation License, Version 1.2
*  or any later version published by the Free Software Foundation;
*  with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
*  Texts.  A copy of the license is included in the section entitled "GNU
*  Free Documentation License".
*********************************************************************************************/

package rnadv;

import java.net.MalformedURLException;
import rnaedit.test.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author Denny Chen Dai
 */
public class JarUpdate {

    private String dir_escape = "";
    private static String URL = "http://www.sfu.ca/~cda18/rna/";

    public JarUpdate() {

        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            dir_escape = "\\";
        } else {
            dir_escape = "/";
        }
    }

    /**
     * Download specified jar file and extract to current working directory
     * 
     * @param url  The url containing the jar file resource
     * @param destDir destination directory for extraction
     * @param jarName Targetted temperatory jar file 
     * @throws java.io.IOException
     */
    public void executeRemote(String url, String jarName, String destDir, boolean extract) {

        this.downloadJarFile(url, destDir + File.separator + jarName);
        if (extract) {
            extractZipFileAnother(destDir + File.separator + jarName, destDir);
        }
    }

    /**
     * Extract all jar file content into current folder
     * 
     * @param filename the source jar file name 
     * @param destinationname Destination - not used
     */
    public void extractZipFileAnother(String filename, String destDir) {
        try {

            ZipFile zipFile = new ZipFile(filename);

            Enumeration entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();

                if (entry.isDirectory()) {
                    // Assume directories are stored parents first then children.
                    System.out.println("Extracting directory: " + entry.getName());
                    // This is not robust, just for demonstration purposes.
                    (new File(destDir + File.separator + entry.getName())).mkdir();
                } else {
                    System.out.println("Extracting file: " + entry.getName());
                    extractFile(zipFile, entry, destDir);
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
     * @param zipFile The complete zip file handler
     */
    private void extractFile(ZipFile zipFile, ZipEntry src, String destDir) {

        String filename = src.getName();
        /*check existence of subdirectory*/
        String[] paths = filename.split("/");
        String curP = "";
        for (int i = 0; i < paths.length; i++) {
            if (paths[i] != null && !paths[i].equals("") && i < paths.length - 1) {
                /*check existience of directory, if not, create it*/
                curP += paths[i] + dir_escape;
                if (!(new File(destDir + File.separator + curP)).exists()) {
                    (new File(destDir + File.separator + curP)).mkdir();
                }

            }
        }
        /*real single file name*/
        //filename = paths[paths.length - 1];
        try {
            /*now extract the real single files*/
            copyInputStream(zipFile.getInputStream(src),
                    new BufferedOutputStream(new FileOutputStream(destDir + File.separator + filename)));
            /*make the file executable*/
            if (!System.getProperty("os.name").startsWith("Windows")) {
                runCommand("chmod +x " + destDir + File.separator + filename);
            }

        } catch (IOException ex) {
            Logger.getLogger(JarUpdate.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * Download sample CT file and store in ct directory
     * @param filename
     */
    public void downloadCTSample(String filename, String destDir) {
        /*make lib dir*/
        if (!(new File(destDir + File.separator + "ct")).exists()) {
            (new File(destDir + File.separator + "ct")).mkdir();
        /*download jar and put in ct*/
        }
        System.out.println("Downloading sample file...." + filename);
        executeRemote(URL + "ct/" + filename,
                "ct" + dir_escape + filename, destDir, false);
    }

    /**
     * download single jar file and put into "lib" directory
     * 
     * param load Flag to load jar library into java environment
     */
    public void downloadJarLibrary(String filename, String destDir, boolean load) {

        /*make lib dir*/
        if (!(new File(destDir + File.separator + "lib")).exists()) {
            (new File(destDir + File.separator + "lib")).mkdir();
        /*download jar and put in lib*/
        }
        System.out.println("Downloading program library...." + filename);
        executeRemote(URL + "lib/" + filename,
                "lib" + dir_escape + filename, destDir, false);
        

        
   /*     
        try {

            loadJarLibrary(destDir + File.separator + "lib" + dir_escape + filename);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JarUpdate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JarUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
    }
    
    
    

    private void loadJarLibrary(String jarName) throws FileNotFoundException, IOException {

        URLClassLoader urlLoader = getURLClassLoader(new URL("file", null, jarName));

        JarInputStream jis = new JarInputStream(new FileInputStream(jarName));
        JarEntry entry = jis.getNextJarEntry();
        int loadedCount = 0, totalCount = 0;

        while (entry != null) {
            String name = entry.getName();
            if (name.endsWith(".class")) {
                totalCount++;
                name = name.substring(0, name.length() - 6);
                name = name.replace('/', '.');
                System.out.print("> " + name);

                try {
                    urlLoader.loadClass(name);
                    System.out.println("\t- loaded");
                    loadedCount++;
                } catch (Throwable e) {
                    System.out.println("\t- not loaded");
                    System.out.println("\t " + e.getClass().getName() + ": " + e.getMessage());
                }
            }
            entry = jis.getNextJarEntry();
        }

        System.out.println("\n---------------------");
        System.out.println("Summary:");
        System.out.println("\tLoaded:\t" + loadedCount);
        System.out.println("\tFailed:\t" + (totalCount - loadedCount));
        System.out.println("\tTotal:\t" + totalCount);
    }

    private static URLClassLoader getURLClassLoader(URL jarURL) {
        return new URLClassLoader(new URL[]{jarURL});
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
            Logger.getLogger(JarUpdate.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * invoke a unix commmand and return result string
     * 
     * @param command
     * @return
     */
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
            Logger.getLogger(JarUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    public static void main(String[] args) {
    }
}
