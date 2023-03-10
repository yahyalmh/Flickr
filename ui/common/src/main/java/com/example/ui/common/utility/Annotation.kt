package com.example.ui.common

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(showSystemUi = true, name = "phone", device = Devices.PHONE)
@Preview(showSystemUi = true, name = "foldable", device = Devices.FOLDABLE)
@Preview(showSystemUi = true,name = "custom", device = "spec:width=1280dp, height=800dp,dpi=480")
@Preview(showSystemUi = true,name = "tablet", device = Devices.TABLET)
@Preview(showSystemUi = true,name = "desktop", device = "id:desktop_medium")
annotation class ReferenceDevices