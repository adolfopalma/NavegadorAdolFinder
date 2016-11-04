package com.example.adolfo.navegadorweb;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.webkit.WebChromeClient;
import android.widget.ProgressBar;

import java.util.Vector;

import static android.R.attr.version;

public class MainActivity extends Activity {

    private WebView buscador;
    private AutoCompleteTextView texto;
    private Button b;
    private Button b2;
    private Sqlite admin;
    SQLiteDatabase bd;
    private ProgressBar barra;
    Vector<String> arrayUrls = new Vector<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texto = (AutoCompleteTextView) findViewById(R.id.texto);
        b = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.BotonTitulo);
        buscador = (WebView)findViewById(R.id.webkit);
        buscador.getSettings().setJavaScriptEnabled(true);
        buscador.getSettings().setBuiltInZoomControls(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            buscador.getSettings().setPluginState(WebSettings.PluginState.ON);
        }


        buscador.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

        });

        buscador.loadUrl("http://google.com");


        barra = (ProgressBar) findViewById(R.id.barra);
        consulta();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arrayUrls);
        texto.setAdapter(adapter);

        buscador.setWebChromeClient(new WebChromeClient() {
            @Override

            public void onProgressChanged(WebView view, int progress) {
                barra.setProgress(0);
                barra.setVisibility(View.VISIBLE);
                MainActivity.this.setProgress(progress * 1000);

                barra.incrementProgressBy(progress);

                if (progress == 100) {
                    barra.setVisibility(View.GONE);
                }
            }

        });

    }



    public long alta(){
        long resultado = -1;
        admin = new Sqlite(this);
        bd = admin.getWritableDatabase();
        ContentValues valor = new ContentValues();

        valor.put("titulo", texto.getText().toString());
        bd.insert("paginas", null, valor);

        admin.close();
        return resultado;
    }

    public void consulta(){
        SQLiteDatabase db2 = null;
        Sqlite admin = new Sqlite(this);
        db2 = admin.getReadableDatabase();
        db2.beginTransaction();
        arrayUrls.clear();
        try {
            String sql = "SELECT titulo FROM paginas";
            Cursor cursor = db2.rawQuery(sql, null);
            int cont=0;
            while (cursor.moveToNext())
            {
                arrayUrls.add(cursor.getString(0));
                //arrayUrls.add(cursor.getString(0).substring(7,cursor.getString(0).length()-1));

                // Toast.makeText(this, cursor.getString(0).substring(7,cursor.getString(0).length()-1), Toast.LENGTH_LONG).show();
            }
            db2.setTransactionSuccessful();//else
            //  Toast.makeText(this, "Error registro duplicado", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            //Toast.makeText(this, "Error:2" + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        } finally {
            db2.endTransaction();
            db2.close();


        }
    }

    public void buscar(View view){

        // oculta el teclado al pulsar el botón
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(texto.getWindowToken(), 0);
        buscador.loadUrl("http://"+texto.getText().toString());
        buscador.setWebViewClient(new WebViewClient()
        {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                return false;


            }

        });
        alta();
        consulta();


    }
    public void inicio(View view){

        buscador.loadUrl("http://google.com");
        buscador.setWebViewClient(new WebViewClient()
        {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                return false;


            }

        });
    }

    public void atras(View view){
        buscador.goBack();
    }
    public void siguiente(View view){
        buscador.goForward();
    }






}
