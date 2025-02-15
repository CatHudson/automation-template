package com.jetbrains.teamcity.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
/**
 * Fields will be set with a random value
 */
annotation class Random()
