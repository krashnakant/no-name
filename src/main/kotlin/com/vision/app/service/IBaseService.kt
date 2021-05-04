package com.vision.app.service

interface IBaseService<T, ID> {
    fun findById(id: ID): T?
    fun <S : T> save(entity: S): S
}
