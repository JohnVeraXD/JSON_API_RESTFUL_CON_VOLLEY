package com.example.json_api_restful_con_volley;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

    EditText txtUsuario, txtcontrasena;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Localizar los controles
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtcontrasena = (EditText) findViewById(R.id.txtContrasena);
    }

    public void OnclicIngresar (View view){
        RequestQueue queue= Volley.newRequestQueue(this);
        String url= "https://api.uealecpeterson.net/public/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,"Bienvenido, Login correcto",
                                Toast.LENGTH_SHORT).show();

                        //Obtener el token que nos envio
                        JSONObject respuesta = null;
                        try {
                            respuesta = new JSONObject(response);
                            token = respuesta.getString("access_token").toString();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        IniciarActividad2();

                        //Conmprobar llegada de token
                        //Log.i("Prueba: ",(token));
                        //Log.i("Datos: ",(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }){
            //Mandamos los parametros del void para el login
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("correo",txtUsuario.getText().toString());
                params.put("clave",txtcontrasena.getText().toString());
                return params;
            }};

        queue.add(stringRequest);
    }

    //Inicimos la actividad 2, enviandole el token obetenido
    private void IniciarActividad2() {
        Intent intent = new Intent(MainActivity.this,MainActivity2ListaProductos.class);
        Bundle b = new Bundle();
        b.putString("Token",token);
        intent.putExtras(b);
        startActivity(intent);
    }
}