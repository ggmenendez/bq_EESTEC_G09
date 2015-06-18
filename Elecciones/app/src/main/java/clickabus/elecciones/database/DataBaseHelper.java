package clickabus.elecciones.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import clickabus.elecciones.database.DataBaseContract.*;
import clickabus.elecciones.model.AdministrativeArea;
import clickabus.elecciones.model.Answer;
import clickabus.elecciones.model.Association;
import clickabus.elecciones.model.Party;
import clickabus.elecciones.model.PartyAdminArea;
import clickabus.elecciones.model.Question;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper{

    /**
     * Variables útiles.
     */
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String FOREIGN = " FOREIGN KEY(";
    private static final String REFERENCE = ") REFERENCES ";
    private static final String COMMA_SEP = ",";

    /**
     * Sintaxis creación y borrado de tabla Preguntas.
     */
    private static final String SQL_CREATE_QUESTIONS =
            "CREATE TABLE " + QuestionsTable.TABLE_NAME + " (" +
                    QuestionsTable.COLUMN_NAME_QUESTION_ID + " INTEGER PRIMARY KEY," +
                    QuestionsTable.COLUMN_NAME_QUESTION + TEXT_TYPE + " )";

    private static final String SQL_DELETE_QUESTIONS =
            "DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME;

    /**
     * Sintaxis creación y borrado de tabla Comunidades.
     */
    private static final String SQL_CREATE_ADMIN_AREAS =
            "CREATE TABLE " + AdministrativeAreaTable.TABLE_NAME + " (" +
                    AdministrativeAreaTable.COLUMN_NAME_ADMIN_AREA_ID + " INTEGER PRIMARY KEY," +
                    AdministrativeAreaTable.COLUMN_NAME_ADMIN_AREA_NAME + TEXT_TYPE + COMMA_SEP +
                    AdministrativeAreaTable.COLUMN_NAME_ADMIN_AREA_SHORT_NAME + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ADMIN_AREAS =
            "DROP TABLE IF EXISTS " + AdministrativeAreaTable.TABLE_NAME;

    /**
     * Sintaxis creación y borrado de tabla Asociaciones Partido Comunidad.
     */
    private static final String SQL_CREATE_PARTY_AREA =
            "CREATE TABLE " + PartyAreaAssociationTable.TABLE_NAME + " (" +
                    PartyAreaAssociationTable.COLUMN_NAME_ADMIN_PARTY_ID + " INTEGER," +
                    PartyAreaAssociationTable.COLUMN_NAME_ADMIN_AREA_ID + INTEGER_TYPE + COMMA_SEP +
                    PartyAreaAssociationTable.COLUMN_NAME_ADMIN_SCORE + INTEGER_TYPE + " )";

    private static final String SQL_DELETE_PARTY_AREA =
            "DROP TABLE IF EXISTS " + PartyAreaAssociationTable.TABLE_NAME;
    /**
     * Sintaxis creación y borrado de tabla Respuestas.
     */
    private static final String SQL_CREATE_ANSWERS =
            "CREATE TABLE " + AnswersTable.TABLE_NAME + " (" +
                    AnswersTable.COLUMN_NAME_ANSWER_ID + " INTEGER PRIMARY KEY," +
                    AnswersTable.COLUMN_NAME_QUESTION_ID + INTEGER_TYPE + COMMA_SEP +
                    AnswersTable.COLUMN_NAME_ANSWER + TEXT_TYPE + COMMA_SEP +
                    FOREIGN + AnswersTable.COLUMN_NAME_QUESTION_ID + REFERENCE
                    + QuestionsTable.TABLE_NAME + "(" + QuestionsTable.COLUMN_NAME_QUESTION_ID + ")" +" )";

    private static final String SQL_DELETE_ANSWERS =
            "DROP TABLE IF EXISTS " + AnswersTable.TABLE_NAME;


    /**
     * Sintaxis creación y borrado de tabla Partidos.
     */
    private static final String SQL_CREATE_PARTIES =
            "CREATE TABLE " + PartiesTable.TABLE_NAME + " (" +
                    PartiesTable.COLUMN_NAME_PARTY_ID + " INTEGER PRIMARY KEY," +
                    PartiesTable.COLUMN_NAME_PARTY_NAME + TEXT_TYPE + COMMA_SEP +
                    PartiesTable.COLUMN_NAME_PARTY_SCORE + INTEGER_TYPE + COMMA_SEP +
                    PartiesTable.COLUMN_NAME_PARTY_TOTAL_SCORE + INTEGER_TYPE + " )";

    private static final String SQL_DELETE_PARTIES =
            "DROP TABLE IF EXISTS " + PartiesTable.TABLE_NAME;

    /**
     * Sintaxis creación y borrado de tabla Respuestas-Partido.
     */
    private static final String SQL_CREATE_ANS_PAR =
            "CREATE TABLE " + AnswersPartiesTable.TABLE_NAME + " (" +
                    AnswersPartiesTable.COLUMN_NAME_ANSWER_ID + INTEGER_TYPE + COMMA_SEP +
                    AnswersPartiesTable.COLUMN_NAME_PARTY_ID + INTEGER_TYPE + COMMA_SEP +
                    AnswersPartiesTable.COLUMN_NAME_SCORE + INTEGER_TYPE + COMMA_SEP +
                    FOREIGN + AnswersPartiesTable.COLUMN_NAME_ANSWER_ID + REFERENCE +
                    AnswersTable.TABLE_NAME + "(" + AnswersTable.COLUMN_NAME_ANSWER_ID + ")" + COMMA_SEP +
                    FOREIGN + AnswersPartiesTable.COLUMN_NAME_PARTY_ID + REFERENCE +
                    PartiesTable.TABLE_NAME + "(" + PartiesTable.COLUMN_NAME_PARTY_ID + ")" + " )";

    private static final String SQL_DELETE_ANS_PAR =
            "DROP TABLE IF EXISTS " + AnswersPartiesTable.TABLE_NAME;

    /**
     * Sintaxis creación y borrado de tabla Version.
     */
    private static final String SQL_CREATE_VERSION =
            "CREATE TABLE " + VersionTable.TABLE_NAME + " (" +
                    VersionTable.COLUMN_NAME_VERSION + INTEGER_TYPE + " DEFAULT 0 )";

    private static final String SQL_DELETE_VERSION =
            "DROP TABLE IF EXISTS " + VersionTable.TABLE_NAME;

    // Si cambias el esquema de la base de datos, hay que cambiar la versión.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Elecciones.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Ejecuto las sentencias para crear las tablas.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_QUESTIONS);
        db.execSQL(SQL_CREATE_ANSWERS);
        db.execSQL(SQL_CREATE_PARTIES);
        db.execSQL(SQL_CREATE_ANS_PAR);
        db.execSQL(SQL_CREATE_VERSION);
        db.execSQL(SQL_CREATE_ADMIN_AREAS);
        db.execSQL(SQL_CREATE_PARTY_AREA);
    }

    // En caso de haber actualización de la bbdd.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_QUESTIONS);
        db.execSQL(SQL_DELETE_ANSWERS);
        db.execSQL(SQL_DELETE_PARTIES);
        db.execSQL(SQL_DELETE_ANS_PAR);
        db.execSQL(SQL_DELETE_VERSION);
        db.execSQL(SQL_DELETE_ADMIN_AREAS);
        db.execSQL(SQL_DELETE_PARTY_AREA);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Añade todas las preguntas a la base de datos.
     * @param questions: Array con todas las preguntas.
     */
    public void addQuestions(ArrayList<Question> questions){

        // Obtiene la base de datos en modo escritura.
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // Recorro array con todas las preguntas.
        for(Question q : questions){
            // Cargo los valores con los datos de la pregunta.
            values.put(QuestionsTable.COLUMN_NAME_QUESTION_ID, q.getQuestionId());
            values.put(QuestionsTable.COLUMN_NAME_QUESTION, q.getQuestion());

            // Los inserto en la tabla.
            db.insert(
                    QuestionsTable.TABLE_NAME, // Nombre de la tabla donde los quiero insertar.
                    null, // Name of a column in which the framework can insert NULL in the event that the ContentValues is empty
                    values); // Valores a insertar.
        }
    }

    /**
     * Añade todas las respuestas a la base de datos.
     * @param answers: Array con todas las respuestas.
     */
    public void addAnswers(ArrayList<Answer> answers){
        // Obtiene la base de datos en modo escritura.
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // Recorro array con todas las respuestas.
        for(Answer a : answers){
            // Cargo los valores con los datos de la respuesta.
            values.put(AnswersTable.COLUMN_NAME_ANSWER_ID, a.getAnswerId());
            values.put(AnswersTable.COLUMN_NAME_QUESTION_ID, a.getQuestionId());
            values.put(AnswersTable.COLUMN_NAME_ANSWER, a.getAnswer());

            // Los inserto en la tabla.
            db.insert(
                    AnswersTable.TABLE_NAME, // Nombre de la tabla donde los quiero insertar.
                    null, // Name of a column in which the framework can insert NULL in the event that the ContentValues is empty
                    values); // Valores a insertar.
        }
    }
    /**
     * Añade todos los partidos a la base de datos.
     * @param parties: Array de partidos.
     */
    public void addParties(ArrayList<Party> parties){
        // Obtiene la base de datos en modo escritura.
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // Recorro array con todos los partidos.
        for(Party p : parties){
            // Cargo los valores con los datos del partido.
            values.put(PartiesTable.COLUMN_NAME_PARTY_ID, p.getPartyId());
            values.put(PartiesTable.COLUMN_NAME_PARTY_NAME, p.getPartyName());
            values.put(PartiesTable.COLUMN_NAME_PARTY_TOTAL_SCORE, p.getPartyScore());

            // Los inserto en la tabla.
            db.insert(
                PartiesTable.TABLE_NAME, // Nombre de la tabla donde los quiero insertar.
                null, // Name of a column in which the framework can insert NULL in the event that the ContentValues is empty
                values); // Valores a insertar.
        }

    }

    /**
     * Añade todas las asociaciones respuesta-partido a la base de datos.
     * @param associations: Array con todas las asociaciones.
     */
    public void addAssociation(ArrayList<Association> associations){
        // Obtiene la base de datos en modo escritura.
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // Recorro array con todas las asociaciones.
        for(Association a : associations){
            // Cargo los valores con los datos de la asociación.
            values.put(AnswersPartiesTable.COLUMN_NAME_ANSWER_ID, a.getAnswerId());
            values.put(AnswersPartiesTable.COLUMN_NAME_PARTY_ID, a.getPartyId());
            values.put(AnswersPartiesTable.COLUMN_NAME_SCORE, a.getScore());

            // Los inserto en la tabla.
            db.insert(
                    AnswersPartiesTable.TABLE_NAME, // Nombre de la tabla donde los quiero insertar.
                    null, // Name of a column in which the framework can insert NULL in the event that the ContentValues is empty
                    values); // Valores a insertar.
        }
    }

    /**
     * Añade las comunidades autónomas a la base de datos.
     * @param admin_areas
     */
    public void addAdminArea(ArrayList<AdministrativeArea> admin_areas){
        // Obtiene la base de datos en modo escritura.
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // Recorro array con todas las asociaciones.
        for(AdministrativeArea a : admin_areas){
            // Cargo los valores con los datos de la asociación.
            values.put(AdministrativeAreaTable.COLUMN_NAME_ADMIN_AREA_ID, a.getAdminAreaId());
            values.put(AdministrativeAreaTable.COLUMN_NAME_ADMIN_AREA_NAME, a.getName());
            values.put(AdministrativeAreaTable.COLUMN_NAME_ADMIN_AREA_SHORT_NAME, a.getShortName());

            // Los inserto en la tabla.
            db.insert(
                    AdministrativeAreaTable.TABLE_NAME, // Nombre de la tabla donde los quiero insertar.
                    null, // Name of a column in which the framework can insert NULL in the event that the ContentValues is empty
                    values); // Valores a insertar.
        }
    }


    public void addPartyArea(ArrayList<PartyAdminArea> partyAdminAreas){
        // Obtiene la base de datos en modo escritura.
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        // Recorro array con todas las asociaciones.
        for(PartyAdminArea pa : partyAdminAreas){
            // Cargo los valores con los datos de la asociación.
            values.put(PartyAreaAssociationTable.COLUMN_NAME_ADMIN_PARTY_ID, pa.getPartyId());
            values.put(PartyAreaAssociationTable.COLUMN_NAME_ADMIN_AREA_ID, pa.getAreaId());
            values.put(PartyAreaAssociationTable.COLUMN_NAME_ADMIN_SCORE, pa.getScore());

            // Los inserto en la tabla.
            db.insert(
                    PartyAreaAssociationTable.TABLE_NAME, // Nombre de la tabla donde los quiero insertar.
                    null, // Name of a column in which the framework can insert NULL in the event that the ContentValues is empty
                    values); // Valores a insertar.
        }
    }

    public void addVersion(int version){
        // Obtiene la base de datos en modo escritura.
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(VersionTable.COLUMN_NAME_VERSION, version);

        db.insert(VersionTable.TABLE_NAME,
                null,
                values);

    }

    public int getDatabaseVersion(){
        // Obtiene la base de datos en modo lectura.
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + VersionTable.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if(c.getCount()<=0){
            c.close();
            return 0;
        }
        int version = c.getInt(c.getColumnIndexOrThrow(VersionTable.COLUMN_NAME_VERSION));
        c.close();
        return version;
    }


    public ArrayList<PartyAdminArea> getPartyAreaAssociations(){
        // Obtiene la base de datos en modo lectura.
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<PartyAdminArea> partyAdminAreas = new ArrayList<>();

        String query = "SELECT * FROM " + PartyAreaAssociationTable.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        do{
            PartyAdminArea pa = new PartyAdminArea(c.getInt(c.getColumnIndexOrThrow(PartyAreaAssociationTable.COLUMN_NAME_ADMIN_PARTY_ID)),
                    c.getInt(c.getColumnIndexOrThrow(PartyAreaAssociationTable.COLUMN_NAME_ADMIN_AREA_ID)),
                    c.getInt(c.getColumnIndexOrThrow(PartyAreaAssociationTable.COLUMN_NAME_ADMIN_SCORE)));
            partyAdminAreas.add(pa);
        }while(c.moveToNext());
        c.close();
        return partyAdminAreas;
    }

    public ArrayList<Question> getQuestions(){
        // Obtiene la base de datos en modo lectura.
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Question> questions = new ArrayList<>();

        String query = "SELECT * FROM " + QuestionsTable.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        do{
           Question q = new Question(c.getInt(c.getColumnIndexOrThrow(QuestionsTable.COLUMN_NAME_QUESTION_ID)),
                   c.getString(c.getColumnIndexOrThrow(QuestionsTable.COLUMN_NAME_QUESTION)));
           questions.add(q);
        }while(c.moveToNext());
        c.close();
        return questions;
    }

    public ArrayList<Answer> getAnswers(){
        // Obtiene la base de datos en modo lectura.
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Answer> answers = new ArrayList<>();

        String query = "SELECT * FROM " + AnswersTable.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        do{
            Answer a = new Answer(c.getInt(c.getColumnIndexOrThrow(AnswersTable.COLUMN_NAME_ANSWER_ID)),
                    c.getInt(c.getColumnIndexOrThrow(AnswersTable.COLUMN_NAME_QUESTION_ID)),
                    c.getString(c.getColumnIndexOrThrow(AnswersTable.COLUMN_NAME_ANSWER)));
            answers.add(a);
        }while(c.moveToNext());
        c.close();
        return answers;
    }

    public ArrayList<Party> getParties(){
        // Obtiene la base de datos en modo lectura.
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Party> parties = new ArrayList<>();

        String query = "SELECT * FROM " + PartiesTable.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        do{
            Party p = new Party(c.getInt(c.getColumnIndexOrThrow(PartiesTable.COLUMN_NAME_PARTY_ID)),
                    c.getString(c.getColumnIndexOrThrow(PartiesTable.COLUMN_NAME_PARTY_NAME)),
                    c.getInt(c.getColumnIndexOrThrow(PartiesTable.COLUMN_NAME_PARTY_SCORE)));
            parties.add(p);
        }while(c.moveToNext());
        c.close();
        return parties;
    }

    public ArrayList<Association> getAssociations(){
        // Obtiene la base de datos en modo lectura.
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Association> associations = new ArrayList<>();

        String query = "SELECT * FROM " + AnswersPartiesTable.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        do{
            Association a = new Association(c.getInt(c.getColumnIndexOrThrow(AnswersPartiesTable.COLUMN_NAME_ANSWER_ID)),
                    c.getInt(c.getColumnIndexOrThrow(AnswersPartiesTable.COLUMN_NAME_PARTY_ID)),
                    c.getInt(c.getColumnIndexOrThrow(AnswersPartiesTable.COLUMN_NAME_SCORE)));
            associations.add(a);
        }while(c.moveToNext());
        c.close();
        return associations;
    }

    public ArrayList<AdministrativeArea> getAdminAreas(){
        // Obtiene la base de datos en modo lectura.
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<AdministrativeArea> admin_areas = new ArrayList<>();

        String query = "SELECT * FROM " + AdministrativeAreaTable.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        do{
            AdministrativeArea admin_area = new AdministrativeArea((c.getInt(c.getColumnIndexOrThrow(AdministrativeAreaTable.COLUMN_NAME_ADMIN_AREA_ID))),
                    c.getString(c.getColumnIndexOrThrow(AdministrativeAreaTable.COLUMN_NAME_ADMIN_AREA_NAME)),
                    c.getString(c.getColumnIndexOrThrow(AdministrativeAreaTable.COLUMN_NAME_ADMIN_AREA_SHORT_NAME)));
            admin_areas.add(admin_area);
        }while(c.moveToNext());
        c.close();
        return admin_areas;
    }

    public void resetTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "DELETE FROM " + VersionTable.TABLE_NAME;
        String selectQuery2 = "DELETE FROM " + PartiesTable.TABLE_NAME;
        String selectQuery3 = "DELETE FROM " + AnswersTable.TABLE_NAME;
        String selectQuery4 = "DELETE FROM " + QuestionsTable.TABLE_NAME;
        String selectQuery5 = "DELETE FROM " + AnswersPartiesTable.TABLE_NAME;
        String selectQuery6 = "DELETE FROM " + AdministrativeAreaTable.TABLE_NAME;
        String selectQuery7 = "DELETE FROM " + PartyAreaAssociationTable.TABLE_NAME;
        db.execSQL(selectQuery);
        db.execSQL(selectQuery2);
        db.execSQL(selectQuery3);
        db.execSQL(selectQuery4);
        db.execSQL(selectQuery5);
        db.execSQL(selectQuery6);
        db.execSQL(selectQuery7);
    }

}
