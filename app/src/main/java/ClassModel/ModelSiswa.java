package ClassModel;

public class ModelSiswa {
    String NIS;
    String Nama;
    String Tanggal_lahir;
    String Tempat_lahir;
    String Alamat;
    String Kelas;

    public String getKelas() {
        return Kelas;
    }

    public void setKelas(String kelas) {
        Kelas = kelas;
    }

    String Jenis_kelamin;
    String Agama;
    String Foto;
    String Password;

    public String getNIS() {
        return NIS;
    }

    public void setNIS(String NIS) {
        this.NIS = NIS;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getTanggal_lahir() {
        return Tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        Tanggal_lahir = tanggal_lahir;
    }

    public String getTempat_lahir() {
        return Tempat_lahir;
    }

    public void setTempat_lahir(String tempat_lahir) {
        Tempat_lahir = tempat_lahir;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public String getJenis_kelamin() {
        return Jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        Jenis_kelamin = jenis_kelamin;
    }

    public String getAgama() {
        return Agama;
    }

    public void setAgama(String agama) {
        Agama = agama;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
