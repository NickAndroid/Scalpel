/*
 * Copyright (c) 2016 Nick Guo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nick.scalpel.core.request;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.nick.scalpel.annotation.opt.Since;
import com.nick.scalpel.annotation.request.RequirePermission;
import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.AbsClassWirer;
import com.nick.scalpel.core.utils.Preconditions;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@TargetApi(value = Build.VERSION_CODES.M)
@Since("1.0.6")
class RequestPermissionWirer extends AbsClassWirer {

    public RequestPermissionWirer(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return RequirePermission.class;
    }

    @Override
    public void wire(Object obj) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        Activity activity = null;

        if (obj instanceof Activity) {
            activity = (Activity) obj;
        }

        if (obj instanceof Fragment) {
            Fragment fragment = (Fragment) obj;
            activity = fragment.getActivity();
        }

        if (obj instanceof android.support.v4.app.Fragment) {
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) obj;
            activity = fragment.getActivity();
        }

        Preconditions.checkNotNull(activity, "Only Activity or Fragment(app/v4) is supported for this annotation.");

        RequirePermission autoRequirePermission = activity.getClass().getAnnotation(RequirePermission.class);
        int code = autoRequirePermission.requestCode();

        String[] scope = autoRequirePermission.permissions();
        String[] required;
        if (scope.length == 0) {
            scope = getPkgInfo(activity).requestedPermissions;
            required = extractUnGranted(activity, scope);
        } else {
            required = extractUnGranted(activity, scope);
        }
        if (required == null || required.length == 0) {
            int[] codes = new int[0];
            if (required != null) {
                codes = new int[scope.length];
                for (int i = 0; i < codes.length; i++) {
                    codes[i] = PackageManager.PERMISSION_GRANTED;
                }
            }
            activity.onRequestPermissionsResult(code, scope, codes);
        } else {
            activity.requestPermissions(required, code);
        }
    }

    private String[] extractUnGranted(Activity activity, String[] declaredPerms) {
        if (declaredPerms == null || declaredPerms.length == 0) return null;
        PackageManager packageManager = activity.getPackageManager();
        List<String> requestList = new ArrayList<>(declaredPerms.length);
        for (String info : declaredPerms) {
            int code = packageManager.checkPermission(info, activity.getPackageName());
            if (code == PackageManager.PERMISSION_GRANTED) continue;
            logV("Will request perm:" + info + ", current code:" + code);
            requestList.add(info);
        }
        String[] out = new String[requestList.size()];
        for (int i = 0; i < requestList.size(); i++) {
            out[i] = requestList.get(i);
        }
        return out;
    }

    private PackageInfo getPkgInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
