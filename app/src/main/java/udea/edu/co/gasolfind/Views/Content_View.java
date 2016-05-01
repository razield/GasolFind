package udea.edu.co.gasolfind.Views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import udea.edu.co.gasolfind.R;

/**
 * Created by Juan Felipe Zuluaga on 22/04/2016.
 */
public class Content_View  implements GoogleMap.InfoWindowAdapter {
    
    private Marker marker;
	private View myContentsView = null;

	public Content_View(Context contex){
       myContentsView = View.inflate(contex, R.layout.view_content, null);
	}

    @Override
    public View getInfoWindow(Marker marker) {
        this.marker = marker;

        ImageView image = (ImageView)myContentsView.findViewById(R.id.imageContent);
        image.setImageResource(R.drawable.gas_station_black);
        TextView tittleTextView = ((TextView)myContentsView.findViewById(R.id.tittle));
        TextView infoTextView = ((TextView)myContentsView.findViewById(R.id.info));
        TextView priceTextView = (TextView)myContentsView.findViewById(R.id.priceRG);
        TextView priceGas = (TextView)myContentsView.findViewById(R.id.priceGas);
        TextView priceAcpm = (TextView)myContentsView.findViewById(R.id.priceAcpm);
        TextView priceGasP = (TextView)myContentsView.findViewById(R.id.priceP);

        tittleTextView.setText(marker.getTitle());
        infoTextView.setText(marker.getSnippet());
        priceTextView.setText("Precio!!!!");
        return myContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (this.marker != null&& this.marker.isInfoWindowShown()) {
            this.marker.hideInfoWindow();
            this.marker.showInfoWindow();
        }
        return null;
    }
}
