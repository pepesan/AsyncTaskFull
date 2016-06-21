package com.cursosdedesarrollo.asynctask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    // Static so that the thread access the latest attribute
    private static ImageView imageView;
    private static Bitmap downloadBitmap;

    private static ProgressBar cargando;
    private static String url="http://www.telecinco.es/todoelmundoesbueno/pilar-rubio-estrena-todo-el-mundo-es-bueno_MDSVID20120625_0117_4.jpg";

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
        // Did we already download the image?
        if (downloadBitmap != null) {
            imageView.setImageBitmap(downloadBitmap);
        }
    }



    public void resetPicture(View view) {
        resetAction();
    }



    public void downloadPicture(View view) {
        downloadAction();
    }




    static public class MiTarea extends AsyncTask
            <String,Integer,Bitmap>{
        protected void onPreExecute(){
            cargando.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
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
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                URL imageURL = new URL(params[0]);

                downloadBitmap =BitmapFactory.decodeStream(imageURL.openStream());

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return downloadBitmap;
        }
        protected void onPostExecute(Bitmap bm){

            imageView.setImageBitmap(bm);
            cargando.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            //dialog.dismiss();

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
