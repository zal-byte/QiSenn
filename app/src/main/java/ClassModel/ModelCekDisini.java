package ClassModel;

public class ModelCekDisini {
    String nama, status_absen, img_date, img_time, path;

    public String getNama() {
        return nama;
    }

    public String getImg_date() {
        return img_date;
    }

    public void setImg_date(String img_date) {
        this.img_date = img_date;
    }

    public String getImg_time() {
        return img_time;
    }

    public void setImg_time(String img_time) {
        this.img_time = img_time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getStatus_absen() {
        return status_absen;
    }

    public void setStatus_absen(String status_absen) {
        this.status_absen = status_absen;
    }
}
