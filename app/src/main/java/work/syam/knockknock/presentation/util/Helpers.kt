package work.syam.knockknock.presentation.util

import android.app.Activity
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Insets
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.util.DisplayMetrics
import android.util.Size
import android.util.TypedValue
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

// UI Helpers and Extension functions
fun Context.shortToast(msg: CharSequence) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Lifecycle.safe() =
    this.currentState.isAtLeast(Lifecycle.State.INITIALIZED) && this.currentState != Lifecycle.State.DESTROYED

operator fun String.get(range: IntRange) = substring(range.first, range.last + 1)

fun Context.drawable(@DrawableRes resId: Int) = ResourcesCompat.getDrawable(resources, resId, null)

fun Context.font(@FontRes resId: Int) = ResourcesCompat.getFont(this, resId)

fun Context.dimen(@DimenRes resId: Int) = resources.getDimension(resId)

fun Context.anim(@AnimRes resId: Int) = AnimationUtils.loadAnimation(this, resId)

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Float.sp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp get() = toFloat().dp
val Int.sp get() = toFloat().sp

val EditText.value get() = text?.toString() ?: ""

fun Activity.startActivity(
    cls: Class<*>,
    finishCallingActivity: Boolean = true,
    block: (Intent.() -> Unit)? = null
) {
    val intent = Intent(this, cls)
    block?.invoke(intent)
    startActivity(intent)
    if (finishCallingActivity) finish()
}

fun Context.isNetworkAvailable(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
    return if (capabilities != null) {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } else false
}

fun Fragment.isNetworkAvailable() = requireContext().isNetworkAvailable()

fun Context.isPermissionGranted(permission: String) = run {
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun String.removeAllWhitespaces(): String {
    return this.replace("\\s+".toRegex(), "")
}

fun String.removeDuplicateWhitespaces(): String {
    return this.replace("\\s+".toRegex(), " ")
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

val Context.screenSize: Size
    get() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val size = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = windowManager.currentWindowMetrics
            val windowInsets = metrics.windowInsets
            val insets: Insets = windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars()
                        or WindowInsets.Type.displayCutout()
            )

            val insetsWidth: Int = insets.right + insets.left
            val insetsHeight: Int = insets.top + insets.bottom
            val bounds: Rect = metrics.bounds
            Size(
                bounds.width() - insetsWidth,
                bounds.height() - insetsHeight
            )
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay?.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            Size(width, height)
        }
        return size
    }

val Context.windowManager
    get() = ContextCompat.getSystemService(this, WindowManager::class.java)

val Context.connectivityManager
    get() = ContextCompat.getSystemService(this, ConnectivityManager::class.java)

val Context.notificationManager
    get() = ContextCompat.getSystemService(this, NotificationManager::class.java)

val Context.downloadManager
    get() = ContextCompat.getSystemService(this, DownloadManager::class.java)

fun String.copyToClipboard(context: Context) {
    val clipboardManager = ContextCompat.getSystemService(context, ClipboardManager::class.java)
    val clip = ClipData.newPlainText("clipboard", this)
    clipboardManager?.setPrimaryClip(clip)
}

@OptIn(ExperimentalContracts::class)
fun Boolean?.isTrue(): Boolean {
    contract {
        returns(true) implies (this@isTrue != null)
    }
    return this == true
}

@OptIn(ExperimentalContracts::class)
fun Boolean?.isFalse(): Boolean {
    contract {
        returns(true) implies (this@isFalse != null)
    }
    return this == false
}

val Boolean?.orTrue: Boolean
    get() = this ?: true

val Boolean?.orFalse: Boolean
    get() = this ?: false

fun <T> Observable<T>.retryWhenError(retryCount: Int, delayInSeconds: Long): Observable<T> {
    return retryWhen { errors ->
        errors.zipWith(
            Observable.range(1, retryCount)
        ) { throwable: Throwable, count: Int -> Pair(throwable, count) }
            .flatMap { count: Pair<Throwable, Int> ->
                if (count.second < retryCount) {
                    Observable.timer(delayInSeconds, TimeUnit.SECONDS)
                } else {
                    Observable.error(count.first)
                }
            }
    }
}
