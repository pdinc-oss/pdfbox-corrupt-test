import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.junit.jupiter.api.Test;

public class CorruptFilesTest
{
    @Test
    public void corruptedThroughFileOutputStream() throws IOException {

        InputStream fis = this.getClass().getResourceAsStream("chicken.pdf");
        if (fis == null)
        {
            throw new NullPointerException();
        }
        PDDocument document = Loader.loadPDF(fis);
        PDStream stream = new PDStream(document, fis);

        fis.close();
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

        InputStream fis = this.getClass().getResourceAsStream("chicken.pdf");
        if (fis == null)
        {
            throw new NullPointerException();
        }
        PDDocument document = Loader.loadPDF(fis);
        fis.close();

        File tempFile = new File("C:\\Users\\Public\\Music\\file" + new Date().getTime() + ".pdf");
        document.save(tempFile);
        document.close();
        System.out.println(tempFile.getAbsolutePath());
    }

    @Test
    public void loadFileAndSave() throws IOException {

        File fil = new File("C:\\Users\\Public\\Desktop\\projects\\test-pdfbox-corruption\\src\\main\\resources\\chicken.pdf");
        PDDocument document = Loader.loadPDF(fil);

        document.save("C:\\Users\\Public\\Music\\file" + new Date().getTime() + ".pdf");
        document.close();

    }
}
