package com.vk_edu.feed_and_eat.common.graphics

//@Composable
//fun ExpandableInfo(width : Int, surface : @Composable () -> Unit) {
//    var expanded by remember { mutableStateOf(false) }
//    val xOffset by animateDpAsState(targetValue = if (expanded) (0).dp else (width - 60).dp,
//        animationSpec = tween(durationMillis = 500), label = ""
//    )
//
//    Surface(
//        color = colorResource(R.color.transparent),
//        modifier = Modifier
//            .width(width.dp)
//            .fillMaxHeight()
//            .background(colorResource(R.color.transparent))
//            .offset(x = xOffset)
//    ) {
//        Row {
//            Box(
//                modifier = Modifier
//                    .background(colorResource(R.color.transparent))
//            ){
//                InfoSquareButton { expanded = !expanded }
//            }
//            surface()
//        }
//
//    }
//}