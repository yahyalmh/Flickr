package com.example.ui.common.connectivity.di

import com.example.ui.common.connectivity.ConnectivityMonitor
import com.example.ui.common.connectivity.ConnectivityMonitorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ConnectivityMonitorModule {
    @Binds
    @Singleton
    fun bindConnectivityMonitor(networkMonitor: ConnectivityMonitorImpl): ConnectivityMonitor
}