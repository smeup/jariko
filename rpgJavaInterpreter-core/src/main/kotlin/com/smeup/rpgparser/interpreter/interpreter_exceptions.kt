/*
 * Copyright 2019 Sme.UP S.p.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 *
 */

package com.smeup.rpgparser.interpreter

import com.smeup.rpgparser.parsing.ast.Statement
import com.strumenta.kolasu.model.Position

// TODO: Convert current thrown exception to ProgramStatusCode based ones
enum class ProgramStatusCode(
    val code: String,
    val description: String,
) {
    // Normal Codes
    NO_EXCEPTION("00000", "No exception/error occurred"),
    LR_INDICATOR_ON("00001", "Called program returned with the LR indicator on."),
    CONVERSION_SUBSTITUTION("00050", "Conversion resulted in substitution."),

    // Exception/Error Codes
    VALUE_OUT_OF_RANGE("00100", "Value out of range for string operation"),
    NEGATIVE_SQUARE_ROOT("00101", "Negative square root"),
    DIVIDE_BY_ZERO("00102", "Divide by zero"),
    INTERMEDIATE_RESULT_TOO_SMALL("00103", "An intermediate result is not large enough to contain the result."),
    FLOAT_UNDERFLOW("00104", "Float underflow. An intermediate value is too small to be contained in the intermediate result field."),
    INVALID_CHARACTERS("00105", "Invalid characters in character to numeric conversion functions."),
    INVALID_DATE_TIME("00112", "Invalid Date, Time or Timestamp value."),
    DATE_OVERFLOW_UNDERFLOW(
        "00113",
        "Date overflow or underflow. (For example, when the result of a Date calculation results in a number greater than *HIVAL or less than *LOVAL.)",
    ),
    DATE_MAPPING_ERROR(
        "00114",
        "Date mapping errors, where a Date is mapped from a 4-character year to a 2-character year, and the date range is not 1940-2039.",
    ),
    VARIABLE_LENGTH_FIELD_INVALID("00115", "Variable-length field has a current length that is not valid."),
    TABLE_ARRAY_OUT_OF_SEQUENCE("00120", "Table or array out of sequence."),
    ARRAY_INDEX_NOT_VALID("00121", "Array index not valid"),
    OCCUR_OUTSIDE_RANGE("00122", "OCCUR outside of range"),
    RESET_DURING_INITIALIZATION("00123", "Reset attempted during initialization step of program"),
    VARYING_DIMENSION_ARRAY_INVALID("00124", "The number of elements for the varying-dimension array is not valid"),
    STRING_OPERAND_INVALID("00125", "The string operand is not valid."),
    ERROR_SENDING_MESSAGE("00126", "An error occurred while sending a message."),

    CALLED_PROGRAM_HALT_FAILED("00202", "Called program or procedure failed; halt indicator (H1 through H9) not on"),
    ERROR_CALLING_PROGRAM("00211", "Error calling program or procedure"),
    POINTER_PARAMETER_ERROR("00222", "Pointer or parameter error"),
    HALT_INDICATOR_RETURNED("00231", "Called program or procedure returned with halt indicator on"),
    HALT_INDICATOR_ON_IN_THIS_PROGRAM("00232", "Halt indicator on in this program"),
    HALT_INDICATOR_ON_RETURN("00233", "Halt indicator on when RETURN operation run"),
    RPG_IV_FORMATTED_DUMP_FAILED("00299", "RPG IV formatted dump failed"),
    CLASS_METHOD_NOT_FOUND("00301", "Class or method not found for a method call, or error in method call."),
    ERROR_CONVERTING_ARRAY_TO_RPG("00302", "Error while converting a Java™ array to an RPG parameter on entry to a Java native method."),
    ERROR_CONVERTING_RPG_TO_ARRAY("00303", "Error converting RPG parameter to Java array on exit from an RPG native method."),
    ERROR_PREPARING_ARRAY_FOR_JAVA("00304", "Error converting RPG parameter to Java array in preparation for a Java method call."),
    ERROR_CONVERTING_ARRAY_TO_RPG_RETURN("00305", "Error converting Java array to RPG parameter or return value after a Java method."),
    ERROR_CONVERTING_RPG_RETURN_TO_ARRAY("00306", "Error converting RPG return value to Java array."),
    ERROR_ON_DSPLY_OPERATION("00333", "Error on DSPLY operation"),
    ERROR_PARSING_XML_DOCUMENT("00351", "Error parsing XML document"),
    INVALID_XML_OPTION("00352", "Invalid option for %XML"),
    XML_DOES_NOT_MATCH_RPG_VARIABLE("00353", "XML document does not match RPG variable"),
    ERROR_PREPARING_XML_PARSING("00354", "Error preparing for XML parsing"),
    DATA_GEN_PROC_NOT_AVAILABLE("00355", "The program or procedure is not available for the DATA-GEN or DATA-INTO operation"),
    DATA_INTO_DOCUMENT_NOT_MATCH_RPG("00356", "The document for the DATA-INTO operation does not match the RPG variable"),
    DATA_INTO_PARSER_DETECTED_ERROR("00357", "The parser for the DATA-INTO operation detected an error"),
    DATA_INTO_PARSER_INFORMATION_ERROR("00358", "The information supplied by the parser for the DATA-INTO operation was in error"),
    DATA_INTO_RUNNING_PROGRAM_ERROR(
        "00359",
        "An error occurred while running the program or procedure for the DATA-INTO or DATA-GEN operation",
    ),
    PREPARING_DATA_FOR_GENERATOR_ERROR(
        "00361",
        "An error occurred preparing the data from the RPG variable for the generator for DATA-GEN",
    ),
    GENERATOR_INFORMATION_ERROR("00362", "The information supplied by the generator for the DATA-GEN operation was in error"),
    GENERATOR_DETECTED_ERROR("00363", "The generator for the DATA-GEN operation detected an error"),
    OUTPUT_FILE_HANDLING_ERROR("00364", "An error occurred while handling the output file or output variable for the DATA-GEN operation"),
    ERROR_SEQUENCE_OF_DATA_GEN("00365", "An error occurred with the sequence of DATA-GEN operations"),

    DATA_AREA_NOT_FOUND("00401", "Data area specified on IN/OUT not found"),
    PDA_NOT_VALID("00402", "*PDA not valid for non-prestart job"),
    DATA_AREA_TYPE_LENGTH_NOT_MATCH("00411", "Data area type or length does not match"),
    DATA_AREA_NOT_LOCKED_FOR_OUTPUT("00412", "Data area not locked for output"),
    ERROR_ON_IN_OUT_OPERATION("00413", "Error on IN/OUT operation"),
    USER_NOT_AUTHORIZED_DATA_AREA("00414", "User not authorized to use data area"),
    USER_NOT_AUTHORIZED_CHANGE_DATA_AREA("00415", "User not authorized to change data area"),
    ERROR_ON_UNLOCK_OPERATION("00421", "Error on UNLOCK operation"),
    LENGTH_OUT_OF_RANGE("00425", "Length requested for storage allocation is out of range"),
    ERROR_STORAGE_MANAGEMENT_OPERATION("00426", "Error encountered during storage management operation"),
    DATA_AREA_LOCKED_BY_ANOTHER_PROGRAM("00431", "Data area previously locked by another program"),
    DATA_AREA_LOCKED_BY_PROGRAM_SAME_PROCESS("00432", "Data area locked by program in the same process"),
    CHARACTER_FIELD_NOT_ENCLOSED("00450", "Character field not entirely enclosed by shift-out and shift-in characters"),
    CCSID_CONVERSION_NOT_SUPPORTED("00451", "Conversion between two CCSIDs is not supported"),
    CHARACTERS_COULD_NOT_BE_CONVERTED("00452", "Some characters could not be converted between two CCSIDs"),
    ERROR_CONVERSION_BETWEEN_CCSIDS("00453", "An error occurred during conversion between two CCSIDs"),

    FAILURE_TO_RETRIEVE_SORT_SEQUENCE("00501", "Failure to retrieve sort sequence."),
    FAILURE_TO_CONVERT_SORT_SEQUENCE("00502", "Failure to convert sort sequence."),

    COMMITMENT_CONTROL_NOT_ACTIVE("00802", "Commitment control not active."),
    ROLLBACK_OPERATION_FAILED("00803", "Rollback operation failed."),
    ERROR_ON_COMMIT_OPERATION("00804", "Error occurred on COMMIT operation"),
    ERROR_ON_ROLBK_OPERATION("00805", "Error occurred on ROLBK operation"),

    DECIMAL_DATA_ERROR("00907", "Decimal data error (digit or sign not valid)"),
    COMPILER_LEVEL_MISMATCH(
        "00970",
        "The level number of the compiler used to generate the program does not agree with the level number of the RPG IV run-time subroutines.",
    ),
    INTERNAL_COMPILER_FAILURE("09998", "Internal failure in ILE RPG compiler or in run-time subroutines"),
    EXCEPTION_MESSAGE_RECEIVED("09999", "Exception message received by procedure"),
    ;

    fun toThrowable(
        additionalInformation: String? = null,
        statement: Statement? = null,
        position: Position? = null,
    ) = InterpreterProgramStatusErrorException(this, statement, position, additionalInformation)

    fun matches(status: String): Boolean {
        val codeValue = code.toInt()
        return when (val normalizedStatus = status.trim().uppercase()) {
            "*PROGRAM" -> (codeValue in 100..999) || codeValue == 9999
            "*FILE" -> codeValue in 1000..2999
            "*ALL", "" -> codeValue in 100..9999
            else -> normalizedStatus.toInt() == codeValue
        }
    }
}

/**
 * RPGLE interpreter errors.
 * Can be caught by [com.smeup.rpgparser.parsing.ast.MonitorStmt]s.
 */
data class InterpreterProgramStatusErrorException(
    /** Status code of the error. */
    val statusCode: ProgramStatusCode,
    /** The statement in which the error occurred. */
    var statement: Statement?,
    /** The source position in which the error occurred. */
    var position: Position?,
    /** Additional information about the error. */
    val additionalInformation: String? = null,
) : RuntimeException(
        additionalInformation?.let { "${statusCode.description} - $it" } ?: statusCode.description,
    )
