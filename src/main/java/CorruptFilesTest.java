import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.naming.NameAlreadyBoundException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.junit.jupiter.api.Test;

public class CorruptFilesTest
{


    @Test
    public void corruptedThroughDocSave() throws InvalidPasswordException, IOException {

        InputStream fis = this.getClass().getResourceAsStream("chicken.pdf");
        if (fis == null)
        {
            throw new NullPointerException();
        }
        PDDocument document = Loader.loadPDF(fis);
        fis.close();

        File tempFile = File.createTempFile("inputStreamSave", ".pdf");
        document.save(tempFile);
        document.close();
        System.out.println(tempFile.getAbsolutePath());
    }

    @Test
    public void loadFileAndSave() throws IOException {

        String fileName = "chicken.pdf";
        CorruptFilesTest thisClass = new CorruptFilesTest();
        ClassLoader classLoader = thisClass.getClass().getClassLoader();
        if(classLoader == null)
        {
            throw new NullPointerException();
        }
        File fil = new File(classLoader.getResource(fileName).getFile());
        PDDocument document = Loader.loadPDF(fil);
        File tempFile = File.createTempFile("classLoaderSave", ".pdf");
        document.save(tempFile);
        document.close();
        System.out.println(tempFile.getAbsolutePath());

    }
}
