package ru.magnum.musicapp.domain.repository

class TrackNotFoundException(val trackId: Int) : Exception()