package com.example.json_api_restful_con_volley;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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

public class MainActivity2ListaProductos extends AppCompatActivity {

    TextView txtLista ;
    String TokenEnviar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_lista_productos);

        //Localizar los controles
        txtLista = (TextView) findViewById(R.id.txtListaProduc);

        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        TokenEnviar = bundle.getString("Token").toString();

        //Ejecutar la API Restful con volley para sacar los productos
        SacarListaProduc();
    }

    private void SacarListaProduc (){
        RequestQueue queue= Volley.newRequestQueue(this);
        String url= "https://api.uealecpeterson.net/public/productos/search";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText( MainActivity2ListaProductos.this,"Lista de Productos",
                                Toast.LENGTH_SHORT).show();

                        //Parcear los datos a mostrar
                        try {
                            String Nombre = "";
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonListaProduc = jsonObject.getJSONArray("productos");
                            for (int i=0;i<jsonListaProduc.length();i++){
                                JSONObject productos = jsonListaProduc.getJSONObject(i);
                                Nombre = Nombre + "\n" + productos.getString("id").toString() + ", "
                                        + productos.getString("barcode").toString() + ", "
                                        + productos.getString("descripcion").toString() + ", "
                                        + productos.getString("costo").toString() + ", "
                                        + productos.getString("precio_unidad").toString() + "\n" ;
                            }

                            //Asignar los datos al txt de la vista para visualizar
                            txtLista.setText(Nombre);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        //Conmprobar la llegada de los datos
                        Log.i("Datos: ",(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity2ListaProductos.this, "Error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }){
            //Mandar el token
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();
                param.put("Authorization", "Bearer "+ TokenEnviar);
                return param;
            }
            //Mandamos los parametros del void para sacar los productos
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("fuente","1");
                return params;
            }};

        queue.add(stringRequest);
    }
}