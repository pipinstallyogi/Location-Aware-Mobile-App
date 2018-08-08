package de.darmstadt.tu.informatik.tk.iptk.utilities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import de.darmstadt.tu.informatik.tk.iptk.activities.BaseFragmentActivity;

/**
 * The type Marsh mellow permissions.
 */
public class MarshMellowPermissions {

    private static final int EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE =10;

    private static final int EXTERNAL_STORAGE_READ_PERMISSION_REQUEST_CODE =11;

    private static final int CAMERA_PERMISSION_REQUEST_CODE =12;

    private static final int LOCATION_PERMISSION_REQUEST_CODE =13;


    private BaseFragmentActivity mActivity;

    /**
     * Instantiates a new Marsh mellow permissions.
     *
     * @param mActivity the m activity
     */
    public MarshMellowPermissions(BaseFragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * Check permission for read external storage boolean.
     *
     * @return the boolean
     */
    public boolean checkPermissionForReadExternalStorage(){
        int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check permission for write external storage boolean.
     *
     * @return the boolean
     */
    public boolean checkPermissionForWriteExternalStorage(){
        int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check permission for camera boolean.
     *
     * @return the boolean
     */
    public boolean checkPermissionForCamera(){
        int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check permission for location boolean.
     *
     * @return the boolean
     */
    public boolean checkPermissionForLocation(){
            int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }


    /**
     * Request permission for read external storage.
     */
    public void requestPermissionForReadExternalStorage(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(mActivity," External Storage permission is needed. Please turn it on inside the settings"
                    ,Toast.LENGTH_SHORT).show();
        } else{
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                    ,EXTERNAL_STORAGE_READ_PERMISSION_REQUEST_CODE);
        }
    }


    /**
     * Request permission for write external storage.
     */
    public void requestPermissionForWriteExternalStorage(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(mActivity," Write Storage permission is needed. Please turn it on inside the settings"
                    ,Toast.LENGTH_SHORT).show();
        } else{
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    ,EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Request permission for camera.
     */
    public void requestPermissionForCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.CAMERA)){
            Toast.makeText(mActivity," Camera permission is needed. Please turn it on inside the settings"
                    ,Toast.LENGTH_SHORT).show();
        } else{
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.CAMERA}
                    ,CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Request permission for location.
     */
    public void requestPermissionForLocation(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(mActivity," Location permission is needed. Please turn it on inside the settings"
                    ,Toast.LENGTH_LONG).show();
        } else{
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    ,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}
