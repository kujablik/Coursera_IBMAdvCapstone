package com.example.courseraibmcapstone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnCapture;
    private ImageView imgCapture;
    private TextView txtPrediction;
    private static final int Image_Capture_Code = 1;
    private ImagePredictor predictor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        predictor = new ImagePredictor();
        btnCapture = findViewById(R.id.LOAD_BUTTON);
        imgCapture = findViewById(R.id.MAIN_IMAGE_VIEW);
        txtPrediction = findViewById(R.id.TXT_PEDICTION);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt,Image_Capture_Code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                bp = Bitmap.createScaledBitmap(bp, ImagePredictor.IMG_W, ImagePredictor.IMG_H, false);
                int w = bp.getWidth();
                int h = bp.getHeight();
                int [] pixels = new int[w*h];
                bp.getPixels(pixels, 0, w, 0, 0,
                        w, h);
                float pred_prob = predictor.predict(pixels);
                if(pred_prob < 50.0)
                    txtPrediction.setTextColor(Color.GREEN);
                else
                    txtPrediction.setTextColor(Color.RED);
                txtPrediction.setText("Probability of disease " + String.format("%.2f", pred_prob) + "%");
                imgCapture.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
