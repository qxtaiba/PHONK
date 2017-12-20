/*
 * Part of Phonk http://www.phonk.io
 * A prototyping platform for Android devices
 *
 * Copyright (C) 2013 - 2017 Victor Diaz Barrales @victordiaz (Protocoder)
 * Copyright (C) 2017 - Victor Diaz Barrales @victordiaz (Phonk)
 *
 * Phonk is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Phonk is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Phonk. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.phonk.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.phonk.MainActivity;
import io.phonk.gui.settings.NewUserPreferences;
import io.phonk.runner.base.utils.MLog;

/**
 * Created by biquillo on 13/09/16.
 */
public class ProtocoderBootReceiver extends BroadcastReceiver {

    private static final String TAG = ProtocoderBootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if ((boolean) NewUserPreferences.getInstance().get("launch_on_device_boot")) {
            MLog.d(TAG, "launching Protocoder on boot");
            Intent in = new Intent(context, MainActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
    }
}