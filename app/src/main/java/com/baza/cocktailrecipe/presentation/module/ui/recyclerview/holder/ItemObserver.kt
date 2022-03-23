package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder

interface ItemObserver<T> {
    fun onItemClicked(entity: T)
}