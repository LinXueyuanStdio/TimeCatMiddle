package com.timecat.middle.block.security

import com.timecat.component.setting.DEF

const val KEY_SECURITY_CODE = "KEY_SECURITY_CODE"
const val KEY_FINGERPRINT_ENABLED = "KEY_FINGERPRINT_ENABLED"
const val KEY_APP_LOCK_ENABLED = "app_lock_enabled"
const val KEY_ASK_PIN_ALWAYS = "ask_pin_always"

var sSecurityCode: String
    get() = DEF.setting().getString(KEY_SECURITY_CODE, "")!!
    set(value) {
        DEF.setting().putString(KEY_SECURITY_CODE, value)
    }
var sSecurityBiometricEnabled: Boolean
    get() = DEF.setting().getBoolean(KEY_FINGERPRINT_ENABLED, true)
    set(value) {
        DEF.setting().putBoolean(KEY_FINGERPRINT_ENABLED, value)
    }
var sSecurityAppLockEnabled: Boolean
    get() = DEF.setting().getBoolean(KEY_APP_LOCK_ENABLED, false)
    set(value) {
        DEF.setting().getBoolean(KEY_APP_LOCK_ENABLED, value)
    }
var sSecurityAskPinAlways: Boolean
    get() = DEF.setting().getBoolean(KEY_ASK_PIN_ALWAYS, true)
    set(value) {
        DEF.setting().getBoolean(KEY_ASK_PIN_ALWAYS, value)
    }

