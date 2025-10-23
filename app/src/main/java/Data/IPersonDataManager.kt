package Data

import Entity.Person


interface IPersonDataManager{
    fun add(person: android.app.Person)
    fun update(person: Person)
    fun remove(id:String)
    fun getAll(): List<android.app.Person>
    fun getById(id:String): Person?
    fun getByFullName(fullName: String): Person?
}