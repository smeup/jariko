package com.smeup.rpgparser.db

import com.smeup.dbnative.model.CharacterType
import com.smeup.dbnative.model.FileMetadata
import com.smeup.dbnative.utils.fieldByType

const val CONVENTIONAL_INDEX_SUFFIX = "_INDEX"

fun createEMPLOYEE() =
    """
    CREATE TABLE EMPLOYEE ( 
	EMPNO CHAR(6) DEFAULT '' NOT NULL , 
	FIRSTNME CHAR(12) DEFAULT '' NOT NULL , 
	MIDINIT CHAR(1) DEFAULT '' NOT NULL , 
	LASTNAME CHAR(15) DEFAULT '' NOT NULL , 
	WORKDEPT CHAR(3) DEFAULT '' NOT NULL, 
	PRIMARY KEY( EMPNO ) )   
        """.trimIndent()

fun createEmployeeMetadata(): FileMetadata = FileMetadata(
        "EMPLOYEE",
        "EMPLOYEE",
        listOf( "EMPNO" fieldByType CharacterType(6),
                "FIRSTNME" fieldByType CharacterType(12),
                "MIDINIT" fieldByType  CharacterType(1),
                "LASTNAME" fieldByType CharacterType(15),
                "WORKDEPT" fieldByType CharacterType(3)),
        listOf("EMPNO"),
        true
        )

fun createXEMP2() = "CREATE VIEW XEMP2 AS SELECT * FROM EMPLOYEE ORDER BY WORKDEPT"

fun createXEMP2Index() = "CREATE INDEX XEMP2$CONVENTIONAL_INDEX_SUFFIX ON EMPLOYEE (WORKDEPT ASC)   "

fun insertRecords() =
    """
INSERT INTO EMPLOYEE (EMPNO, FIRSTNME, MIDINIT, LASTNAME, WORKDEPT) VALUES 
('000010', 'CHRISTINE', 'I', 'HAAS', 'A00'),
('000020', 'MICHAEL', 'L', 'THOMPSON', 'B01'),
('000030', 'SALLY', 'A', 'KWAN', 'C01'),
('000050', 'JOHN', 'B', 'GEYER', 'E01'),
('000060', 'IRVING', 'F', 'STERN', 'D11'),
('000070', 'EVA', 'D', 'PULASKI', 'D21'),
('000090', 'EILEEN', 'W', 'HENDERSON', 'E11'),
('000100', 'THEODORE', 'Q', 'SPENSER', 'E21'),
('000110', 'VINCENZO', 'G', 'LUCCHESSI', 'A00'),
('000120', 'SEAN', '', 'O''CONNELL', 'A00'),
('000130', 'DELORES', 'M', 'QUINTANA', 'C01'),
('000140', 'HEATHER', 'A', 'NICHOLLS', 'C01'),
('000150', 'BRUCE', '', 'ADAMSON', 'D11'),
('000160', 'ELIZABETH', 'R', 'PIANKA', 'D11'),
('000170', 'MASATOSHI', 'J', 'YOSHIMURA', 'D11'),
('000180', 'MARILYN', 'S', 'SCOUTTEN', 'D11'),
('000190', 'JAMES', 'H', 'WALKER', 'D11'),
('000200', 'DAVID', '', 'BROWN', 'D11'),
('000210', 'WILLIAM', 'T', 'JONES', 'D11'),
('000220', 'JENNIFER', 'K', 'LUTZ', 'D11'),
('000230', 'JAMES', 'J', 'JEFFERSON', 'D21'),
('000240', 'SALVATORE', 'M', 'MARINO', 'D21'),
('000250', 'DANIEL', 'S', 'SMITH', 'D21'),
('000260', 'SYBIL', 'P', 'JOHNSON', 'D21'),
('000270', 'MARIA', 'L', 'PEREZ', 'D21'),
('000280', 'ETHEL', 'R', 'SCHNEIDER', 'E11'),
('000290', 'JOHN', 'R', 'PARKER', 'E11'),
('000300', 'PHILIP', 'X', 'SMITH', 'E11'),
('000310', 'MAUDE', 'F', 'SETRIGHT', 'E11'),
('000320', 'RAMLAL', 'V', 'MEHTA', 'E21'),
('000330', 'WING', '', 'LEE', 'E21'),
('000340', 'JASON', 'R', 'GOUNOT', 'E21'),
('200010', 'DIAN', 'J', 'HEMMINGER', 'A00'),
('200120', 'GREG', '', 'ORLANDO', 'A00'),
('200140', 'KIM', 'N', 'NATZ', 'C01'),
('200170', 'KIYOSHI', '', 'YAMAMOTO', 'D11'),
('200220', 'REBA', 'K', 'JOHN', 'D11'),
('200240', 'ROBERT', 'M', 'MONTEVERDE', 'D21'),
('200280', 'EILEEN', 'R', 'SCHWARTZ', 'E11'),
('200310', 'MICHELLE', 'F', 'SPRINGER', 'E11'),
('200330', 'HELENA', '', 'WONG', 'E21'),
('200340', 'ROY', 'R', 'ALONZO', 'E21')         
        """.trimIndent()
