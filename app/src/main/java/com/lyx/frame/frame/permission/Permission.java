package com.lyx.frame.frame.permission;

/**
 * Permission
 * <p>
 * Created by luoyingxing on 2017/5/27.
 */

public interface Permission {
    /**
     * Requested Permissions
     *
     * @return Requested Permissions
     */
    String getPermission();

    /**
     * The result of Requested Permissions
     *
     * @param succeed return true is permit permission, otherwise return false
     */
    void onApplyResult(boolean succeed);
}