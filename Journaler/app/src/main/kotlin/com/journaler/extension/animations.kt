package com.journaler.extension

import android.app.Activity
import android.view.animation.Animation
import android.view.animation.AnimationUtils


fun Activity.getAnimation(animation: Int): Animation = AnimationUtils.loadAnimation(this, animation)