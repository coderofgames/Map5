package com.discretegames.dave.map5;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.util.Arrays;
import java.util.List;

import java.io.ByteArrayOutputStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);

    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);

    private static final LatLng PERTH = new LatLng(-31.95285, 115.85734);

    private static final LatLng LHR = new LatLng(51.471547, -0.460052);

    private static final LatLng LAX = new LatLng(33.936524, -118.377686);

    private static final LatLng JFK = new LatLng(40.641051, -73.777485);

    private static final LatLng AKL = new LatLng(-37.006254, 174.783018);

    private static final LatLng LONDON =  new LatLng(51.5074, 0.1278);


    private Polygon mMutablePolygon;

    private Polygon mClickablePolygonWithHoles;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mClickablePolygonWithHoles = mMap.addPolygon(new PolygonOptions()
                .addAll(createRectangle(new LatLng(-20, 130), 5, 5))
                .addHole(createRectangle(new LatLng(-22, 128), 1, 1))
                .addHole(createRectangle(new LatLng(-18, 133), 0.5, 1.5))
                .fillColor(Color.CYAN)
                .strokeColor(Color.BLUE)
                .strokeWidth(5));

        // Create a rectangle centered at Sydney.
        PolygonOptions options = new PolygonOptions()
                .addAll(createRectangle(LONDON, 5, 8));

        int fillColor = Color.GREEN;//(
        //0.7, new float[]{255, 1, 1});

        mMutablePolygon = mMap.addPolygon(options
                .strokeWidth(5)
                .strokeColor(Color.BLACK)
                .fillColor(fillColor));

        // Create another polygon that overlaps the previous two.
        // Clickability defaults to false, so this one won't accept clicks.
        mMap.addPolygon(new PolygonOptions()
                .addAll(createRectangle(new LatLng(-27, 140), 10, 7))
                .fillColor(Color.WHITE)
                .strokeColor(Color.BLACK));

        // Add a listener for polygon clicks that changes the clicked polygon's stroke color.
        // A simple polyline with the default options from Melbourne-Adelaide-Perth.
        mMap.addPolyline((new PolylineOptions())
                .add(MELBOURNE, ADELAIDE, PERTH));

        // A geodesic polyline that goes around the world.
        mMap.addPolyline((new PolylineOptions())
                .add(LHR, AKL, LAX, JFK, LHR)
                .width(5)
                .color(Color.BLUE)
                .geodesic(true)
        );
        // Rectangle centered at Sydney.  This polyline will be mutable.
        int radius = 5;
        PolylineOptions options2 = new PolylineOptions()
                .add(new LatLng(SYDNEY.latitude + radius, SYDNEY.longitude + radius))
                .add(new LatLng(SYDNEY.latitude + radius, SYDNEY.longitude - radius))
                .add(new LatLng(SYDNEY.latitude - radius, SYDNEY.longitude - radius))
                .add(new LatLng(SYDNEY.latitude - radius, SYDNEY.longitude + radius))
                .add(new LatLng(SYDNEY.latitude + radius, SYDNEY.longitude + radius));

        //(
        //               mAlphaBar.getProgress(), new float[]{mColorBar.getProgress(), 1, 1});
        mMap.addPolyline(options2
                .color(Color.GREEN)
                .width(15)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));


        TileProvider coordTileProvider = new CoordTileProvider(this.getApplicationContext());
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(coordTileProvider));
        MapStyleOptions style = new MapStyleOptions("[" +
                "  {" +
                "    \"featureType\":\"poi.business\"," +
                "    \"elementType\":\"all\"," +
                "    \"stylers\":[" +
                "      {" +
                "        \"visibility\":\"off\"" +
                "      }" +
                "    ]" +
                "  }," +
                "  {" +
                "    \"featureType\":\"transit\"," +
                "    \"elementType\":\"all\"," +
                "    \"stylers\":[" +
                "      {" +
                "        \"visibility\":\"off\"" +
                "      }" +
                "    ]" +
                "  }" +
                "]");

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json ));

            if (!success) {
                mMap.setMapStyle(style);
            }
        } catch (Exception e) {
           // Log.e("MapsActivityRaw", "Can't find style.", e);
        }


    }

    /**
     * Creates a List of LatLngs that form a rectangle with the given dimensions.
     */
    private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
        return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
    }

    private static class CoordTileProvider implements TileProvider {

        private static final int TILE_SIZE_DP = 256;

        private final float mScaleFactor;

        private final Bitmap mBorderTile;

        public CoordTileProvider(Context context) {
            /* Scale factor based on density, with a 0.6 multiplier to increase tile generation
             * speed */
            mScaleFactor = context.getResources().getDisplayMetrics().density * 0.6f;
            Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            borderPaint.setStyle(Paint.Style.STROKE);
            mBorderTile = Bitmap.createBitmap((int) (TILE_SIZE_DP * mScaleFactor),
                    (int) (TILE_SIZE_DP * mScaleFactor), android.graphics.Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mBorderTile);
            canvas.drawRect(0, 0, TILE_SIZE_DP * mScaleFactor, TILE_SIZE_DP * mScaleFactor,
                    borderPaint);
        }

        @Override
        public Tile getTile(int x, int y, int zoom) {
            Bitmap coordTile = drawTileCoords(x, y, zoom);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            coordTile.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byte[] bitmapData = stream.toByteArray();
            return new Tile((int) (TILE_SIZE_DP * mScaleFactor),
                    (int) (TILE_SIZE_DP * mScaleFactor), bitmapData);
        }

        private Bitmap drawTileCoords(int x, int y, int zoom) {
            // Synchronize copying the bitmap to avoid a race condition in some devices.
            Bitmap copy = null;
            synchronized (mBorderTile) {
                copy = mBorderTile.copy(android.graphics.Bitmap.Config.ARGB_8888, true);
            }
            Canvas canvas = new Canvas(copy);
            String tileCoords = "(" + x + ", " + y + ")";
            String zoomLevel = "zoom = " + zoom;
            /* Paint is not thread safe. */
            Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTextSize(18 * mScaleFactor);
            canvas.drawText(tileCoords, TILE_SIZE_DP * mScaleFactor / 2,
                    TILE_SIZE_DP * mScaleFactor / 2, mTextPaint);
            canvas.drawText(zoomLevel, TILE_SIZE_DP * mScaleFactor / 2,
                    TILE_SIZE_DP * mScaleFactor * 2 / 3, mTextPaint);
            return copy;
        }
    }
}
