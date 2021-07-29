import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.naming.NameAlreadyBoundException;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.junit.jupiter.api.Test;

public class CorruptFilesTest
{

    private static final String CHICKEN_PDF = "chicken.pdf";
    public static final String KNOWN_MD5_SUM = "105fc577c7d166d59f49f9ea81d27aec"; //md5sum.exe src/main/resources/chicken.pdf

    @Test
    public void corruptedThtroughDocSave() throws InvalidPasswordException, IOException, NoSuchAlgorithmException {
        
        String fileName = CHICKEN_PDF;
        File file = new File(this.getClass().getClassLoader().getResource(fileName).getFile());
        FileInputStream fis = new FileInputStream(file);
        String hex = checksum(file);
        assertEquals(KNOWN_MD5_SUM, hex);
        PDDocument document = Loader.loadPDF(fis);
        corruptPDFDocument("inputStreamSave", document);
        fis.close();

    }

    @Test
    public void loadFileAndSave() throws IOException, NoSuchAlgorithmException {

        String fileName = CHICKEN_PDF;
        File file = new File(this.getClass().getClassLoader().getResource(fileName).getFile());
        String hex = checksum(file);
        assertEquals(KNOWN_MD5_SUM, hex);
        PDDocument document = Loader.loadPDF(file);
        corruptPDFDocument("classLoaderSave", document);

    }

    public void corruptPDFDocument(String outprefix, PDDocument document) throws IOException {
        File tempFile = File.createTempFile(outprefix, ".pdf");
        document.save(tempFile);
        document.close();
        System.out.println(tempFile.getAbsolutePath());
    }

    private static String checksum(File fil) throws IOException, NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");

        // file hashing with DigestInputStream
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(fil), md))
        {
            while (dis.read() != -1)
                ; //empty loop to clear the data
            md = dis.getMessageDigest();
        }

        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest())
        {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}
