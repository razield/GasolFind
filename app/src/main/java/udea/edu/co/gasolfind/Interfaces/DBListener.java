package udea.edu.co.gasolfind.Interfaces;

/**
 * Created by santiago on 6/15/16.
 */
public interface DBListener {
    public void onResult(Parametros datos, boolean existe);
    public void onResultGas(Parametros datos, float precio);
    public void onResultACPM(Parametros datos, float precio);
    public void onResultRegular(Parametros datos, float precio);
    public void onResultPremium(Parametros datos, float precio);
}


