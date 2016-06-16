package udea.edu.co.gasolfind.Firebasegasolfind;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import udea.edu.co.gasolfind.ControlFunctions.Parametros;
import udea.edu.co.gasolfind.Interfaces.DBListener;
import udea.edu.co.gasolfind.Modelos.Comentario;
import udea.edu.co.gasolfind.Modelos.Estacion;
import udea.edu.co.gasolfind.Modelos.TipoGasolina;
import udea.edu.co.gasolfind.Modelos.Usuario;
import udea.edu.co.gasolfind.fbprueba;

public class FirebaseGasolfind {

    private DatabaseReference mDatabase;

    private DBListener dbListener;

    private final String USUARIOS = "usuarios";
    private final String ESTACIONES = "estaciones";
    private final String COMENTARIOS = "comentarios";
    private final String TIPOSGASOLINA = "tipos_gasolina";
    private final String PRECIOGASOLINA = "precio_gasolina";
    private final String NUMEROCALIFICACIONES = "numero_calificaciones";
    private final String PROMEDIOCALIFICACIONES = "promedio_calificaciones";
    private float precio_regular;
    private float precio_premium;
    private float precio_acpm;
    private float precio_gas;
    private boolean existe_estacion;
    private Parametros data;

    public FirebaseGasolfind() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public String registrarUsuario(String nick, String email){
        String clave = mDatabase.child(USUARIOS).push().getKey();
        Usuario usuario = new Usuario(clave, nick, email);
        Map<String, Object> nuevoUsuario = usuario.toMap();
        Map<String, Object> actualizarUsuarios = new HashMap<>();
        actualizarUsuarios.put("/" + USUARIOS + "/" + clave, nuevoUsuario);
        mDatabase.updateChildren(actualizarUsuarios);
        return clave;
    }

    public void registrarEstacion(String estacion_id, String numeroCalificaciones, String promedioCalificaciones, String compania, String telefono, String direccion, String latitud, String longitud, String horario){
        //String clave = mDatabase.child("estaciones").push().getKey();
        String clave = estacion_id;
        Estacion estacion = new Estacion(clave, numeroCalificaciones, promedioCalificaciones, compania, telefono, direccion, latitud, longitud, horario);
        Map<String, Object> nuevaEstacion = estacion.toMap();
        Map<String, Object> actualizarEstaciones = new HashMap<>();
        actualizarEstaciones.put("/" + ESTACIONES + "/" + clave, nuevaEstacion);
        mDatabase.updateChildren(actualizarEstaciones);
    }

    public void registrarComentario(String estacion_id, String descripcion, String autor, String calificacion){
        String clave =  mDatabase.child(ESTACIONES).child(estacion_id).child(COMENTARIOS).push().getKey();;
        Comentario comentario = new Comentario(clave, descripcion, autor, calificacion);
        Map<String, Object> nuevoComentario = comentario.toMap();
        Map<String, Object> actualizarComentarios = new HashMap<>();
        actualizarComentarios.put("/" + USUARIOS + "/" + autor + "/" + COMENTARIOS + "/" + clave, nuevoComentario);
        actualizarComentarios.put("/" + ESTACIONES + "/" + estacion_id + "/" + COMENTARIOS + "/" + clave, nuevoComentario);
        mDatabase.updateChildren(actualizarComentarios);
    }

    public void registrarTipoGasolina(String estacion_id, String nombreGasolina, String precioGasolina){
        // String clave =  mDatabase.child("estaciones").child(estacion_id).child("tipos_gasolina").push().getKey();
        String clave = nombreGasolina;
        TipoGasolina tipoGasolina = new TipoGasolina(clave, precioGasolina);
        Map<String, Object> nuevoTipoGasolina = tipoGasolina.toMap();
        Map<String, Object> actualizarTipo = new HashMap<>();
        actualizarTipo.put("/" + ESTACIONES + "/" + estacion_id + "/" + TIPOSGASOLINA + "/" + clave, nuevoTipoGasolina);
        mDatabase.updateChildren(actualizarTipo);
    }

