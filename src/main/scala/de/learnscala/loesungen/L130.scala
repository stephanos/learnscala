package de.learnscala.loesungen

import de.learnscala.base.Loesung

object L130 extends Loesung {

    /**
     * Produkte eines Supermarktes.
     *
     * Basisklasse 'Item' mit den Methoden
     *  - price: Float
     *  - description: String
     *
     * Gesucht werden die fehlenden Klassen aus dem unteren Beispiel und den Tests.
     */
    abstract class Item {

        def price: Float

        def description: String
    }

    class SimpleItem(val price: Float,
                     val description: String) extends Item

    class ReducedItem(val basePrice: Float,
                      val reducedBy: Int,
                      val description: String) extends Item {

        val price =
            basePrice * (1 + reducedBy / 100)
    }

    class WeightedItem(val kilos: Int,
                       val pricePerKilo: Float,
                       val baseDescription: String) extends Item {

        val price =
            kilos * pricePerKilo

        val description =
            kilos + "kg " + baseDescription
    }

    class Bundle(item1: Item, item2: Item) extends Item {

        override def price =
            item1.price + item2.price

        override def description =
            item1.description + " & " + item2.description
    }


    /**
     * Einfaches Produkt 'Butter'
     */
    def butter =
        new SimpleItem(.95f, "Butter")

    /**
     * Reduziertes Produkt 'Milch'
     */
    def milk =
        new ReducedItem(.99f, 5, "Milk")

    /**
     * Gebündeltes Produkt 'Butter' und 'Milch'
     */
    def butterAndMilk =
        new Bundle(butter, milk)

    /**
     * Gewichtetes Produkt 'Äpfel'
     */
    def apple5kg =
        new WeightedItem(5, 0.2f, "Apples")

    /**
     * Gebündeltes Produkt 'Butter' und 'Milch' und 'Äpfel'
     */
    def butterMilkAndApples =
        new Bundle(apple5kg, butterAndMilk)
}