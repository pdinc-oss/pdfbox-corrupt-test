import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.junit.jupiter.api.Test;


public class CorruptFilesTest
{
    @Test
    public void corruptedThroughFileOutputStream() throws InvalidPasswordException, IOException {
        InputStream fis = this.getClass().getResourceAsStream("DD_2875.pdf");
        if (fis == null)
        {
            throw new NullPointerException();
        }
        
        PDDocument document = PDDocument.load(fis);
        PDStream stream = new PDStream(document);
        byte[] bytes = stream.toByteArray();
        File tempFile = File.createTempFile("fosCorrupted", ".pdf");
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(bytes, 0, bytes.length);
        fos.flush();
        fos.close();
        System.out.println(tempFile.getAbsolutePath());
    }

    @Test
    public void corruptedThroughDocSave() throws InvalidPasswordException, IOException {
        InputStream fis = this.getClass().getResourceAsStream("DD_2875.pdf");
        if (fis == null)
        {
            throw new NullPointerException();
        }
        PDDocument document = PDDocument.load(fis);

        File tempFile = File.createTempFile("saveCorrupted", ".pdf");
        document.save(tempFile);
        System.out.println(tempFile.getAbsolutePath());
    }
}
