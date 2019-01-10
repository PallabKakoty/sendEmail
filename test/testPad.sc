val x= 100
println(x)

val str = ""
val z = str.split(",")


val newStr = "abnbb,bhhh,,vhhhh"
val y = newStr.split(",").filter(_.nonEmpty)
y.map { i =>
  println(i)
}
