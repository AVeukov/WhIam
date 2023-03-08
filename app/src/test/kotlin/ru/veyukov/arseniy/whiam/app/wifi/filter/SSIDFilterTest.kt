
package ru.veyukov.arseniy.whiam.wifi.filter

import android.app.AlertDialog
import android.os.Build
import android.text.Editable
import android.view.View
import android.widget.EditText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.RobolectricUtil
import ru.veyukov.arseniy.whiam.wifi.filter.SSIDFilter.OnChange
import ru.veyukov.arseniy.whiam.wifi.filter.adapter.SSIDAdapter
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.wifi.filter.SSIDFilter

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class SSIDFilterTest {
    private val alertDialog: AlertDialog = mock()
    private val editText: EditText = mock()
    private val view: View = mock()
    private val ssidAdapter: SSIDAdapter = mock()
    private val editable: Editable = mock()

    @Before
    fun setUp() {
        RobolectricUtil.INSTANCE.activity
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(alertDialog)
        verifyNoMoreInteractions(editText)
        verifyNoMoreInteractions(view)
        verifyNoMoreInteractions(ssidAdapter)
        verifyNoMoreInteractions(editable)
    }

    @Test
    fun testSSIDFilterWithValues() {
        // setup
        val values: Set<String> = setOf("", " ", "ABC", " JDS ")
        whenever(ssidAdapter.selections).thenReturn(values)
        whenever(alertDialog.findViewById<EditText>(R.id.filterSSIDtext)).thenReturn(editText)
        whenever(alertDialog.findViewById<View>(R.id.filterSSID)).thenReturn(view)
        val expected = "ABC JDS"
        // execute
        SSIDFilter(ssidAdapter, alertDialog)
        // verify
        verify(ssidAdapter).selections
        verify(editText).setText(expected)
        verify(alertDialog).findViewById<EditText>(R.id.filterSSIDtext)
        verify(alertDialog).findViewById<View>(R.id.filterSSID)
        verify(view).visibility = View.VISIBLE
        verify(editText).addTextChangedListener(any())
    }

    @Test
    fun testOnChangeAfterTextChangedWithValues() {
        // setup
        val value = " ABS ADF "
        val onChange = OnChange(ssidAdapter)
        whenever(editable.toString()).thenReturn(value)
        val expected: Set<String> = setOf("ABS", "ADF")
        // execute
        onChange.afterTextChanged(editable)
        // verify
        verify(ssidAdapter).selections = expected
    }
}