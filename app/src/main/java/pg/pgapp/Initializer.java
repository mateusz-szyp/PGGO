package pg.pgapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import lombok.NonNull;
import pg.pgapp.Database.DatabaseConnector;
import pg.pgapp.Models.BuildingDisplayModel;

public class Initializer {

    private final Context context;

    public Initializer(Context context) {
        this.context = context;
    }

    public void initialize(@NonNull final GoogleMap mMap) {

        setBuildingsOnMap(mMap);

        //pobranie mojej lokalizacji
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        LatLng noweEti = new LatLng(54.371648, 18.612357);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(noweEti)); //TODO: domyślnie będzie move to my location
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }

    private void setBuildingsOnMap(@NonNull final GoogleMap mMap) {
        /*        //todo skonfigurowac ewentualne pobieranie danych z pliku lokalnie
        Type listType = new TypeToken<ArrayList<BuildingDisplayModel>>() {
        }.getType(); //potrzebne żeby wczytać listę obiektów z jsona
        Gson gson = new Gson();
        ArrayList<BuildingDisplayModel> buildings = gson.fromJson(readDataFromFile("BuildingsConfiguration.json"), listType);
        */
        ArrayList<BuildingDisplayModel> buildings = new DatabaseConnector().getBuildingDisplays(0L);
        buildings.forEach(
                building -> {
                    PolygonOptions buildingOptions = new PolygonOptions()
                            .clickable(true)
                            .strokeColor(Color.RED)
                            .strokeWidth(2);

                    building.getCoordinates().forEach(coordinate ->
                            buildingOptions.add(new LatLng(coordinate.getLatitude(), coordinate.getLongitude())));

                    Polygon polygon = mMap.addPolygon(buildingOptions);
                    polygon.setClickable(true);
                    polygon.setTag(building.getBuildingId());
                }
        );
        mMap.setOnPolygonClickListener(new OnPolygonClickListener(context));
    }

    private String readDataFromFile(String filename) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
