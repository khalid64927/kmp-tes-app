package com.multiplatform.app.android.ui.preview.designsystem

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.multiplatform.app.ui.designsystem.components.prepaidTopAppBar
import com.multiplatform.app.ui.theme.prepaidLightPrimary


@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun PrePaidTopAppBarPreview() {
    prepaidTopAppBar(
        titleString = "Login",
        navigationIconContentDescription = "Navigation icon",
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = prepaidLightPrimary,
        ),
    )
}