package com.mole.androidcodestudy.library.activity

import android.content.Context
import android.os.Bundle
import com.mole.androidcodestudy.activity.BaseActivity
import com.mole.androidcodestudy.databinding.ActivitySqlCipherBinding
import com.mole.androidcodestudy.extension.viewBinding
import net.zetetic.database.sqlcipher.SQLiteDatabase
import java.io.File
import kotlin.random.Random


/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2025/03/13
 *     desc   : sqlcipher 测试类
 *     version: 1.0
 * </pre>
 */
class SqlcipherActivity : BaseActivity() {
    private val binding by viewBinding(ActivitySqlCipherBinding::inflate)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 2. 创建或打开数据库
        val databaseName = "my_secure_db.db"
        val password = "your_secure_password"
        val databasePath = getDatabasePath(databaseName)

        val database: SQLiteDatabase =
            SQLiteDatabase.openOrCreateDatabase(databasePath, password, null, null, null)

        binding.button1.apply {
            setOnClickListener {
                database.execSQL(
                    """
    CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT,
        age INTEGER
    )
"""
                )
            }
        }


        binding.buttonInsert.apply {
            setOnClickListener {
                val radomName = lastNameList[Random.nextInt(
                    0, lastNameList.size - 1
                )] + firstNameList[Random.nextInt(0, firstNameList.size - 1)]

                // 插入数据
                database.execSQL(
                    "INSERT INTO users (name, age) VALUES (?, ?)",
                    arrayOf(radomName, Random.nextInt(0, 100))
                )
            }
        }

        binding.buttonShow.apply {
            setOnClickListener {
                // 查询数据
                val cursor = database.rawQuery("SELECT * FROM users WHERE age > ?", arrayOf("20"))
                val tableData = mutableListOf<String>()
                cursor.use {
                    while (cursor.moveToNext()) {
                        val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                        val age = cursor.getInt(cursor.getColumnIndexOrThrow("age"))
                        tableData.add("name: $name, age: $age")
                    }
                }
                cursor.close()
                binding.textView.text = tableData.joinToString("\n")
            }
        }

        binding.buttonExport.apply {
            setOnClickListener {
                // 获取数据库文件路径
                databasePath
                // 复制到外部存储
                val exportFile = File(context.getExternalFilesDir(null), "exported_db.db")
                databasePath.copyTo(exportFile, overwrite = true)
            }
        }

        binding.buttonExportNoCipher.apply {
            setOnClickListener {
                exportToUnencryptedDatabase(this@SqlcipherActivity, password)
            }
        }
    }

    fun exportToUnencryptedDatabase(context: Context, password: String) {
        // 源数据库（加密的）
        val sourceFile = getDatabasePath("my_secure_db.db")
        // 目标数据库（未加密的）
        val destFile = File(context.getExternalFilesDir(null), "decrypted.db")

        // 打开加密数据库，注意，如果使用的是openDatabase，请使用absolutePath
        val encryptedDb = SQLiteDatabase.openOrCreateDatabase(
            sourceFile, password, null, null, null
        )

        try {
            // 导出为未加密数据库
            encryptedDb.rawExecSQL("ATTACH DATABASE '${destFile.path}' AS plaintext KEY '';")
            encryptedDb.rawExecSQL("SELECT sqlcipher_export('plaintext');")
            encryptedDb.rawExecSQL("DETACH DATABASE plaintext;")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            encryptedDb.close()
        }
    }

    companion object {
        private val lastNameList = arrayOf(
            "趙",
            "錢",
            "孫",
            "李",
            "周",
            "吳",
            "鄭",
            "王",
            "馮",
            "陳",
            "褚",
            "衛",
            "蔣",
            "沈",
            "韓",
            "楊",
            "朱",
            "秦",
            "尤",
            "許",
            "何",
            "呂",
            "施",
            "張",
            "孔",
            "曹",
            "嚴",
            "華",
            "金",
            "魏",
            "陶",
            "姜",
            "戚",
            "謝",
            "鄒",
            "喻",
            "柏",
            "水",
            "竇",
            "章",
            "雲",
            "蘇",
            "潘",
            "葛",
            "奚",
            "范",
            "彭",
            "郎",
            "魯",
            "韋",
            "昌",
            "馬",
            "苗",
            "鳳",
            "花",
            "方",
            "俞",
            "任",
            "袁",
            "柳",
            "酆",
            "鮑",
            "史",
            "唐"
        )
        private val firstNameList = arrayOf(
            "子璇",
            "淼",
            "国栋",
            "夫子",
            "甜",
            "敏",
            "尚",
            "国贤",
            "贺祥",
            "晨涛",
            "昊轩",
            "易轩",
            "益辰",
            "益帆",
            "益冉",
            "瑞鑫",
            "泽",
            "瑾",
            "皓",
            "晨涛",
            "杰",
            "俊",
            "珺",
            "嘉磊",
            "晨涛",
            "天佑",
            "瑞",
            "政",
            "智",
            "展鹏",
            "笑",
            "志",
            "冰",
            "海",
            "洋",
            "晨",
            "坤",
            "桦",
            "霖",
            "楠",
            "榕",
            "风",
            "航",
            "弘",
            "泰",
            "宇",
            "浩",
            "哲",
            "博",
            "舟",
            "峻",
            "炎",
            "烨",
            "熠",
            "昊",
            "瀚"
        )

    }
}