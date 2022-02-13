package MetaData;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.drew.metadata.exif.ExifDirectoryBase.TAG_DATETIME_ORIGINAL;

public class ImageMetaData {
    Metadata metadata;
    final Date date;

    public ImageMetaData(File file) throws Exception {
        this.metadata = ImageMetadataReader.readMetadata(file);
        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        this.date = directory.getDateDigitized();
    }

    public String getImageTime() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Jakarta");
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz); // strip timezone
        return df.format(new Date());
    }
    public String getImageDate()
    {
        System.out.println(this.date);
        return dateFormat("yyyy-MM-dd");
    }


    public String dateFormat(String format) {
        Locale locale = new Locale("id","ID");
        SimpleDateFormat formatter = new SimpleDateFormat(format, locale);
        if( this.date != null)
        {
            return formatter.format(this.date);
        }else{
            return "Date error";
        }

    }




}
