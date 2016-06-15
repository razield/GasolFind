package udea.edu.co.gasolfind.Modelos;
import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

public class TipoGasolina {
    public String nombreGasolina;
    public String precioGasolina;

    public TipoGasolina(){}

    public TipoGasolina(String nombreGasolina, String precioGasolina){
        this.nombreGasolina = nombreGasolina;
        this.precioGasolina = precioGasolina;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre_gasolina", nombreGasolina);
        result.put("precio_gasolina", precioGasolina);
        return result;
    }
}
