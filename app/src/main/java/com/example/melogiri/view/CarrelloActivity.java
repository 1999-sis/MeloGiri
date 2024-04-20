package com.example.melogiri.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.melogiri.R;
import com.example.melogiri.adapter.CarrelloAdapter;
import com.example.melogiri.controller.ControllerCarrello;
import com.example.melogiri.fragment.CarrelloFragment;
import com.example.melogiri.model.Bevanda;
import com.example.melogiri.model.Ordine;
import com.example.melogiri.model.Utente;

public class CarrelloActivity extends AppCompatActivity implements CarrelloAdapter.OnCarrelloChangeListener {

    private TextView prezzoTotaleTextView;
    private ControllerCarrello controllerCarrello;
    private Utente utente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrello_activity);

        Intent intent = getIntent();
        utente = (Utente) intent.getSerializableExtra("utente");
        prezzoTotaleTextView = findViewById(R.id.prezzoTotale);

        controllerCarrello = ControllerCarrello.getInstance();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CarrelloFragment())
                    .commitNowAllowingStateLoss();
        }

        Button buttonAcquista = findViewById(R.id.button3);
        buttonAcquista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerCarrello.finalizzaAcquisto(utente, CarrelloActivity.this, new ControllerCarrello.AcquistoCallback() {
                    @Override
                    public void onSuccess(Ordine ordine) {
                        Log.d("CarrelloDebug", "Ordine: " + ordine.toString());
                        // Implementa la logica per finalizzare l'ordine qui
                    }

                    @Override
                    public void onQuantitaZero() {
                        // Gestito nel ControllerCarrello
                    }

                    @Override
                    public void onCarrelloVuoto() {
                        // Gestito nel ControllerCarrello
                    }
                });
            }
        });
    }

    @Override
    public void onPrezzoTotaleChanged(double nuovoPrezzoTotale) {
        runOnUiThread(() -> prezzoTotaleTextView.setText(String.format("%.2f euro", nuovoPrezzoTotale)));
    }

    @Override
    public void onProdottoRimosso(Bevanda prodotto) {
        runOnUiThread(() -> {
            Toast.makeText(CarrelloActivity.this, prodotto.getNome() + " rimosso dal carrello", Toast.LENGTH_SHORT).show();
            // Aggiornamento dell'interfaccia utente e delle strutture dati, se necessario
        });
    }

    // Aggiungi qui altri metodi e implementazioni necessari
}
