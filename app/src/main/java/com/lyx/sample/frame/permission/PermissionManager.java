package com.lyx.sample.frame.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * PermissionManager Requested Permissions.
 * Use likes following, can apply for multiple access continuously.
 * <p>
 * <p>
 * permissionManager.addPermission(new Permission() {
 *
 * @Override public String getPermission() {
 * return Manifest.permission.READ_EXTERNAL_STORAGE;
 * }
 * @Override public void onApplyResult(boolean succeed) {
 * }
 * <p>
 * permissionManager.apply(this);
 * <p>
 * @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 * super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 * permissionManager.onPermissionsResult(requestCode, permissions, grantResults);
 * }
 * <p>
 * Created by luoyingxing on 2017/5/27.
 */

public class PermissionManager {
    private static final int CODE = 5001;
    private List<Permission> mPermissionList;
    private List<Permission> mNeedPermissionList;
    private Activity mActivity;
    private Fragment mFragment;

    /**
     * apply for permission in the Activity
     *
     * @param activity Activity
     */
    public PermissionManager(Activity activity) {
        this.mActivity = activity;
        mPermissionList = new ArrayList<>();
        mNeedPermissionList = new ArrayList<>();
    }

    /**
     * apply for permission in the Fragment
     *
     * @param fragment Fragment
     */
    public PermissionManager(Fragment fragment) {
        this.mFragment = fragment;
        mPermissionList = new ArrayList<>();
        mNeedPermissionList = new ArrayList<>();
    }

    /**
     * add permission
     *
     * @param permission the permission Need to apply
     * @return PermissionManager
     */
    public PermissionManager addPermission(Permission permission) {
        mPermissionList.add(permission);
        return this;
    }

    /**
     * Call this method request permissions, introduced to the Context object
     *
     * @param context context
     */
    public void apply(@NonNull Context context) {
        if (null != mNeedPermissionList) {
            mNeedPermissionList.clear();
        }

        for (int i = 0; i < mPermissionList.size(); i++) {
            Permission permission = mPermissionList.get(i);
            if (ContextCompat.checkSelfPermission(context, permission.getPermission()) != PackageManager.PERMISSION_GRANTED) {
                mNeedPermissionList.add(permission);
            } else {
                permission.onApplyResult(true);
            }
        }

        if (mNeedPermissionList.size() == 0) {
            if (null != mPermissionList) {
                mPermissionList.clear();
            }
            mNeedPermissionList.clear();
            return;
        }

        String[] permission = new String[mNeedPermissionList.size()];
        for (int j = 0; j < mNeedPermissionList.size(); j++) {
            permission[j] = mNeedPermissionList.get(j).getPermission();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity != null) {
                mActivity.requestPermissions(permission, CODE);
            } else {
                mFragment.requestPermissions(permission, CODE);
            }
        }

        if (null != mPermissionList) {
            mPermissionList.clear();
        }

    }

    /**
     * This method must be recall in the Activity or fragment , otherwise no callback event
     *
     * @param requestCode  requestCode
     * @param permissions  permissions
     * @param grantResults PackageManager.PERMISSION_GRANTED is request succeed
     */
    public void onPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE) {
            for (int i = 0; i < mNeedPermissionList.size(); i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mNeedPermissionList.get(i).onApplyResult(true);
                } else {
                    mNeedPermissionList.get(i).onApplyResult(false);
                }
            }
        }
    }

    /**
     * Release resources
     */
    public void release() {
        if (mPermissionList != null) {
            mPermissionList = null;
        }
        if (mNeedPermissionList != null) {
            mNeedPermissionList = null;
        }
        if (mActivity != null) {
            mActivity = null;
        }
        if (mFragment != null) {
            mFragment = null;
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}