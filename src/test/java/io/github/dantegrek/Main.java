package io.github.dantegrek;


import static io.github.dantegrek.jplay.Actor.actor;

public class Main {

    public static void main(String[] args) {
        actor()
                .getPseudoElementContent(".star-rating", "::after");
    }
}
