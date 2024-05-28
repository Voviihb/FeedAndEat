package com.vk_edu.feed_and_eat.common.graphics

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.vk_edu.feed_and_eat.R
import kotlinx.coroutines.launch

@Composable
fun Switch(
    direction: LayoutDirection,
    content: @Composable () -> Unit,
){
    CompositionLocalProvider(LocalLayoutDirection provides direction ) {
        content()
    }
}

@Composable
fun CustomSideDrawer(
//  if you want info to move from the right, pass Rtl here
    direction: LayoutDirection,
//  create a drawer state and pass it here
    drawerState: DrawerState,
//  this is a Composable function responsible for content before the phone's side
    hiddenContent : @Composable () -> Unit,
//  pass a Composable of type (onClick: () -> Unit) : Unit here
//  this is responsible for a display of the button which moves from the sid
    actionButton: @Composable (() -> Unit) -> Unit,
//  this function works as onClick for actionButton
//  it is important to separate the button and it's operator since...
//  ...intercepting clicks is significant for the whole component
    onClick: () -> Unit,
//  if you want to color hiddenContent, pass custom modifier here
    @SuppressLint("ModifierParameter") hiddenContentModifier : Modifier,
//  main content which will not move
    content : @Composable () -> Unit,
){

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp
    var previous by remember { mutableStateOf((-360.0).dp) }
    var current by remember { mutableStateOf(0.dp) }

    var actualPadding by remember { mutableIntStateOf(0) }
    val buttonSize = 60
    val maxWidth = 360.dp
    val density = LocalDensity.current

    val newOnClick = {
        actualPadding = if ((maxWidth + buttonSize.dp - actualPadding.dp) > screenWidth.dp){
            ((maxWidth.value + buttonSize) - screenWidth).toInt()
        } else {
            0
        }
        onClick()
    }

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { drawerState.currentOffset }
            .collect {
                val job = launch {
                    current = with(density) { it.toDp() }
                    if (previous < current) {
                        while ((maxWidth + buttonSize.dp - actualPadding.dp) > screenWidth.dp) {
                            actualPadding += 1
                        }
                    }
                    if (previous >= current) {
                        while (actualPadding > 0) {
                            actualPadding -= 1
                        }
                    }
                    previous = current
                }
                job.join()
            }
        }

    Switch(direction){
        ModalNavigationDrawer(
            drawerState = drawerState,
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.white)),
            drawerContent = {
                FitDrawerConditions(
                    maxWidth = maxWidth,
                    actualPadding = actualPadding,
                    onClick = newOnClick,
                    hiddenContent = hiddenContent,
                    actionButton = actionButton,
                    modifier = hiddenContentModifier
                )
            },
        ) {
            Switch(LayoutDirection.Ltr) {
                content()
            }
        }
    }
}

@Composable
fun FitDrawerConditions(
    maxWidth: Dp,
    actualPadding: Int,
    onClick: () -> Unit,
    hiddenContent : @Composable () -> Unit,
    actionButton: @Composable (() -> Unit) -> Unit,
    modifier : Modifier
){
    Switch(LayoutDirection.Ltr) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(Alignment.End, unbounded = true)
                .width(420.dp)
                .padding(start = actualPadding.dp)
        ){
            Box(
                contentAlignment = Alignment.TopStart,
            ){
                actionButton{
                    onClick()
                }
            }
            Box(
                modifier = modifier
                    .width((maxWidth.value - actualPadding).dp)
            ){
                hiddenContent()
            }
        }
    }
}