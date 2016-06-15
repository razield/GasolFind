package udea.edu.co.gasolfind.Modelos;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Comentario {

    public String cid;
    public String calificacion;
    public String descripcion;
    public String autor;

    public Comentario(){}

    public Comentario(String cid, String descripcion, String autor, String calificacion){
        this.cid = cid;
        this.calificacion = calificacion;
        this.descripcion = descripcion;
        this.autor = autor;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cid", cid);
        result.put("calificacion", calificacion);
        result.put("descripcion", descripcion);
        result.put("autor", autor);
        return result;
    }
}
