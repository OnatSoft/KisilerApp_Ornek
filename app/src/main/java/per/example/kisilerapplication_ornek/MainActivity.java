package per.example.kisilerapplication_ornek;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    FloatingActionButton fab;
    RecyclerView rv;
    Toolbar toolbar;
    TextView txtHata;
    ArrayList<Kisiler> kisilerArrayList = new ArrayList<>();
    KisilerAdapter adapter;
    DBConnection dbc = new DBConnection(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
        txtHata = findViewById(R.id.txtHata);


        setSupportActionBar(toolbar);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        veriCekme();

        fab.setOnClickListener(view -> {

            alertGoster();
        });
    }

    public void veriCekme() {

        try {
            txtHata.setVisibility(View.INVISIBLE);
            kisilerArrayList = new Kisilerdao().tumKisiler(dbc);
            adapter = new KisilerAdapter(MainActivity.this, kisilerArrayList, dbc);
            rv.setAdapter(adapter);
        } catch (NullPointerException e) {
            txtHata.setVisibility(View.VISIBLE);
        }

    }

    public void alertGoster() {

        try {

            LayoutInflater inflater = LayoutInflater.from(this);
            View v = inflater.inflate(R.layout.alertview_design, null);

            TextInputEditText editTxtAd = v.findViewById(R.id.editTextAd);
            TextInputEditText editTxtTel = v.findViewById(R.id.editTextTel);
            TextInputLayout txtLayoutAd = v.findViewById(R.id.txtLayoutAd);
            TextInputLayout txtLayoutTel = v.findViewById(R.id.txtLayoutTel);

            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Yeni Kişi Ekle");
            ad.setIcon(R.drawable.add_icon_24);
            ad.setView(v);

            ad.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String kisiAd = editTxtAd.getText().toString().trim();
                    String kisiTel = editTxtTel.getText().toString().trim();

                    if (Objects.requireNonNull(editTxtAd.getText().toString().isEmpty() & editTxtTel.getText().toString().isEmpty())) {

                        txtLayoutAd.setError("Lütfen bu alanı doldurun.");
                        txtLayoutTel.setError("Lütfen bu alanı doldurun.");
                    } else {

                        new Kisilerdao().kisiEkle(dbc, kisiAd, kisiTel);
                        kisilerArrayList = new Kisilerdao().tumKisiler(dbc);
                        veriCekme();

                        Snackbar.make(editTxtAd, "Kişi başarıyla kaydedildi.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });

            ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ad.create().show();
        } catch (Exception e) {
            Log.e("KİŞİ EKLEME HATASI", String.valueOf(e));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) menuItem.getActionView();
        sv.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("onQueryTextSubmit", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (kisilerArrayList != null) {
            kisilerArrayList = new Kisilerdao().kisiArama(dbc, newText);
            adapter = new KisilerAdapter(MainActivity.this, kisilerArrayList, dbc);
            rv.setAdapter(adapter);
        } else {
            txtHata.setVisibility(View.VISIBLE);
        }
        return true;
    }
}