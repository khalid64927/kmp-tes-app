package com.multiplatform.app.ui.components.core

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiplatform.app.ui.designsystem.components.Body2Text
import com.multiplatform.app.ui.designsystem.components.HyperlinkText
import com.multiplatform.app.ui.designsystem.components.LinkType


@Composable
fun NumberedListItem(
    modifier: Modifier = Modifier,
    number: String,
    pointString: String,
    onClick: (token: String, linkType: LinkType)->Unit = { _, _ -> }
) {
    modifier.apply {
        fillMaxWidth()
    }
    Row(modifier = modifier) {
        Body2Text(
            modifier = Modifier.padding(
                PaddingValues(start = 24.dp)
            ).weight(1f),
            string = number
        )

        HyperlinkText(
            onClick = onClick,
            modifier = Modifier.padding(
                PaddingValues(end = 24.dp)
            ).weight(9f),
            string = pointString
        )
    }

}