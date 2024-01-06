package com.multiplatform.app.ui.components.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import com.multiplatform.app.ui.theme.PrepaidLightColors
import com.multiplatform.app.ui.theme.prepaidLightPrimary
import dev.icerock.moko.resources.ImageResource
import moe.tlaster.precompose.navigation.Navigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedTopAppBar(
    onBackPressed: ()-> Unit = {},
    title: String = "Login",
    navigationIcon: ImageResource? = null,
    content: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = prepaidLightPrimary,
                ),
                title = {
                    Text(
                        title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,

            )
        },
    ) { innerPadding ->
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: Navigator? = null,
    title: String = "App",
    navigationIcon: ImageResource? = null,
    content: @Composable () -> Unit
) {
    Scaffold(
        containerColor = PrepaidLightColors.primary,
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    if (navigationIcon != null && navController != null) {
                        IconButton(
                            onClick = {
//navController.navigateUp()
 }
                        ) {
                            // TODO:
                            //Icon(imageVector = ImageVector, contentDescription = null)
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        },
        content = {
            content()
        }
    )
}
