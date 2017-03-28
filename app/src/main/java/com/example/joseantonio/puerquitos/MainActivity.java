package com.example.joseantonio.puerquitos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView Total,Puercos;
    Button busqueda;
    EditText Busca;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Total=(TextView)findViewById(R.id.Total);
        Puercos=(TextView)findViewById(R.id.Puercos);
        busqueda=(Button)findViewById(R.id.busquedas);
        Busca=(EditText)findViewById(R.id.busqueda);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Busca.setVisibility(View.INVISIBLE);

        busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscando();


            }
        });


        Puercos();
        Total();
    }

    public void  Puercos(){
        String json="http://ventanaesperanza.webcindario.com/apis/get_puercos.php";
        StringRequest request=new StringRequest(Request.Method.POST, json, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject o = new JSONObject(response);

                    Log.d("dates",String.valueOf(response));
                    JSONArray a = o.getJSONArray("registros");
                    for (int i = 0; i < a.length(); i++) {
                        JSONObject contacto=a.getJSONObject(i);

                        String nombre=contacto.getString("puercos");



                        if (nombre.isEmpty()){
                            nombre="No hay Puercos Registrados";
                            Puercos.setText(nombre);
                        }
                        else{ Puercos.setText("Puercos Registrados"+"\n"+"\n"+nombre);}


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
                parameters.put("id",id);


                return parameters;

            }
        };
        requestQueue.add(request);

    }

    public void Total(){
//total
        String json="http://ventanaesperanza.webcindario.com/apis/get_dinero.php";
        StringRequest request=new StringRequest(Request.Method.POST, json, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject o = new JSONObject(response);

                    Log.d("dates",String.valueOf(response));
                    JSONArray a = o.getJSONArray("registros");
                    for (int i = 0; i < a.length(); i++) {
                        JSONObject contacto=a.getJSONObject(i);

                        String nombre=contacto.getString("puercos");

                        if (nombre.isEmpty()){
                            nombre="No hay Puercos Registrados";
                            Total.setText(nombre);
                        }
                        else{ Total.setText("Dinero Recaudado"+"\n"+"\n"+nombre);}

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
                parameters.put("id",id);


                return parameters;

            }
        };
        requestQueue.add(request);

    }


    public void buscar(){



        String puerco=Busca.getText().toString();


        SharedPreferences preferencias=getSharedPreferences("busca", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("mail", Busca.getText().toString());
        editor.commit();


        Log.d("dato",String.valueOf(puerco));
        if (puerco.length()>0){


            Intent favoritos=new Intent(MainActivity.this,Buscador.class);

            favoritos.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            favoritos.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            favoritos.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            favoritos.putExtra("id",puerco);
            startActivity(favoritos);
            finish();



        }

    }


    public  void buscando(){

        Busca.setVisibility(View.VISIBLE);
        Busca.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                boolean procesado = false;
                // Mostrar mensaje
                buscar();
                // Habilitar up button

                // Ocultar teclado virtual
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                procesado = true;

                return procesado;
            }

        });

    }
}
