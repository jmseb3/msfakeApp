package com.wonddak.fakems

import android.content.Intent
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wonddak.fakems.ui.theme.FakemsTheme

class MainActivity : ComponentActivity() {

    private val lockViewModel by viewModels<LockViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseLock(intent)
        enableEdgeToEdge()
        setContent {
            FakemsTheme {
                val lockState by lockViewModel.lockState.collectAsState()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("현재 기능 상태 lock : [$lockState]")
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        parseLock(intent)
    }

    private fun parseLock(intent: Intent?) {
        intent?.let {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
                    ?.also { rawMessages ->
                        val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                        // Process the messages array.
                        messages.forEach {
                            it.records.forEach { record ->
                                Log.d("JWH", "-- $record")
                                if (record.type.contentEquals(NdefRecord.RTD_URI)) {
                                    val uri: Uri = record.toUri()
                                    Log.d("JWH", uri.toString())
                                    if (uri.scheme == "msfake" && uri.host == "action") {
                                        val command = uri.getQueryParameter("command")
                                        Log.d("JWH", command.toString())
                                        if (command == "lock") {
                                            lockViewModel.lock()
                                        } else if (command == "unlock") {
                                            lockViewModel.unlock()
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}