package com.kekens.soa_lab_1.validator;

import com.kekens.soa_lab_1.model.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class CoordinatesValidator {

    public List<IntegrityError> validateCoordinates(Coordinates coordinates) {
        List<IntegrityError> errorList = new ArrayList<>();

        if (coordinates.getY() <= -895) {
            errorList.add(new IntegrityError(200, "Y coordinate must be more than -895"));
        }

        return errorList;
    }
}
