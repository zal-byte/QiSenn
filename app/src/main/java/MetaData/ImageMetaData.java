package MetaData;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.drew.metadata.exif.ExifDirectoryBase.TAG_DATETIME_ORIGINAL;

public class ImageMetaData {
    Metadata metadata;
    Date date;

    public ImageMetaData(File file) throws Exception {
        metadata = ImageMetadataReader.readMetadata(file);
        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        date = directory.getDate(TAG_DATETIME_ORIGINAL);
    }

    public String getImageTime() {
        return dateFormat("HH:mm:ss");
    }
    public String getImageDate()
    {
        return dateFormat("yyyy-mm-dd");
    }


    public String dateFormat(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
        return formatter.format(date);
    }




}
