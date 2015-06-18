package clickabus.elecciones.database;

import android.provider.BaseColumns;

public final class DataBaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.

    public DataBaseContract() {}

    /* Inner class that defines the table contents */
    public static abstract class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "preguntas";
        public static final String COLUMN_NAME_QUESTION_ID = "id_pregunta";
        public static final String COLUMN_NAME_QUESTION = "pregunta";
    }

    /* Inner class that defines the table contents */
    public static abstract class AnswersTable implements BaseColumns {
        public static final String TABLE_NAME = "respuestas";
        public static final String COLUMN_NAME_ANSWER_ID = "id_respuesta";
        public static final String COLUMN_NAME_QUESTION_ID = "id_pregunta";
        public static final String COLUMN_NAME_ANSWER = "respuesta";
    }

    /* Inner class that defines the table contents */
    public static abstract class PartiesTable implements BaseColumns {
        public static final String TABLE_NAME = "partidos";
        public static final String COLUMN_NAME_PARTY_ID = "id_partido";
        public static final String COLUMN_NAME_PARTY_NAME = "nombre";
        public static final String COLUMN_NAME_PARTY_SCORE = "puntuacion";
        public static final String COLUMN_NAME_PARTY_TOTAL_SCORE = "puntuacion_total";
    }

    /* Inner class that defines the table contents */
    public static abstract class AnswersPartiesTable implements BaseColumns {
        public static final String TABLE_NAME = "asociaciones";
        public static final String COLUMN_NAME_PARTY_ID = "id_partido";
        public static final String COLUMN_NAME_ANSWER_ID = "id_respuesta";
        public static final String COLUMN_NAME_SCORE = "puntuacion";
    }

    /* Inner class that defines the table contents */
    public static abstract class AdministrativeAreaTable implements BaseColumns {
        public static final String TABLE_NAME = "comunidades";
        public static final String COLUMN_NAME_ADMIN_AREA_ID = "id_comunidad";
        public static final String COLUMN_NAME_ADMIN_AREA_NAME = "nombre";
        public static final String COLUMN_NAME_ADMIN_AREA_SHORT_NAME = "siglas";
    }

    /* Inner class that defines the table contents */
    public static abstract class PartyAreaAssociationTable implements BaseColumns {
        public static final String TABLE_NAME = "partidos_comunidades";
        public static final String COLUMN_NAME_ADMIN_AREA_ID = "id_comunidad";
        public static final String COLUMN_NAME_ADMIN_PARTY_ID = "id_partido";
        public static final String COLUMN_NAME_ADMIN_SCORE = "puntuacion";
    }

    /* Inner class that defines the table contents */
    public static abstract class VersionTable implements BaseColumns {
        public static final String TABLE_NAME = "version";
        public static final String COLUMN_NAME_VERSION = "version";
    }

}
