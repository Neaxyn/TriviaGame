package com.example.myapplication.classes


class ListeQuestions {
    var listeQuestionsReponses: MutableList<QuestionReponse> =  mutableListOf(
        //initisalisation de la liste des questions
        QuestionReponse(
            "Quelle est la capitale de la France?",
            "Lyon",
            "Marseille",
            "Paris",
            "Nice",
            "Paris"
        ),

        QuestionReponse(
            "Quel est le plus grand océan?",
            "Océan Atlantique",
            "Océan Indien",
            "Océan Pacifique",
            "Océan Arctique",
            "Océan Pacifique"
        ),

        QuestionReponse(
            "Qui a écrit 'Les Misérables'?",
            "Victor Hugo",
            "Albert Camus",
            "Gustave Flaubert",
            "Émile Zola",
            "Victor Hugo"
        ),

        QuestionReponse(
            "Quel est le symbole chimique de l'or?",
            "Ag",
            "Au",
            "Fe",
            "O",
            "Au"
        ),

        QuestionReponse(
            "Quelle est la distance moyenne entre la Terre et le Soleil?",
            "149,6 millions de km",
            "92,96 millions de km",
            "12,742 km",
            "384 400 km",
            "149,6 millions de km"
        ),

        QuestionReponse(
            "Quel est le plus haut sommet du monde?",
            "K2",
            "Kangchenjunga",
            "Mont Everest",
            "Lhotse",
            "Mont Everest"
        ),

        QuestionReponse(
            "Quel est le plus long fleuve du monde?",
            "Le Nil",
            "L'Amazone",
            "Le Yangtsé",
            "Le Mississippi",
            "L'Amazone"
        ),

        QuestionReponse(
            "Qui a peint 'La Joconde'?",
            "Vincent Van Gogh",
            "Pablo Picasso",
            "Leonardo da Vinci",
            "Claude Monet",
            "Leonardo da Vinci"
        ),

        QuestionReponse(
            "Quel est le plus grand pays du monde en superficie?",
            "Canada",
            "États-Unis",
            "Chine",
            "Russie",
            "Russie"
        ),

        QuestionReponse(
            "Quel est l'animal terrestre le plus rapide?",
            "Le lion",
            "Le guépard",
            "Le lièvre",
            "Le kangourou",
            "Le guépard"
        ),
        QuestionReponse(
            "c'est quoi le plus lourd?",
            "1g",
            "1 mg",
            "1 t",
            "1 kg",
            "1 t"
        )
    )

    // Méthode pour tirer aléatoirement une question de la liste
    fun tirageNumero(numero: Int): QuestionReponse? {
        if (numero >= 0 && numero < listeQuestionsReponses.size) {
            return listeQuestionsReponses[numero]
        }
        return null // L'indice est hors des limites
    }

}
