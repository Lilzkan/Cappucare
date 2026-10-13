package com.example.cappucare.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cappucare.data.model.TipoMovimiento
import com.example.cappucare.ui.utils.VibradorHelper

@Composable
fun SelectorSquishTipoMovimiento(
    selectedTipo: TipoMovimiento,
    onTipoSelected: (TipoMovimiento) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val targetTarget = if (selectedTipo == TipoMovimiento.INGRESO) 0f else 1f
    val targetBias = if (selectedTipo == TipoMovimiento.INGRESO) -1f else 1f

    val animProgress by animateFloatAsState(
        targetValue = targetTarget,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "SquishSlideAnimation"
    )

    val animBias by animateFloatAsState(
        targetValue = targetBias,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "BiasAnimation"
    )

    val greenColor = Color(0xFF2E7D32)
    val amberTransitionColor = Color(0xFFD97706)
    val redColor = Color(0xFFBA1A1A)

    val currentBgColor = if (animProgress < 0.5f) {
        lerp(greenColor, amberTransitionColor, animProgress * 2f)
    } else {
        lerp(amberTransitionColor, redColor, (animProgress - 0.5f) * 2f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
                .align(BiasAlignment(animBias, 0f))
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(currentBgColor)
        )

        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        VibradorHelper.vibrarSuave(context)
                        onTipoSelected(TipoMovimiento.INGRESO)
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDownward,
                        contentDescription = null,
                        tint = if (animProgress < 0.5f) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Recepción (+)",
                        fontWeight = FontWeight.Bold,
                        color = if (animProgress < 0.5f) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        VibradorHelper.vibrarSuave(context)
                        onTipoSelected(TipoMovimiento.SALIDA)
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.ArrowUpward,
                        contentDescription = null,
                        tint = if (animProgress >= 0.5f) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Venta / Merma (-)",
                        fontWeight = FontWeight.Bold,
                        color = if (animProgress >= 0.5f) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}