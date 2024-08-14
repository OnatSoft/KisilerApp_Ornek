package per.example.kisilerapplication_ornek;

public class Kisiler {
    private int kisiID;
    private String kisiAd;
    private String kisiTelefon;

    public Kisiler() {
    }

    public Kisiler(int kisiID, String kisiAd, String kisiTelefon) {
        this.kisiID = kisiID;
        this.kisiAd = kisiAd;
        this.kisiTelefon = kisiTelefon;
    }

    public int getKisiID() {
        return kisiID;
    }

    public void setKisiID(int kisiID) {
        this.kisiID = kisiID;
    }

    public String getKisiAd() {
        return kisiAd;
    }

    public void setKisiAd(String kisiAd) {
        this.kisiAd = kisiAd;
    }

    public String getKisiTelefon() {
        return kisiTelefon;
    }

    public void setKisiTelefon(String kisiTelefon) {
        this.kisiTelefon = kisiTelefon;
    }
}
