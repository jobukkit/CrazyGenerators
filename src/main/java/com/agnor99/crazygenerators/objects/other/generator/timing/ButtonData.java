package com.agnor99.crazygenerators.objects.other.generator.timing;

import java.awt.*;

public class ButtonData {
    public static final Dimension buttonDimension = new Dimension(65,11);
    public static final Point buttonPlacePoint1 = new Point(7,30);
    public static final Point buttonPlacePoint2 = new Point(134,90);
    public static final Point buttonPlacePoint2WithoutButton = new Point(buttonPlacePoint2.x-buttonDimension.width, buttonPlacePoint2.y - buttonDimension.height);
}
