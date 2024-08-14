package per.example.kisilerapplication_ornek;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBConnection extends SQLiteOpenHelper {

    public DBConnection(@Nullable Context context) {
        super(context, "Kisilerdb.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE \"kisiler\" (\n" +
                "\t\"kisiID\"\tINTEGER,\n" +
                "\t\"kisiAd\"\tTEXT,\n" +
                "\t\"kisiTel\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"kisiID\" AUTOINCREMENT)\n" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS kisiler");
        onCreate(db);
    }
}
