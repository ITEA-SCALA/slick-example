package kz.example

import slick.jdbc.PostgresProfile.api._

package object repository {

  val db = Database.forConfig("database.postgre")

}
