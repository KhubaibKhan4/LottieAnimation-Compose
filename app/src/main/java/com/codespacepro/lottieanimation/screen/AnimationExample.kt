package com.codespacepro.lottieanimation.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.codespacepro.lottieanimation.R
import com.codespacepro.lottieanimation.service.TimeService
import kotlin.math.max

@Composable
fun Animation() {

    var speed by remember {
        mutableStateOf(1f)
    }

    var isPlaying by remember {
        mutableStateOf(true)
    }

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.mobile)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        restartOnPlay = false,
        speed = speed,
        iterations = LottieConstants.IterateForever
    )

    var counter by remember {
        mutableStateOf(0)
    }
    val context = LocalContext.current

    val intentService = remember {
        Intent(context, TimeService::class.java)
    }
    val broadcast = remember {
        object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                counter = p1?.getIntExtra("counter", 0) ?: 0
            }

        }
    }
    LaunchedEffect(key1 = true) {
        val intentFilters = IntentFilter("counter_action")
        context.registerReceiver(broadcast, intentFilters)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color =
                if (counter >= 5)
                    Color.Red
                else if (counter >= 15) Color.Blue
                else if (counter >= 30) Color.Red
                else Color.White
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        com.airbnb.lottie.compose.LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(240.dp)
        )
        Text(
            text = "$counter",
            textAlign = TextAlign.Center,
            fontSize = 20.sp, color = if (counter >= 5)
                Color.White
            else if (counter >= 15) Color.White
            else if (counter >= 30) Color.White
            else Color.Black
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { context.startService(intentService) }) {
                Text(text = "Start")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = { context.stopService(intentService) }) {
                Text(text = "Stop")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                speed = max(speed - 0.25f, 0f)
            }) {
                Text(text = "-")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "$speed", fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = { speed += 0.25f }) {
                Text(text = "+")
            }
        }
    }

}