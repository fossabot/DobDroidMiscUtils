package ro.dobrescuandrei.dobdroidmiscutils

import android.os.Environment
import ro.dobrescuandrei.utils.FileManager

object AppFileManager : FileManager()
{
    override fun folderBasePath() : String = Environment.getExternalStorageDirectory().absolutePath
    override fun folderName() : String = "Dob"

    fun getSampleFilePath() : String = getFilePath("sample.pdf")
}