package arman.opencv;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.opencv.imgcodecs.Imgcodecs.imread;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PTW";
    int [] gr={11,11};
    String [] sr={"00","11"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();
        setContentView(R.layout.activity_main);
        OpenCVLoader.initDebug();
    }

    public void displayToast(View v){

        Mat img = null;

        try {
            img = Utils.loadResource(getApplicationContext(), R.drawable.he);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);

        Mat img_result = img.clone();
        Imgproc.Canny(img, img_result, 80, 90);
        Bitmap img_bitmap = Bitmap.createBitmap(img_result.cols(), img_result.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_result, img_bitmap);
        ImageView imageView = findViewById(R.id.img);
        imageView.setImageBitmap(img_bitmap);

        //Mat cokeBGR = imread("/sdcard/Downloads/ivan_1.jpg");
        //Mat cokeRGBA = new Mat();
        //Imgproc.cvtColor(cokeBGR, cokeRGBA, Imgproc.COLOR_BGR2RGBA);
        //Imgproc.resize(cokeRGBA, sm, sm.size());

        //File imgFile = new  File("/storage/emulated/0/Downloads/pants.jpg");
        //Boolean mn=imgFile.exists();
        //if(imgFile.exists())
        //{
        //   ImageView myImage = new ImageView(this);
        //    myImage.setImageURI(Uri.fromFile(imgFile));
        //}


        InputStream stream = null;
        String fileName = "ivan_1.jpg";
        // see View > Tool Windows > Device File Explorer for VD directores
        String completePath = "/sdcard/Download/" + fileName;

        //String completePath = "storage/emulated/0/Download/pants.jpg";
        //File f = new File(DesktopPath);
        //File f = new File(getApplicationInfo().dataDir);
        //File[] files = f.listFiles();
        //Log.v(TAG,getApplicationInfo().dataDir);

        File file = new File(completePath);
        Uri uri = Uri.fromFile(file);
        //  Uri uri = Uri.parse("android.resource://arman.opencv/drawable/pants");
        //  Uri uri = Uri.parse(completePath);

        try {
            stream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmp = BitmapFactory.decodeStream(stream, null, bmpFactoryOptions);
        Mat ImageMat = new Mat();
        Utils.bitmapToMat(bmp, ImageMat);
        imageView.setImageBitmap(bmp);
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission to READ EXTERNAL STORAGE is granted");
                return true;
            } else {

                Log.v(TAG,"Permission to READ EXTERNAL STORAGE is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"permission to READ EXTERNAL STORAGE is automatically granted on sdk<23 upon installation");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission to WRITE EXTERNAL STORAGE is granted");
                return true;
            } else {

                Log.v(TAG,"Permission to WRITE EXTERNAL STORAGE is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"permission to WRITE EXTERNAL STORAGE is automatically granted on sdk<23 upon installation");
            return true;
        }
    }

}