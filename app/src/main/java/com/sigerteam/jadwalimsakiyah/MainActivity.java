package com.sigerteam.jadwalimsakiyah;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    TextView tvTanggal, tvWaktuImsak, tvWaktuShubuh, tvWaktuDzuhur, tvWaktuAshar, tvWaktuMaghrib, tvWaktuIsya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Proses Penamaan Action Bar
        getSupportActionBar().setTitle("Jadwal Imsakiyah");

        //Proses Insialisasi widget
        tvTanggal = (TextView) findViewById(R.id.tv_tanggal);
        tvWaktuImsak = (TextView) findViewById(R.id.tv_waktu_imsak);
        tvWaktuShubuh = (TextView) findViewById(R.id.tv_waktu_shubuh);
        tvWaktuDzuhur = (TextView) findViewById(R.id.tv_waktu_dzuhur);
        tvWaktuAshar = (TextView) findViewById(R.id.tv_waktu_ashar);
        tvWaktuMaghrib = (TextView) findViewById(R.id.tv_waktu_maghrib);
        tvWaktuIsya = (TextView) findViewById(R.id.tv_waktu_isya);

        //Proses mendapatkan tanggal hari ini
        Date bodyPartDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        tvTanggal.setText(format.format(bodyPartDate));

        //Proses Ambil Jadwal Imsakiyah
        ambilData();

    }

    public void ambilData(){
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Mengambil Data...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest("http://muslimsalat.com/bandar+lampung.json?key=0387c1d0b4ce527ab02f7b8d6981b58e",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject obj = new JSONObject(response);

                            JSONArray items = obj.getJSONArray("items");

                            JSONObject jadwal = items.getJSONObject(0);

                            String shubuh = jadwal.getString("fajr").substring(0,4);
                            Date date = null;
                            SimpleDateFormat format = new SimpleDateFormat("h:mm");
                            try {
                                date = format.parse(shubuh);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Date dateConvert = new Date(date.getTime()-600000);
                            DateFormat formatter = new SimpleDateFormat("h:mm");
                            String dateFormatted = formatter.format(dateConvert);

                            tvWaktuImsak.setText(dateFormatted + " am");
                            tvWaktuShubuh.setText(jadwal.getString("fajr"));
                            tvWaktuDzuhur.setText(jadwal.getString("dhuhr"));
                            tvWaktuAshar.setText(jadwal.getString("asr"));
                            tvWaktuMaghrib.setText(jadwal.getString("maghrib"));
                            tvWaktuIsya.setText(jadwal.getString("isha"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh :
                ambilData();
                Toast.makeText(this, "Data Berhasil Di-Refresh", Toast.LENGTH_SHORT).show();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }
}