package jp.bj_one.re.webservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFile implements AutoCloseable {
	static final int BUFFER_SIZE = 1024 * 32;
	
    private final ZipOutputStream zipOutputStream;
    
    public ZipFile(File zipFile) throws IOException {
    	this(new BufferedOutputStream(new FileOutputStream(zipFile)));
    }
    
    public ZipFile(OutputStream outputStream) throws IOException {
        zipOutputStream = new ZipOutputStream(outputStream, Charset.forName("Shift-JIS"));
    }
    
    public void add(File file) throws IOException {
    	this.add(file, file.getName());
    }
    
    public void add(File file, String filePath) throws IOException {
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
        	this.add(inputStream, filePath);
        }
    }

    public void add(InputStream inputStream, String filePath) throws IOException {
    	byte[] buffer = new byte[BUFFER_SIZE];
        ZipEntry entry = new ZipEntry(filePath);
        zipOutputStream.putNextEntry(entry);
        int readLength = 0;
        while (0 < (readLength = inputStream.read(buffer)))
            zipOutputStream.write(buffer, 0, readLength);
    }
    
    @Override
    public void close() throws IOException {
        zipOutputStream.close();
    }
}
