package com.safeline.safelinecranes.db;

import android.provider.BaseColumns;

public class Contract {

    private Contract() {}

    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String LICENSE = "license";
        public static final String DEVICE = "device";
        public static final String VESSEL = "vessel";
    }

    public static class PositionTable implements BaseColumns {
        public static final String TABLE_NAME = "positions";
        public static final String NAME = "name";
        public static final String TYPE = "type_of_work";
        public static final String X = "x";
        public static final String Y = "y";
        public static final String SYNC = "sync";
        public static final String STORAGE = "is_storage";
    }

    public static class SyncDeleteTable implements BaseColumns {
        public static final String TABLE_NAME = "sync_deleted";
        public static final String TYPE = "type";
        public static final String KEY = "key";
    }

    public static class RopeTypeTable implements BaseColumns {
        public static final String TABLE_NAME = "rope_types";
        public static final String MANUFACTURER = "manufacturer";
        public static final String TYPE = "type";
        public static final String MODEL = "model";
        public static final String DIAMETER = "diameter";
        public static final String LENGTH = "length";
        public static final String MATERIAL = "material";
        public static final String BREAKING_FORCE = "breaking_Force";
        public static final String SYNC = "sync";
    }

    public static class RopeTable implements BaseColumns {
        public static final String TABLE_NAME = "ropes";
        public static final String SERIAL_NUMBER = "serial_Nr";
        public static final String TYPE = "type";
        public static final String WORK_HOURS = "workHours";
        public static final String POSITION = "position";
        public static final String CERTIFICATE_DATE = "date";
        public static final String CERTIFICATE = "certficate";
        public static final String SYNC = "sync";
        public static final String HAS_RETIRE = "has_retire";
        public static final String RETIRE_DATE = "date_retired";
        public static final String TAG = "tag";
    }

    public static class AnswersTable implements BaseColumns {
        public static final String TABLE_NAME = "answers";
        public static final String HEADER = "answer";
        public static final String DETAILS = "action";
        public static final String QUESTION_ID = "question";
        public static final String IMAGE1 = "image_1";
        public static final String RECOMMENDATIONS = "recommendation";
        public static final String VALUE = "value";
    }

    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "questions";
        public static final String HEADER = "header";
        public static final String DETAILS = "details";
        public static final String TEST = "subtest";
        public static final String IS_ROUTINE = "isRoutine";
        public static final String MATERIAL = "material";
        public static final String OUTCOME = "outcome";
        public static final String ORDER = "question_order";
        public static final String OPERATION = "operation";
        public static final String CONDITION = "condition";
        public static final String RISK = "risk";
    }

    public static class InspectionTable implements BaseColumns {
        public static final String TABLE_NAME = "inspections";
        public static final String IS_FINISHED = "finished";
        public static final String STEP = "step";
        public static final String DATE_CREATED = "date_created";
        public static final String DATE_FINISHED = "date_finished";
        public static final String WORK_HOURS = "work_hous";
        public static final String POSITION = "position";
    }

    public static class OutComeCodesTable implements BaseColumns {
        public static final String TABLE_NAME = "outcome_codes";
        public static final String SEVERITY = "severity";
        public static final String CODE_ID = "code_id";
        public static final String FRIENDLY_TEXT = "friendly_text";
        public static final String TYPE = "type";
    }

    public static class ResultsTable implements BaseColumns {
        public static final String TABLE_NAME = "results";
        public static final String INSPECTION = "inspection_id";
        public static final String POSITION = "position_id";
        public static final String QUESTION = "question_id";
        public static final String ANSWER = "answer_id";
        public static final String ROPE = "rope_id";
        public static final String IS_GLOBAL = "is_global";
    }

    public static class FinishedInspection implements BaseColumns {
        public static final String TABLE_NAME = "finished_results";
        public static final String INSPECTION_ID = "inspection_id";
        public static final String SYNC = "sync";
        public static final String DATE_FINISHED = "date_finished";
        public static final String RESULTS = "results";
    }
}
