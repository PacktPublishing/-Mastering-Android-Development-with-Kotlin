package com.journaler.perferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesProvider : PreferencesProviderAbstract() {

    override fun obtain(configuration: PreferencesConfiguration, ctx: Context): SharedPreferences {
        return ctx.getSharedPreferences(configuration.key, configuration.mode)
    }

}