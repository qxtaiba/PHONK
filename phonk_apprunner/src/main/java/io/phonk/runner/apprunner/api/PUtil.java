/*
 * Part of Phonk http://www.phonk.io
 * A prototyping platform for Android devices
 *
 * Copyright (C) 2013 - 2017 Victor Diaz Barrales @victordiaz (Protocoder)
 * Copyright (C) 2017 - Victor Diaz Barrales @victordiaz (Phonk)
 *
 * Phonk is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Phonk is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Phonk. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.phonk.runner.apprunner.api;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.FaceDetector;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.welie.blessed.BluetoothBytesParser;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;

import io.phonk.runner.apidoc.annotation.PhonkMethod;
import io.phonk.runner.apidoc.annotation.PhonkMethodParam;
import io.phonk.runner.apidoc.annotation.PhonkObject;
import io.phonk.runner.apprunner.AppRunner;
import io.phonk.runner.apprunner.api.common.ReturnObject;
import io.phonk.runner.apprunner.api.other.PDelay;
import io.phonk.runner.apprunner.api.other.PLooper;
import io.phonk.runner.apprunner.api.other.SignalUtils;
import io.phonk.runner.base.utils.MLog;
import io.phonk.runner.base.views.CanvasUtils;

@PhonkObject
public class PUtil extends ProtoBase {

    public PUtil(AppRunner appRunner) {
        super(appRunner);
    }

    // --------- getRequest ---------//
    interface getRequestCB {
        void event(int eventType, String responseString);
    }

    public void setAnObject(NativeObject obj) {
        for (Map.Entry<Object, Object> entry : obj.entrySet()) {
            String key = (String) entry.getKey();
            Object o = entry.getValue();

            MLog.d(TAG, "setAnObject -> " + key + " " + o);
        }

        MLog.d(TAG, "q --> " + obj.get("q"));
    }

    public ReturnObject getAnObject() {
        // HashMap map = new HashMap();

        ReturnObject ret = new ReturnObject();
        ret.put("qq", 1);
        ret.put("qq 2", 2);

        /*
        NativeObject ret = (NativeObject) getAppRunner().interp.newNativeObject();
        ret.defineProperty("q", 2, NativeObject.READONLY);

        ReturnObject ret1 = new ReturnObject();
        ret1.put();

        */

        return ret;
    }

    /*
     * 1. get arraylist to native array
     * 2. set native array to arraylist
     */
    public NativeArray getAnArray() {
        ArrayList array = new ArrayList();
        array.add("1");
        array.add("2");

        // NativeArray ret = (NativeArray) getAppRunner().interp.newNativeArrayFrom(array.toArray());

        // return ret;
        return null;
    }

    public void setAnArray(NativeArray array) {
        for (int i = 0; i < array.size(); i++) {
            MLog.d(TAG, "setArrayList -> " + array.get(i));
        }
    }

    public String getCharFromUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    @PhonkMethod(description = "Creates a looper that loops a given function every 'n' milliseconds", example = "")
    @PhonkMethodParam(params = {"milliseconds", "function()"})
    public PLooper loop(final int duration, final PLooper.LooperCB callbackkfn) {
        return new PLooper(getAppRunner(), duration, callbackkfn);
    }

    @PhonkMethod(description = "Creates a looper that loops a given function every 'n' milliseconds", example = "")
    @PhonkMethodParam(params = {"milliseconds"})
    public PLooper loop(final int duration) {
        return new PLooper(getAppRunner(), duration, null);
    }

    @PhonkMethod(description = "Delay a given function 'n' milliseconds", example = "")
    @PhonkMethodParam(params = {"milliseconds", "function()"})
    public PDelay delay(final int delay, final PDelay.DelayCB fn) {
        return new PDelay(getAppRunner(), delay, fn);
    }

    // http://stackoverflow.com/questions/4605527/converting-pixels-to-dp

    @PhonkMethod(description = "Convert given dp to pixels", example = "")
    @PhonkMethodParam(params = {""})
    public float dpToPixels(float dp) {
        Resources resources = getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }


    @PhonkMethod(description = "Convert given px to dp", example = "")
    @PhonkMethodParam(params = {""})
    public float pixelsToDp(float px) {
        Resources resources = getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }


    @PhonkMethod(description = "Convert given mm to pixels", example = "")
    @PhonkMethodParam(params = {""})
    public float mmToPixels(float mm) {
        float px = TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_MM, mm, getContext().getResources().getDisplayMetrics());
        return px;
    }


    @PhonkMethod(description = "Convert given pixels to mm", example = "")
    @PhonkMethodParam(params = {""})
    public float pixelsToMm(int px) {
        float onepx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, getContext().getResources()
                .getDisplayMetrics());

        return px * onepx;
    }

    public Object parseBytes(byte[] bytes, String type) {
        BluetoothBytesParser parser = new BluetoothBytesParser(bytes);

        switch (type) {
            case "uint8":
                return parser.getIntValue(BluetoothBytesParser.FORMAT_UINT8);
            case "string":
                return parser.getStringValue(0);
            default:
                return null;
        }
    }

    public interface AnimCB {
        void event(float data);
    }


    //TODO include the new support lib v22 interpolations
    @PhonkMethod(description = "Animate a variable from min to max in a specified time using 'bounce', 'linear', 'decelerate', 'anticipate', 'aovershoot', 'accelerate' type  ", example = "")
    @PhonkMethodParam(params = {"type", "min", "max", "time", "function(val)"})
    public ValueAnimator anim(String type, float min, float max, int time, final AnimCB callback) {
        TimeInterpolator interpolator = null;
        if (type.equals("bounce")) {
            interpolator = new BounceInterpolator();
        } else if (type.equals("linear")) {
            interpolator = new LinearInterpolator();
        } else if (type.equals("decelerate")) {
            interpolator = new DecelerateInterpolator();
        } else if (type.equals("anticipate")) {
            interpolator = new AnticipateInterpolator();
        } else if (type.equals("aovershoot")) {
            interpolator = new AnticipateOvershootInterpolator();
        } else {
            interpolator = new AccelerateDecelerateInterpolator();
        }

        ValueAnimator va = ValueAnimator.ofFloat(min, max);
        va.setDuration(time);
        va.setInterpolator(interpolator);

        va.addUpdateListener(animation -> {
            Float value = (Float) animation.getAnimatedValue();
            callback.event(value);
            MLog.d(TAG, "val " + value + " " + animation.getAnimatedValue());
        });

        return va;
    }


    @PhonkMethod(description = "Parse a color and return and int representing it", example = "")
    @PhonkMethodParam(params = {"colorString"})
    public int parseColor(String c) {
        return Color.parseColor(c);
    }


    @PhonkMethod(description = "Detect faces in a bitmap", example = "")
    @PhonkMethodParam(params = {"Bitmap", "numFaces"})
    public int detectFaces(Bitmap bmp, int num_faces) {
        FaceDetector face_detector = new FaceDetector(bmp.getWidth(), bmp.getHeight(), num_faces);
        FaceDetector.Face[] faces = new FaceDetector.Face[num_faces];
        int face_count = face_detector.findFaces(bmp, faces);

        return face_count;
    }

    public String bitmapToBase64String(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }

    @PhonkMethod(description = "Converts byte array to bmp", example = "")
    @PhonkMethodParam(params = {"encodedImage"})
    public Bitmap base64StringToBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);

        // MLog.d(TAG, "bytes--> " + decodedString);
        BitmapFactory.Options bitmap_options = new BitmapFactory.Options();
        bitmap_options.inPreferredConfig = Bitmap.Config.RGB_565;

        final Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length, bitmap_options);

        // MLog.d(TAG, "bitmap --> " + bitmap);

        return bitmap;
    }

    public float map(float val, float istart, float istop, float ostart, float ostop) {
        return CanvasUtils.map(val, istart, istop, ostart, ostop);
    }

    public SignalUtils signal(int n) {
        return new SignalUtils(getAppRunner(), n);
    }

    @Override
    public void __stop() {

    }
}