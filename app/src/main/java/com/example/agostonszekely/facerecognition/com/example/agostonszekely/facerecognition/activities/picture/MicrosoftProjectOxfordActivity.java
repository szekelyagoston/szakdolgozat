package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.activities.picture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agostonszekely.facerecognition.R;
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
import java.io.IOException;
import java.util.List;


public class MicrosoftProjectOxfordActivity extends SimpleFaceDetectingActivity{

    private final int PICK_IMAGE = 1;
    private final int MENUPOSITION = 0;
    private ProgressDialog detectionProgressDialog;

    private IFaceRecognition faceRecognition;

    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        drawerList.setItemChecked(MENUPOSITION, true);
        setTitle(titles[MENUPOSITION]);
        drawerLayout.closeDrawer(drawerList);



        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_GET_CONTENT);
                callIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(callIntent, "Select Picture"), PICK_IMAGE);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap src = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Matrix rotationMatrix = OrientationHelper.rotate270();
                bitmap = BitmapProducer.CreateBitmap(src, rotationMatrix);
                ImageView imageView = (ImageView) findViewById(R.id.imageView1);
                imageView.setImageBitmap(bitmap);

                detectAndFrame(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
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

}
