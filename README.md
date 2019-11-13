# aplicativo-camera
Projeto da Disciplica DESENHO DE APLICATIVOS PARA DISPOSITIVOS MÓVEIS  
Criação de um aplicativo utilizando o recurso da câmera  

Utilizando a API [BarCodeScanner](https://github.com/dm77/barcodescanner) foi criado um leitor que QR Code
  
![Imagem1 do app](https://i.imgur.com/aBTQ8i0m.jpg)  
  
![Imagem2 do app](https://i.imgur.com/NQEKjvcm.jpg)  

### Dependencia  
```
    implementation 'me.dm7.barcodescanner:zxing:1.9.13'
```

### Permissões   
```
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.FLASHLIGHT" />

    <uses-feature
        android:name="android.hardware.camera2.CameraDevice"
        android:required="true" />
    <uses-feature
        android:name="android.hardware.camera.flash"
        android:required="true" />
```

### Metodos essenciais do app:
```
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
```
