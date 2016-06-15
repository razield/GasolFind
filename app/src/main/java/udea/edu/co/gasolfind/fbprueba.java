package udea.edu.co.gasolfind;
import udea.edu.co.gasolfind.Firebasegasolfind.FirebaseGasolfind;

public class fbprueba {

    public fbprueba() {

        FirebaseGasolfind fb = new FirebaseGasolfind();

        /*
        // Para usuarios
        String claveUsuario1 = fb.registrarUsuario("ensayo1", "correo1@mail.com");
        String claveUsuario2 = fb.registrarUsuario("ensayo2", "correo2@mail.com");
        String claveUsuario3 = fb.registrarUsuario("ensayo3", "correo3@mail.com");

        // Hay que añadir lo del place_id que es con Juan Felipe
        fb.registrarEstacion("-KK6PcabS_GJkSxyR28b", "12","4.6","EssoMobile","1234567","CalleX CarreraY","-25.6456","84.5645","L-D 5 a 20");
        fb.registrarEstacion("-KK6Pcao8XvkAsKF0NGp", "24","2.6","Exxon","7654321","Carrera M CarreraN","-25.6456","84.5645","L-D 5 a 20");
        fb.registrarEstacion("-KK6Pcam3QuDAgkV3AyD", "36","2.1","Pirat","7564542","Calle3 Carrera4","-25.6456","84.5645","L-D 5 a 20");
        fb.registrarEstacion("-KK6PcaksUtEQsADo-As", "48","2.5","Bar-Ata","7564231","Calle6 CarreraO","-25.6456","84.5645","L-D 5 a 20");

        // Hay que añadir lo del place_id que es con Juan Felipe
        fb.registrarTipoGasolina("-KK6PcabS_GJkSxyR28b", "ACPM", "12000");
        fb.registrarTipoGasolina("-KK6Pcao8XvkAsKF0NGp", "REGULAR", "13000");
        fb.registrarTipoGasolina("-KK6PcabS_GJkSxyR28b", "PREMIUM", "14000");
        fb.registrarTipoGasolina("-KK6Pcam3QuDAgkV3AyD", "GAS", "15000");
        fb.registrarTipoGasolina("-KK6PcabS_GJkSxyR28b", "ACPM", "16000");
        fb.registrarTipoGasolina("-KK6Pcam3QuDAgkV3AyD", "ACPM", "17000");
        fb.registrarTipoGasolina("-KK6Pcao8XvkAsKF0NGp", "GAS", "18000");

        // Comentarios
        fb.registrarComentario("-KK6PcabS_GJkSxyR28b", "Me parece un robo", claveUsuario1, "1");
        fb.registrarComentario("-KK6Pcam3QuDAgkV3AyD", "Excelente servicio", claveUsuario2, "5");
        fb.registrarComentario("-KK6Pcao8XvkAsKF0NGp", "MASO", claveUsuario2, "3");
        fb.registrarComentario("-KK6Pcao8XvkAsKF0NGp", "Nada mal", claveUsuario3, "3.9");

        // Actualizar precio gasolina
        fb.actualizarPrecioGasolina("-KK6PcabS_GJkSxyR28b","ACPM","15000");
        */

        boolean b = fb.existeEstacion("-KK6PcabS_GJkSxyR28b");
        System.out.println("---------------+++++++++++++ EXISTE O NO: " + b);
    }

    public static void resultadoEstacion(boolean b){
        System.out.println("mmmmmmmmmmmmmmmm: " + b);
        b2 = b;
    }
}
