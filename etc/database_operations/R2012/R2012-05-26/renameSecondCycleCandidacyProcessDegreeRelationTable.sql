RENAME TABLE SECOND_CYCLE_CANDIDACY_PROCESS_DEGREE TO CANDIDACY_PROCESS_DEGREE;
ALTER TABLE CANDIDACY_PROCESS_DEGREE CHANGE OID_SECOND_CYCLE_CANDIDACY_PROCESS OID_CANDIDACY_PROCESS bigint(20) unsigned;