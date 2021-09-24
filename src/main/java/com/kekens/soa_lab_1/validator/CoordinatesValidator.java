package com.kekens.soa_lab_1.validator;

import com.kekens.soa_lab_1.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class CoordinatesValidator {

    public List<String> validateCoordinates(Coordinates coordinates) {
        List<String> errorList = new ArrayList<>();

        if (coordinates.getY() <= -895) {
            errorList.add("Y coordinate must be more than -895");
        }

        return errorList;
    }
}
