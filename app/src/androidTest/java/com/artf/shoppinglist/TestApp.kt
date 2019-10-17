package com.artf.shoppinglist

import com.artf.shoppinglist.di.DaggerTestAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 */
class TestApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTestAppComponent.factory().create(applicationContext)
    }
}
