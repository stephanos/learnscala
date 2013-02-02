package com.loops101.model.dto

trait Job {

  def id: String
  def content: JobData
}

trait JobData {

  def id: Option[String]
  def args: Map[String, String]
  def entity: Option[String]
  def binary: Array[Byte]
}