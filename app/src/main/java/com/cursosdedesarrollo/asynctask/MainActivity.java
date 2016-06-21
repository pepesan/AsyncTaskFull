package com.cursosdedesarrollo.asynctask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    // Static so that the thread access the latest attribute
    private static ImageView imageView;
    private static Bitmap downloadBitmap;

    private static ProgressBar cargando;
    private static String url="http://www.telecinco.es/todoelmundoesbueno/pilar-rubio-estrena-todo-el-mundo-es-bueno_MDSVID20120625_0117_4.jpg";
    private static Button descarga;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
    }
    public void setUpViews(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // get the latest imageView after restart of the application
        imageView = (ImageView) findViewById(R.id.imageView1);
        cargando = (ProgressBar) findViewById(R.id.cargando);
        //descarga=(Button)findViewById(R.id.download);
        // Did we already download the image?
        if (downloadBitmap != null) {
            imageView.setImageBitmap(downloadBitmap);
        }
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }



    public void resetPicture(View view) {
        resetAction();
    }



    public void downloadPicture(View view) {
        downloadAction();
    }





    // Utiliy method to download image from the internet
    static private Bitmap downloadBitmap(String url) throws IOException {
        HttpUriRequest request = new HttpGet(url.toString());
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(entity);

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                    bytes.length);
            return bitmap;
        } else {
            throw new IOException("Download failed, HTTP response code "
                    + statusCode + " - " + statusLine.getReasonPhrase());
        }
    }
    static public class MiTarea extends AsyncTask
            <String,Integer,Bitmap>{
        protected void onPreExecute(){
            cargando.setVisibility(View.VISIBLE);
            //descarga.setEnabled(false);
        }
        protected void onProgressUpdate(Integer... progress) {
            Log.d("App","Progreso"+progress[0]);
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            for (int i=0;i<100;i++){
                publishProgress(i);
            }
            try {
                downloadBitmap =
                        downloadBitmap(params[0]);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return downloadBitmap;
        }
        protected void onPostExecute(Bitmap bm){
            imageView.setImageBitmap(bm);
            //dialog.dismiss();
            cargando.setVisibility(View.GONE);
            //descarga.setEnabled(true);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d("Boton Descargar", "Pulsado");
            downloadAction();
            return true;
        }
        if (id == R.id.action_reset) {
            Log.d("Boton Reset", "Pulsado");
            resetAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void downloadAction(){
        MiTarea tarea=new MiTarea();
        tarea.execute(new String[]{url});
    }
    public void resetAction(){
        if (downloadBitmap != null) {
            downloadBitmap = null;
        }
        imageView.setImageResource(R.mipmap.ic_launcher);
    }
}
