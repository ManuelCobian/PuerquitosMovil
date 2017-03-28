package com.example.joseantonio.puerquitos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Buscador extends AppCompatActivity {
    CollapsingToolbarLayout  name;
    String nombre="Puerco",Puerco;
    Boolean hay=false;
    TableLayout table;
    TableRow row;
    Toolbar toolbar;
    Button enviar;
    FloatingActionButton compartir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView image = (ImageView) findViewById(R.id.image_paralax);//La imagen de cada indicador
        setSupportActionBar(toolbar);
        name = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        enviar=(Button)findViewById(R.id.button2) ;
        compartir=(FloatingActionButton) findViewById(R.id.imageButton);
        final String id_indica = String.valueOf(getIntent().getExtras().getInt("id"));//recibo el di lo convierto en int
        tablas();

        mensajes();

        SharedPreferences prefe=getSharedPreferences("busca", Context.MODE_PRIVATE);
        Puerco=(prefe.getString("mail",""));


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } // Habilitar up button
        Glide.with(Buscador.this)
                .load(R.drawable.pig)
                .centerCrop()
                .into(image);
            menu();


        name.setTitle("Puerquito"+"  "+Character.toUpperCase(Puerco.charAt(0)) + Puerco.substring(1,Puerco.length()).toLowerCase()+"\n");

        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "#Puerco2017 Yo ya lleve mi Puerquito @Ventana de Espernaza"+"\n"+"Con mi Puerco #"+Puerco);
                startActivity(Intent.createChooser(intent, "Share with"));
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite();
            }
        });
    }


    public void menu(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent favoritos=new Intent(Buscador.this,MainActivity.class);
                    favoritos.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    favoritos.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    favoritos.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    favoritos.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(favoritos);
                    finish();

                onBackPressed();//para serrar
            }
        });

    }


    public void tablas(){

        RequestQueue requestQueue;
        String json="http://ventanaesperanza.webcindario.com/apis/get_busqueda.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request=new StringRequest(Request.Method.POST, json, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject o = new JSONObject(response);

                    Log.d("dates",String.valueOf(response));
                   // Log.d("dato",String.valueOf(id_indicar));
                    JSONArray a = o.getJSONArray("busqueda");

                    for (int i = 0; i < a.length(); i++) {
                        JSONObject contacto=a.getJSONObject(i);

                        String Pig=contacto.getString("Puerco");
                        String Familia=contacto.getString("Familia");
                        String Total=contacto.getString("Total");

                        table = (TableLayout) findViewById(R.id.repaymentTable);



                        row = (TableRow) LayoutInflater.from(getBaseContext()).inflate(R.layout.row_repayment_plan, null);

                        if(i % 2 != 1) {

                            row.setBackgroundColor(Color.WHITE);
                        }

                        Log.d("Puerco",Pig);

                        if (Puerco==null) {

                            Update_tabla();
                            Toast.makeText(Buscador.this,"No se econtro el Puerquito",Toast.LENGTH_SHORT);


                        }
                        else{
                            ((TextView) row.findViewById(R.id.amount)).setTextSize(20);
                            ((TextView) row.findViewById(R.id.amount)).setText(Familia);
                            if (Familia.equals("pendiente")){
                                mensaje();

                            }


                            ((TextView) row.findViewById(R.id.date)).setTextSize(20);
                            ((TextView) row.findViewById(R.id.date)).setText(Total);
                            hay=true;
                        }
                        if (hay==false){
                            Toast.makeText(Buscador.this,"No se econtro el Puerquito",Toast.LENGTH_SHORT);

                        }


                        ((TextView) row.findViewById(R.id.amount)).setGravity(Gravity.CENTER);
                        ((TextView) row.findViewById(R.id.date)).setGravity(Gravity.CENTER);

                        table.addView(row);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parameters=new HashMap<String, String>();
                Bundle bundle = getIntent().getExtras();
                String id="1";
                parameters.put("puerco",Puerco);


                return parameters;

            }
        };
        requestQueue.add(request);
    }



    private void  Update_tabla(){
        int count = table.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = table.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
    }

    private void mensaje(){
        Context context=this;
        AlertDialog.Builder aler=new AlertDialog.Builder(context)

                .setMessage("Puerquito aun no  registrado").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog alertDialog=aler.create();
        alertDialog.show();
    }

    private void EnviarMensaje (String Numero, String Mensaje){
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(Numero,null,Mensaje,null,null);
           // Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
          //  Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        PackageManager pm = this.getPackageManager();


    }


    public void invite(){

        RequestQueue requestQueue;
        String json="http://ventanaesperanza.webcindario.com/apis/invitacion.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request=new StringRequest(Request.Method.POST, json, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject o = new JSONObject(response);

                    Log.d("dates",String.valueOf(response));
                    // Log.d("dato",String.valueOf(id_indicar));
                    JSONArray a = o.getJSONArray("busqueda");

                    for (int i = 0; i < a.length(); i++) {
                        JSONObject contacto=a.getJSONObject(i);

                        String Pig=contacto.getString("Telefono");

                        EnviarMensaje(Pig,"Buen dia el equipo de Ventana IAP de Esperanza le hace la invitacion de visitar nuestro sitio http://ventanaesperanza.webcindario.com/");


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parameters=new HashMap<String, String>();
                Bundle bundle = getIntent().getExtras();
                String id="1";
                parameters.put("puerco",Puerco);


                return parameters;

            }
        };
        requestQueue.add(request);
    }


    public void mensajes(){

        RequestQueue requestQueue;
        String json="http://ventanaesperanza.webcindario.com/apis/get_sms.php";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request=new StringRequest(Request.Method.POST, json, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject o = new JSONObject(response);

                    Log.d("dates",String.valueOf(response));
                    // Log.d("dato",String.valueOf(id_indicar));
                    JSONArray a = o.getJSONArray("busqueda");

                    for (int i = 0; i < a.length(); i++) {
                        JSONObject contacto=a.getJSONObject(i);
                        String Pig=contacto.getString("Telefono");
                        String Familia=contacto.getString("Familia");

                        Log.d("Puerco",Pig);

                        if (Puerco.equals(null)){

                            //EnviarMensaje(Pig,"EL equipo de Ventana de esperanza le informa que su Puerco aun no se registra");
                            //Toast.makeText(Buscador.this,"No se econtro el Puerquito",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            EnviarMensaje(Pig,"EL equipo de Ventana de esperanza le informa que su Puerco ya se registro revisar nuestro sitio web http://ventanaesperanza.webcindario.com/");
                        }







                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parameters=new HashMap<String, String>();
                Bundle bundle = getIntent().getExtras();
                String id="1";
                parameters.put("puerco",Puerco);


                return parameters;

            }
        };
        requestQueue.add(request);
    }



}





