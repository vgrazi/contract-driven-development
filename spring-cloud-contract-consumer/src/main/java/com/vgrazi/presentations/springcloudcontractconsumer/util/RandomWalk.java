package com.vgrazi.presentations.springcloudcontractconsumer.util;

import java.util.Random;

public class RandomWalk {
    private static Random RANDOM = new Random(0);

    // A function that will generate random numbers, but prefer higher numbers. Named after the Monte Carlo Method
    public static double monteCarlo() {

        // We do this “forever” until we find a qualifying random value.
        while (true) {
            // Pick a random value.
            double r1 = RANDOM.nextDouble();
            // Assign a probability.
            // Pick a second random value.
            double r2 = RANDOM.nextDouble();
            // Does it qualify? If so, we’re done!
            if (r2 > r1) {
                return 1 + (r1 - .5)/50;
            }
        }
    }

    public static void main(String[] args) {
// Generate 10 random numbers and size ellipses based on them
        for (int i = 0; i < 10; i++) {
            double probability = monteCarlo();
            System.out.println(probability);
//            double radius = probability * 30;
            System.out.println(probability);
        }
    }
}
