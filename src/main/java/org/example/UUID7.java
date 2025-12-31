package org.example;

import com.fasterxml.uuid.Generators;

import java.util.UUID;

public class UUID7 {
    public static UUID randomUUID(){
        return Generators.timeBasedEpochGenerator().generate();
    }
}
