package com.smeup.rpgparser.parsing.ast

import com.smeup.rpgparser.parsing.facade.RpgParserFacade
import com.strumenta.kolasu.model.Node
import com.strumenta.kolasu.model.Position
import kotlinx.serialization.Serializable
import java.io.InputStream

@Serializable
data class ApiId(
    override val position: Position?,
    val library: String?,
    val file: String?,
    val member: String,
) : Node(position = position) {
    override fun toString(): String =
        StringBuffer()
            .apply {
                if (library != null) {
                    append("$library/")
                }
                if (file != null) {
                    append("$file,")
                }
                append(member)
            }.toString()
}

/**
 * Loading API policy.
 * */
enum class LoadApiPolicy {
    /**
     * The API will always be loaded
     * */
    Static,

    /**
     * The API will be loaded only if program declares to require at least one of the features exposed by the ApiDescriptor
     * (for example in main program we have a EXSR MYSUBROUTINE statement)
     * */
    Dynamic,

    /**
     * Like Dynamic but just during program execution
     * */
    JustInTime,
}

/**
 *
 * Exposes the API features.
 * */
@Serializable
data class ApiDescriptor(
    /**
     * Api declares how jariko should be load itself.
     * */
    val loadApiPolicy: LoadApiPolicy = LoadApiPolicy.Static,
    /**
     * Api declares to expose these variable names
     * */
    var variables: Set<String>? = null,
    /**
     * Api declares to expose these subroutines names
     * */
    var subroutines: Set<String>? = null,
    /**
     * Api declares to expose these procedures names
     * */
    var procedures: Set<String>? = null,
)

/**
 * Represents an API
 * @see Api.loadApiDescriptor
 * @see Api.loadApi
 * */
data class Api(
    val compilationUnit: CompilationUnit,
) {
    companion object {
        /**
         * Load API descriptor from rpgle source
         * */
        fun loadApiDescriptor(
            inputStream: InputStream,
            sourceProgram: SourceProgram,
        ): ApiDescriptor {
            inputStream.use {
                val cu = RpgParserFacade().parseAndProduceAst(inputStream, sourceProgram)
                return ApiDescriptor(
                    variables =
                        cu.allDataDefinitions
                            .map {
                                it.name
                            }.toSet(),
                    subroutines =
                        cu.subroutines
                            .map {
                                it.name
                            }.toSet(),
                    procedures = null,
                )
            }
        }

        /**
         * Load API from rpgle source
         * */
        fun loadApi(
            inputStream: InputStream,
            sourceProgram: SourceProgram,
        ): Api {
            inputStream.use {
                val cu = RpgParserFacade().parseAndProduceAst(inputStream, sourceProgram)
                return Api(cu)
            }
        }
    }
}

/**
 * Registry of the APIs used by program
 * */
internal class ApiRegistry {
    val apiDescriptors = mutableMapOf<ApiId, ApiDescriptor>()

    /**
     * Get first ApiId matches variables, subroutines and procedures
     * */
    fun getApiMatches(
        filterByVariables: Collection<String>?,
        filterBySubroutines: Collection<String>?,
        filterByProcedures: Collection<String>?,
    ): ApiId? =
        apiDescriptors
            .filter { apiDescriptor ->
                filterByVariables?.let {
                    apiDescriptor.value.variables
                        ?.intersect(it)
                        ?.isNotEmpty() ?: false
                } ?: true
            }.filter { apiDescriptor ->
                filterBySubroutines?.let {
                    apiDescriptor.value.subroutines
                        ?.intersect(it)
                        ?.isNotEmpty() ?: false
                } ?: true
            }.filter { apiDescriptor ->
                filterByProcedures?.let {
                    apiDescriptor.value.procedures
                        ?.intersect(it)
                        ?.isNotEmpty() ?: false
                } ?: true
            }.keys
            .firstOrNull()
}

/**
 * Get key related receiver, format is as follows:
 * library/file/member.ext
 * Since library and file are not mandatory returned key could be:
 * library/member.ext
 * file/member.ext
 * member.ext
 * */
internal fun ApiId.key(sourceProgram: SourceProgram): String {
    val key =
        this.file?.let {
            "$it/$member.${sourceProgram.extension}"
        } ?: "$member.${sourceProgram.extension}"
    return library?.let {
        "$library/$key"
    } ?: "$key"
}

internal fun ApiId.apiDescriptorKey(sourceProgram: SourceProgram): String {
    val key =
        this.file?.let {
            "$it/$member.descriptor.${sourceProgram.extension}"
        } ?: "$member.descriptor.${sourceProgram.extension}"
    return library?.let {
        "$library/$key"
    } ?: "$key"
}
