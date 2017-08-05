package com.aotuman.notepad.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by kieth on 3/15.
 */
public final class FileTool {
    private static final String TAG = FileTool.class.getSimpleName();
    public final static String FILE_EXTENSION_SEPARATOR = ".";
    public static final int BUFFER_SIZE = 8192;

    public static String streamToString(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static byte[] stream2Byte(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] b = new byte[1024];
        while ((len = is.read(b, 0, b.length)) != -1) {
            baos.write(b, 0, len);
        }
        byte[] buffer = baos.toByteArray();
        return buffer;
    }


    /**
     * read file
     *
     * @param filePath
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, charsetName);
            reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
            if (null != isr) {
                try {
                    isr.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return null;
    }

    /**
     * write file
     *
     * @param filePath
     * @param content
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bufWriter = null;
        try {
            makeDirs(filePath);
            fos = new FileOutputStream(filePath, append);
            osw = new OutputStreamWriter(fos, "UTF-8");
            bufWriter = new BufferedWriter(osw);
            bufWriter.write(content);
            return true;
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (null != bufWriter) {
                try {
                    bufWriter.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
            if (null != osw) {
                try {
                    osw.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return false;
    }

    /**
     * write file
     *
     * @param filePath
     * @param contentList
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (null == contentList || contentList.isEmpty()) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (null != fileWriter) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return false;
    }

    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * write file, the string list will be written to the begin of the file
     *
     * @param filePath
     * @param contentList
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath
     * @param stream
     * @return
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * write file
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        if (null == file) {
            return false;
        }
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (null != o) {
                try {
                    o.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
            if (null != stream) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return false;
    }

    public static boolean writeBitmap(String path, Bitmap img, int quality) {
        return writeBitmap(path, img, quality, true);
    }

    public static boolean writeBitmap(String path, Bitmap img, int quality, boolean deleteExist) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return writeBitmap(new File(path), img, quality, deleteExist);
    }

    public static boolean writeBitmap(File file, Bitmap img, int quality, boolean deleteExist) {
        FileOutputStream fos = null;
        if (null == file || null == img || img.isRecycled()) {
            return false;
        }
        boolean ret = false;
        try {
            if (!deleteExist && file.exists()) {
                return false;
            }
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                Log.i(TAG, "writeBitmap make dirs failed");
            }
            if (file.exists() && !file.delete()) {
                Log.i(TAG, "writeBitmap delete old failed");
            }

            fos = new FileOutputStream(file);
            ret = img.compress(Bitmap.CompressFormat.PNG, quality, fos);
            fos.flush();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            ret = false;
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return ret;
    }

    /**
     * move file
     */
    public static void moveFile(String sourceFilePath, String destFilePath) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
            throw new RuntimeException("Both sourceFilePath and destFilePath cannot be null.");
        }
        moveFile(new File(sourceFilePath), new File(destFilePath));
    }

    /**
     * move file
     *
     * @param srcFile
     * @param destFile
     */
    public static void moveFile(File srcFile, File destFile) {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }

    /**
     * copy file
     *
     * @param sourceFile
     * @param destFile
     */
    public static boolean copyFile(File sourceFile, File destFile) {
        return copyFile(sourceFile.getPath(),destFile.getPath());
    }
    /**
     * copy file
     *
     * @param sourceFilePath
     * @param destFilePath
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
            return writeFile(destFilePath, inputStream);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
        return false;
    }


    /**
     * get file name from path, not include suffix
     * <p/>
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * get file name from path, include suffix
     * <p/>
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * get folder name from path
     * <p/>
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(filePath.trim())) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }


    public static boolean deleteFile(String path) {
        try {
            if (TextUtils.isEmpty(path) || TextUtils.isEmpty(path.trim())) {
                return true;
            }
            File file = new File(path);
            if (!file.exists()) {
                return true;
            }
            if (file.isFile()) {
                return file.delete();
            }
            if (!file.isDirectory()) {
                return false;
            }
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                } else if (f.isDirectory()) {
                    deleteFile(f.getAbsolutePath());
                }
            }
            return file.delete();
        }catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return false;
    }

    /**
     * 删除文件夹中的所有文件包括子文件夹，但不包括文件夹本身
     *
     * @param folderPath
     * @return
     */
    public static boolean deleteFileInFolder(String folderPath) {
        boolean result = true;
        File file = new File(folderPath);
        if (!file.exists() || !file.isDirectory()) {
            return true;
        }

        File filePaths[] = file.listFiles();
        if (null != filePaths && filePaths.length != 0) {
            for (File path : filePaths) {
                result = result & deleteFile(path.getAbsolutePath());
            }
        }
        return result;
    }

    /**
     * 新建文件
     *
     * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
     * @param fileContent     String 文件内容
     * @return boolean
     */
    public static void newFile(String filePathAndName, String fileContent) {

        try {
            File myFilePath = new File(filePathAndName);
            if (!myFilePath.exists()) {
                if (!myFilePath.createNewFile()) {
                    Log.w(TAG, "File create failed");
                }
            }

            if (fileContent != null && !fileContent.equals("")) {
                FileWriter resultFile = new FileWriter(myFilePath);
                PrintWriter myFile = new PrintWriter(resultFile);
                myFile.println(fileContent);
                myFile.flush();
                myFile.close();
                resultFile.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "newFile Exception ", e);
        }
    }

    public static File getCacheDir(Context context) {
        File file = context.getExternalCacheDir();
        if (null == file) {
            file = context.getCacheDir();
        }
        return file;
    }

    public static File getFilesDir(Context context, String folder) {
        File file = context.getExternalFilesDir(folder);
        if (null == file) {
            file = context.getFilesDir();
        }
        return file;
    }

    public static String getImageSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath()+File.separator+"notepad";
    }

    /**
     * 外置存储有效时使用外置存储缓存,
     * 外置存储无效时使用应用的的缓存空间
     */
    public static File getDiskCacheDir(Context context, String dir) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                cachePath = file.getAbsolutePath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + dir);
    }

    /**
     * InputStream 转File
     * */
    public static void inputStreamToFile(InputStream is, File file) {
        if (file==null){
            Log.i(TAG,"inputStreamToFile:file not be null");
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 10];
            int i;
            while ((i = is.read(buffer)) != -1) {
                fos.write(buffer, 0, i);
            }
        } catch (IOException e) {
            Log.e(TAG,"InputStream 写入文件出错:"+e.toString());
        } finally {
            try {
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}

