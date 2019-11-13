package br.edu.puc.cameraandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView scannerView;
    private EditText edtResultado;
    private Button btnAbrirUrl;
    private ImageButton btnDeletar;
    private ImageButton btnCopiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
            Toast.makeText(this,"Não a permissão para usar a CÂMERA",Toast.LENGTH_SHORT).show();
        }

        edtResultado = findViewById(R.id.edtResultado);
        btnAbrirUrl = findViewById(R.id.btnAbrirUrl);
        btnDeletar = findViewById(R.id.btnDelete);
        btnCopiar = findViewById(R.id.btnCopiar);
        scannerView = findViewById(R.id.scanner);

        scannerView.setBorderColor(Color.BLUE);
        scannerView.setLaserColor(Color.YELLOW);

        scannerView.setResultHandler(this);
        scannerView.startCamera();

        ligarFlash();
        apagarTexto();
        copiarTexto();
        abrirInfo();
    }
    @Override
    public void handleResult(Result rawResult) {
        edtResultado.setText( rawResult.getText() );

        btnDeletar.setVisibility(View.VISIBLE);
        btnCopiar.setVisibility(View.VISIBLE);

        if(rawResult.getText().contains("http")){
            btnAbrirUrl.setVisibility(View.VISIBLE);
            abrirUrl(rawResult.getText());
        }else{
            btnAbrirUrl.setVisibility(View.INVISIBLE);
        }
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    private void ligarFlash(){
        final ImageButton btnFlash = findViewById(R.id.btnFlash);
        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( scannerView.getFlash() ){
                    scannerView.setFlash(false);
                    btnFlash.setImageResource(R.drawable.ic_flash_off_55dp);
                }else{
                    scannerView.setFlash(true);
                    btnFlash.setImageResource(R.drawable.ic_flash_on_55dp);
                }
            }
        });
    }

    private void copiarTexto(){
        btnCopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager Copiar = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData texto = ClipData.newPlainText("texto", edtResultado.getText());
                Copiar.setPrimaryClip(texto);
                Toast.makeText(MainActivity.this, "Texto copiado!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void apagarTexto(){
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtResultado = findViewById(R.id.edtResultado);
                edtResultado.setText("");
                btnDeletar.setVisibility(View.INVISIBLE);
                btnCopiar.setVisibility(View.INVISIBLE);
                btnAbrirUrl.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this,"Área de texto limpa!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirUrl(final String url){
        btnAbrirUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navegador = new Intent(Intent.ACTION_VIEW);
                navegador.setData(Uri.parse(url));
                startActivity(navegador);

                Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();
                onPause();
            }
        });
    }


    private void abrirInfo(){
        ImageButton btnInfo = findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, InfoActivity.class), 0);
            }
        });
    }


}
