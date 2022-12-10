package com.safeline.safelinecranes.db;

import static com.safeline.safelinecranes.db.Contract.InspectionTable;
import static com.safeline.safelinecranes.db.Contract.ResultsTable;
import static com.safeline.safelinecranes.db.Contract.SyncDeleteTable;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.safeline.safelinecranes.db.Contract.AnswersTable;
import com.safeline.safelinecranes.db.Contract.OutComeCodesTable;
import com.safeline.safelinecranes.db.Contract.PositionTable;
import com.safeline.safelinecranes.db.Contract.QuestionsTable;
import com.safeline.safelinecranes.db.Contract.RopeTable;
import com.safeline.safelinecranes.db.Contract.RopeTypeTable;
import com.safeline.safelinecranes.db.Contract.UserTable;
import com.safeline.safelinecranes.models.Answer;
import com.safeline.safelinecranes.models.DeletedObject;
import com.safeline.safelinecranes.models.FinishedResults;
import com.safeline.safelinecranes.models.Inspection;
import com.safeline.safelinecranes.models.OutcomeCodes;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Question;
import com.safeline.safelinecranes.models.Result;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.RopeType;
import com.safeline.safelinecranes.models.Severity;
import com.safeline.safelinecranes.models.User;

import java.util.ArrayList;
import java.util.List;


