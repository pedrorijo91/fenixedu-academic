create table `PERSON_IDENTIFICATION_DOCUMENT_EXTRA_INFO` (`OID` bigint unsigned, `VALUE` text, `OID_PERSON` bigint unsigned, `REGISTERED_IN_SYSTEM_TIMESTAMP` timestamp NULL default NULL, `OJB_CONCRETE_CLASS` varchar(255) NOT NULL DEFAULT '', `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_PERSON)) ENGINE=InnoDB, character set latin1;