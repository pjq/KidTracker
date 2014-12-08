package com.gracecode.tracker.ui.activity.base;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.gracecode.tracker.util.Helper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public abstract class MapActivity extends Activity{
    protected MapView mapView = null;
//    protected MapController mapViewController;
//    protected BMapManager bMapManager;
    private static final String BAIDU_MAP_KEY = "30183AD8A6AFE7CE8F649ED4CD258211E8DE78D7";
//    private static final String BAIDU_MAP_KEY = "w3yjAdy9zK5ZAoO3XaGXrWQL";
    protected Helper helper;
    protected Context context;
    protected android.app.ActionBar actionBar;

    protected ArrayList<Location> locations;

    protected double topBoundary;
    protected double leftBoundary;
    protected double rightBoundary;
    protected double bottomBoundary;

    protected Location locationTopLeft;
    protected Location locationBottomRight;
    protected float maxDistance;
//    protected GeoPoint mapCenterPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        actionBar = getActionBar();

        helper = new Helper(context);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    protected void setCenterPoint(Location location, boolean animate) {
        if (mapView == null) {
            return;
        }

//        GeoPoint geoPoint = getRealGeoPointFromLocation(location);
//        if (animate) {
//            mapViewController.animateTo(geoPoint);
//        } else {
//            mapViewController.setCenter(geoPoint);
//        }
    }

    protected void setCenterPoint(Location location) {
        setCenterPoint(location, false);
    }
//
//    protected GeoPoint getGeoPoint(Location location) {
//        GeoPoint geoPoint = new GeoPoint(
//            (int) (location.getLatitude() * 1E6),
//            (int) (location.getLongitude() * 1E6)
//        );
//
//        return geoPoint;
//    }
//
//    protected GeoPoint getRealGeoPointFromLocation(Location location) {
//        GeoPoint geoPoint = getGeoPoint(location);
//        return CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(geoPoint));
//    }
//
//    protected GeoPoint getRealGeoPointFromGeo(GeoPoint geoPoint) {
//        return CoordinateConvert.bundleDecode(CoordinateConvert.fromWgs84ToBaidu(geoPoint));
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


//    @Override
//    public void onGetPermissionState(int iError) {
//        if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
//
//        }
//    }

    protected void getBoundary() {
        leftBoundary = locations.get(0).getLatitude();
        bottomBoundary = locations.get(0).getLongitude();

        rightBoundary = locations.get(0).getLatitude();
        topBoundary = locations.get(0).getLongitude();

        for (Location location : locations) {
            if (leftBoundary > location.getLatitude()) {
                leftBoundary = location.getLatitude();
            }

            if (rightBoundary < location.getLatitude()) {
                rightBoundary = location.getLatitude();
            }

            if (topBoundary < location.getLongitude()) {
                topBoundary = location.getLongitude();
            }

            if (bottomBoundary > location.getLongitude()) {
                bottomBoundary = location.getLongitude();
            }
        }

        locationTopLeft = new Location("");
        locationTopLeft.setLongitude(topBoundary);
        locationTopLeft.setLatitude(leftBoundary);

        locationBottomRight = new Location("");
        locationBottomRight.setLongitude(bottomBoundary);
        locationBottomRight.setLatitude(rightBoundary);

        maxDistance = locationTopLeft.distanceTo(locationBottomRight);
//        mapCenterPoint = getRealGeoPointFromGeo(new GeoPoint(
//            (int) ((leftBoundary + (rightBoundary - leftBoundary) / 2) * 1e6),
//            (int) ((bottomBoundary + (topBoundary - bottomBoundary) / 2) * 1e6)
//        ));
    }

//    protected int getFixedZoomLevel() {
//        int fixedLatitudeSpan = (int) ((rightBoundary - leftBoundary) * 1e6);
//        int fixedLongitudeSpan = (int) ((topBoundary - bottomBoundary) * 1e6);
//
//
//        for (int i = (int)mapView.getMap().getMaxZoomLevel(); i > 0; i--) {
//            mapViewController.setZoom(i);
//            int latSpan = mapView.getLatitudeSpan();
//            int longSpan = mapView.getLongitudeSpan();
//
//            if (latSpan > fixedLatitudeSpan && longSpan > fixedLongitudeSpan) {
//                return i;
//            }
//        }
//
//        return mapView.getMaxZoomLevel();
//    }

    protected LatLng locatio2LatLng(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
