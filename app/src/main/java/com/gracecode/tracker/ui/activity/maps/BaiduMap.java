package com.gracecode.tracker.ui.activity.maps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.gracecode.tracker.R;
import com.gracecode.tracker.dao.ArchiveMeta;
import com.gracecode.tracker.dao.Archiver;
import com.gracecode.tracker.ui.activity.Detail;
import com.gracecode.tracker.ui.activity.Records;
import com.gracecode.tracker.ui.activity.base.MapActivity;
import com.umeng.socialize.utils.BitmapUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BaiduMap extends MapActivity implements SeekBar.OnSeekBarChangeListener {
    private Archiver archiver;

    private Context context;

    private String archiveFileName;
    private SeekBar mSeeker;
    private SimpleDateFormat dateFormat;
    private ToggleButton mSatellite;
    private View mapController;
    //private PathOverlay pathOverlay;
//    private PointMarkLayout currentMarkLayout;
    private Marker marker;
    private com.baidu.mapapi.map.BaiduMap baiduMap;


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        // Helper.Logger.i("onProgressChanged");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Helper.Logger.i("onStartTrackingTouch");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        try {
            Location location = locations.get(seekBar.getProgress() - 1);
            String recordsFormatter = getString(R.string.records_formatter);

            helper.showShortToast(
                    dateFormat.format(location.getTime()) + "\n" +
                            String.format(recordsFormatter, location.getSpeed() * ArchiveMeta.KM_PER_HOUR_CNT) + "km/h"
            );

            // if (currentMarkLayout != null) {
            //mapView.getOverlay().remove(currentMarkLayout);
            // mapView.getMap().

            // }
            // currentMarkLayout = new PointMarkLayout(location, R.drawable.point);
//            mapView.getOverlays().add(currentMarkLayout);
//            mapView.getMap().addOverlay(currentMarkLayout);

            //setCenterPoint(location, true);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.baidu_map);

        context = this;
        mapView = (MapView) findViewById(R.id.bmapsView);
        baiduMap = mapView.getMap();
        mapController = findViewById(R.id.map_controller);
        archiveFileName = getIntent().getStringExtra(Records.INTENT_ARCHIVE_FILE_NAME);
        mSeeker = (SeekBar) findViewById(R.id.seek);

        // Default date format.
        dateFormat = new SimpleDateFormat(getString(R.string.time_format), Locale.getDefault());

        archiver = new Archiver(getApplicationContext(), archiveFileName);
        locations = archiver.fetchAll();

        // 计算边界
        getBoundary();
    }


    @Override
    public void onResume() {
//        actionBar.removeAllActions();
        super.onResume();

        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getIntent();
        if (intent.getBooleanExtra(Detail.INSIDE_TABHOST, false)) {
            if (actionBar != null) {
//                actionBar.setVisibility(View.GONE);
            }
            mapController.setVisibility(View.GONE);
        }

        mSeeker.setMax(locations.size());
        mSeeker.setProgress(0);
        mSeeker.setOnSeekBarChangeListener(this);

//        mSatellite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mSatellite.isChecked()) {
//                    mapView.setSatellite(true);
//                } else {
//                    mapView.setSatellite(false);
//                }
//
//                bMapManager.stop();
//                bMapManager.start();
//                helper.showShortToast(getString(R.string.toggle_satellite));
//            }
//        });

        addPathOverlay();


        //mapView.getMap().addOverlay(new PointMarkLayout(archiver.getFirstRecord(), R.drawable.point_start));
        // mapView.getMap().addOverlay(new PointMarkLayout(archiver.getLastRecord(), R.drawable.point_end));
//        mapView.getOverlays().add(new PointMarkLayout(archiver.getFirstRecord(), R.drawable.point_start));
//        mapView.getOverlays().add(new PointMarkLayout(archiver.getLastRecord(), R.drawable.point_end));

        // @todo 自动计算默认缩放的地图界面
        //mapView.getMap().setMaxAndMinZoomLevel(getFixedZoomLevel(), getFixedZoomLevel());
        // mapViewController.setCenter(mapCenterPoint);
//        mapViewController.setZoom(getFixedZoomLevel());
    }

    private void addCircle() {
//        LatLng point = new LatLng(39.963175, 116.400244);
//        OverlayOptions polygonOption = new PolygonOptions()
//                .points(pts)
//                .stroke(new Stroke(5, 0xAA00FF00))
//                .fillColor(0xAAFFFF00);
//
//        mapView.getMap().addOverlay(polygonOption);

    }

    @Override
    public void onPause() {
        super.onPause();

        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        archiver.close();
        super.onDestroy();

        mapView.onDestroy();
    }

    private void addPathOverlay() {
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.91923, 116.327428);
        LatLng pt3 = new LatLng(39.89923, 116.347428);
        LatLng pt4 = new LatLng(39.89923, 116.367428);
        LatLng pt5 = new LatLng(39.91923, 116.387428);
        List<LatLng> pts = new ArrayList<LatLng>();

        for (Location location : locations) {
            pts.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        if (locations.size() <= 4) {
            for (int i = 0; i< 10;i++){
                pts.add(new LatLng(locations.get(0).getLatitude()+ 0.01 * i,  locations.get(0).getLongitude()));
            }

        }

//构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(5, 0xAA00FF00))
                .fillColor(0xAAFFFF00);
//在地图上添加多边形Option，用于显示
        mapView.getMap().addOverlay(polygonOption);


        OverlayOptions options = new MarkerOptions()
                .position(locatio2LatLng(archiver.getFirstRecord()))  //设置marker的位置
                .icon(BitmapDescriptorFactory
                        .fromResource((R.drawable.point_start)))  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .title("Text marker")
                .draggable(true);  //设置手势拖拽
//将marker添加到地图上
        marker = (Marker) (mapView.getMap().addOverlay(options));

        // 添加圆
        LatLng llCircle = new LatLng(39.90923, 116.447428);
        llCircle = locatio2LatLng(archiver.getFirstRecord());
        OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
                .center(llCircle).stroke(new Stroke(5, 0xAA000000))
                .radius(1400);
        mapView.getMap().addOverlay(ooCircle);

        LatLng llDot = new LatLng(39.98923, 116.397428);
        llDot = llCircle;
        OverlayOptions ooDot = new DotOptions().center(llDot).radius(6)
                .color(0xFF0000FF);
        mapView.getMap().addOverlay(ooDot);

    }

