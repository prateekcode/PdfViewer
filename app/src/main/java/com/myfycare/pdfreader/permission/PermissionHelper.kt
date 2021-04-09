package com.myfycare.pdfreader.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionHelper {

    private val TAG = "Permission Helper"
    private var REQUEST_CODE: Int = 0

    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private var permissions: Array<String>? = null
    private var mPermissionCallback: PermissionCallback? = null
    private var showRational: Boolean = false

    //=========Constructors - START=========
    constructor(activity: Activity, fragment: Fragment, permissions: Array<String>, requestCode: Int) {
        this.activity = activity
        this.fragment = fragment
        this.permissions = permissions
        this.REQUEST_CODE = requestCode
        checkIfPermissionPresentInAndroidManifest()
    }

    constructor(activity: Activity, permissions: Array<String>, requestCode: Int = 100) {
        this.activity = activity
        this.permissions = permissions
        this.REQUEST_CODE = requestCode
        checkIfPermissionPresentInAndroidManifest()
    }

    constructor(fragment: Fragment, permissions: Array<String>, requestCode: Int = 100) {
        this.fragment = fragment
        this.permissions = permissions
        this.REQUEST_CODE = requestCode
        checkIfPermissionPresentInAndroidManifest()
    }


    private fun checkIfPermissionPresentInAndroidManifest() {
        for (permission in permissions!!) {
            if (!hasPermissionInManifest(permission)) {
                throw RuntimeException("Permission ($permission) Not Declared in manifest")
            }
        }
    }

    fun request(permissionCallback: PermissionCallback?) {
        this.mPermissionCallback = permissionCallback
        if (!hasPermission()) {
            showRational = shouldShowRational(permissions!!)
            if (activity != null)
                ActivityCompat.requestPermissions(activity!!, filterNotGrantedPermission(permissions!!), REQUEST_CODE)
            else
                fragment!!.requestPermissions(filterNotGrantedPermission(permissions!!), REQUEST_CODE)
        } else {
            Log.i(TAG, "PERMISSION: Permission Granted")
            mPermissionCallback?.onPermissionGranted()
            requestAllCallback()
        }
    }

    private var requestAllCallback: () -> Unit? = fun() {};
    fun requestAll(callback: () -> Unit) {
        this.requestAllCallback = callback;
        request(null)
    }

    private var requestIndividualCallback: (grantedPermission: Array<String>) -> Unit? = fun(grantedPermission: Array<String>) {};
    fun requestIndividual(callback: (grantedPermission: Array<String>) -> Unit) {
        this.requestIndividualCallback = callback;
        request(null)
    }

    private var deniedCallback: (isSystem: Boolean) -> Unit? = fun(isSystem: Boolean) {};
    fun denied(callback: (isSystem: Boolean) -> Unit) {
        this.deniedCallback = callback;
    }

    interface PermissionCallback {
        fun onPermissionGranted()

        fun onIndividualPermissionGranted(grantedPermission: Array<String>)

        fun onPermissionDenied()

        fun onPermissionDeniedBySystem()
    }

    private fun <T : Context> getContext(): T? {
        return if (activity != null) activity as T? else fragment!!.context as T
    }

    private fun filterNotGrantedPermission(permissions: Array<String>): Array<String> {
        val notGrantedPermission = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(getContext()!!, permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermission.add(permission)
            }
        }
        return notGrantedPermission.toTypedArray()
    }

    private fun shouldShowRational(permissions: Array<String>): Boolean {
        var currentShowRational = false
        for (permission in permissions) {

            if (activity != null) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)) {
                    currentShowRational = true
                    break
                }
            } else {
                if (fragment!!.shouldShowRequestPermissionRationale(permission)) {
                    currentShowRational = true
                    break
                }
            }
        }
        return currentShowRational
    }

    fun hasPermission(): Boolean {
        for (permission in permissions!!) {
            if (ContextCompat.checkSelfPermission(getContext()!!, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun hasPermissionInManifest(permission: String): Boolean {
        try {
            val context = if (activity != null) activity else fragment!!.activity
            val info = context?.getPackageManager()?.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS)
            if (info?.requestedPermissions != null) {
                for (p in info?.requestedPermissions) {
                    if (p == permission) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun openAppDetailsActivity() {
        if (getContext<Context>() == null) {
            return
        }
        val i = Intent()
        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:" + getContext<Context>()!!.packageName)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        getContext<Context>()!!.startActivity(i)
    }
}