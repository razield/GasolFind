package udea.edu.co.gasolfind.ControlFunctions;

/**
 * Created by juancho on 6/15/16.
 */
public class Parametros {
    public boolean isExisteEstacion() {
        return existeEstacion;
    }

    public void setExisteEstacion(boolean existeEstacion) {
        this.existeEstacion = existeEstacion;
    }

    private boolean existeEstacion;
    private String place_id;
    private String name;
    private String lat;
    private String lng;
    private String address;
    private String phone = "0";
    public String gasolina;
    public String premium;
    public String gas;
    public String acpm;


    public Parametros(boolean existeEstacion, String place_id, String name, String lat, String lng, String address, String phone, String gasolina, String premium, String gas, String acpm) {
        this.existeEstacion = existeEstacion;
        this.place_id = place_id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.phone = phone;
        this.gasolina = gasolina;
        this.premium = premium;
        this.gas = gas;
        this.acpm = acpm;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGasolina() {
        return gasolina;
    }

    public void setGasolina(String gasolina) {
        this.gasolina = gasolina;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getAcpm() {
        return acpm;
    }

    public void setAcpm(String acpm) {
        this.acpm = acpm;
    }





}

