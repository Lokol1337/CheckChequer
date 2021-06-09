package com.example.checkchequer

class Product {

    private var _name: String
    private var _price: Int
    private var _count: Float
    private var _summ: Int

    constructor() {
        _name = ""
        _price = 0
        _count = 0.toFloat()
        _summ = 0
    }


    constructor(name: String, price: Int, count: Int){
        _name = name
        _price = price
        _count = count.toFloat()
        _summ = price * count
    }

    constructor(name: String, price: Int, count: Float, summ: Int){
        _name = name
        _price = price
        _count = count
        _summ = summ
    }


    //GETTER'S
    fun getName(): String {
        return _name
    }

    fun getPrice(): Int {
        return _price
    }

    fun getCount(): Int{
        return _count.toInt()
    }

    fun getSumm(): Int {
        return _summ
    }


    //SETTER'S
    fun setName(name: String) {
        this._name = name
    }

    fun setStatus(price: Int) {
        this._price = price
    }

    fun setCount(count: Int){
        this._count = count.toFloat()
    }

    fun setSumm(summ: Int) {
        this._summ = summ
    }

    override fun toString(): String {
        return "Название: $_name" +
                "\nКоличество: ${_count.toInt()}" +
                "\nЦена: $_price"
    }

}