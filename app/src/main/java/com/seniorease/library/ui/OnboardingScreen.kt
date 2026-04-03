package com.seniorease.library.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seniorease.library.R
import kotlinx.coroutines.launch

private data class OnboardingSlide(
    val emoji: String,
    val titleRes: Int,
    val descRes: Int,
)

private val SLIDES = listOf(
    OnboardingSlide("📚", R.string.onboarding_1_title, R.string.onboarding_1_desc),
    OnboardingSlide("📷", R.string.onboarding_2_title, R.string.onboarding_2_desc),
    OnboardingSlide("🗂️", R.string.onboarding_3_title, R.string.onboarding_3_desc),
    OnboardingSlide("✅", R.string.onboarding_4_title, R.string.onboarding_4_desc),
)

@Composable
fun OnboardingScreen(onDone: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { SLIDES.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == SLIDES.lastIndex

    val brandColor = MaterialTheme.colorScheme.primary
    val bg = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {
        // Skip knop rechtsboven
        TextButton(
            onClick = onDone,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.onboarding_skip),
                color = brandColor,
                fontSize = 18.sp,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Pager met slides
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) { page ->
                val slide = SLIDES[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    // Emoji in gekleurde cirkel
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .background(brandColor.copy(alpha = 0.10f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = slide.emoji,
                            fontSize = 72.sp,
                        )
                    }

                    Spacer(Modifier.height(48.dp))

                    Text(
                        text = stringResource(slide.titleRes),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        lineHeight = 40.sp,
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = stringResource(slide.descRes),
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f),
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp,
                    )
                }
            }

            // Dots voortgangsindicator
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp),
            ) {
                repeat(SLIDES.size) { index ->
                    val selected = index == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(if (selected) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) brandColor else brandColor.copy(alpha = 0.28f)
                            )
                    )
                }
            }

            // Volgende / Aan de slag knop
            Button(
                onClick = {
                    if (isLastPage) {
                        onDone()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = brandColor),
            ) {
                Text(
                    text = if (isLastPage) stringResource(R.string.onboarding_get_started)
                           else stringResource(R.string.onboarding_next),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}