public class MigrationDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cranes.db";
    private static final int DATABASE_VERSION = 3;
    private static MigrationDbHelper instance;
    public SQLiteDatabase db;

    private MigrationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized MigrationDbHelper getInstance(Context context) {
        if(instance == null) {
            instance = new MigrationDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " +
                UserTable.TABLE_NAME + " ( " +
                UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserTable.USERNAME + " TEXT, " + UserTable.PASSWORD + " TEXT, " +
                UserTable.LICENSE + " TEXT, " +  UserTable.VESSEL + " TEXT, " +
                UserTable.DEVICE + " TEXT)";

        final String SQL_CREATE_POSITIONS_TABLE = "CREATE TABLE " +
                PositionTable.TABLE_NAME + " ( " +
                PositionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PositionTable.TYPE + " TEXT, " +
                PositionTable.NAME + " TEXT, " + PositionTable.X + " INTEGER, " + PositionTable.Y + " INTEGER, " +
                PositionTable.SYNC + " INTEGER, " + PositionTable.STORAGE + " INTEGER " +")";

        final String SQL_CREATE_DELETED_SYNC_TABLE =  "CREATE TABLE " +
                SyncDeleteTable.TABLE_NAME + " ( " +
                SyncDeleteTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SyncDeleteTable.TYPE + " TEXT, " + SyncDeleteTable.KEY + " INTEGER)";

        final String SQL_CREATE_ROPE_TYPE_TABLE = "CREATE TABLE " +
                RopeTypeTable.TABLE_NAME + " ( " +
                RopeTypeTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RopeTypeTable.TYPE + " TEXT, " + RopeTypeTable.MANUFACTURER + " TEXT, " +
                RopeTypeTable.MODEL + " TEXT, " + RopeTypeTable.DIAMETER + " REAL, " +
                RopeTypeTable.LENGTH + " REAL, " +
                RopeTypeTable.MATERIAL + " TEXT, " + RopeTypeTable.BREAKING_FORCE + " REAL, " +
                RopeTypeTable.SYNC + " INTEGER" + ")";

        final String SQL_CREATE_ROPE_TABLE = "CREATE TABLE " +
                RopeTable.TABLE_NAME + " ( " +
                RopeTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RopeTable.TYPE + " INTEGER NOT NULL, " +
                RopeTable.WORK_HOURS + " INTEGER NOT NULL, " +
                RopeTable.POSITION + " INTEGER, " +
                RopeTable.CERTIFICATE_DATE + " TEXT, " +
                RopeTable.CERTIFICATE + " BLOB, " +
                RopeTable.SERIAL_NUMBER + " TEXT NOT NULL," +
                RopeTable.SYNC + " INTEGER, " +
                RopeTable.HAS_RETIRE + " INTEGER, " +
                RopeTable.RETIRE_DATE + " TEXT, " +
                RopeTable.TAG + " TEXT, " +
                "CONSTRAINT rope_type_fk FOREIGN KEY(" + RopeTable.TYPE + ") REFERENCES " +
                RopeTypeTable.TABLE_NAME + "(" + RopeTypeTable._ID + ") " + "ON DELETE CASCADE)";

        final String SQL_CREATE_ANSWERS_TABLE = "CREATE TABLE " +
                AnswersTable.TABLE_NAME + " ( " +
                AnswersTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AnswersTable.HEADER + " TEXT, " +
                AnswersTable.DETAILS + " TEXT, " +
                AnswersTable.QUESTION_ID + " INTEGER, " +
                AnswersTable.IMAGE1 + " TEXT, " +
                AnswersTable.RECOMMENDATIONS + " TEXT, " +
                AnswersTable.VALUE + " INTEGER, " +
                "CONSTRAINT answer_question_fk FOREIGN KEY(" + AnswersTable.QUESTION_ID + ") REFERENCES " +
                QuestionsTable.TABLE_NAME + "(" + QuestionsTable._ID + ") " + "ON DELETE CASCADE)";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.HEADER + " TEXT, " +
                QuestionsTable.DETAILS + " TEXT, " +
                QuestionsTable.TEST + " TEXT, " +
                QuestionsTable.IS_ROUTINE + " INTEGER, " +
                QuestionsTable.MATERIAL + " TEXT, " +
                QuestionsTable.OUTCOME + " TEXT, " +
                QuestionsTable.ORDER + " INTEGER, " +
                QuestionsTable.OPERATION + " TEXT, " +
                QuestionsTable.CONDITION + " TEXT, " +
                QuestionsTable.RISK + " TEXT " + ")";

        final String CREATE_INSPECTION_TABLE = "CREATE TABLE " +
                InspectionTable.TABLE_NAME + " ( " +
                InspectionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InspectionTable.WORK_HOURS + " INTEGER, " +
                InspectionTable.IS_FINISHED + " INTEGER, " +
                InspectionTable.STEP + " INTEGER, " +
                InspectionTable.POSITION + " INTEGER, " +
                InspectionTable.DATE_CREATED + " TEXT, " +
                InspectionTable.DATE_FINISHED + " TEXT, " +
                "CONSTRAINT position_inspection_fk FOREIGN KEY(" + InspectionTable.POSITION + ") REFERENCES " +
                PositionTable.TABLE_NAME + "(" + PositionTable._ID + ") " + "ON DELETE CASCADE)";

        final String CREATE_OUTCOME_CODES_TABLE = "CREATE TABLE " +
                OutComeCodesTable.TABLE_NAME + " ( " +
                OutComeCodesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OutComeCodesTable.SEVERITY + " INTEGER, " +
                OutComeCodesTable.CODE_ID + " TEXT, " +
                OutComeCodesTable.FRIENDLY_TEXT + " TEXT, " +
                OutComeCodesTable.TYPE + " TEXT" + ")";

        final String SQL_CREATE_RESULTS_TABLE = "CREATE TABLE " +
                ResultsTable.TABLE_NAME + " ( " +
                ResultsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ResultsTable.INSPECTION + " INTEGER, " +
                ResultsTable.POSITION + " INTEGER, " +
                ResultsTable.QUESTION + " INTEGER, " +
                ResultsTable.ANSWER + " INTEGER, " +
                ResultsTable.ROPE + " INTEGER, " +
                ResultsTable.IS_GLOBAL + " INTEGER, " +
                "CONSTRAINT results_inspection_fk FOREIGN KEY(" + ResultsTable.INSPECTION + ") REFERENCES " +
                InspectionTable.TABLE_NAME + "(" + InspectionTable._ID + ") " + "ON DELETE CASCADE, " +
                "CONSTRAINT results_position_fk FOREIGN KEY(" + ResultsTable.POSITION + ") REFERENCES " +
                PositionTable.TABLE_NAME + "(" + PositionTable._ID + ") " + "ON DELETE CASCADE, " +
                "CONSTRAINT results_rope_fk FOREIGN KEY(" + ResultsTable.ROPE + ") REFERENCES " +
                RopeTable.TABLE_NAME + "(" + RopeTable._ID + ") " + "ON DELETE CASCADE, " +
                "CONSTRAINT results_question_fk FOREIGN KEY(" + ResultsTable.QUESTION + ") REFERENCES " +
                QuestionsTable.TABLE_NAME + "(" + QuestionsTable._ID + ") " + "ON DELETE CASCADE, " +
                "CONSTRAINT results_answer_fk FOREIGN KEY(" + ResultsTable.ANSWER + ") REFERENCES " +
                AnswersTable.TABLE_NAME + "(" + AnswersTable._ID + ") " + "ON DELETE CASCADE)";

        final String CREATE_FINISHED_RESULTS_TABLE = "CREATE TABLE " +
                Contract.FinishedInspection.TABLE_NAME + " ( " +
                Contract.FinishedInspection._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.FinishedInspection.SYNC + " INTEGER, " +
                Contract.FinishedInspection.INSPECTION_ID + " INTEGER, " +
                Contract.FinishedInspection.DATE_FINISHED + " TEXT, " +
                Contract.FinishedInspection.RESULTS + " TEXT, " +
                "CONSTRAINT finished_inspection_id_fk FOREIGN KEY(" + Contract.FinishedInspection.INSPECTION_ID + ") REFERENCES " +
                InspectionTable.TABLE_NAME + "(" + InspectionTable._ID + ") " + "ON DELETE CASCADE)";

        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_POSITIONS_TABLE);
        db.execSQL(SQL_CREATE_DELETED_SYNC_TABLE);
        db.execSQL(SQL_CREATE_ROPE_TYPE_TABLE);
        db.execSQL(SQL_CREATE_ROPE_TABLE);
        db.execSQL(CREATE_OUTCOME_CODES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        db.execSQL(SQL_CREATE_ANSWERS_TABLE);
        db.execSQL(CREATE_INSPECTION_TABLE);
        db.execSQL(SQL_CREATE_RESULTS_TABLE);
        db.execSQL(CREATE_FINISHED_RESULTS_TABLE);

        addOutcomeCodeTable();
        addQuestionsTable();
        addAswersTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PositionTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RopeTypeTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RopeTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AnswersTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SyncDeleteTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OutComeCodesTable.TABLE_NAME);
        onCreate(db);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    private void addOutcomeCodeTable() {
        List<OutcomeCodes> outcomeCodes = FillDbNames.fillOutcomeCodeTable();
        for(int i=0; i<outcomeCodes.size(); i++){
            OutcomeCodes outcomeCode = outcomeCodes.get(i);
            addOutcomeCode(outcomeCode);
        }
    }
    private void addQuestionsTable() {
        List<Question> questions = FillDbNames.fillDBWithQuestions();
        for(int i=0; i<questions.size(); i++){
            Question question = questions.get(i);
            addQuestion(question);
        }
    }
    private void addAswersTable() {
        List<Answer> answers = FillDbNames.fillDBWithAnswers();
        for(int i=0; i<answers.size(); i++){
            Answer answer = answers.get(i);
            addAnswer(answer);
        }
    }

    /* METHODS FOR POSITIONS */
    public void addPosition(Position position) {
        PositionDBApi.addPositionDB(getReadableDatabase(), position);
    }
    public boolean addPositionWithID(Position position) {
        return PositionDBApi.addPositionDBWithID(getReadableDatabase(), position);
    }
    public void updatePosition(Position position) {
        PositionDBApi.updatePositionDB(getWritableDatabase(), position);
    }
    public void updatePositionMakeSync(Position position) {
        PositionDBApi.updatePositionMakeSyncDB(getWritableDatabase(), position);
    }
    public void deletePosition(Position position) {
        PositionDBApi.deletePositionDB(getWritableDatabase(), position);
    }
    public List<Position> getAllPositions() {
        List<Position> positionList = PositionDBApi.getAllPositionsDB(getReadableDatabase());
        return positionList;
    }
    public List<Position> getAllPositionsWithStorage() {
        List<Position> positionList = PositionDBApi.getAllPositionsWithStorageDB(getReadableDatabase());
        return  positionList;
    }
    public Position getPositionByName(String name) {
        Position position = PositionDBApi.getPositionByNameDB(getReadableDatabase(), name);
        return  position;
    }
    public Position getPositionById(int id) {
        Position position = PositionDBApi.getPositionByIdDB(getReadableDatabase(), id);
        return  position;
    }
    public List<Position> getAllNonSyncPositions() {
        List<Position> positionList = PositionDBApi.getAllNonSyncPositionsDB(getReadableDatabase());
        return positionList;
    }
    public void updateSyncPosition(Position position) {
        PositionDBApi.updatePositionDBSynced(getWritableDatabase(), position);
    }

    /* METHODS FOR SYNC DELETE */
    public void addObjectToDelete(String type, int key) {
        if(SyncDBApi.objectSynced(getReadableDatabase(), type, key)){
            if(!SyncDBApi.objectExist(getReadableDatabase(), type, key)){
                SyncDBApi.addObjectToDeleteDB(getWritableDatabase(), type, key);
            }
        }
    }
    public void deleteObjectToDelete(int key) {
        SyncDBApi.deleteObjectToDeleteDB(getWritableDatabase(), key);
    }
    public List<DeletedObject> getDeletedObjects() {
        List<DeletedObject> deletedObjects = SyncDBApi.getDeletedObjectsDB(getReadableDatabase());
        return deletedObjects;
    }

    /* METHODS FOR ROPE TYPES */
    public void addRopeType(RopeType ropeType) {
        RopeTypeDBApi.addRopeTypeDB(getWritableDatabase(), ropeType);
    }
    public boolean addRopeTypeWithID(RopeType ropeType) {
        return RopeTypeDBApi.addRopeTypeDBWithID(getWritableDatabase(), ropeType);
    }
    public void updateRopeType(RopeType ropeType) {
        RopeTypeDBApi.updateRopeTypeDB(getWritableDatabase(), ropeType);
    }
    public List<RopeType> getAllRopeTypes() {
        List<RopeType> ropeTypesList =  RopeTypeDBApi.getAllRopeTypesDB(getReadableDatabase());
        return  ropeTypesList;
    }
    public RopeType getRopeTypeById(int id) {
        RopeType type = RopeTypeDBApi.getRopeTypeByIdDB(getReadableDatabase(), id);
        return  type;
    }
    public void deleteRopeType(int ropeType_id) {
        RopeTypeDBApi.deleteRopeTypeDB(getReadableDatabase(), ropeType_id);
        addObjectToDelete(RopeTypeTable.TABLE_NAME, ropeType_id);
    }
    public void updateSyncRopeType(RopeType ropeType) {
        RopeTypeDBApi.updateSyncRopeTypeDB(getWritableDatabase(), ropeType);
    }
    public void updateRopeTypeMakeSync(RopeType ropeType) {
        RopeTypeDBApi.updateRopeTypeMakeSyncDB(getWritableDatabase(), ropeType);
    }
    public List<RopeType> getAllNonSyncRopeTypes() {
        List<RopeType> ropeTypesList =  RopeTypeDBApi.getAllNonSyncRopeTypesDB(getReadableDatabase());
        return  ropeTypesList;
    }

    /* METHODS FOR ROPES */
    public List<Rope> getAllRopesAndTails() {
        List<Rope> ropeList = RopeDBApi.getAllRopesAndTailsDB(getReadableDatabase());
        return  ropeList;
    }
    public List<Rope> getAllRopes() {
        List<Rope> ropeList = RopeDBApi.getAllRopesDB(getReadableDatabase());
        return  ropeList;
    }
    public List<Rope> getAllTails() {
        List<Rope> ropeList = RopeDBApi.getAllTailsDB(getReadableDatabase());
        return  ropeList;
    }
    public Rope getRopeById(int id) {
        Rope rp = RopeDBApi.getRopeByIdDB(getReadableDatabase(), id);
        return  rp;
    }
    public Rope getRopeByName(Rope rope) {
        Rope rp = RopeDBApi.getRopeByNameDB(getReadableDatabase(), rope);
        return  rp;
    }
    public Rope getRopeByPosition(Position position) {
        Rope rope = RopeDBApi.getRopeByPositionDB(getReadableDatabase(), position);
        return  rope;
    }
    public List<Rope> getRopesByPositionForRoutine(Position position) {
        List<Rope> ropeList = RopeDBApi.getRopesByPositionForRoutineDB(getReadableDatabase(), position);
        return  ropeList;
    }
    public List<Rope> getRopesByPositionForDetailed(Position position) {
        List<Rope> ropeList = RopeDBApi.getRopesByPositionForDetailedDB(getReadableDatabase(), position);
        return  ropeList;
    }
    public String getTypeofRope(int type_id) {
        String type = RopeDBApi.getTypeofRopeDB(getReadableDatabase(), type_id);
        return  type;
    }
    public List<Rope> getRopesByRopeType(RopeType type) {
        List<Rope> ropeList = RopeDBApi.getRopesByRopeTypeDB(getReadableDatabase(), type);
        return  ropeList;
    }
    public List<Rope> getAllRopesForRoutine(List<Position> positions) {
        List<Rope> ropeList = RopeDBApi.getAllRopesForRoutineDB(getReadableDatabase(), positions);
        return  ropeList;
    }
    public List<Rope> getAllRopesInUse() {
        List<Rope> ropeList = RopeDBApi.getAllRopesInUseDB(getReadableDatabase());
        return  ropeList;
    }
    public List<Rope> getAllStoredRopes() {
        List<Rope> ropeList = RopeDBApi.getAllStoredRopesDB(getReadableDatabase());
        return  ropeList;
    }
    public List<Rope> getAllNonStoredRopes() {
        List<Rope> ropeList = RopeDBApi.getAllNonStoredRopesDB(getReadableDatabase());
        return  ropeList;
    }
    public Rope findTailByPosition(String positionName) {
        Rope rp = RopeDBApi.findTailByPositionDB(getReadableDatabase(), positionName);
        return  rp;
    }
    public Rope getRopeBySerialNumber(String ropeSerial) {
        Rope rp = RopeDBApi.getRopeBySerialNumberDB(getReadableDatabase(), ropeSerial);
        return  rp;
    };
    public Rope getRopeByTagId(String tag) {
        Rope rp = RopeDBApi.getRopeByTagIdDB(getReadableDatabase(), tag);
        return  rp;
    };
    public void deleteRope(Rope rope) {
        RopeDBApi.deleteRopeDB(getWritableDatabase(), rope);
        addObjectToDelete(RopeTable.TABLE_NAME, rope.getId());
    }
    public void addRope(Rope rope) {
        RopeDBApi.addRopeDB(getWritableDatabase(), rope);
    }
    public boolean addRopeWithID(Rope rope) {
        return RopeDBApi.addRopeDBWithID(getWritableDatabase(), rope);
    }
    public void updateRope(Rope rope) {
        RopeDBApi.updateRopeDB(getWritableDatabase(), rope);
    }
    public void updateRopeMakeSync(Rope rope) {
        RopeDBApi.updateRopeMakeSyncDB(getWritableDatabase(), rope);
    }
    public void updateRopeWorkHours(int rope_id, int work_hours) {
        RopeDBApi.updateRopeWorkHoursDB(getWritableDatabase(), rope_id, work_hours);
    }
    public void updateSyncRopes(Rope rope) {
        RopeDBApi.updateSyncRopeDB(getWritableDatabase(), rope);
    }
    public List<Rope> getAllNonSyncRopes(){
        List<Rope> ropeList = RopeDBApi.getAllNonSyncRopesDB(getReadableDatabase());
        return  ropeList;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* METHODS FOR QUESTIONS */
    public void addQuestion(Question question) {
        QuestionDBApi.addQuestionDB(db, question);
    }
    public List<Question> getAllQuestions() {
        List<Question> questionList = QuestionDBApi.getAllQuestionsDB(getReadableDatabase());
        return  questionList;
    }
    public List<Question> getAllConditionQuestions(boolean hasData, boolean hasNylon) {
        List<Question> questionList = QuestionDBApi.getAllConditionQuestionsDB(getReadableDatabase(), hasData, hasNylon);
        return  questionList;
    }
    public List<Question> getAllHardwareQuestionsForWire() {
        List<Question> questionList = QuestionDBApi.getAllHardwareQuestionsForWireDB(getReadableDatabase());
        return  questionList;
    }
    public List<Question> getAllHardwareQuestionsForSynthetic() {
        List<Question> questionList = QuestionDBApi.getAllHardwareQuestionsForRopeDB(getReadableDatabase());
        return  questionList;
    }
    public List<Question> getAllStorageQuestions() {
        List<Question> questionList = QuestionDBApi.getAllStorageQuestionsDB(getReadableDatabase());
        return  questionList;
    }
    public List<Question> getAllTailQuestions() {
        List<Question> questionList = QuestionDBApi.getAllTailQuestionsDB(getReadableDatabase());
        return  questionList;
    }
    public List<Question> getAllRopeQuestions(boolean hasJacket, boolean hasTail) {
        List<Question> questionList = QuestionDBApi.getAllRopeQuestionsDB(getReadableDatabase(), hasJacket, hasTail);
        return  questionList;
    }
    public List<Question> getAllWireQuestions() {
        List<Question> questionList = QuestionDBApi.getAllWireQuestionsDB(getReadableDatabase());
        return  questionList;
    }
    public List<Question> getAllQuestionsOfRope(Rope rope, String inspectionType, boolean hasJacket, boolean hasTail){
        List<Question> questionList = QuestionDBApi.getAllQuestionsOfRopeDB(getReadableDatabase(), rope, inspectionType, hasJacket, hasTail);
        return  questionList;
    }
//    public Question getQuestionFromResult(Result result) {
//        Question question = QuestionDBApi.getQuestionFromResultDB(getReadableDatabase(), result);
//        return  question;
//    }
    public Question getQuestionById(int id) {
        Question question = QuestionDBApi.getQuestionByIdDB(getReadableDatabase(), id);
        return  question;
    }
    public List<Question> getAllStorageQuestionsOfRope(String material) {
        List<Question> questionList = QuestionDBApi.getAllQuestionsOfRopeInStorageDB(getReadableDatabase(), material);
        return  questionList;
    }
    public List<Question> getAllRopeQuestionsForRoutine(boolean hasJacket, boolean hasTail){
        List<Question> questionList = QuestionDBApi.getAllRopeQuestionsForRoutineDB(getReadableDatabase(), hasJacket, hasTail);
        return  questionList;
    }
    public List<Question> getAllWireQuestionsForRoutine(){
        List<Question> questionList = QuestionDBApi.getAllWireQuestionsForRoutineDB(getReadableDatabase());
        return  questionList;
    }
    public List<Question> getAllTailQuestionsForRoutine(){
        List<Question> questionList = QuestionDBApi.getAllTailQuestionsForRoutineDB(getReadableDatabase());
        return  questionList;
    }
    public List<Question> getAllConditionQuestionsForDetailed(boolean hasData){
        List<Question> questionList = QuestionDBApi.getAllConditionQuestionsForDetailedDB(getReadableDatabase(), hasData);
        return  questionList;
    }

    /* METHODS FOR ANSWERS */
    public void addAnswer(Answer answer) {
        AnswerDBApi.addAnswerDB(db, answer);
    }
    public List<Answer> getAllAnswersOfQuestion(Question question) {
        List<Answer> answerList = AnswerDBApi.getAllAnswersOfQuestionDB(getReadableDatabase(), question);
        return  answerList;
    }
    public Answer getAllAnswerById(int id) {
        Answer answer = AnswerDBApi.getAllAnswerByIdDB(getReadableDatabase(), id);
        return  answer;
    }

    /* METHODS FOR USERS */
    public void addUser(User user) {
        UserDBApi.addUserDB(getWritableDatabase(), user);
    }
    public void updateUser(User user) {
        UserDBApi.updateUserDB(getWritableDatabase(), user);
    }
    public User getUser() {
        User user = UserDBApi.getUserDB(getReadableDatabase());
        return  user;
    }
    public User verifyUser(String username, String deviceInfo) {
        User user = UserDBApi.verifyUserDB(getReadableDatabase(), username, deviceInfo);
        return  user;
    }
    public void deleteUser(String username) {
        UserDBApi.deleteUserDB(getWritableDatabase(), username);
    }
    public void deleteUsers(){
        UserDBApi.deleteUsersDB(getWritableDatabase());
    }

    /* METHODS FOR INSPECTIONS */
    public List<Inspection> getAllInspections() {
        List<Inspection> inspectionList = InspectionDBApi.getAllInspectionsDB(getReadableDatabase());
        return  inspectionList;
    }
    public Inspection getInspectionById(int id) {
        Inspection inspection = InspectionDBApi.getInspectionByIdDB(getReadableDatabase(), id);
        return  inspection;
    }
    public Inspection getLastInsertedInspection(){
        Inspection inspection = InspectionDBApi.getLastInsertedInspectionDB(getReadableDatabase());
        return  inspection;
    }
    public Inspection getLastInsertedInspectionByPosition(int positionID){
        Inspection inspection = InspectionDBApi.getLastInsertedInspectionInPositionDB(getReadableDatabase(), positionID);
        return  inspection;
    }
    public void addInspection(Inspection inspection) {
        InspectionDBApi.addInspectionDB(getWritableDatabase(), inspection);
    }
    public boolean addInspectionWithID(Inspection inspection) {
        return InspectionDBApi.addInspectionDBWithID(getWritableDatabase(), inspection);
    }
    public void updateInspection(Inspection inspection) {
        InspectionDBApi.updateInspectionDB(getWritableDatabase(), inspection);
    }
    public List<Inspection> getAllInspectionsInPosition(Position position) {
        List<Inspection> inspections = InspectionDBApi.getAllInspectionsInPositionDB(getReadableDatabase(), position.getId());
        return inspections;
    }
    public List<Inspection> getAllInspectionsOfUnsynced(List<FinishedResults> unsyncedResults){
        List<Inspection> inspections = InspectionDBApi.getAllInspectionsOfUnsyncedDB(getReadableDatabase(),  unsyncedResults);
        return inspections;
    }

    /* METHODS FOR OUTCOME CODES TABLE */
    public void addOutcomeCode(OutcomeCodes code) {
        ContentValues cv = new ContentValues();
        cv.put(OutComeCodesTable.SEVERITY, code.getSeverity());
        cv.put(OutComeCodesTable.CODE_ID, code.getCode_id());
        cv.put(OutComeCodesTable.FRIENDLY_TEXT, code.getFriendlyText());
        cv.put(OutComeCodesTable.TYPE, code.getType());
        db.insert(OutComeCodesTable.TABLE_NAME, null, cv);
    }
    @SuppressLint("Range")
    public OutcomeCodes getOutcomeCode(String code) {
        OutcomeCodes cd = new OutcomeCodes();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + OutComeCodesTable.TABLE_NAME + " WHERE " + OutComeCodesTable.CODE_ID + "=?", new String[] {code});

        if(c.moveToFirst()) {
            do {
                cd.setId(c.getInt(c.getColumnIndex(OutComeCodesTable._ID)));
                cd.setSeverity(c.getInt(c.getColumnIndex(OutComeCodesTable.SEVERITY)));
                cd.setCode_id(c.getString(c.getColumnIndex(OutComeCodesTable.CODE_ID)));
                cd.setFriendlyText(c.getString(c.getColumnIndex(OutComeCodesTable.FRIENDLY_TEXT)));
                cd.setType(c.getString(c.getColumnIndex(OutComeCodesTable.TYPE)));
            } while(c.moveToNext());
        }
        c.close();
        return  cd;
    }

    /* METHODS FOR RESULTS */
    public void addResult(Result result) {
        ResultsDBApi.addResultDB(getReadableDatabase(), result);
    }
    public boolean addResultWithID(Result result) {
       return ResultsDBApi.addResultDBWithID(getReadableDatabase(), result);
    }
    public void updateResult(Result result){
        ResultsDBApi.updateResultDB(getWritableDatabase(), result);
    }
    public List<Result> getAllResultsOfInspection(int inspectionID) {
        List<Result> resultList = new ArrayList<>();
        resultList = ResultsDBApi.getAllResultsOfInspectionDB(getReadableDatabase(), inspectionID);
        return  resultList;
    }
    public List<Result> getAllResultsOfNonSyncedInspections(List<Inspection> inspections) {
        List<Result> results = new ArrayList<>();
        results = ResultsDBApi.getAllResultsOfNonSyncedInspectionsDB(getReadableDatabase(), inspections);
        return results;
    }
    public Result getResult(int inspectionID, int positionID, int questionID) {
        Result result = new Result();
        result = ResultsDBApi.getResultDB(getReadableDatabase(), inspectionID, positionID, questionID);
        return  result;
    }
    @SuppressLint("Range")
    public List<Result> getAllResultsOfStorage(int inspection_id, int position_id) {
        List<Result> results = new ArrayList<>();
        db = getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + Contract.ResultsTable.TABLE_NAME + " r INNER JOIN " + QuestionsTable.TABLE_NAME +
                " q ON q." + QuestionsTable._ID + " = r." + ResultsTable.QUESTION + " WHERE q." + QuestionsTable.TEST +
                " = \"STORAGE\" AND r." + ResultsTable.INSPECTION + " = ? AND " + ResultsTable.POSITION + " = ?";

        Cursor c = db.rawQuery(sqlQuery, new String[] {String.valueOf(inspection_id), String.valueOf(position_id)});
        if(c.moveToFirst()) {
            do {
                Result result = new Result();
                result.setId(c.getInt(c.getColumnIndex(ResultsTable._ID)));
                result.setInspectionId(c.getInt(c.getColumnIndex(ResultsTable.INSPECTION)));
                result.setPositionId(c.getInt(c.getColumnIndex(ResultsTable.POSITION)));
                result.setQuestionId(c.getInt(c.getColumnIndex(ResultsTable.QUESTION)));
                result.setAnswerId(c.getInt(c.getColumnIndex(ResultsTable.ANSWER)));
                result.setRopeId(c.getInt(c.getColumnIndex(ResultsTable.ROPE)));
                result.setGlobal(c.getInt(c.getColumnIndex(ResultsTable.IS_GLOBAL)) > 0 ? true : false);
                results.add(result);
            } while(c.moveToNext());
        }
        c.close();
        return results;
    }
    @SuppressLint("Range")
    public List<Result> getAllResultsOfRope(int inspection_id, int position_id, int rope_id) {
        List<Result> results = ResultsDBApi.getAllResultsOfRopeDB(getReadableDatabase(), inspection_id, position_id, rope_id);
        return results;
    }
    @SuppressLint("Range")
    public List<Rope> getRopesFromResultsInPosition(int inspectionID, int positionID){
        List<Rope> ropes = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT rp.* FROM " + ResultsTable.TABLE_NAME + " res INNER JOIN " + RopeTable.TABLE_NAME +
                        " rp ON rp." + RopeTable._ID + " = res." + ResultsTable.ROPE + " WHERE res." +
                        ResultsTable.INSPECTION + " = ? AND res." + ResultsTable.POSITION + " = ? GROUP BY rp." + RopeTable.SERIAL_NUMBER,
                new String[] {String.valueOf(inspectionID), String.valueOf(positionID)});

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(RopeTable.TYPE)));
                ropes.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropes;
    }
    @SuppressLint("Range")
    public List<Position> getPositionsFromResults(int inspectionID) {
        List<Position> positions = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT pos.* FROM " + ResultsTable.TABLE_NAME + " res INNER JOIN " + PositionTable.TABLE_NAME +
                        " pos ON pos." + PositionTable._ID + " = res." + ResultsTable.POSITION + " WHERE res." +
                        ResultsTable.INSPECTION + " = ? GROUP BY pos." + PositionTable.NAME,
                new String[] {String.valueOf(inspectionID)});

        if(c.moveToFirst()) {
            do {
                Position position = new Position();
                position.setId(c.getInt(c.getColumnIndex(PositionTable._ID)));
                position.setName(c.getString(c.getColumnIndex(PositionTable.NAME)));
                position.setX(c.getInt(c.getColumnIndex(PositionTable.X)));
                position.setY(c.getInt(c.getColumnIndex(PositionTable.Y)));
                positions.add(position);
            } while(c.moveToNext());
        }
        c.close();
        return  positions;
    }
    @SuppressLint("Range")
    public Position getPositionOfQuestionFromResults(int inspectionID, Question question) {
        Position position = new Position();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT pos.* FROM " + ResultsTable.TABLE_NAME + "res INNER JOIN " + PositionTable.TABLE_NAME +
                        "pos ON pos." + PositionTable._ID + " = res." + ResultsTable.POSITION + " WHERE res." +
                        ResultsTable.INSPECTION + " = ? AND res." + ResultsTable.QUESTION + " = ?",
                new String[] {String.valueOf(inspectionID), String.valueOf(question.getId())});

        if(c.moveToFirst()) {
            do {
                position.setId(c.getInt(c.getColumnIndex(PositionTable._ID)));
                position.setName(c.getString(c.getColumnIndex(PositionTable.NAME)));
                position.setX(c.getInt(c.getColumnIndex(PositionTable.X)));
                position.setY(c.getInt(c.getColumnIndex(PositionTable.Y)));
            } while(c.moveToNext());
        }
        c.close();
        return  position;
    }
    @SuppressLint("Range")
    public  List<Answer> getAnswersOfRopeFromResults(int inspectionID, Position position, Rope rope) {
        List<Answer> answers = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT a.* FROM " + ResultsTable.TABLE_NAME + " res INNER JOIN " + AnswersTable.TABLE_NAME +
                        " a ON a." + AnswersTable._ID + " = res." + ResultsTable.ANSWER + " WHERE res." +
                        ResultsTable.INSPECTION + " = ? AND res." + ResultsTable.POSITION + " = ? AND res." +
                        ResultsTable.ROPE + " = ? ",
                new String[] {String.valueOf(inspectionID), String.valueOf(position.getId()), String.valueOf(rope.getId())});

        if(c.moveToFirst()) {
            do {
                Answer answer = new Answer();
                answer.setId(c.getInt(c.getColumnIndex(AnswersTable._ID)));
                answer.setHeader(c.getString(c.getColumnIndex(AnswersTable.HEADER)));
                answer.setDetails(c.getString(c.getColumnIndex(AnswersTable.DETAILS)));
                answer.setRecommendations(c.getString(c.getColumnIndex(AnswersTable.RECOMMENDATIONS)));
                answer.setQuestion_id(c.getInt(c.getColumnIndex(AnswersTable.QUESTION_ID)));
                answer.setValue(c.getInt(c.getColumnIndex(AnswersTable.VALUE)));
                answer.setImage1(c.getString(c.getColumnIndex(AnswersTable.IMAGE1)));
                answers.add(answer);
            } while(c.moveToNext());
        }
        c.close();
        return  answers;
    }
    /* FUNCTIONS FOR SEVERITY CODES */
    @SuppressLint("Range")
    public Severity getSeverityByCode(String code) {
        Severity severity = new Severity();
        db = getReadableDatabase();
        String query = "SELECT * FROM " + OutComeCodesTable.TABLE_NAME + " WHERE " + OutComeCodesTable.CODE_ID + " = ? ";
        Cursor c = db.rawQuery(query, new String[] {code});

        if(c.moveToFirst()) {
            do {
                severity.setId(c.getInt(c.getColumnIndex(OutComeCodesTable._ID)));
                severity.setSeverity_level(c.getInt(c.getColumnIndex(OutComeCodesTable.SEVERITY)));
                severity.setCode(c.getString(c.getColumnIndex(OutComeCodesTable.CODE_ID)));
                severity.setFriendly_text(c.getString(c.getColumnIndex(OutComeCodesTable.FRIENDLY_TEXT)));
                severity.setType(c.getString(c.getColumnIndex(OutComeCodesTable.TYPE)));
            } while(c.moveToNext());
        }
        c.close();
        return severity;
    }

    /* FUNCTIONS FOR FINISHED RESULTS */
    public  List<FinishedResults> getFinishedResults() {
        List<FinishedResults> finishedResults = FinishedResultsDBApi.getFinishedResultsDB(getReadableDatabase());
        return finishedResults;
    }
    public  List<FinishedResults> getUnsyncedFinishedResults() {
        List<FinishedResults> finishedResults = FinishedResultsDBApi.getUnSyncedFinishedResultsDB(getReadableDatabase());
        return finishedResults;
    }
    public  FinishedResults getFinishedResultByInspection(int inspection_id) {
        FinishedResults finishedResult = FinishedResultsDBApi.getFinishedResultByInspectionDB(getReadableDatabase(), inspection_id);
        return finishedResult;
    }
    public void addFinishedResult(FinishedResults finishedResult) {
        FinishedResultsDBApi.addFinishedResultDB(getWritableDatabase(), finishedResult);
    }
    public boolean addFinishedResultWithID(FinishedResults finishedResult) {
        return FinishedResultsDBApi.addFinishedResultDBWithID(getWritableDatabase(), finishedResult);
    }
    public void updateFinishedResult(FinishedResults finishedResult) {
        FinishedResultsDBApi.updateFinishedResultDB(getWritableDatabase(), finishedResult);
    }
    public FinishedResults getLastFinishedResult() {
        FinishedResults finishedResults = FinishedResultsDBApi.getLastFinishedResultDB(getReadableDatabase());
        return finishedResults;
    }
    public FinishedResults getLastFinishedResultForPosition(Position position) {
        FinishedResults finishedResults = FinishedResultsDBApi.getLastFinishedResultForPositionDB(getReadableDatabase(), position);
        return finishedResults;
    }
}


