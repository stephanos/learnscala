package de.learnscala.uebungen

import de.learnscala.base.Uebung

object U130 extends Uebung {

    def enabled = false


    /**
     * Produkte eines Supermarktes.
     *
     * Gesucht werden die fehlenden Klassen aus dem unteren Beispiel und den Tests.
     */
    abstract class Item {

        def price: Float

        def description: String
    }


    /**
     * Einfaches Produkt 'Butter'
     */
    def butter =
        TODO //new SimpleItem(.95f, "Butter")

    /**
     * Reduziertes Produkt 'Milch'
     */
    def milk =
        TODO //new ReducedItem(.99f, 5, "Milk")

    /**
     * Gebündeltes Produkt 'Butter' und 'Milch'
     */
    def butterAndMilk =
        TODO //new Bundle(butter, milk)

    /**
     * Gewichtetes Produkt 'Äpfel'
     */
    def apple5kg =
        TODO //new WeightedItem(5, 0.2f, "Apples")

    /**
     * Gebündeltes Produkt 'Butter' und 'Milch' und 'Äpfel'
     */
    def butterMilkAndApples =
        TODO //new Bundle(apple5kg, butterAndMilk)
}