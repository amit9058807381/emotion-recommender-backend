package com.college.EmotionBased_Recommandation.helper;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Getter
@Setter
public class FacePlusPlusResponse {

    private List<Face> faces;

    public List<Face> getFaces() {
        return faces;
    }

    public void setFaces(List<Face> faces) {
        this.faces = faces;
    }

    @Setter
    @Getter
    public static class Face {

        private Attributes attributes;

    }

    @Setter
    @Getter
    public static class Attributes {

        private Map<String, Double> emotion;

    }
}
