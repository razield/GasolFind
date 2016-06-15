package udea.edu.co.gasolfind.Modelos;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Usuario {

    public String uid;
    public String nick_usuario;
    public String correo;
    public ArrayList<String> comentariosUsuario;

    public Usuario(){}

    public Usuario(String uid, String nick_usuario, String correo){
        this.uid = uid;
        this.nick_usuario = nick_usuario;
        this.correo = correo;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("nick_usuario", nick_usuario);
        result.put("correo", correo);
        return result;
    }
}