    public void actualizarCalificacion(String estacion_id, String numCalificaciones, String promedio){
        mDatabase.child(ESTACIONES).child(estacion_id).child(NUMEROCALIFICACIONES).setValue(numCalificaciones);
        mDatabase.child(ESTACIONES).child(estacion_id).child(PROMEDIOCALIFICACIONES).setValue(promedio);
    }

    public void actualizarPrecioGasolina(String estacion_id, String tipo, String nuevoPrecio){
        mDatabase.child(ESTACIONES).child(estacion_id).child(TIPOSGASOLINA).child(tipo).child(PRECIOGASOLINA).setValue(nuevoPrecio);
    }



    public Parametros existeEstacion(final DBListener listener, Parametros datos){

        //dbListener = listener;
        System.out.println("*********** placeid: " + datos.getPlace_id());
        mDatabase.child(ESTACIONES).child(datos.getPlace_id()).addListenerForSingleValueEvent(

                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Estacion e = dataSnapshot.getValue(Estacion.class);
                        Parametros resultados = new Parametros(true, "place_id", "name", "0.0", "0.0", "address", "phone", "0.0", "0.0", "0.0", "0.0");

                        boolean resultado;
                        if (e == null) {
                            // System.out.println("-------------------- siiiiiiiiii");
                            //resultado = false;

                            resultados.setExisteEstacion(false);
                        } else {
                            // System.out.println("-------------------- nooooooooooooo");
                            resultados.setExisteEstacion(true);
                        }
                        System.out.println("****FireBAse" + resultados.isExisteEstacion());
                        listener.onResult(resultados);
                        // fbprueba.resultadoEstacion(resultado);
                        // cambiarExistenciaEstacion(resultado);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // error
                    }

                });
        this.data = datos;
        System.out.println("****FireBAseObtene" + datos.isExisteEstacion());
        return data;
    }

    public float obtenerPrecioRegular(final DBListener listener, Parametros datos){

        dbListener = listener;
        mDatabase.child(ESTACIONES).child(datos.getPlace_id()).child("tipos_gasolina").child("REGULAR").addListenerForSingleValueEvent(

                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String e = dataSnapshot.getValue(String.class);
                        float resultado;
                        if (e == null) {
                            resultado = 0;
                        } else {
                            resultado = Float.parseFloat(e);
                        }

                        listener.onResultRegular(resultado);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // error
                    }

                });
        return precio_regular;
    }

    public float obtenerPrecioPremium(final DBListener listener, Parametros datos){

        dbListener = listener;
        mDatabase.child(ESTACIONES).child(datos.getPlace_id()).child("tipos_gasolina").child("PREMIUM").addListenerForSingleValueEvent(

                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String e = dataSnapshot.getValue(String.class);
                        float resultado;
                        if (e == null) {
                            resultado = 0;
                        } else {
                            resultado = Float.parseFloat(e);
                        }

                        listener.onResultRegular(resultado);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // error
                    }

                });
        return precio_premium;
    }

    public float obtenerPrecioACPM(final DBListener listener, Parametros datos){

        dbListener = listener;
        mDatabase.child(ESTACIONES).child(datos.getPlace_id()).child("tipos_gasolina").child("ACPM").addListenerForSingleValueEvent(

                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String e = dataSnapshot.getValue(String.class);
                        float resultado;
                        if (e == null) {
                            resultado = 0;
                        } else {
                            resultado = Float.parseFloat(e);
                        }

                        listener.onResultRegular(resultado);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // error
                    }

                });
        return precio_acpm;
    }

    public float obtenerPrecioGas(final DBListener listener, Parametros datos){

        dbListener = listener;
        mDatabase.child(ESTACIONES).child(datos.getPlace_id()).child("tipos_gasolina").child("GAS").addListenerForSingleValueEvent(

                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String e = dataSnapshot.getValue(String.class);
                        float resultado;
                        if (e == null) {
                            resultado = 0;
                        } else {
                            resultado = Float.parseFloat(e);
                        }

                        listener.onResultRegular(resultado);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // error
                    }

                });
        return precio_gas;
    }
}


