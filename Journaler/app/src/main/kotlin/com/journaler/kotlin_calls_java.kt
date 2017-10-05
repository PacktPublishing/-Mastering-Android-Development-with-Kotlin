package com.journaler

import android.content.Context
import android.content.Intent
import com.journaler.activity.MigrationActivity
import com.journaler.model.Dummy2

fun kotlinCallsJava(ctx: Context) {

    /**
     * We access Java class and instantiate it.
     */
    val dummy = Dummy2(10)

    /**
     * We use Android related Java code with no problems as well.
     */
    val intent = Intent(ctx, MigrationActivity::class.java)
    intent.putExtra("dummy", dummy)
    ctx.startActivity(intent)

}