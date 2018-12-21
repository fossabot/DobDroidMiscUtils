package ro.dobrescuandrei.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import java.io.File
import java.util.*

abstract class FileManager
{
    companion object
    {
        fun createOutputFile(context : Context, filePath : String) : Uri?
        {
            try
            {
                val file=File(filePath)
                if (!file.exists())
                    file.createNewFile()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                }

                return Uri.fromFile(file)
            }
            catch (ex : Exception)
            {
                return null
            }
        }

        fun appendToDir(dir : String, fileName : String) : String =
            "$dir${if (!dir.endsWith("/")) "/" else ""}$fileName"

        fun removeInvalidCharactersFromFileName(fileName: String): String
        {
            return fileName.replace("[\\\\/:*?\"<>|]".toRegex(), "")
        }
    }

    abstract fun folderBasePath() : String
    abstract fun folderName() : String

    private val folderPath : String by lazy {
        val dirPath=appendToDir(folderBasePath(), folderName())
        val dir=File(dirPath)
        if (!dir.exists())
            dir.mkdir()
        dirPath
    }

    fun getFilePath(fileName : String) =
        appendToDir(folderPath, fileName)

    fun getImageOutputPath() : String = getFileOutputPath(extension = "jpg")

    fun getFileOutputPath(extension : String) : String
    {
        var fileName= UUID.randomUUID().toString()
        var fileExists=true
        var filePath = ""

        do
        {
            filePath=getFilePath("$fileName.$extension")
            fileExists= File(filePath).exists()
            if (fileExists)
                fileName= UUID.randomUUID().toString()
        }
        while (fileExists)

        return filePath
    }

    fun deleteOutputFiles()
    {
        val files= File(folderPath).listFiles()
        for (file in files)
            file.delete()
    }
}