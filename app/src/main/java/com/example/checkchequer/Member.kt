package com.example.checkchequer

class Member {

    private var _name: String
    private var _status: Boolean
    private var _summ: Int

    private var _products: MutableList<Product> = mutableListOf()

    init {
        _name = ""
        _status = false
        _summ = 0
    }

    constructor()

    constructor(name: String, status: Boolean) {
        _name = name
        _status = status
    }

    constructor(name: String, status: Boolean, summ: Int) : this(name, status) {
        _summ = summ
    }


    //GETTER'S
    fun getName(): String {
        return _name
    }

    fun getStatus(): Boolean {
        return _status
    }

    fun getSumm(): Int {
        return _summ
    }

    fun getArrayProducts(): MutableList<Product>{
        return _products
    }


    //ADD
    fun addProduct(product: Product){
        this._products.add(product)
    }

    //SETTER'S
    fun setName(name: String) {
        this._name = name
    }

    fun setStatus(status: Boolean) {
        this._status = status
    }

    fun setSumm(summ: Int) {
        this._summ = summ
    }

    fun setArrayProducts(products: MutableList<Product>){
        this._products = products
    }

    override fun toString(): String {
        return this._name
    }

}