//    private class PathOverlay extends OverlayOptions {
//        private Paint paint;
//        private Projection projection;
//        private static final int MIN_POINT_SPAN = 5;
//
//        public PathOverlay() {
//            setPaint();
//        }
//
//        private void setPaint() {
//            paint = new Paint();
//            paint.setAntiAlias(true);
//            paint.setDither(true);
//
//            paint.setColor(getResources().getColor(R.color.highlight));
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeJoin(Paint.Join.ROUND);
//            paint.setStrokeCap(Paint.Cap.ROUND);
//            paint.setStrokeWidth(5);
//            paint.setAlpha(188);
//        }
//
//
//        @Override
//        public void draw(final Canvas canvas, final MapView mapView, boolean shadow) {
//            this.projection = mapView.getMap().getProjection();
//
//            if (!shadow) {
//                synchronized (canvas) {
//                    final Path path = new Path();
//                    final int maxWidth = mapView.getWidth();
//                    final int maxHeight = mapView.getHeight();
//
//                    Point lastGeoPoint = null;
//                    for (Location location : locations) {
//                        //Point current = projection.toPixels(getRealGeoPointFromLocation(location), null);
//                        Point current = projection.toScreenLocation(new LatLng(location.getLatitude(), location.getLongitude()));
//
//
//                        if (lastGeoPoint != null && (lastGeoPoint.y < maxHeight && lastGeoPoint.x < maxWidth)) {
///*                            if (Math.abs(current.x - lastGeoPoint.x) < MIN_POINT_SPAN
//                                || Math.abs(current.y - lastGeoPoint.y) < MIN_POINT_SPAN) {
//                                continue;
//                            } else {*/
//                            path.lineTo(current.x, current.y);
//                            /*                   }*/
//                        } else {
//                            path.moveTo(current.x, current.y);
//                        }
//                        lastGeoPoint = current;
//                    }
//
//                    canvas.drawPath(path, paint);
//                }
//            }
//        }
//    }
//
//    private class PointMarkLayout extends OverlayOptions {
//        private Location location;
//        private int drawable;
//        private Projection projection;
//
//        PointMarkLayout(Location location, int drawable) {
//            this.location = location;
//            this.drawable = drawable;
//        }
//
////        @Override
////        public void draw(final Canvas canvas, final MapView mapView, boolean shadow) {
////            super.draw(canvas, mapView, shadow);
////
////            this.projection = mapView.getMap().getProjection();
////            //Point point = projection.toPixels(getRealGeoPointFromLocation(location), null);
////            Point point = projection.toScreenLocation(new LatLng(location.getLatitude(), location.getLongitude()));
////
////            Bitmap markerImage = BitmapFactory.decodeResource(getResources(), drawable);
////
////            // 根据实际的条目而定偏移位置
////            canvas.drawBitmap(markerImage,
////                point.x - Math.round(markerImage.getWidth() * 0.4),
////                point.y - Math.round(markerImage.getHeight() * 0.9), null);
////
////            return;
////        }
//    }
}
