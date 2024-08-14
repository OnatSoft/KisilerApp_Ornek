package per.example.kisilerapplication_ornek;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Kisilerdao {
    SQLiteDatabase dbx;

    public ArrayList<Kisiler> tumKisiler(DBConnection dbc) {

        ArrayList<Kisiler> kisilerList = new ArrayList<>();
        dbx = dbc.getWritableDatabase();
        Cursor c = dbx.rawQuery("SELECT * FROM kisiler", null);

        while (c.moveToNext()) {

            Kisiler kisi = new Kisiler(c.getInt(c.getColumnIndexOrThrow("kisiID"))
                    , c.getString(c.getColumnIndexOrThrow("kisiAd"))
                    , c.getString(c.getColumnIndexOrThrow("kisiTel")));
            kisilerList.add(kisi);
        }
        dbx.close();
        return kisilerList;
    }

    public ArrayList<Kisiler> kisiArama(DBConnection dbc, String kelimeArama) {

        ArrayList<Kisiler> kisilerList = new ArrayList<>();
        dbx = dbc.getWritableDatabase();
        Cursor c = dbx.rawQuery("SELECT * FROM kisiler WHERE kisiAd LIKE '%" + kelimeArama + "%' OR kisiTel LIKE '%" + kelimeArama + "%'", null);

        while (c.moveToNext()) {

            Kisiler kisi = new Kisiler(c.getInt(c.getColumnIndexOrThrow("kisiID"))
                    , c.getString(c.getColumnIndexOrThrow("kisiAd"))
                    , c.getString(c.getColumnIndexOrThrow("kisiTel")));
            kisilerList.add(kisi);
        }
        dbx.close();
        return kisilerList;
    }

    public void kisiEkle(DBConnection dbc, String kisiAd, String kisiTel) {

        dbx = dbc.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("kisiAd", kisiAd);
        cv.put("kisiTel", kisiTel);

        dbx.insertOrThrow("kisiler", null, cv);
        dbx.close();
    }

    public void kisiGuncelle(DBConnection dbc, int kisiID, String kisiAd, String kisiTel) {

        dbx = dbc.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("kisiID", kisiID);
        cv.put("kisiAd", kisiAd);
        cv.put("kisiTel", kisiTel);

        dbx.update("kisiler", cv, "kisiID=?", new String[]{String.valueOf(kisiID)});
        dbx.close();
    }

    public void kisiSil(DBConnection dbc, int kisiID) {

        dbx = dbc.getWritableDatabase();
        dbx.delete("kisiler", "kisiID=?", new String[]{String.valueOf(kisiID)});
        dbx.close();
    }
}
