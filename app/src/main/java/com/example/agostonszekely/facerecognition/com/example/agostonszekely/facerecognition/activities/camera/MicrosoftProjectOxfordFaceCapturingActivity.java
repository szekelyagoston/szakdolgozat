package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.camera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agostonszekely.facerecognition.R;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.NavigationDrawerActivity;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services.AsyncResponse;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.BitmapProducer;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.helpers.OrientationHelper;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.rectangle.FaceRectangleEnum;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.commons.face.wrapper.FaceProperties;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.googlemobilevision.exceptions.NoContextPresentException;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.interfaces.IFaceRecognition;
import com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.modules.microsoftoxford.MicrosoftProjectOxford;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by agoston.szekely on 2016.10.19..
 */

public class MicrosoftProjectOxfordFaceCapturingActivity extends NavigationDrawerActivity {
    private final int MENUPOSITION = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ProgressDialog detectionProgressDialog;

    private IFaceRecognition faceRecognition;

    private Bitmap bitmap = null;

    private Uri imageUri;
    private File photo;

    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_detect_picture_mpo);
        super.onCreate(savedInstanceState);

        drawerList.setItemChecked(MENUPOSITION, true);
        setTitle(titles[MENUPOSITION]);
        drawerLayout.closeDrawer(drawerList);



        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try
                {
                    // place where to store camera taken picture
                    photo = createTemporaryFile("picture", ".jpg");
                }
                catch(Exception e)
                {

                }

                imageUri = Uri.fromFile(photo);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                //finding an app which can handle the intent. If there is no such an app, we can not perform the tas
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        faceRecognition = new MicrosoftProjectOxford(new AsyncResponse<List<FaceProperties>>() {
            @Override
            public void processFinish(List<FaceProperties> faces) {
                detectionProgressDialog.dismiss();
                ImageView imageView = (ImageView)findViewById(R.id.imageView1);
                imageView.setImageBitmap(drawFaceRectanglesOnBitmap(bitmap, faces));
                bitmap.recycle();

                //ugly. should be placed above rectangles
                if (faces.size() >0){
                    TextView rollTextView = (TextView)findViewById(R.id.roll);
                    rollTextView.setText(faces.get(0).getFacePosition().getFacePosition().getReadableName() + " ( " + faces.get(0).getFacePosition().getYaw() + " )" ) ;
                }

            }
        });
        detectionProgressDialog = new ProgressDialog(this);
    }

    private File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            ImageView imageView = (ImageView) findViewById(R.id.imageView1);
            grabImage(imageView);
            photo.deleteOnExit();

            try {
                detectAndFrame(bitmap);
            } catch (NoContextPresentException e) {
                e.printStackTrace();
            }

        }
    }

    // Detect faces by uploading face images
    // Frame faces after detection

    private void detectAndFrame(final Bitmap imageBitmap) throws NoContextPresentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //compress quality -> if too high, method will be slow.
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 3, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());

        detectionProgressDialog.setMessage("Detecting");
        detectionProgressDialog.show();
        faceRecognition.getFaceRectangle(inputStream);

    }


    private static Bitmap drawFaceRectanglesOnBitmap(Bitmap originalBitmap, List<FaceProperties> faces) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        int stokeWidth = 6;
        paint.setStrokeWidth(stokeWidth);
        if (faces != null) {
            for (FaceProperties face : faces) {

                canvas.drawRect(
                        face.getFaceRectangle().getFaceRectangleValues(FaceRectangleEnum.LEFT),
                        face.getFaceRectangle().getFaceRectangleValues(FaceRectangleEnum.TOP),
                        face.getFaceRectangle().getFaceRectangleValues(FaceRectangleEnum.RIGHT),
                        face.getFaceRectangle().getFaceRectangleValues(FaceRectangleEnum.BOTTOM),
                        paint);
            }
        }
        return bitmap;
    }

    private void grabImage(ImageView imageView)
    {
        this.getContentResolver().notifyChange(imageUri, null);
        ContentResolver cr = this.getContentResolver();
        try
        {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, imageUri);

            //roll the picture
            Matrix rotationMatrix = null;
            try {
                rotationMatrix = OrientationHelper.rotate270();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = BitmapProducer.CreateBitmap(bitmap, rotationMatrix);

            imageView.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
        }
    }
}
