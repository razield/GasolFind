package udea.edu.co.gasolfind.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import udea.edu.co.gasolfind.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText password;
    AutoCompleteTextView email;

    private String host = "https://gasolfindapp.herokuapp.com/";
    public static final String TAG = "MyTag";

    private RequestQueue mRequestQueue;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (AutoCompleteTextView)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.start();

        prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mRequestQueue != null) {
            mRequestQueue = Volley.newRequestQueue(this);

            mRequestQueue.start();
        }
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    @Override
    public void onClick(View v) {
        String _mail = email.getText().toString();
        String _pass = password.getText().toString();
        Intent intent;

        switch (v.getId()){
            case(R.id.login):
                if (_mail.isEmpty() || _pass.isEmpty()) {
                    Toast.makeText(this, getString(R.string.data_required), Toast.LENGTH_LONG).show();
                    return;
                }else{
                    login(_mail, _pass);
                }

                break;
            case(R.id.register):
                Log.d("register", "entra en el case");
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                return;
        }
    }

    public void login(String user, String pass) {
        String url = host + "auth_user";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", user);
        params.put("password", pass);
        JSONObject jsonBody = new JSONObject(params);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,jsonBody, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                SharedPreferences.Editor editor = prefs.edit();
                String user = "";

                try {
                    editor.putString("token", response.getString("auth_token"));
                    user = response.getJSONObject("user").getString("email");
                    editor.putString("user", response.getJSONObject("user").getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.apply();

                Toast toast = Toast.makeText(getApplicationContext(), "Login Success " + user, Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Verificar datos ", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        mRequestQueue.add(jsObjRequest);
    }

}
