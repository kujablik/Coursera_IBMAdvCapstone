package com.example.courseraibmcapstone;


import android.content.res.AssetManager;
import android.graphics.Color;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;


public class ImagePredictor {
    private static final String MODEL_FILE =
            "file:///android_asset/tf_model.pb";
    private static final String INPUT_TENSOR_NAME =
            "input_1";
    private static final String OUTPUT_TENSOR_NAME =
            "dense_2/Sigmoid";

    public static final int IMG_W = 224;
    public static final int IMG_H = 224;
    private static final int NUMBER_OF_CHANNELS = 3;
    private static final int BATCH_SIZE = 1;
    private   TensorFlowInferenceInterface infer;

    public ImagePredictor() {
        AssetManager assetMgr = MainApplication.getAppContext().getAssets();
        infer = new TensorFlowInferenceInterface(assetMgr, MODEL_FILE);
    }
    public float predict(final int [] pixels){
        float [] rgb = new float[IMG_W * IMG_H * 3];
        for (int i = 0; i < pixels.length; i++){
            int p = pixels[i];
            rgb[i*3] = Color.red(p);
            rgb[i*3 + 1] = Color.green(p);
            rgb[i*3 + 2] = Color.blue(p);
        }
        //float [] fake = new float[244*244*3];
        infer.feed(INPUT_TENSOR_NAME, rgb/*pixels*/,BATCH_SIZE,IMG_W,IMG_H,NUMBER_OF_CHANNELS);
        infer.run(new String[]{OUTPUT_TENSOR_NAME});
        float [] outputs = new float[1];
        infer.fetch(OUTPUT_TENSOR_NAME, outputs);
        return outputs[0] * 100;
    }
}
