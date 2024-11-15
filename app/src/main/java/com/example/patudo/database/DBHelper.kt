package com.example.patudo.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.patudo.model.ContactModel

class DBHelper(context: Context) : SQLiteOpenHelper(context, "patudo.db", null, 1) {

    companion object {
        private const val DATABASE_NAME = "patudo.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CONTACTS = "contatos"

        // Colunas
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOME = "nome"
        private const val COLUMN_ENDERECO = "endereco"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_TELEFONE = "telefone"
        private const val COLUMN_IMAGE_ID = "imageId"
    }

    /* val sql = arrayOf(
         "CREATE TABLE contatos (Id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, endereco TEXT, email TEXT, telefone INTEGER, imageId INTEGER)",
         "INSERT INTO contatos (nome, endereco, email, telefone, imageId) VALUES ('Canil Gatil Municipal de Ourém', 'MCG8+PG, Ourém', 'jaquelina.simoes@mail.cm-ourem.pt', 917223546, 1)",
         "INSERT INTO contatos (nome, endereco, email, telefone, imageId) VALUES ('Encosta dos Carvalhos', 'P8J2+8M, Caranguejeira', 'pousadacanina@goldpet.pt', 910970416, 2)",
         "INSERT INTO contatos (nome, endereco, email, telefone, imageId) VALUES ('Domus Pet Shop', 'MC4C+8G, Ourém', 'susanatorcatof@gmail.com', 919707509, 3)"
     )*/

    private val CREATE_TABLE_CONTACTS = """
    CREATE TABLE $TABLE_CONTACTS (
        $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
        $COLUMN_NOME TEXT NOT NULL,
        $COLUMN_ENDERECO TEXT,
        $COLUMN_EMAIL TEXT,
        $COLUMN_TELEFONE INTEGER NOT NULL,
        $COLUMN_IMAGE_ID INTEGER
    )
"""

    override fun onCreate(db: SQLiteDatabase) {
        /*sql.forEach {
            db.execSQL(it)
        }*/
        db.execSQL(CREATE_TABLE_CONTACTS)
        // Inserção de registos iniciais
        inserirDadosIniciais(db)
    }

    private fun inserirDadosIniciais(db: SQLiteDatabase) {
        val initialContacts = listOf(
            ContactModel(
                nome = "Canil Gatil Municipal de Ourém",
                endereco = "MCG8+PG, Ourém",
                email = "",
                telefone = 917223546,
                imageId = -1
            ),
            ContactModel(
                nome = "Encosta dos Carvalhos",
                endereco = "P8J2+8M Caranguejeira",
                email = "pousadacanina@goldpet.pt",
                telefone = 910970416,
                imageId = -1
            ),
            ContactModel(
                nome = "Domus Pet Shop",
                endereco = "MC4C+8G Ourém",
                email = "susanatorcatof@gmail.com",
                telefone = 919707509,
                imageId = -1
            ),
            ContactModel(
                nome = "Clínica Veterinária de Fátima",
                endereco = "J8CQ+22 Fátima",
                email = "geral@cvfatima.pt",
                telefone = 926401509,
                imageId = -1
            )
        )

        for (contact in initialContacts) {
            val contentValues = ContentValues().apply {
                put(COLUMN_NOME, contact.nome)
                put(COLUMN_ENDERECO, contact.endereco)
                put(COLUMN_EMAIL, contact.email)
                put(COLUMN_TELEFONE, contact.telefone)
                put(COLUMN_IMAGE_ID, contact.imageId)
            }
            db.insert(TABLE_CONTACTS, null, contentValues)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    /*    fun inserirContato(
            nome: String,
            endereco: String,
            email: String,
            telefone: Int,
            imageId: Int
        ): Long {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("nome", nome)
            contentValues.put("endereco", endereco)
            contentValues.put("email", email)
            contentValues.put("telefone", telefone)
            contentValues.put("imageId", imageId)
            val res = db.insert("contatos", null, contentValues)
            db.close()
            return res
        }

        fun atualizarContato(
            id: Int,
            nome: String,
            endereco: String,
            email: String,
            telefone: Int,
            imageId: Int
        ): Int {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("nome", nome)
            contentValues.put("endereco", endereco)
            contentValues.put("email", email)
            contentValues.put("telefone", telefone)
            contentValues.put("imageId", imageId)
            val res = db.update("contatos", contentValues, "id=?", arrayOf(id.toString()))
            db.close()
            return res
        }

        fun removerContato(id: Int): Int {
            val db = this.writableDatabase
            val res = db.delete("contatos", "id=?", arrayOf(id.toString()))
            db.close()
            return res
        }

        fun getContato(id: Int): ContactModel {
            val db = this.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM contatos WHERE id=?", arrayOf(id.toString()))
            var contactModel = ContactModel()

            if (cursor.count == 1) {
                cursor.moveToFirst()
                val idIndex = cursor.getColumnIndex("id")
                val nomeIndex = cursor.getColumnIndex("nome")
                val enderecoIndex = cursor.getColumnIndex("endereco")
                val emailIndex = cursor.getColumnIndex("email")
                val telefoneIndex = cursor.getColumnIndex("telefone")
                val imageIdIndex = cursor.getColumnIndex("imageId")

                contactModel = ContactModel(
                    id = cursor.getInt(idIndex),
                    nome = cursor.getString(nomeIndex),
                    endereco = cursor.getString(enderecoIndex),
                    email = cursor.getString(emailIndex),
                    telefone = cursor.getInt(telefoneIndex),
                    imageId = cursor.getInt(imageIdIndex)
                )
            }
            db.close()
            return contactModel
        }*/

    /*fun getAllContatos(): ArrayList<ContactModel>  {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM contatos", null)
        var listContactModel = ArrayList<ContactModel>()

        if (cursor.count > 0) {
            cursor.moveToFirst()
            val idIndex = cursor.getColumnIndex("id")
            val nomeIndex = cursor.getColumnIndex("nome")
            val enderecoIndex = cursor.getColumnIndex("endereco")
            val emailIndex = cursor.getColumnIndex("email")
            val telefoneIndex = cursor.getColumnIndex("telefone")
            val imageIdIndex = cursor.getColumnIndex("imageId")
            do {
                val contactModel = ContactModel(
                    id = cursor.getInt(idIndex),
                    nome = cursor.getString(nomeIndex),
                    endereco = cursor.getString(enderecoIndex),
                    email = cursor.getString(emailIndex),
                    telefone = cursor.getInt(telefoneIndex),
                    imageId = cursor.getInt(imageIdIndex)
                )
                listContactModel.add(contactModel)
            } while (cursor.moveToNext())

        }
        db.close()
        return listContactModel
    }*/

    fun inserirContato(contact: ContactModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NOME, contact.nome)
            put(COLUMN_ENDERECO, contact.endereco)
            put(COLUMN_EMAIL, contact.email)
            put(COLUMN_TELEFONE, contact.telefone)
            put(COLUMN_IMAGE_ID, contact.imageId)
        }
        return try {
            db.insert(TABLE_CONTACTS, null, contentValues)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        } finally {
            db.close()
        }
    }

