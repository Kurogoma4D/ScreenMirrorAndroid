package tech.takenoko.screenmirror.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import tech.takenoko.screenmirror.utils.MLog
import java.net.URI


class QRReaderModel : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        MLog.info(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        IntentIntegrator(this).also {
            it.setPrompt("WebSocket URLを読み込みます")
            it.setOrientationLocked(false)
            it.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        MLog.info(TAG, "onActivityResult")
        val activityResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        kotlin.runCatching {
            if (activityResult != null) {
                MLog.info(TAG, activityResult.contents)
                uri(activityResult.contents)
            }
        }.fold(
            onSuccess = {},
            onFailure = { e -> MLog.info(TAG, e.toString()) }
        )
        finish()
    }

    companion object {
        val TAG: String = QRReaderModel::class.java.simpleName

        private var uri: (String) -> Unit = {}
        val run: (context: Context, (String) -> Unit) -> Unit = { context, callback ->
            uri = callback
            context.startActivity(Intent(context, QRReaderModel::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }
}
