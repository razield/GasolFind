package udea.edu.co.gasolfind.Modelos;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Estacion {

    public String eid;
    public String numeroCalificaciones;
    public String promedioCalificaciones;
    public String compania;
    public String telefono;
    public String direccion;
    public String latitud;
    public String longitud;
    public String horario;

    public Estacion(){}

    public Estacion(String eid, String numeroCalificaciones, String promedioCalificaciones, String compania, String telefono, String direccion, String latitud, String longitud, String horario) {
        this.eid = eid;
        this.numeroCalificaciones = numeroCalificaciones;
        this.promedioCalificaciones = promedioCalificaciones;
        this.compania = compania;
        this.telefono = telefono;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.horario = horario;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", eid);
        result.put("numero_calificaciones", numeroCalificaciones);
        result.put("promedio_calificaciones", promedioCalificaciones);
        result.put("compania", compania);
        result.put("telefono", telefono);
        result.put("direccion", direccion);
        result.put("latitud", latitud);
        result.put("longitud", longitud);
        result.put("horario", horario);
        return result;
    }
}