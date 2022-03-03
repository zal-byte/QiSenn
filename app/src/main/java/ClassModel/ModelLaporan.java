package ClassModel;

public class ModelLaporan {
    String nis;
    String status_absen;
    String tanggal_absen;
    String img_date;
    String img_time;


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

    String path;

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getStatus_absen() {
        return status_absen;
    }

    public void setStatus_absen(String status_absen) {
        this.status_absen = status_absen;
    }

    public String getTanggal_absen() {
        return tanggal_absen;
    }

    public void setTanggal_absen(String tanggal_absen) {
        this.tanggal_absen = tanggal_absen;
    }
}
