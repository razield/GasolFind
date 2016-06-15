package udea.edu.co.gasolfind.Views;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import udea.edu.co.gasolfind.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        FirebaseAuth.AuthStateListener {

    EditText password;
    AutoCompleteTextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (AutoCompleteTextView)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    public void onClick(View v) {
        String _mail = email.getText().toString();
        String _pass = password.getText().toString();

        if (_mail.isEmpty() || _pass.isEmpty()) {
            Toast.makeText(this, getString(R.string.data_required), Toast.LENGTH_LONG).show();
            return;
        }

        switch (v.getId()){
            case(R.id.login):
                FirebaseAuth.getInstance().signInWithEmailAndPassword(_mail, _pass);
                break;
            case(R.id.register):
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(_mail, _pass);
                break;
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            firebaseAuth.removeAuthStateListener(this);
            finish();
        }
    }
}
