package com.holdonly.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/04/19
 *     desc   : 利用KotlinPoet生成类
 *     生成LogUtils的扩展方法，固定传入关键字
 *     version: 1.0
 * </pre>
 */
object CustomGenerator {
    fun generate(codeGenerator: CodeGenerator, list: List<KSFunctionDeclaration>) {
        val fileSpecBuilder = FileSpec.builder(
            "com.holdonly.ksp",
            "LogUtilsKSP"
        )
        list.forEach {
            //获取className
            val functionName = it.simpleName.getShortName()
            val functionBuilder = FunSpec.builder(functionName).receiver(Unit::class)
            fileSpecBuilder.addFunction(functionBuilder.build()).build()
                .writeTo(codeGenerator, false)

        }
    }
}