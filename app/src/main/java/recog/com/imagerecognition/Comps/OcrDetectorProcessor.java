/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package recog.com.imagerecognition.Comps;

import android.util.Log;
import android.util.SparseArray;


import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import recog.com.imagerecognition.camera.GraphicOverlay;

/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    private String[] recognizable_numbers = {"0","1","2","3","4","5","6","7","8","9"};
    private String[] recognizable_text = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec",
            "januray","feburary","march","april","may","june","july","august","september","october","november","december"};
    public OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay) {
        mGraphicOverlay = ocrGraphicOverlay;
    }
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                Log.d("OcrDetectorProcessor", "Text detected! " + item.getValue());
                if(checkContainNumber(item.getValue()) || checkContainText(item.getValue())) {
                    OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
                    mGraphicOverlay.add(graphic);
                }
            }
        }
    }
    public boolean checkContainNumber(String value){
        for(int i = 0; i< recognizable_numbers.length; i++) {
            if (value.contains(recognizable_numbers[i]))
                return true;
        }
        return false;
    }
    public boolean checkContainText(String value){
        for(int i = 0; i< recognizable_text.length; i++) {
            if (value.contains(recognizable_text[i]))
                return true;
        }
        return false;
    }
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }
}
