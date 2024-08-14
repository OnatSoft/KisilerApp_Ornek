package per.example.kisilerapplication_ornek;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class KisilerAdapter extends RecyclerView.Adapter<KisilerAdapter.CardNesneTutucu> {
    private Context context;
    private ArrayList<Kisiler> kisilerList;
    private DBConnection dbc;

    public KisilerAdapter(Context context, ArrayList<Kisiler> kisilerList, DBConnection dbc) {
        this.context = context;
        this.kisilerList = kisilerList;
        this.dbc = dbc;
    }

    public class CardNesneTutucu extends RecyclerView.ViewHolder {
        private TextView txtAd;
        private ImageView imgPopupMenu;
        private ImageButton imgBtnPhone;

        public CardNesneTutucu(@NonNull View itemView) {
            super(itemView);
            imgPopupMenu = itemView.findViewById(R.id.imgPopupMenu);
            txtAd = itemView.findViewById(R.id.txtAd);
            imgBtnPhone = itemView.findViewById(R.id.imgBtnPhone);
        }
    }

    @NonNull
    @Override
    public CardNesneTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kisi_card_tasarim, parent, false);
        return new CardNesneTutucu(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardNesneTutucu holder, int position) {

        final Kisiler kisi = kisilerList.get(position);

        holder.txtAd.setText(kisi.getKisiAd());
        holder.imgBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, kisi.getKisiTelefon() + " Aranıyor...", Toast.LENGTH_LONG).show();
            }
        });

        holder.imgPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, holder.imgPopupMenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_cardmenu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.action_guncelle) {
                            alertGoster(kisi);

                        } else if (item.getItemId() == R.id.action_sil) {

                            Snackbar.make(holder.imgPopupMenu, "Seçtiğiniz kişiyi silmek istediğinizden emin misiniz?", Snackbar.LENGTH_LONG)
                                    .setAction("Evet", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            new Kisilerdao().kisiSil(dbc, kisi.getKisiID());
                                            kisilerList = new Kisilerdao().tumKisiler(dbc);
                                            notifyDataSetChanged();

                                            Snackbar.make(holder.imgPopupMenu, "Kişi başarıyla silindi.", Snackbar.LENGTH_SHORT).show();
                                        }
                                    }).show();
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return kisilerList.size();
    }


    public void alertGoster(Kisiler kisi) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.alertview_design, null);

        TextInputEditText editTxtAd = v.findViewById(R.id.editTextAd);
        TextInputEditText editTxtTel = v.findViewById(R.id.editTextTel);
        TextInputLayout txtLayoutAd = v.findViewById(R.id.txtLayoutAd);
        TextInputLayout txtLayoutTel = v.findViewById(R.id.txtLayoutTel);

        editTxtAd.setText(kisi.getKisiAd());
        editTxtTel.setText(kisi.getKisiTelefon());

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle("Kişiyi Güncelle");
        ad.setIcon(R.drawable.updateicon_24);
        ad.setView(v);

        ad.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String kisiAd = editTxtAd.getText().toString();
                String kisiTel = editTxtTel.getText().toString();

                if (Objects.requireNonNull(editTxtAd.getText().toString().isEmpty() && editTxtTel.getText().toString().isEmpty())) {

                    txtLayoutAd.setError("Lütfen bu alanı doldurun.");
                    txtLayoutTel.setError("Lütfen bu alanı doldurun.");
                } else {

                    new Kisilerdao().kisiGuncelle(dbc, kisi.getKisiID(), kisiAd, kisiTel);
                    kisilerList = new Kisilerdao().tumKisiler(dbc);
                    Snackbar.make(editTxtAd, "Kişi başarıyla güncellendi.", Snackbar.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.create().show();
    }
}