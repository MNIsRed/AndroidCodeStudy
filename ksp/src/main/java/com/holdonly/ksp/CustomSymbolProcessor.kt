package com.holdonly.ksp

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import com.holdonly.ksp.annatation.KeyName

/**
 * <pre>
 *     author : holdonly
 *     e-mail : suliliveinchina@gmail.com
 *     time   : 2024/04/19
 *     desc   : 自定义注解处理器
 *     version: 1.0
 * </pre>
 */
class CustomSymbolProcessor(private val mEnvironment: SymbolProcessorEnvironment) :
    SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        //为什么是qualifiedName能够获取注解属性
        val symbols = resolver.getSymbolsWithAnnotation(KeyName::class.qualifiedName!!)
        val ret = symbols.filter { !it.validate() }.toList()
        val nameList = symbols.filter {
            it is KSFunctionDeclaration && it.validate()
        }.map {
            it as KSFunctionDeclaration
        }.toList()
        CustomGenerator.generate(mEnvironment.codeGenerator, nameList)
        return ret
    }
}