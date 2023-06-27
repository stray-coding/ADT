package com.coding.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.coding.compose.base.MyIcons
import com.coding.compose.ui.AABUI
import com.coding.compose.ui.ADBUI
import com.coding.compose.ui.ApktoolUI
import com.coding.compose.ui.DexUI
import com.coding.compose.ui.SignUI
import com.coding.compose.ui.SignManagerUI

enum class UI(val description: String) {
    TYPE_APKTOOL("apktool"),
    TYPE_DEX("dex"),
    TYPE_SIGN_MANAGER("sign manager"),
    TYPE_SIGN("sign"),
    TYPE_AAB("aab"),
    TYPE_ADB("adb");
}

lateinit var mWindow: ComposeWindow
private val LEFT_WIDTH = 150.dp
private val RIGHT_WIDTH = 400.dp
val ITEM_HEIGHT = 60.dp
val DIVIDER_WIDTH = 1.dp
val DIVIDER_COLOR = Color.Gray
fun main() = application {
    val curType = remember { mutableStateOf(UI.TYPE_APKTOOL) }
    val alwaysOnTop = remember { mutableStateOf(false) }
    Window(
            onCloseRequest = ::exitApplication,
            title = "ADT",
            icon = MyIcons.getAppIconPainter(),
            state = rememberWindowState(
                    width = LEFT_WIDTH + RIGHT_WIDTH,
                    height = ((ITEM_HEIGHT + DIVIDER_WIDTH) * UI.values().size + 40.dp)
            ),
            alwaysOnTop = alwaysOnTop.value,
            resizable = false
    ) {
        mWindow = window
        MaterialTheme {
            Row {
                Divider(modifier = Modifier.fillMaxHeight().width(DIVIDER_WIDTH), color = DIVIDER_COLOR)
                //左边界面
                Column(
                        modifier = Modifier.width(LEFT_WIDTH).fillMaxHeight(),
                ) {
                    for (type in UI.values()) {
                        Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = DIVIDER_COLOR)
                        Box(modifier = Modifier.fillMaxWidth().height(ITEM_HEIGHT)
                                .background(if (type == curType.value) Color.Gray else Color.White)
                                .clickable {
                                    curType.value = type
                                },
                                contentAlignment = Alignment.Center,
                                content = {
                                    Text(text = type.description)
                                }
                        )
                    }
                    Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = DIVIDER_COLOR)
                }
                Divider(modifier = Modifier.fillMaxHeight().width(DIVIDER_WIDTH), color = DIVIDER_COLOR)
                //右边界面
                Box(Modifier.width(RIGHT_WIDTH).fillMaxHeight()) {
                    Divider(modifier = Modifier.fillMaxWidth().height(1.dp).align(Alignment.TopStart), color = DIVIDER_COLOR)
                    when (curType.value) {
                        UI.TYPE_APKTOOL -> ApktoolUI()
                        UI.TYPE_DEX -> DexUI()
                        UI.TYPE_SIGN_MANAGER -> SignManagerUI()
                        UI.TYPE_SIGN -> SignUI()
                        UI.TYPE_AAB -> AABUI()
                        UI.TYPE_ADB -> ADBUI()
                    }
                    Icon(if (alwaysOnTop.value) MyIcons.getOnTopSelectedPainter() else MyIcons.getOnTopNormalPainter(),
                            "窗口置顶",
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(5.dp).size(15.dp, 15.dp).align(Alignment.TopEnd).background(Color.White)
                                    .clickable {
                                        alwaysOnTop.value = !alwaysOnTop.value
                                    }
                    )
                    Divider(modifier = Modifier.fillMaxWidth().height(1.dp).align(Alignment.BottomStart), color = DIVIDER_COLOR)
                }
            }
        }
    }
}