    fun atualizarContato(contact: ContactModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NOME, contact.nome)
            put(COLUMN_ENDERECO, contact.endereco)
            put(COLUMN_EMAIL, contact.email)
            put(COLUMN_TELEFONE, contact.telefone)
            put(COLUMN_IMAGE_ID, contact.imageId)
        }

        return try {
            // Atualiza o contato e retorna o número de linhas afetadas
            db.update(TABLE_CONTACTS, contentValues, "$COLUMN_ID=?", arrayOf(contact.id.toString()))
        } catch (e: Exception) {
            e.printStackTrace() // Pode ser substituído por um log mais robusto
            -1 // Retorna -1 em caso de erro
        } finally {
            db.close()
        }
    }

    fun removerContato(id: Int): Int {
        val db = this.writableDatabase
        return try {
            db.delete(TABLE_CONTACTS, "$COLUMN_ID=?", arrayOf(id.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
            -1 // Retorna -1 em caso de erro
        } finally {
            db.close()
        }
    }

    fun getAllContatos(): ArrayList<ContactModel> {
        val listContactModel = ArrayList<ContactModel>()
        val db = this.readableDatabase

        db.rawQuery("SELECT * FROM $TABLE_CONTACTS", null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    try {
                        val contactModel = ContactModel(
                            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                            nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                            endereco = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ENDERECO)),
                            email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                            telefone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TELEFONE)),
                            imageId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_ID))
                        )
                        listContactModel.add(contactModel)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } while (cursor.moveToNext())
            }
        }
        return listContactModel
    }

    fun getContato(id: Int): ContactModel? {
        val db = this.readableDatabase
        var contato: ContactModel? = null

        // Consulta SQL para obter o contato pelo ID
        val cursor = db.query(
            TABLE_CONTACTS,
            arrayOf(
                COLUMN_ID,
                COLUMN_NOME,
                COLUMN_ENDERECO,
                COLUMN_EMAIL,
                COLUMN_TELEFONE,
                COLUMN_IMAGE_ID
            ),
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        // Verifica se o cursor retornou algum resultado
        if (cursor != null && cursor.moveToFirst()) {
            // Cria um objeto ContactModel a partir dos dados do cursor
            contato = ContactModel(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                endereco = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ENDERECO)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                telefone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TELEFONE)),
                imageId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_ID))
            )
        }

        // Fecha o cursor após o uso
        cursor?.close()
        db.close()
        return contato
    }

    fun procuraContatos(query: String): ArrayList<ContactModel> {
        val listContactModel = ArrayList<ContactModel>()
        val db = this.readableDatabase
        val selection = "$COLUMN_NOME LIKE ? OR $COLUMN_EMAIL LIKE ?"
        val selectionArgs = arrayOf("%$query%", "%$query%")

        db.query(TABLE_CONTACTS, null, selection, selectionArgs, null, null, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val contactModel = ContactModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                        endereco = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ENDERECO)),
                        email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        telefone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TELEFONE)),
                        imageId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_ID))
                    )
                    listContactModel.add(contactModel)
                } while (cursor.moveToNext())
            }
        }
        return listContactModel
    }

    fun getContatosOrdenados(orderBy: String = COLUMN_NOME): ArrayList<ContactModel> {
        val listContactModel = ArrayList<ContactModel>()
        val db = this.readableDatabase

        db.query(TABLE_CONTACTS, null, null, null, null, null, orderBy).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val contactModel = ContactModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                        endereco = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ENDERECO)),
                        email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        telefone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TELEFONE)),
                        imageId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_ID))
                    )
                    listContactModel.add(contactModel)
                } while (cursor.moveToNext())
            }
        }
        return listContactModel
    }
}