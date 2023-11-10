package com.example.lin_li_tranimg

import android.app.Application
import com.cmoney.backend2.di.backendServicesModule
import com.cmoney.data_logdatarecorder.recorder.LogDataRecorder
import com.example.lin_li_tranimg.presentation.di.appModule
import com.google.androidbrowserhelper.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LogDataRecorder.initialization(this) {
            isEnable = true
            appId = 99
            platform = com.cmoney.domain_logdatarecorder.data.information.Platform.Android
        }
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@App)
            loadKoinModules(
                listOf(
                    backendServicesModule,
                    appModule
                )
            )
        }
    }
}